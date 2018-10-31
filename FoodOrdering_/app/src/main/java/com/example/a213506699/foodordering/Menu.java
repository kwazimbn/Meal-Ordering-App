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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {
    private static final String TAG = "Menu";

    private ArrayList<String> menuDescriptions = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    ConnectionClass connClass;
    ArrayList<MenuItem> menuItems = new ArrayList();
    int cat_ID;
    int shop_ID;
    int quant;
    Toolbar toolbar;
    android.view.MenuItem item2, item3;
    int loggedInStatus;
    SharedPreferences sharedPref;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        loggedInStatus = sharedPref.getInt("status", 0);
        item2 = menu.findItem(R.id.logout);
        item3 = menu.findItem(R.id.login);

        item2.setVisible(false);
        item3.setVisible(false);

        return true;

    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(this, Cart.class));
                return true;
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.account:
                if(loggedInStatus == 0) {
                    startActivity(new Intent(this, Login.class));
                }else{
                startActivity(new Intent(this, Login.class));
                }
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Category");



        connClass = new ConnectionClass();

        PopulateMenu populateMenu = new PopulateMenu();
        populateMenu.execute("");
    }


    public void initRecyclerView() {
        Log.d(TAG, "initPrices: Init RecyclerView");
        RecyclerView recyclerView = this.findViewById(R.id.recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, quant, idList, shop_ID, menuDescriptions, prices);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public class PopulateMenu extends AsyncTask<String, String, String> {
        //Declaration of Variables
        String message;
        TextView txt;
        List<String> titles;// = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            initPrices();
            initIDs();
            initDescriptions();
            initRecyclerView();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Connection conn = connClass.CONN();
                if (conn == null) {
                    message = "Error in Connection with SQL Server";
                } else {
                    //Reading cakes from Database using Stored Procedure
                    /*PreparedStatement getProc = conn.prepareCall("EXEC GetCakes");
                    ResultSet rs = getProc.executeQuery();
                    menuItems = new ArrayList<>();
                    while (rs.next())
                    {
                        Cake c = new Cake(rs.getString(2));
                        c.setRecipe(rs.getString(3));
                        c.setCakeID(Integer.parseInt(rs.getString(1)));
                        message = "This was found\n" + rs.getString(3);
                        cakeList.add(c);
                    }

*/

                    //Reading cakes from Database using SQL Command on the Client device
                    cat_ID = Integer.parseInt(getIntent().getStringExtra("category_ID"));
                    shop_ID = Integer.parseInt(getIntent().getStringExtra("shop_ID"));
                    String query = "Select * from mblMenuItem where categoryID = " + cat_ID;
                    Statement sm = conn.createStatement();
                    ResultSet rs = sm.executeQuery(query);

                    while (rs.next()) {
                        MenuItem menuItem = new MenuItem();
                        menuItem.setMenu_ID(Integer.parseInt(rs.getString(1)));
                        menuItem.setCategory_ID(Integer.parseInt(rs.getString(2)));
                        menuItem.setPrice(Double.parseDouble(rs.getString(3)));
                        menuItem.setDescription(rs.getString(4));
                        menuItem.setItem_size(rs.getString(5));
                        message = "This was found\n" + rs.getString(4);
                        menuItems.add(menuItem);
                    }

                    ResultSet rs2 = sm.executeQuery("select sum(quantity) from mblCart");
                    while (rs2.next()) {
                        quant = Integer.parseInt(rs2.getString(1));
                    }
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Menu.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("key1", quant);
                    editor.commit();
                }

            } catch (Exception ex) {
                Log.e("New Error", ex.getMessage());
            }
            return message;

        }

        public void initDescriptions() {
            for (int i = 0; i < menuItems.size(); i++) {
                menuDescriptions.add(menuItems.get(i).getDescription().trim());
            }
        }

        public void initPrices() {
            for (int i = 0; i < menuItems.size(); i++) {
                prices.add((menuItems.get(i).getPrice() + "").trim());
            }
        }

        public void initIDs() {
            for (int i = 0; i < menuItems.size(); i++) {
                idList.add((menuItems.get(i).getMenu_ID() + "").trim());
            }
        }

        public int getQuant() {
            return quant;
        }
    }
}
