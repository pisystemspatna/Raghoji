package com.pisystem.raghoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MobileRegistration extends AppCompatActivity {
    private Button btn_register;
    private Toolbar toolbar;
    private TextView mobileNumber;
    private String strmobileNumber;
    private Context mContext=MobileRegistration.this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_registration);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Mobile Registration");
        btn_register = (Button)findViewById(R.id.btn_register);
        mobileNumber=(TextView)findViewById(R.id.et_mobile);
        strmobileNumber=mobileNumber.getText().toString();
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if("".equals(mobileNumber.getText().toString()))
                {
                    Toast.makeText(mContext, "Please provide valid mobile number", Toast.LENGTH_LONG).show();
                }else {
                    registerMobile();

                }
            }
        });
    }

    private void registerMobile() {
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "please wait....", false, false);
        String API = "http://www.pactive.co.in/api/Account/SendOTP?mobileno=" + mobileNumber.getText().toString() +  "&apiKey=RaghoJiApiKey";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(API, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                progressDialog.dismiss();

                JSONObject json = null;
                try {

                    json = jsonArray.getJSONObject(0);
                    String result = json.getString("Output");
                    String resp=json.getString("Response");
                    if ("success".equalsIgnoreCase(resp)) {

                        SharedPreferences.Editor sp = getSharedPreferences("otp", Context.MODE_PRIVATE).edit();
                        sp.putString("otp", result);
                        sp.commit();
                        SharedPreferences.Editor sp1 = getSharedPreferences("regMobile", Context.MODE_PRIVATE).edit();
                        sp1.putString("regMobile", mobileNumber.getText().toString());
                        sp1.commit();
                        SharedPreferences sp2=getSharedPreferences("regMobile", Context.MODE_PRIVATE);
                        String islogin=sp2.getString("regMobile","0");
                        Intent verifyintent = new Intent(mContext, OtpVerification.class);
                        verifyintent.putExtra("mobileNumber", mobileNumber.getText().toString());
                        startActivity(verifyintent);
                    } else {
                        Toast.makeText(mContext, "Please enter correct Mobile number", Toast.LENGTH_LONG).show();
                        // progressDialog.dismiss();
                        mobileNumber.requestFocus();

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
