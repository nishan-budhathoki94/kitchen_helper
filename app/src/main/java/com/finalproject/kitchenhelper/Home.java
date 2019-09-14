package com.finalproject.kitchenhelper;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    //instantiate to avoid null exception
    private String type = "employee", name;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progressBar = findViewById(R.id.progressBarHome);
        int multiFlag = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(multiFlag);
        ConstraintLayout noInternetLayout = findViewById(R.id.layoutNoInternet);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (!hasInternet())
        {
            noInternetLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        }
        //if user was previously logged in
        if( mUser!= null) {
            Log.d("Home", "use was logged in");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUpUser();
                }
            },1000);

        }
        else {
            //delaying the activity as splash screen
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                }
            }, 2000);
        }


    }

    //check if the device has internet connection
    public boolean hasInternet(){
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() ){
            return true;
        }
        return false;
    }

    //restart the activity
    public void btnRetryClick(View view){
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void setUpUser(){
        String server_url = "https://everestelectricals.com.au/kitchen_helper/getuser.php";
        StringRequest request = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("home", "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("user");
                            JSONObject jo = array.getJSONObject(0);
                            type = jo.getString("type").trim();
                            name = jo.getString("name").trim();


                        } catch (JSONException e) {
                            Toast.makeText(Home.this,"Error in database connection",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                        if (type.equalsIgnoreCase(Constants.TYPE_ADMIN)){
                            Intent adminPanel = new Intent(Home.this, MainActivityAdmin.class);
                            adminPanel.putExtra("name",name);
                            adminPanel.putExtra("email",mUser.getEmail());
                            adminPanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(adminPanel);
                        }
                        else{
                            Intent employee = new Intent(Home.this, MainActivityStaff.class);
                            employee.putExtra("name",name);
                            employee.putExtra("email",mUser.getEmail());
                            employee.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(employee);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "Cannot connect to database");
                        Toast.makeText(Home.this,"Error in database connection",Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mUser.getEmail());
                return params;
            }
        };
        VolleySingleton.getInstance(Home.this).addToRequestQueue(request);
    }

}
