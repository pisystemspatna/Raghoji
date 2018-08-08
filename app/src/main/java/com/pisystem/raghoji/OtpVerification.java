package com.pisystem.raghoji;

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

public class OtpVerification extends AppCompatActivity {
    private Button btn_verify;
    private Toolbar toolbar;
    private TextView otp;
    private String strotp;
    private Context mContext=OtpVerification.this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("OTP Verification");
        btn_verify = (Button)findViewById(R.id.btn_verify);
        otp=(TextView)findViewById(R.id.et_otp);
        btn_verify.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if("".equals(otp.getText().toString())){
                    Toast.makeText(mContext, "Please provide valid otp", Toast.LENGTH_LONG).show();
                }
                SharedPreferences sp = getSharedPreferences("otp", Context.MODE_PRIVATE);
                String otpnumber=(String) sp.getString("otp","otp");
                if(otpnumber.equalsIgnoreCase(otp.getText().toString())){
                    Intent verifyintent=new Intent(mContext,FarmerRegistration.class);
                    startActivity(verifyintent);
                }else{
                    Toast.makeText(mContext, "Please enter correct OTP", Toast.LENGTH_LONG).show();
                    otp.requestFocus();
                }

            }
        });

    }
}
