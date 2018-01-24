package com.example.nancy.gasleak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
    TextView signup;
    Button signin;
    EditText name, password;
    String user, pass, user1, pass1;
    Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        signup = (TextView) findViewById(R.id.tv_signup);
        signin = (Button) findViewById(R.id.email_sign_in_button);

        name = (EditText) findViewById(R.id.edt_mobile);
        password = (EditText) findViewById(R.id.edt_password);
        signin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Intent intent = new Intent(Login.this, Home.class);
                // startActivity(intent);
                user = name.getText().toString();
                pass = password.getText().toString();
                Log.d("username", user);
                Log.d("password", pass);

				/*
				 * lati=String.valueOf(lat); longi=String.valueOf(lon);
				 * if(lati.equals(null)||longi.equals(null)){ lati="71.000112";
				 * longi="92.123343";
				 */
                // }
                new QuerySQL().execute(user, pass);
            }

        });
        signup.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

            }
        });
    }

    public class QuerySQL extends AsyncTask<String, Void, Boolean> {

        ProgressDialog pDialog;
        Exception error;
        ResultSet rs;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Authentication");
            pDialog.setMessage("Verifying your credentials...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {

            user1 = new String(args[0]);
            pass1 = new String(args[1]);

            try {

                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(
                        "jdbc:mysql://10.1.24.146:3306/gas", "aaa", "aaa");
            } catch (SQLException se) {
                Log.e("ERRO1", se.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e("ERRO2", e.getMessage());
            } catch (Exception e) {
                Log.e("ERRO3", e.getMessage());
            }

            try {
                String COMANDOSQL = "select * from register where username='"
                        + user1 + "' && password='" + pass1 + "'";
                Statement statement = conn.createStatement();
                rs = statement.executeQuery(COMANDOSQL);
                if (rs.next()) {
                    // String COMANDOSQL1 = "update register  where username='"
                    // + user1 + "' && password='" + pass1 + "'";
                    // Statement statement1 = conn.createStatement();
                    // statement1.executeUpdate(COMANDOSQL1);
                    return true;
                }

                return false;

                // Toast.makeText(getBaseContext(),
                // "Successfully Inserted.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                error = e;
                return false;
                // Toast.makeText(getBaseContext(),"Successfully Registered...",
                // Toast.LENGTH_LONG).show();
            }

        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Boolean result1) {
            pDialog.dismiss();
            if (result1) {

                // System.out.println("ELSE(JSON) LOOP EXE");
                try {// try3 open

                    Intent intent = new Intent(Login.this, Home.class);

                    intent.putExtra("loginuser", user1);
                    intent.putExtra("password", pass1);

                    startActivity(intent);

                } catch (Exception e1) {
                    Toast.makeText(getBaseContext(), e1.toString(),
                            Toast.LENGTH_LONG).show();

                }

            } else {
                if (error != null) {
                    Toast.makeText(getBaseContext(),
                            error.getMessage().toString(), Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getBaseContext(),
                            "Check your credentials!!!", Toast.LENGTH_LONG)
                            .show();
                }
            }
            super.onPostExecute(result1);
        }

    }
}



