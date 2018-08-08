package com.pisystem.raghoji;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.pisystem.raghoji.adapter.DashBoardViewAdapter;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener {

    private GridView gridView;
    private ArrayList<String> images;
    private ArrayList<String> names;
    private Context mContext=DashBoard.this;
    private Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

         gridView=(GridView)findViewById(R.id.gridView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DashBoard");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        Intent i=getIntent();
        String regNo=i.getStringExtra("RegNo");

        images = new ArrayList<>();
        names = new ArrayList<>();
        images.add("registration");
        images.add("messageHR");
        images.add("messageRP");
        images.add("trsfrfunds");
        images.add("query");
        images.add("debt");

        names.add("Registration");
        names.add("Collect Funds");
        names.add("Today's Task");
        names.add("Transfer Funds");
        names.add("Query");
        names.add("Contact Us");

        DashBoardViewAdapter dbadapter=new DashBoardViewAdapter(this,images,names);
        gridView.setAdapter(dbadapter);
        if(regNo!=null ||!("".equalsIgnoreCase(regNo)))
        {
            Toast.makeText(mContext, "Farmer regd successfull Regno is " +regNo, Toast.LENGTH_LONG).show();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView register=(TextView)findViewById(R.id.tv_register);
                String registerText=register.getText().toString();

                if("registration".equalsIgnoreCase(registerText)){
                    Intent registerIntent=new Intent(mContext,MobileRegistration.class);
                    startActivity(registerIntent);
                }
                else if("debt".equals(registerText)) {
                    startActivity(new Intent(mContext,LandDetails.class));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

     /*   //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart)
        {
            startActivity(new Intent(mContext,Cartlist.class));
            overridePendingTransitionEnter();
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_services)
        {
            startActivity(new Intent(mContext,Services.class));
            overridePendingTransitionEnter();
        }
        else if(id==R.id.nav_myorder){
            startActivity(new Intent(mContext,MyOrder.class));
            overridePendingTransitionEnter();
        }
        else if (id == R.id.nav_myaccount)
        {
            startActivity(new Intent(mContext,UserAccount.class));
            overridePendingTransitionEnter();
        }

        else if (id == R.id.nav_logout) {

            SharedPreferences.Editor splogin=getSharedPreferences("login",Context.MODE_PRIVATE).edit();
            splogin.putBoolean("islogin",false);
            splogin.commit();

            Intent  intentdashboard=new Intent(mContext,Login.class);
            intentdashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentdashboard);
            finish();
            overridePendingTransitionEnter();
        }
        else if (id == R.id.nav_calltohelpdesk)
        {
            String mob="tel:"+"07033292808";
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(mob));
            startActivity(callIntent);
        }
        else if(id==R.id.nav_contactus)
        {
            startActivity(new Intent(mContext,Contactus.class));
            overridePendingTransitionEnter();
        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
