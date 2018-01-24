package com.example.nancy.gasleak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends Activity {

    Spinner city;
    EditText name, mob, email, uname, pass, conpass;
    String name1, mob1, email1, uname1, pass1, conpass1, city1;
    Button save;
    Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        name = (EditText) findViewById(R.id.signup_editText_name);
        // mob = (EditText) findViewById(R.id.signup_editText_mobileNumber);
        email = (EditText) findViewById(R.id.signup_editText_email);
        uname = (EditText) findViewById(R.id.signup_editText_username);
        pass = (EditText) findViewById(R.id.signup_editText_password);
        save = (Button) findViewById(R.id.signup_button_signup);

        String[] state = { "Ariyalur", "Chennai", "Coimbatore", "Cuddalore",
                "Dharmapuri", "Dindigul", "Erode", "Kanchipuram", "Karur",
                "Krishnagiri", "Madurai", "Nagapattinam", "Nagercoil",
                "Namakkal", "Perambalur", "Pudukkottai", "Ramanathapuram",
                "Salem", "Sivagangai", "Thanjavur", "Theni", "Thiruvallur",
                "Thiruvarur", "Thoothukudi", "Tiruchirappalli", "Tirunelveli",
                "Tiruppur", "Tiruvannamalai", "Udagamandalam (Ootacamund)",
                "Vellore", "Vilupuram", "Virudhunagar" };
        city = (Spinner) findViewById(R.id.signup_city);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapter_state);

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                name1 = name.getText().toString();
                // mob1 = mob.getText().toString();
                email1 = email.getText().toString();
                uname1 = uname.getText().toString();
                pass1 = pass.getText().toString();

                //conpass1 = conpass.getText().toString();
                city1 = city.getSelectedItem().toString();
                try {
                    new QuerySQL().execute();

                } catch (Exception e) {
                    Log.e("ERRO", e.getMessage());
                }
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

            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Registration");
            pDialog.setMessage("Registering your credentials...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {

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

                Statement statement = conn.createStatement();
                int success = statement
                        .executeUpdate("insert into register values('" + name1
                                + "','" + email1 + "','" + uname1 + "','"
                                + pass1 + "','" + city1 + "')");

                if (success >= 1) {
                    // successfully created product

                    return true;
                    // closing this screen
                    // finish();
                } else {
                    // failed to create product
                    return false;
                }

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

                Toast.makeText(getBaseContext(),
                        "Successfully created your credentials.",
                        Toast.LENGTH_LONG).show();

                // System.out.println("ELSE(JSON) LOOP EXE");
                try {// try3 open

                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);

                } catch (Exception e1) {
                    Toast.makeText(getBaseContext(), e1.toString(),
                            Toast.LENGTH_LONG).show();

                }

            } else {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    Log.d("Error not null...", error.toString());
                } else {
                    Toast.makeText(getBaseContext(),
                            "Not crreated your credentials!!!",
                            Toast.LENGTH_LONG).show();
                }
            }
            super.onPostExecute(result1);
        }
    }
}