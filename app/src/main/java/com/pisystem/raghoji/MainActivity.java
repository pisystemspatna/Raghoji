package com.pisystem.raghoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:Deepak Sharma
 * This is the main class for the application and will be called on launch of the application.The class will do client side validation
 * blank fields,password composition etc. and server side validation like validating if user has entered valid userId and password.
 * If the entered UserId or password is found to be valid ,The control will go to the Dashboard .
 */

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button login_button;
    private Button forgotPassword_btn;
    private Context mContext = MainActivity.this;
    private EditText et_login, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        login_button = (Button) findViewById(R.id.btn_login);
        forgotPassword_btn = (Button) findViewById(R.id.btn_forgotpassword);
        et_login = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(et_login.getText().toString()) || "".equals(et_password.getText().toString())) {
                    Toast.makeText(mContext, "Please provide Username and Password", Toast.LENGTH_LONG).show();
                    et_login.requestFocus();
                } else {
                    validateUser();
                   Intent dashboardIntent = new Intent(mContext, DashBoard.class);
                    //startActivity(dashboardIntent);
                  //  Intent verifyintent=new Intent(mContext,FarmerRegistration.class);
                   // startActivity(verifyintent);

              }
           }
        });
        forgotPassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboardIntent = new Intent(mContext, LandDetails.class);
                startActivity(dashboardIntent);
            }
        });
    }

    private void validateUser() {
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "please wait....", false, false);
        String API = "http://www.pactive.co.in/api/Account/Login?userName=" + et_login.getText().toString() + "&pass=" + et_password.getText().toString() + "&apiKey=RaghoJiApiKey";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(API, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                progressDialog.dismiss();

                JSONObject json = null;
                try {

                    json = jsonArray.getJSONObject(0);
                    String result = json.getString("Response");
                    String userId=json.getString("Output");

                    if ("success".equalsIgnoreCase(result)) {

                        SharedPreferences.Editor sp = getSharedPreferences("usermobile", Context.MODE_PRIVATE).edit();
                        sp.putString("mobile", et_login.getText().toString());
                        sp.commit();
                        SharedPreferences.Editor sp1 = getSharedPreferences("userid", Context.MODE_PRIVATE).edit();
                        sp1.putString("userid", userId);
                        sp1.commit();
                        Intent dashboardIntent = new Intent(mContext, DashBoard.class);
                        startActivity(dashboardIntent);
                        } else {
                        Toast.makeText(mContext, "Please provide correct userName and Password", Toast.LENGTH_LONG).show();
                       // progressDialog.dismiss();
                        et_login.requestFocus();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                volleyError.printStackTrace();
                Toast.makeText(mContext, "" + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(mContext);
        queue.add(jsonArrayRequest);
    }
}

