package com.pisystem.raghoji;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.api.client.util.DateTime;
import com.pisystem.raghoji.utils.GpsTracker;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class FarmerRegistration extends AppCompatActivity {
    Bitmap bitmap;
    ByteArrayOutputStream out;
    ImageView imv,uploadimg,farmerImage;
    EditText etAddress,fileUri,noofplots,landcultivated,nocows,nobuffaloes,nogoats,fpondacre;
    GpsTracker gps;
    HttpEntity resEntity;
    Context mContext=(FarmerRegistration.this);
    double addresslatitude,addresslongitude;
    Button uploadButton,submitButton;
    Spinner caste,sub_caste,district,block,panchayat,village;
    Uri file;
    EditText farmerName,fatherName,fullAddress,aadharno,altmobno,govtRegno;
    RadioGroup gender;
    private Toolbar toolbar;
    String userId,selectDistrictId,selectBlockId,selectPanchayatId,selectVillageId;
    String pathfile;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Farmer Registration");
//Initialize Handles
        initilizeHandles();
        //Load District Dropdowns
        loadItem(district,"0","district");
        //Load Caste dropdowns
        loadCaste(caste);
        //Check and Grant Permissions for Camera ,Location etc Usage.
        checkPermission();
        caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {

                //String category = arg0.getItemAtPosition(arg2).toString();
                TextView tvid=(TextView)arg1.findViewById(R.id.tv_id);
                TextView tvname=(TextView)arg1.findViewById(R.id.tv_name);

              loadSubCaste(tvid.getText().toString());
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                 // TODO Auto-generated method stub
             }}
            );
           district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                           long arg3) {

                    //String district = arg0.getItemAtPosition(arg2).toString();
                    TextView tvid=(TextView)arg1.findViewById(R.id.tv_id);
                    TextView tvname=(TextView)arg1.findViewById(R.id.tv_name);
                    if (tvname!=null) {
                        selectDistrictId=tvid.getText().toString();
                        loadItem(block, tvname.getText().toString(), "block");
                    }
                }
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }}
            );

            block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {

                //String district = arg0.getItemAtPosition(arg2).toString();
                TextView tvid=(TextView)arg1.findViewById(R.id.tv_id);
                TextView tvname=(TextView)arg1.findViewById(R.id.tv_name);

                if(tvname!=null) {
                    selectBlockId=tvid.getText().toString();
                    loadItem(panchayat, tvname.getText().toString(), "panchayat");
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }}
        );
        panchayat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {

                //String district = arg0.getItemAtPosition(arg2).toString();
                TextView tvid=(TextView)arg1.findViewById(R.id.tv_id);
                TextView tvname=(TextView)arg1.findViewById(R.id.tv_name);

                if(tvname!=null) {
                    selectPanchayatId=tvid.getText().toString();
                    loadItem(village, tvname.getText().toString(), "village");
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }}
        );
        //Call to populate address automatically
        imv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                locationSet(etAddress);
            }
        });

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File fl=getOutputMediaFile();
                    file = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", fl);
                    pathfile=fl.toString();
                    // file = Uri.fromFile(getOutputMediaFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                    startActivityForResult(intent, 100);
                }
            });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
                new SubmitOperation().execute();
            }
        });
        }

    private void validateFields() {
        if("".equals(farmerName.getText().toString())){
            Toast.makeText(mContext, "Famer Name cannot be blank", Toast.LENGTH_LONG).show();
            farmerName.requestFocus();
            return;
        }else if("".equals(fatherName.getText().toString())){
            Toast.makeText(mContext, "FatherName cannot be blank", Toast.LENGTH_LONG).show();
            farmerName.requestFocus();
            return;
        }else if("".equalsIgnoreCase(aadharno.getText().toString())||(aadharno.getText().toString().length()!=12))
        {
            Toast.makeText(mContext, "Please enter 12 digits correct aadhar no.", Toast.LENGTH_LONG).show();
            aadharno.requestFocus();
            return;
        }
        else if("".equalsIgnoreCase(altmobno.getText().toString())||(altmobno.getText().toString().length()!=10))
        {
            Toast.makeText(mContext, "Please enter correct mobile no. of 10 digits", Toast.LENGTH_LONG).show();
            altmobno.requestFocus();
            return;
        }
        else if("".equalsIgnoreCase(fullAddress.getText().toString()))
        {
            Toast.makeText(mContext, "Please enter address details", Toast.LENGTH_LONG).show();
            fullAddress.requestFocus();
            return;
        }
    }


    private void initilizeHandles() {
        SharedPreferences sp1 = getSharedPreferences("userid", Context.MODE_PRIVATE);
        userId=sp1.getString("userid","0");
        uploadimg=(ImageView)findViewById(R.id.upload);
        imv=(ImageView)findViewById(R.id.imageView3);
         //farmerImage=(ImageView)findViewById(R.id.iv_farmerview);
       // etAddress=(EditText)findViewById(R.id.editText);
     //   uploadButton=(Button)findViewById(R.id.uploadton);
        fileUri=(EditText)findViewById(R.id.imagepath);
        caste=(Spinner)findViewById(R.id.spnr_caste);
        sub_caste=(Spinner)findViewById(R.id.spnr_subcaste);
        district=(Spinner)findViewById(R.id.spnr_distirict);
        block=(Spinner)findViewById(R.id.spnr_block);
        panchayat=(Spinner)findViewById(R.id.spnr_panchayat);
        village=(Spinner)findViewById(R.id.spnr_village);
        fullAddress =(EditText)findViewById(R.id.et_fulladdress);
        aadharno=(EditText)findViewById(R.id.et_aadharno);
        altmobno=(EditText)findViewById(R.id.et_altmobno);
        submitButton=(Button)findViewById(R.id.submit_button);
        farmerName=(EditText)findViewById(R.id.et_farmerName);
        fatherName=(EditText)findViewById(R.id.et_fatherName);
        govtRegno=(EditText)findViewById(R.id.et_govtregisno);
        gender=(RadioGroup) findViewById(R.id.radio_gender);
         }


    private void loadItem( final Spinner itemSpinner,final String filter,final String indicator) {
       // final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "please wait....", false, false);
        String API=null ;
        if("district".equalsIgnoreCase(indicator)) {
            API = "http://www.pactive.co.in/api/CommonMethods/GetDIstrictList?stateId=1&zoneId=1&include_SelectAllOption=false";
        }else if("block".equalsIgnoreCase(indicator)){ ArrayList<HashMap<String, String>> itemList=new ArrayList<HashMap<String,String>>();
            HashMap<String, String> items=null;
            API = "http://www.pactive.co.in/api/CommonMethods/GetBlockList_?district="+filter+"&include_SelectAllOption=false";
        }else if("panchayat".equalsIgnoreCase(indicator))
        {
            API = "http://www.pactive.co.in/api/CommonMethods/GetPanchayatList_?block="+filter+"&include_SelectAllOption=false";
        }else if("village".equalsIgnoreCase(indicator))
        {
            API = "http://www.pactive.co.in/api/CommonMethods/GetVillageList_?panchayat="+filter+"&include_SelectAllOption=false";
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(API, new Response.Listener<JSONArray>() {
            ArrayList<HashMap<String, String>> itemList=new ArrayList<HashMap<String,String>>();
            HashMap<String, String> items=null;
            @Override
            public void onResponse(JSONArray jsonArray) {
         //       progressDialog.dismiss();

                JSONObject json = null;
                try {
                      for(int i=0;i<jsonArray.length();i++){
                          items=new HashMap<String,String>();
                        if("district".equalsIgnoreCase(indicator)) {
                            items.put("Id",jsonArray.getJSONObject(i).getString("Id"));
                            items.put("type",jsonArray.getJSONObject(i).getString("District"));
                        } else if("block".equalsIgnoreCase(indicator))
                        {
                            items.put("Id",jsonArray.getJSONObject(i).getString("Id"));
                            items.put("type",jsonArray.getJSONObject(i).getString("Block"));
                        } else if("panchayat".equalsIgnoreCase(indicator))
                        {
                            items.put("Id",jsonArray.getJSONObject(i).getString("Id"));
                            items.put("type",jsonArray.getJSONObject(i).getString("Panchayat"));
                        }else if("village".equalsIgnoreCase(indicator))
                        {
                            items.put("Id",jsonArray.getJSONObject(i).getString("Id"));
                            items.put("type",jsonArray.getJSONObject(i).getString("Village"));
                        }
                          itemList.add(i,items);
                    }

                    ListAdapter adapter = new SimpleAdapter(mContext,
                            itemList, R.layout.spinner_two_item, new String[]{"id","type"},
                            new int[]{R.id.tv_id,R.id.tv_name});
                    /*ArrayAdapter<String> adp1 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_list_item_1,0,itemList);*/
                    //adapter.setDropDownViewResource(R.layout.spinner_two_item);
                    itemSpinner.setAdapter((SpinnerAdapter) adapter);
                    itemSpinner.setSelection(itemList.indexOf(0));
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //progressDialog.dismiss();
                volleyError.printStackTrace();
                Toast.makeText(mContext, "" + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(mContext);
        queue.add(jsonArrayRequest);
    }


    private void loadSubCaste(String category) {
        //final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "please wait....", false, false);
        String API = "http://www.pactive.co.in/api/CommonMethods/GetCasteList?categoryId="+category+"&include_SelectAllOption=false";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(API, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
          //      progressDialog.dismiss();

                JSONObject json = null;
                try {
                    List subCasteList=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        subCasteList.add(jsonArray.getJSONObject(i).getString("Caste"));
                    }
                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_list_item_1,0,subCasteList);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sub_caste.setAdapter(adp1);
                    sub_caste.setSelection(0);
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
              //  progressDialog.dismiss();
                volleyError.printStackTrace();
                Toast.makeText(mContext, "" + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(mContext);
        queue.add(jsonArrayRequest);
    }



    private void loadCaste(final Spinner caste) {
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "please wait....", false, false);
        String API = "http://www.pactive.co.in/api/CommonMethods/GetCategoryList?include_SelectAllOption=false";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(API, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                ArrayList<HashMap<String, String>> itemList=new ArrayList<HashMap<String,String>>();
                HashMap<String, String> items=null;
                progressDialog.dismiss();

                JSONObject json = null;
                try {
                    List casteList=new ArrayList<>();
                  for(int i=0;i<jsonArray.length();i++){
                      items=new HashMap<String, String>();
                      items.put("Id",jsonArray.getJSONObject(i).getString("Id"));
                      items.put("type",jsonArray.getJSONObject(i).getString("Category"));
                      itemList.add(i,items);

                     // casteList.add(jsonArray.getJSONObject(i).getString("Category"));
                  }
                    ListAdapter adapter = new SimpleAdapter(mContext,
                            itemList, R.layout.spinner_two_item, new String[]{"Id","type"},
                            new int[]{R.id.tv_id,R.id.tv_name});
                    /*ArrayAdapter<String> adp1 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_list_item_1,0,itemList);*/
                    //adapter.setDropDownViewResource(R.layout.spinner_two_item);
                    caste.setAdapter((SpinnerAdapter) adapter);
                    caste.setSelection(itemList.indexOf(0));

                    /*ArrayAdapter<String> adp1 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_list_item_1,0,casteList);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
                   // caste.setAdapter(adp1);
                    //caste.setSelection(0);
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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
           // uploadButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // uploadButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // uploadButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.INTERNET, Manifest.permission.RECEIVE_SMS }, 0);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // uploadButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }

    private void locationSet(EditText etaddress)
    {
        gps = new GpsTracker(mContext);

        // check if GPS enabled
        if(gps.canGetLocation()){

            addresslatitude = gps.getLatitude();
            addresslongitude = gps.getLongitude();

            //////Cobvert Lat long to address
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(mContext, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(addresslatitude, addresslongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addresses!=null&&addresses.get(0)!=null) {
                String address ="";
                if(addresses.get(0).getMaxAddressLineIndex()==1||addresses.get(0).getMaxAddressLineIndex()==0){
                       address = address+" "+addresses.get(0).getAddressLine(0)   ;
                }    else {
                    for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                        address = address + " " + addresses.get(0).getAddressLine(i); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    }
                  }
                 fullAddress.setText(address);
               }else{
                Toast.makeText(mContext, "Unable to retrieve address from GPS ,Please fill manually", Toast.LENGTH_LONG).show();
            }
            /*String city = addresses.get(0).getLocality();
            Sring state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            String sublocality = addresses.get(0).getSubLocality();
*/
            //Toast.makeText(mContext,address,Toast.LENGTH_LONG).show();

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ Math.random() + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       try {
           if (requestCode == 100) {
               if (resultCode == RESULT_OK) {
                   fileUri.setText(pathfile);
                   uploadimg.setImageResource(R.drawable.uploaded);
                   uploadimg.requestFocus();
                   bitmap = BitmapFactory.decodeFile(pathfile);
                   out=new ByteArrayOutputStream();
                   bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                   farmerImage.setImageBitmap(bitmap);

                   //pathfile=getOutputMediaFile().toString();
               }
           }
       }catch(Exception e){
           e.printStackTrace();;
       }
    }
    public void doFileUpload() {


        String urlString = "http://www.pactive.co.in/api/Farmer/InsertFarmer";
        //String urlString = "http://localhost:3236/api/Farmer/InsertFarmer";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            post.addHeader("Content-type","application/x-www-form-urlencoded");
            BasicNameValuePair farmerNamepair=new BasicNameValuePair("Farmer",farmerName.getText().toString());
            BasicNameValuePair fatherNamepair=new BasicNameValuePair("Father",fatherName.getText().toString());
            BasicNameValuePair aadharNopair=new BasicNameValuePair("AadharNo",aadharno.getText().toString());
            BasicNameValuePair altmobNopair=new BasicNameValuePair("AltContactNo",altmobno.getText().toString());
            BasicNameValuePair genderPair =null;
            if(gender.getCheckedRadioButtonId()==R.id.radio_male) {
                 genderPair = new BasicNameValuePair("Gender", "Male");
            } else{
                   genderPair = new BasicNameValuePair("Gender", "Female");
            }
         //   BasicNameValuePair categoryPair=new BasicNameValuePair("Category",caste.getSelectedItem().toString());
          //  BasicNameValuePair castePair=new BasicNameValuePair("Caste",sub_caste.getSelectedItem().toString());
            BasicNameValuePair addressPair=new BasicNameValuePair("Address",fullAddress.getText().toString());
            //BasicNameValuePair districtPair=new BasicNameValuePair("DIstrict",district.getSelectedItem().toString());
            //BasicNameValuePair blockPair=new BasicNameValuePair("Block",block.getSelectedItem().toString());
            //BasicNameValuePair panchayatPair=new BasicNameValuePair("Panchayat",panchayat.getSelectedItem().toString());
           // double radnum=Math.random();
           //String regnumStr= ((Double) radnum).toString();
            BasicNameValuePair regNumberPair=new BasicNameValuePair("RegistrationNo","");
            //BasicNameValuePair casteIdPair=new BasicNameValuePair("CasteId","3");
            BasicNameValuePair regMobilePair=new BasicNameValuePair("ContactNo",getSharedPreferences("regMobile", Context.MODE_PRIVATE).getString("regMobile","0"));
            BasicNameValuePair govRegNumberPair=new BasicNameValuePair("GovRegistrationNo",govtRegno.getText().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date dt=new Date();
            String date=formatter.format(dt);

            //BasicNameValuePair entryDatePair=new BasicNameValuePair("EntryDate", date);
            BasicNameValuePair entryByPair=new BasicNameValuePair("EntryById", getSharedPreferences("userid", Context.MODE_PRIVATE).getString("userid","0"));
            HashMap<String,String> villItem=(HashMap<String, String>) village.getSelectedItem();
            String villId=villItem.get("Id");
            String villageName=villItem.get("type");
            BasicNameValuePair villageIdPair=new BasicNameValuePair("VillageId",villId);
            BasicNameValuePair villagePair=new BasicNameValuePair("RevenueVillage",villageName);
            HashMap<String,String> casteItem=(HashMap<String, String>) caste.getSelectedItem();
            String casteId=casteItem.get("Id");
            BasicNameValuePair casteIdPair=new BasicNameValuePair("CasteId",casteId);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            HashMap<String,String> blockItem=(HashMap<String, String>) block.getSelectedItem();
            String blockId=blockItem.get("Id");
            HashMap<String,String> panchayatItem=(HashMap<String, String>) panchayat.getSelectedItem();
            String panchayatId=panchayatItem.get("Id");
            HashMap<String,String> districtItem=(HashMap<String, String>) district.getSelectedItem();
            String districtId=panchayatItem.get("Id");
            nameValuePairList.add(farmerNamepair);
            nameValuePairList.add(fatherNamepair);
            nameValuePairList.add(aadharNopair);
            nameValuePairList.add(altmobNopair);
           // nameValuePairList.add(categoryPair);
            nameValuePairList.add(genderPair) ;
            //nameValuePairList.add(castePair);
            //nameValuePairList.add(districtPair);
            //nameValuePairList.add(blockPair);
            //nameValuePairList.add(panchayatPair);
            nameValuePairList.add(villagePair);
            nameValuePairList.add(addressPair);
            nameValuePairList.add(regMobilePair);
            nameValuePairList.add(regNumberPair);
            nameValuePairList.add(casteIdPair);
            nameValuePairList.add(govRegNumberPair);
            nameValuePairList.add(entryByPair);
            nameValuePairList.add(villageIdPair);
            //nameValuePairList.add(entryDatePair);


            /*MultipartEntity reqEntity = new MultipartEntity();


            reqEntity.addPart("Farmer", new StringBody(farmerName.getText().toString().trim()));

            reqEntity.addPart("Father", new StringBody(fatherName.getText().toString().trim()));
            reqEntity.addPart("AadharNo", new StringBody(aadharno.getText().toString().trim()));


            reqEntity.addPart("AltContactNo", new StringBody(altmobno.getText().toString()));
            // reqEntity.addPart("Student_studentsurname",new StringBody("CHayan"));

            //   reqEntity.addPart("Student_class", new StringBody(StudentClass.getText().toString()));
            reqEntity.addPart("GovRegistrationNo", new StringBody(govtRegno.getText().toString()));

            *//*if (capturedimg == null || capturedimg.equals("NONE")) {

                reqEntity.addPart("ProfileImage", new StringBody("NONE"));

            } else {

                reqEntity.addPart("ProfileImage", new FileBody(new File(capturedimg)));

            }

            if (capturedimg2 == null || capturedimg2.equals("NONE")) {

                reqEntity.addPart("AddressProof", new StringBody("NONE"));
            } else {

                reqEntity.addPart("AddressProof", new FileBody(new File(capturedimg2)));
            }

            if (capturedimg3 == null || capturedimg3.equals("NONE")) {

                reqEntity.addPart("DOBProof", new StringBody("NONE"));
            } else {

                reqEntity.addPart("DOBProof", new FileBody(new File(capturedimg3)));
            }

            if (capturedimg4 == null || capturedimg4.equals("NONE")) {

                reqEntity.addPart("Admiissionform", new StringBody("NONE"));
            } else {

                reqEntity.addPart("Admiissionform", new FileBody(new File(capturedimg4)));
            }

            if (capturedimg5 == null || capturedimg5.equals("NONE")) {

                reqEntity.addPart("StudparentForm", new StringBody("NONE"));
            } else {

                reqEntity.addPart("StudparentForm", new FileBody(new File(capturedimg5)));
            }
*/
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);

            post.setEntity(urlEncodedFormEntity);

         //   Log.e(TAG, "doFileUpload: " + post.getRequestLine());

            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);

            //  System.out.println("EDIT-PROFILE-USER-ID"+prefs.getString("USER-ID", ""));

            System.out.println("EDIT-PROFILE-response" + response_str);

            if(!("false".equalsIgnoreCase(response_str)))
            {
                Intent dashboardIntent = new Intent(mContext, DashBoard.class);
                dashboardIntent.putExtra("RegNo",response_str);
                startActivity(dashboardIntent);
            }



            //JSONObject json = new JSONObject(response_str);

            /*if (json.getString("message").equals("success")) {
                Toast.makeText(getActivity(), "" + json.getString("message"), Toast.LENGTH_LONG).show();
            }
*/

        } catch (Exception ex) {
           ex.printStackTrace();
        }

    }
    private class SubmitOperation extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

          //  progressDialog = new ProgressDialog(mContext);
            //progressDialog.setMessage("Please wait...");
           // progressDialog.setCancelable(true);
            //progressDialog.show();

        }


        @Override
        protected Void doInBackground(Void... params) {


            doFileUpload();
           // doImageUpload();


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

//            progressDialog.dismiss();
      //      getFragmentManager().popBackStack();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void doImageUpload() {
        try {
            URL url = new URL("http://www.pactive.co.in/api/Farmer/InsertFarmer");
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setDoInput(true);
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.connect();
            OutputStream output =  c.getOutputStream();
            bitmap = BitmapFactory.decodeFile(pathfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.close();
            Scanner result = new Scanner(c.getInputStream());
            String response = result.nextLine();
            result.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ImageUploader", "Error uploading image", e);
        }
    }
}

