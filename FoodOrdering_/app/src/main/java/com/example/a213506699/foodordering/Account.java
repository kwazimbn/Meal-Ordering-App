package com.example.a213506699.foodordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Account activity. This shows account details for a user, order list and details made by the user
 */
public class Account extends AppCompatActivity {
    ConnectionClass connectionClass;
    TextView txtRole, txtName, txtPhNo, txtMail;
    Toolbar toolbar;
    MenuItem item1, item2, item3;
    private static final String TAG = "Account";
    ArrayList<String> descrptions;
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<Integer> quantities;
    ArrayList<Double> prices;
    String orderDate, message;
    Spinner spDate;
    Button viewOrders;
    int status;

    /*
    Initialization of menu on the action bar toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        item2 = menu.findItem(R.id.login);
        item2.setVisible(false);
        item3 = menu.findItem(R.id.account);
        item3.setVisible(false);
        return true;

    }

    /*
       Handling of the action bar icons, icon onclicks navigating you to the desired Activity screens
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(this, Cart.class));
                return true;
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(this, Login.class));
                SharedPreferences mySharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                mySharedPref.edit().clear().commit();
                finish();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("status", 0);
                editor.commit();
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Account");


        connectionClass = new ConnectionClass();

        SharedPreferences mySharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int userId = mySharedPref.getInt("loginUserID", 0);
        String name = mySharedPref.getString("loginUserName", "");
        String phNo = mySharedPref.getString("loginUserCell", "");
        String eMail = mySharedPref.getString("loginUserEmail", "");
        int uRole = mySharedPref.getInt("loginUserRoleID", 0);

        txtMail = findViewById(R.id.aEmail);
        txtName = findViewById(R.id.aname);
        txtPhNo = findViewById(R.id.aPhNo);
        txtRole = findViewById(R.id.rOle);
        txtMail.setText(eMail);
        txtPhNo.setText(phNo);
        txtName.setText(name);
        Log.e(TAG, "onCreate: " + uRole);
        if (uRole == 1)
            txtRole.setText("Customer");
        else if (uRole == 2)
            txtRole.setText("Staffmember");
        else if (uRole == 3)
            txtRole.setText("Shop Manager");
        else if (uRole == 4)
            txtRole.setText("Administrator");


        MyOrders myOrders = new MyOrders(userId);
        myOrders.execute("");
    }

    /**
     * A class used to fetch data from database using asynchronous tasks.
     */
    public class MyOrders extends AsyncTask<String, String, String> {
        int userID;
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        ArrayList<Double> prices = new ArrayList<>();
        ArrayList<OrderItem> myItems;
        Spinner spinner;
        String quant = "";
        String des = "";
        String price = "";
        double total = 0;

        TextView txtPrice, txtDescription, txtQuantity, txtTotal;


        public MyOrders(int userID) {
            this.userID = userID;
        }

        /*
            Get dates that will be displayed on spinner.
         */
        public ArrayList<String> getDates() {
            try {
                String temp = myItems.get(0).getDate();
                dates.add(temp);
                for (int i = 0; i < myItems.size(); i++) {
                    if (!temp.equals(myItems.get(i).getDate()))
                        dates.add(myItems.get(i).getDate());
                    temp = myItems.get(i).getDate();

                }

            } catch (Exception e) {
                Log.e(TAG, "getDates: " + e.getMessage());
            }
            return dates;
        }

        /*
        After query execution
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            spinner = findViewById(R.id.spinner);
            Log.e(TAG, "onPostExecute: " + getDates().size());
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.spinner_item, getDates());
            spinner.setAdapter(spinnerArrayAdapter);

            txtDescription = findViewById(R.id.OLmenuDescription);
            txtPrice = findViewById(R.id.OLitem_price);
            txtQuantity = findViewById(R.id.txtQuantity);
            txtTotal = findViewById(R.id.myTotalPrice);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    quant = "";
                    des = "";
                    price = "";
                    total = 0;
                    for (int i = 0; i < myItems.size(); i++) {
                        //Comparing the list of cake names with what is selected on the Spinner (DropdownList)
                        if (myItems.get(i).getDate() == spinner.getSelectedItem().toString()) {
                            quant += myItems.get(i).getQuantity() + "\n";
                            des += myItems.get(i).getDescription() + "\n";
                            price += "R " + Math.round(myItems.get(i).getPrice() / 1.00) + "\n";
                            total += Math.round((myItems.get(i).getPrice()) / 1.00);
                        }
                    }
                    txtDescription.setText(des);
                    txtPrice.setText(price);
                    txtQuantity.setText("" + quant);
                    txtTotal.setText("ORDER TOTAL\t: R " + total);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });
        }
        /*
        Read data and store them in order item array for future usage
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();
                if (conn == null) {
                    message = "Error in Connection with SQL Server";
                } else {
                    //Reading cakes from Database using SQL Command on the Client device
                    String query = "select orderDate, menuItemDescription, oi.quantity, oi.price " +
                            "from mblOrder mo, mblMenuItem mi,mblOrderItem oi, mblUser mu " +
                            "where mi.menuItemID=oi.menuItemID and mo.userID=mu.userID " +
                            "and oi.orderID = mo.orderID and mo.userID = " + userID;
                    Statement sm = conn.createStatement();
                    ResultSet rs = sm.executeQuery(query);
                    myItems = new ArrayList<>();
                    while (rs.next()) {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setDate(rs.getString(1));
                        orderItem.setDescription(rs.getString(2));
                        orderItem.setQuantity(rs.getInt(3));
                        orderItem.setPrice(rs.getDouble(4));
                        myItems.add(orderItem);
                    }
                }
            } catch (SQLException e) {


                Log.e(TAG, "doInBackground: " + e.getMessage());
            }

            return message;
        }
    }
}
