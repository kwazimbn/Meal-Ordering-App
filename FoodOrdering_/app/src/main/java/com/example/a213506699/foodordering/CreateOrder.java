package com.example.a213506699.foodordering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.security.auth.login.LoginException;

public class CreateOrder extends AppCompatActivity {
    TextView itemDescription, itemPrice, totalPrice, quantity, txtTotQuantity;
    Button addToCart, btnIncrease, btndecrease;
    String itemDes;
    double item_Price, totPrice = item_Price;
    int count = 1;
    ResultSet rs2;
    int totalQuantity;
    int itemID, shopID;
    ConnectionClass connectionClass;
    private static final String TAG = "CreateOrder";
    SharedPreferences sharedPref;
    MenuItem myCart;
    Toolbar toolbar;
    int loggedInStatus;
    MenuItem item1, item2, item3;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        item2 = menu.findItem(R.id.logout);
        item3 = menu.findItem(R.id.login);
        if(loggedInStatus == 0) {
            item2.setVisible(false);
            item3.setVisible(false);
        }
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
        setContentView(R.layout.activity_create_order);
        connectionClass = new ConnectionClass();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Order");


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        itemID = Integer.parseInt(getIntent().getStringExtra("itemID"));
        shopID = getIntent().getIntExtra("shopID", 0);

        totalPrice = findViewById(R.id.txtTotPrice);
        quantity = findViewById(R.id.txtcount);


        itemDes = getIntent().getStringExtra("item");
        item_Price = Double.parseDouble(getIntent().getStringExtra("itemPrice"));
        totPrice = item_Price * count;

        itemDescription = findViewById(R.id.txtDescription);
        itemDescription.setText(itemDes);

        itemPrice = findViewById(R.id.txtPrice);
        itemPrice.setText(("R " + item_Price));

        totalPrice.setText(itemPrice.getText());

        btndecrease = findViewById(R.id.btnDecrease);
        btndecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
                quantity.setText(("" + count).trim());
                totPrice = item_Price * count;
                totalPrice.setText(("R " + totPrice));

            }
        });


        quantity.setText(("" + count).trim());
        btnIncrease = findViewById(R.id.btnIncrease);
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
                quantity.setText(("" + count).trim());
                totPrice = item_Price * count;
                totalPrice.setText(("R " + totPrice));

            }
        });
        ;
        addToCart = findViewById(R.id.btnAddToCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCart atc = new AddToCart();
                atc.execute("");
                startActivity(new Intent(getApplicationContext(), com.example.a213506699.foodordering.MainActivity.class));


                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("shopID",shopID);
                editor.putInt("sameStore",shopID);
                editor.putInt("returning",1);
                editor.commit();

            }
        });


    }

    protected void decrease() {
        if (count > 1)
            count--;
        else
            Toast.makeText(CreateOrder.this, "Items MUST BE > 0", Toast.LENGTH_LONG).show();
    }

    protected void increase() {
        count++;
    }

    public class AddToCart extends AsyncTask<String, String, String> {

        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            sharedPref = PreferenceManager.getDefaultSharedPreferences(CreateOrder.this);


            Toast.makeText(CreateOrder.this, s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();
                if (conn == null) {
                    message = "Error in Connection with SQL Server";
                } else {
                    String query2 = "select * from mblcart where menuItemID = " + itemID;
                    //Reading cakes from Database using SQL Command on the Client device
                    String line = itemID + "," + shopID + "," + count + "," + totPrice;
                    String query = "insert into mblCart(menuItemID,shopID,quantity,cartItemPrice) values(" + line + ")" +
                            ";select sum(quantity) from mblCart";
                    Statement sm = conn.createStatement();
                    ResultSet rs = sm.executeQuery(query2);


                    if (rs.next() == false) {
                        rs2 = sm.executeQuery(query);

                    } else {
                        query = "update mblcart set quantity = quantity + " + count + ", cartItemprice = cartItemprice + " + totPrice +
                                "  where menuItemID = " + itemID + "; select sum(quantity) from mblCart";
                        rs2 = sm.executeQuery(query);

                    }

                    message = "Successfully added to cart";
                    while (rs2.next()) {
                        totalQuantity = Integer.parseInt(rs2.getString(1));

                    }
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(CreateOrder.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("key1", totalQuantity);
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
