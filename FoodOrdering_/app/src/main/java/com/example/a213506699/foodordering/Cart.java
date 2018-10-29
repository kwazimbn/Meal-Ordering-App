package com.example.a213506699.foodordering;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {
    ArrayList<String> cartItemDescriptions = new ArrayList<>();
    ArrayList<Double> cartPrices = new ArrayList<>();
    ArrayList<Integer> cartQuantities = new ArrayList<>();
    ArrayList<Integer> menuItemIDS = new ArrayList<>();
    ArrayList<CartItem> cartItems = new ArrayList<>();
    Button checkout;
    Toolbar toolbar;
    SharedPreferences mySharedPref;
    int loggedInStatus;
    Intent intent;

    private static final String TAG = "Cart";
    private ConnectionClass connectionClass;
    MenuItem item1, item2, item3;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        item1 = menu.findItem(R.id.cart);
        item1.setVisible(false);
        item2 = menu.findItem(R.id.login);
        item3 = menu.findItem(R.id.logout);

        mySharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loggedInStatus = mySharedPref.getInt("status", 0);
        if (loggedInStatus == 1)
            item2.setVisible(false);
        else
            item2.setVisible(false);
            item3.setVisible(false);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
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
        setContentView(R.layout.activity_cart);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");


        connectionClass = new ConnectionClass();
        GetCartItems getCartItems = new GetCartItems();
        getCartItems.execute("");
        getCartItems.onPostExecute("");

        checkout = findViewById(R.id.btnCheckout);
        mySharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        double price = (double) mySharedPref.getFloat("myPrice", 0);

        if (price<30)
            checkout.setEnabled(false);
        Log.e(TAG, "onCreate: "+price);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Cart.this, Complete_order.class);
                startActivity(intent);
            }
        });
    }

    public double getTotalPrice(List<CartItem>myList) {
        double sum = 0;
        for(int i = 0;i<myList.size();i++){
            sum+=myList.get(i).getPrice();
        }
        return sum;
    }

    public int initializeVariables(ArrayList<CartItem> myList) {
        for (int i = 0; i < myList.size(); i++) {
            cartItemDescriptions.add(myList.get(i).getDescription());
            cartQuantities.add(myList.get(i).getQuantity());
            cartPrices.add(myList.get(i).getPrice());
            menuItemIDS.add(myList.get(i).getMenuItemID());
        }
        return myList.size();
    }

    public void initRecyclerView() {
        Log.d(TAG, "initPrices: Init RecyclerView");
        RecyclerView recyclerView = this.findViewById(R.id.cRecyclerView);
        CartRecyclerViewAdapter recyclerViewAdapter = new CartRecyclerViewAdapter(this, menuItemIDS, cartItemDescriptions, cartPrices, cartQuantities);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    /**
     * Background Worker class for DB connections
     */
    public class GetCartItems extends AsyncTask<String, String, String> {
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            initializeVariables(cartItems);
            initRecyclerView();

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

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putFloat("myPrice", (float) getTotalPrice(cartItems));
                    editor.commit();
                }
            } catch (SQLException e) {
                Log.e(TAG, e.toString());
                message = "SQLException!!!!";
            }

            return message;
        }
    }

}
