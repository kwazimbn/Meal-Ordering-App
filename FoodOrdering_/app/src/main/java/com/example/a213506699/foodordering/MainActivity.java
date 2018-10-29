package com.example.a213506699.foodordering;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ImageButton btnCityDrinks, btnCitySides, btnCityStrips, btnCityWraps;
    ImageButton btnGrDrinks, btnGrShawarma, btnGrMunchies, btnGrToasties;
    ImageButton btnJoDrinks, btnJoShawarma, btnJoFalafel, btnJoSides;
    String shop_ID, cat_ID;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    MenuItem item1, item2, item3;
    int loggedInStatus;
    int isReturning, isSameShop;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isReturning = sharedPref.getInt("returning", 0);
        isSameShop = sharedPref.getInt("sameStore", 0);
        try {
            loggedInStatus = sharedPref.getInt("status", 0);
        } catch (Exception e) {
            Log.e(TAG, "onCreateOptionsMenu: " + e.getMessage());
        }
        item1 = menu.findItem(R.id.home);
        item1.setVisible(false);
        item2 = menu.findItem(R.id.login);
        item2.setVisible(false);
        if (loggedInStatus == 0)
            item2.setVisible(false);
        item3 = menu.findItem(R.id.logout);
        item3.setVisible(false);

        //item3.setVisible(false);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(this, Cart.class));
                return true;
            case R.id.account:
                if (loggedInStatus == 0)
                    startActivity(new Intent(this, Login.class));
                else
                    startActivity(new Intent(this, Account.class));
                return true;
            default:
                return false;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = new Intent(MainActivity.this, Menu.class);
        setContentView(R.layout.activity_main);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPref.edit();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main Menu");
        getSupportActionBar().setSubtitle("Categories");



        /**
         * Onclick events for categories in V-City Chicks shop
         */
        //Onclick for category 1 which is drinks from VCity
        btnCityDrinks = findViewById(R.id.cityDrinks);
        btnCityDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "1";
                shop_ID = "1";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 1) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }

            }
        });

        //Onclick for category 2 which is sides from VCity
        btnCitySides = findViewById(R.id.citySides);
        btnCitySides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "3";
                shop_ID = "1";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 1) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Onclick for category 3 which is strips from VCity
        btnCityStrips = findViewById(R.id.cityStrips);
        btnCityStrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "4";
                shop_ID = "1";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 1) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Onclick for category 4 which is wraps from VCity
        btnCityWraps = findViewById(R.id.cityWraps);
        btnCityWraps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "5";
                shop_ID = "1";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 1) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        /**
         * Onclick events for categories in Green Bean shop
         */

        //Onclick for category 1 which is drinks from Green Bean
        btnGrDrinks = findViewById(R.id.grDrinks);
        btnGrDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "6";
                shop_ID = "2";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 2) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Onclick for category 2 which is sides from Green Bean
        btnGrShawarma = findViewById(R.id.grSchwarma);
        btnGrShawarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "7";
                shop_ID = "2";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 2) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Onclick for category 3 which is strips from Green Bean
        btnGrMunchies = findViewById(R.id.grMunchies);
        btnGrMunchies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "8";
                shop_ID = "2";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 2) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Onclick for category 4 which is wraps from Green Bean
        btnGrToasties = findViewById(R.id.grToast);
        btnGrToasties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "9";
                shop_ID = "2";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 2) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        /**
         * Onclick events for categories in Jolaj Shawarma shop
         */

        //Onclick for category 1 which is drinks from VCity
        btnJoDrinks = findViewById(R.id.joDrinks);
        btnJoDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "10";
                shop_ID = "3";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 3) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Onclick for category 2 which is sides from Jolaj Shawarma
        btnJoShawarma = findViewById(R.id.joSchwarma);
        btnJoShawarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "11";
                shop_ID = "3";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 3) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Onclick for category 3 which is strips from Jolaj Shawarma
        btnJoFalafel = findViewById(R.id.joFalafel);
        btnJoFalafel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "12";
                shop_ID = "3";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 3) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Onclick for category 4 which is wraps from Jolaj Shawarma
        btnJoSides = findViewById(R.id.joSides);
        btnJoSides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_ID = "13";
                shop_ID = "3";
                intent.putExtra("shop_ID", shop_ID);
                intent.putExtra("category_ID", cat_ID);
                editor.putInt("shopID", Integer.parseInt(shop_ID));
                editor.commit();

                if ((isReturning == 0 || (isSameShop == 3) && isReturning == 1))
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(), "Only one order per shop!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
