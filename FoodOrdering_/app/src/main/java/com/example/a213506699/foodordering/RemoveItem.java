package com.example.a213506699.foodordering;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoveItem extends AsyncTask<String, String, String> {
    String message;
    ConnectionClass connectionClass;
    int menuItemID;
    Context ctx;

    public RemoveItem(Context ctx, int menuItemID) {
        this.ctx = ctx;
        this.menuItemID = menuItemID;
        connectionClass = new ConnectionClass();
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
                String query = "delete from mblcart where menuItemID = " + menuItemID +
                        "; select c.cartID, c.menuItemID,c.shopID,c.quantity,m.menuItemDescription,c.cartItemPrice "+
                        "from mblCart c INNER JOIN mblMenuItem m ON c.menuItemID = m.menuItemID";
                Statement sm = conn.createStatement();
                ResultSet rs = sm.executeQuery(query);
                message = "item Deleted";
                while (rs.next()) {
                    int x = Integer.parseInt(rs.getString(1));
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("key1", x);
                    editor.commit();

                }
            }
        } catch (SQLException e) {

            message = "SQLException!!!!";
        }

        return message;

    }
}
