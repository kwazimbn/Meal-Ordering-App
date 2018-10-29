package com.example.a213506699.foodordering;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Complete_order extends AppCompatActivity {
    Button completeOrder, cancel;
    TextView finalPrice;
    Spinner locationSelector;
    ArrayList<String> cartItemDescriptions = new ArrayList<>();
    ArrayList<Double> cartPrices = new ArrayList<>();
    ArrayList<Integer> cartQuantities = new ArrayList<>();
    ArrayList<Integer> menuItemIDS = new ArrayList<>();
    ArrayList<CartItem> cartItems = new ArrayList<>();
    Context ctx;
    String message;
    double price;
    String rec, subject, textMessage;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    private ConnectionClass connectionClass;
    private static final String TAG = "Complete_order";
    MenuItem item1, item2, item3;
    private Session session;
    private SharedPreferences mySharedPref;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getMenuInflater().inflate(R.menu.menu, menu);
        item1 = menu.findItem(R.id.login);
        item2 = menu.findItem(R.id.logout);
        item1.setVisible(false);
        item2.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(this, Cart.class));
                return true;
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.account:
                startActivity(new Intent(this, Account.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Complete Order");

        String[] locations = {"Pick-Up", "J-Block", "Oval(A-Block)", "Oval(B-Block)", "Oval(C-Block)", "Oval(D-Block)", "Oval(E-Block)", "O-Block", "P-Block",
                "R-Block", "S-Block", "Z-Block", "H-1 Building", "Bio-Building", "RMS", "Printing Services", "M-Block", "OR-Tambo Building", "CHS-E Block"};
        locationSelector = findViewById(R.id.locationSpinner);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations);
        locationSelector.setAdapter(spinnerArrayAdapter);

        connectionClass = new ConnectionClass();
        GetCompleteOrder getCompleteOrder = new GetCompleteOrder();
        getCompleteOrder.execute("");

        completeOrder = findViewById(R.id.btn_CompleteOrder);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int myShopID = sharedPref.getInt("shopID", -1);
                int myLoginID = sharedPref.getInt("loginUserID", -1);
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                String orderDate = dateFormat.format(date);

                AddToOrder addToOrder = new AddToOrder(myShopID, myLoginID, "Pending", locationSelector.getSelectedItem().toString(), price, orderDate);

                if (price != 0)
                    addToOrder.execute("");
                else
                    Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_LONG);

                SharedPreferences mySharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int userId = mySharedPref.getInt("loginUserID", 0);
                String uName = mySharedPref.getString("loginUserName", "");
                String phNo = mySharedPref.getString("loginUserCell", "");
                String uEmail = mySharedPref.getString("loginUserEmail", "");
                int uRole = mySharedPref.getInt("loginUserRoleID", 0);


                subject = "Ordered Successfully";
                textMessage = "Dear " + uName + "\nWe have received your order. Please wait for our call as we still prepare your meal. Your order total price is R"+price;

                              rec = uEmail;
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("weeat32@gmail.com", "Eat266548");
                    }
                });

                RetreiveFeedTask task = new RetreiveFeedTask();
                task.execute();

              mySharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int maxVal = mySharedPref.getInt("maxID", 0) + 1;
                ConfirmOrder confirmOrder = new ConfirmOrder(maxVal);
                confirmOrder.execute("");
                Log.e(TAG, "onClick: " + maxVal);

            }
        });


    }

    public void initializeVariables(ArrayList<CartItem> myList) {
        for (int i = 0; i < myList.size(); i++) {
            cartItemDescriptions.add(myList.get(i).getDescription());
            cartQuantities.add(myList.get(i).getQuantity());
            cartPrices.add(myList.get(i).getPrice());
            menuItemIDS.add(myList.get(i).getMenuItemID());
        }
    }

    public void initRecyclerView() {
        Log.d(TAG, "initPrices: Init RecyclerView");
        RecyclerView recyclerView = this.findViewById(R.id.completeRecycler);
        CompleteOrderAdapter recyclerViewAdapter = new CompleteOrderAdapter(ctx, cartItemDescriptions, cartPrices, cartQuantities, menuItemIDS);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("noreply@weeat.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
    public class GetCompleteOrder extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            initializeVariables(cartItems);
            initRecyclerView();

            finalPrice = findViewById(R.id.finalprice);
            finalPrice.setText("R " + price);

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();
                if (conn == null) {
                    message = "Error in Connection with SQL Server";
                } else {
                    //Reading cakes from Database using SQL Command on the Client device
                    String query = "select c.cartID, c.menuItemID,c.shopID,c.quantity,m.menuItemDescription,c.cartItemPrice " +
                            "from mblCart c INNER JOIN mblMenuItem m ON c.menuItemID = m.menuItemID";
                    Statement sm = conn.createStatement();
                    ResultSet rs = sm.executeQuery(query);

                    while (rs.next()) {
                        CartItem cartItem = new CartItem();
                        cartItem.setCartID(Integer.parseInt(rs.getString(1)));
                        cartItem.setMenuItemID(Integer.parseInt(rs.getString(2)));
                        cartItem.setShopID(Integer.parseInt(rs.getString(3)));
                        cartItem.setQuantity(Integer.parseInt(rs.getString(4)));
                        cartItem.setDescription(rs.getString(5));
                        cartItem.setPrice(Double.parseDouble(rs.getString(6)));
                        cartItems.add(cartItem);

                    }
                    rs = sm.executeQuery("select sum(cartItemPrice) from mblCart");
                    while (rs.next()) {
                        price = rs.getInt(1);
                    }

                }
            } catch (SQLException e) {
                Log.e(TAG, e.toString());
                message = "SQLException!!!!";
            }

            return message;
        }

    }

    public class AddToOrder extends AsyncTask<String, String, String> {
        int shopID, userID, maxID;
        String orderStatus, orderDate, location;
        double totalCost;
        private static final String TAG = "AddToOrder";

        public AddToOrder(int shopID, int userID, String orderStatus, String location, double totalCost, String orderDate) {
            this.shopID = shopID;
            this.userID = userID;
            this.orderStatus = orderStatus;
            this.orderDate = orderDate;
            this.location = location;
            this.totalCost = totalCost;
        }

        public int getMaxID() {
            return maxID;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();

                if (conn == null) {
                    message = "Error in Connection with SQL Server";
                } else {
                    //Reading cakes from Database using SQL Command on the Client device
                    String query = "insert into mblOrder(shopID,userID,orderStatus,location,totalPrice,orderDate)" +
                            " values(" + shopID + "," + userID + ",'" + orderStatus + "','" + location + "'," + totalCost + ",'" + orderDate + "');" +
                            "select max(orderID) from mblOrder";
                    Statement sm = conn.createStatement();
                    ResultSet rs;
                    if (totalCost != 0) {
                        rs = sm.executeQuery(query);

                        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        final SharedPreferences.Editor editor = sharedPref.edit();
                        while (rs.next()) {

                            editor.putInt("maxID", rs.getInt(1));
                            editor.commit();
                        }
                    }
                }
            } catch (SQLException e) {

                message = "SQLException!!!!";
                Log.e(TAG, "doInBackground: " + e.getMessage());
            }

            return message;
        }
    }

    public class ConfirmOrder extends AsyncTask<String, String, String> {
        int orderID, menuItemID;
        double price, totalPrice;
        private static final String TAG = "ConfirmOrder";

        public ConfirmOrder(int orderID) {
            this.orderID = orderID;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TruncateCart trc = new TruncateCart();
            trc.execute("");

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("returning",0);
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();

                if (conn == null) {
                    message = "Error in Connection with SQL Server";
                } else {
                    totalPrice = 0;
                    for (int i = 0; i < cartItems.size(); i++) {
                        totalPrice = totalPrice + cartItems.get(i).price;
                        String query = "insert into mblOrderItem(orderID,menuItemID, quantity,price)" +
                                " values(" + orderID + "," + cartItems.get(i).getMenuItemID() + "," + cartQuantities.get(i) + "," + cartItems.get(i).price + ");" +
                                "select* from mblOrderItem";
                        Statement sm = conn.createStatement();
                        ResultSet rs;
                        if (totalPrice != 0) {
                            rs = sm.executeQuery(query);
                            message = "Order placed successfully";
                        } else
                            message = " Error: Nothing in cart";
                    }

                }
            } catch (SQLException e) {

                Log.e(TAG, "doInBackground: " + e.getMessage());
            }

            return message;
        }
    }

    public class TruncateCart extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();

                if (conn == null) {

                } else {
                    String query = "truncate table mblCart; select * from mblCart";
                    Statement sm = conn.createStatement();
                    sm.executeQuery(query);

                }


            } catch (SQLException e) {

                message = "SQLException!!!!";
                Log.e(TAG, "doInBackground: " + e.getMessage());
            }

            return "";
        }
    }
}
