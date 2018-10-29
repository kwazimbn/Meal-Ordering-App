package com.example.a213506699.foodordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    TextView txtRegister;
    ConnectionClass connectionClass;
    EditText editTextMail, editTextPass;
    private static final String TAG = "Login";
    String loginEmail, loginPassword;
    Button btnLogin;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectionClass = new ConnectionClass();

        editTextMail = findViewById(R.id.txtEmailLogin);
        editTextPass = findViewById(R.id.txtPassword);



        txtRegister = findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmail = editTextMail.getText().toString();
                loginPassword = editTextPass.getText().toString();
                DoLogin doLogin = new DoLogin(loginEmail, loginPassword);
                doLogin.execute("");
            }
        });
    }

    public class DoLogin extends AsyncTask<String, String, String> {
        String message;
        String email;
        String password;
        String name;
        boolean status;


        public DoLogin(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (status) {
                startActivity(new Intent(getApplicationContext(),Account.class));
            }
            else {
                Toast.makeText(getApplicationContext(),"Check your credentials",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Connection conn = connectionClass.CONN();
                if (conn == null) {
                    message = "Error in Connection with SQL Server";
                } else {
                    //Reading cakes from Database using SQL Command on the Client device
                    String query = "select * from mblUser where eMail = '" + email + "' and userPassword = '" + password + "'";
                    Statement sm = conn.createStatement();
                    ResultSet rs = sm.executeQuery(query);
                    while (rs.next()) {
                        status = true;
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("loginUserID",rs.getInt(1));
                        editor.putInt("loginUserRoleID",rs.getInt(2));
                        editor.putString("loginUserName",rs.getString(3));
                        editor.putString("loginUserCell",rs.getString(4));
                        editor.putString("loginUserEmail",rs.getString(5));
                        editor.putString("loginUserPassword",rs.getString(6));
                        editor.putInt("status",1);
                        editor.commit();
                    }
                }
            } catch (SQLException e) {


                Log.e(TAG, "doInBackground: " + e.getMessage());
            }

            return message;
        }
    }
}
