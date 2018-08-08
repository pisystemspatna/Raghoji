package com.pisystem.raghoji;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LandDetails extends AppCompatActivity {

    private Spinner spnrnumber_of_land,spnr_land_number,spnr_soil_type,spnr_number_of_last_crop_year;
    private EditText edttotal_area,edtland_name,edt_year_soil_test_year,edtlast_crop;

    private TextView txtsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_details);

        spnrnumber_of_land=(Spinner)findViewById(R.id.spnrnumber_of_land);
        spnr_land_number=(Spinner)findViewById(R.id.spnrlandnumber);
        spnr_soil_type=(Spinner)findViewById(R.id.spnrsoil_type);
        spnr_number_of_last_crop_year=(Spinner) findViewById(R.id.spnr_number_of_last_crop_year);

        edttotal_area=(EditText)findViewById(R.id.edttotal_area);
        edtland_name=(EditText)findViewById(R.id.edtland_name);
        edt_year_soil_test_year=(EditText)findViewById(R.id.edtyear_soil_test);
        edtlast_crop=(EditText)findViewById(R.id.edt_last_crop);

        txtsubmit=(TextView)findViewById(R.id.txtsubmit);

        txtsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String TotalArea=edttotal_area.getText().toString();
                String LandName=edtland_name.getText().toString();
                String SoilTestYear=edt_year_soil_test_year.getText().toString();
                String LastCrop=edtlast_crop.getText().toString();
                
                if(TotalArea.equals(""))
                {
                    Toast.makeText(LandDetails.this, "please enter Total area", Toast.LENGTH_SHORT).show();
                }
                else if(LandName.equals("")) {
                    Toast.makeText(LandDetails.this, "Please enter land name", Toast.LENGTH_SHORT).show();
                }
                else if(SoilTestYear.equals("")) {
                    Toast.makeText(LandDetails.this, "Please enter SoilTestYear", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LandDetails.this, "please enter Last crop year", Toast.LENGTH_SHORT).show();
                }
                
                uploadDetails();
            }
        });
    }

    private void uploadDetails() {

        final ProgressDialog progressDialog=ProgressDialog.show(LandDetails.this,"please wait...","",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://www.pactive.co.in/api/FarmerLandDetails/InsertFarmerLandDetails?" +
                "ContactNo=7051136612&IsLandOwner_Farmer=0&LandUniqueNm=1& Area_InAcres=10&LocalName=sky&SoilTypeId=1& WaterLogging=1&Latitude=s&Longitude=b&EntryById=1&IpAddress=120.0&Status=A", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(LandDetails.this, response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(LandDetails.this);
        requestQueue.add(stringRequest);
    }
}
