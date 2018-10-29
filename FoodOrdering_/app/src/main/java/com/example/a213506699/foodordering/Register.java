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
import android.widget.Toast;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    ConnectionClass connectionClass;
    EditText editTextUname, editTextUeMail, editTextUphone, editTextUpassword, editTextUcPassword;
    String uName, uEmail, uPhone, uPassword, uCpassword;
    Button btnSignUp;
    String rec, subject, textMessage;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        connectionClass = new ConnectionClass();

        editTextUname = findViewById(R.id.Rtxt_uname);
        editTextUeMail = findViewById(R.id.R_txtREmailLogin);
        editTextUphone = findViewById(R.id.ReditTextPhone);
        editTextUpassword = findViewById(R.id.RtxtRPassword);
        editTextUcPassword = findViewById(R.id.RtxtRcPassword);

        btnSignUp = findViewById(R.id.btnRSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uName = editTextUname.getText().toString();
                uEmail = editTextUeMail.getText().toString();
                uPhone = editTextUphone.getText().toString();
                uPassword = editTextUpassword.getText().toString();
                uCpassword = editTextUcPassword.getText().toString();

                if(!uEmail.contains("@")){
                    Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();
                    editTextUeMail.setText("");

                }
                else if (!uPassword.equals(uCpassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords missmatch", Toast.LENGTH_LONG).show();
                    editTextUcPassword.setText("");
                    editTextUpassword.setText("");
                }else if(uPassword.length()<5||uCpassword.length()<5){
                    Toast.makeText(getApplicationContext(), "Password too short", Toast.LENGTH_LONG).show();
                    editTextUcPassword.setText("");
                    editTextUpassword.setText("");
                }

                else {
                    DoRegister doRegister = new DoRegister(uName, uEmail, uPhone, uPassword, uCpassword);
                    doRegister.execute("");

                }


                subject = "Successful Registration";
                textMessage = "Dear " + uName + "\nWe are thrilled to be joined by you today in our lovely family. Satisfying your hunger is our goal." +
                        "\nPlease take note of your email : " + uEmail + " and password : " + uPassword + " as you need them to access our Foodie App";

                //Log.i("MyActivity", "MyClass.getView() — i assign this issue to " + employeeID);
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

            }


        });

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

    public class DoRegister extends AsyncTask<String, String, String> {
        String message;
        boolean status;
        String uName, uEmail, uPhone, uPassword, uCpassword, error;

        public DoRegister(String uName, String uEmail, String uPhone, String uPassword, String uCpassword) {
            this.uName = uName;
            this.uEmail = uEmail;
            this.uPhone = uPhone;
            this.uPassword = uPassword;
            this.uCpassword = uCpassword;


        }

        @Override

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            if (status) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Complete_order.class));
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();
                String query = "";
                if (conn == null) {
                    message = "Error in Connection with SQL Server";
                } else {
                    //Reading cakes from Database using SQL Command on the Client device
                    if (uPassword.equals(uCpassword)) {
                        query = " insert into mblUser(userRoleID,userName,cellNo,eMail,userPassword) " +
                                " values(1,'" + uName + "','" + uPhone + "','" + uEmail + "','" + uPassword + "');" +
                                "select * from mblUser where eMail = '" + uEmail + "' and userPassword = '" + uPassword + "'";
                        message = "Registered annd Logged in";
                    } else {
                        query = "";
                        message = "Passwords Missmatch!!!";
                        status = false;
                        return message;
                    }
                    Statement sm = conn.createStatement();
                    ResultSet rs = sm.executeQuery(query);
                    while (rs.next()) {
                        status = true;
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("loginUserID", rs.getInt(1));
                        editor.putInt("loginUserRoleID", rs.getInt(2));
                        editor.putString("loginUserName", rs.getString(3));
                        editor.putString("loginUserCell", rs.getString(4));
                        editor.putString("loginUserEmail", rs.getString(5));
                        editor.putString("loginUserPassword", rs.getString(6));
                        editor.putBoolean("status", true);
                        editor.commit();
                    }
                }
            } catch (SQLException e) {

                message = "SQLException!!!!";
                Log.e(TAG, "doInBackground: " + e.getMessage());
            }

            return message;
        }
    }
}
