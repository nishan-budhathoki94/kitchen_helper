package com.finalproject.kitchenhelper.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.finalproject.kitchenhelper.R;
import com.finalproject.kitchenhelper.VolleySingleton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetAvailabilityFragment extends Fragment {

    private CheckBox alldaySun,alldayMon,alldayTue,alldayWed,alldayThurs,allDayFri,allDaySat;
    private ProgressBar progressBar;
    private Button buttonSetAvailability;
    private TextView userName;
    private Bundle args;
    private boolean insertIntoDB = true;
    private FirebaseAuth mAuth;
    private View view;
    private String server_url_get = "https://everestelectricals.com.au/kitchen_helper/get_availability.php";
    private String server_url_set = "https://everestelectricals.com.au/kitchen_helper/set_availability.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_availability, container, false);
        setCheckboxes();
        progressBar = view.findViewById(R.id.progressBarSetAvailability);
        buttonSetAvailability = view.findViewById(R.id.buttonSetAvailability);
        userName = view.findViewById(R.id.textViewNameOfStaff);
        args = this.getArguments();
        if (args!= null) {
            if (args.getBoolean("isViewOnly")) {
                buttonSetAvailability.setVisibility(View.INVISIBLE);
            }
            if (!args.getString("name").isEmpty()) {
                userName.setText("Displaying Availability of:"+args.getString("name"));
                userName.setVisibility(View.VISIBLE);
            }
        }
        mAuth = FirebaseAuth.getInstance();
        getCurrentAvailability();
        buttonSetAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAvailabilityButtonClick();
            }
        });
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setCheckboxes() {
        alldaySun = view.findViewById(R.id.availabilitySundayAllDay);
        alldayMon = view.findViewById(R.id.availabilityMondayAllDay);
        alldayTue = view.findViewById(R.id.availabilityTuesdayAllDay);
        alldayWed = view.findViewById(R.id.availabilityWednesdayAllDay);
        alldayThurs = view.findViewById(R.id.availabilityThursdayAllDay);
        allDayFri = view.findViewById(R.id.availabilityFridayAllDay);
        allDaySat = view.findViewById(R.id.availabilitySaturdayAllDay);
    }

    public void getCurrentAvailability() {
        StringRequest request = new StringRequest(Request.Method.POST, server_url_get,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("json response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("availability");
                            JSONObject jo = array.getJSONObject(0);
                            if(jo.getString("Sunday").trim().equalsIgnoreCase("no")) {
                                alldaySun.setChecked(false);
                            }
                            if(jo.getString("Monday").trim().equalsIgnoreCase("no")) {
                                alldayMon.setChecked(false);
                            }
                            if(jo.getString("Tuesday").trim().equalsIgnoreCase("no")) {
                                alldayTue.setChecked(false);
                            }
                            if(jo.getString("Wednesday").trim().equalsIgnoreCase("no")) {
                                alldayWed.setChecked(false);
                            }
                            if(jo.getString("Thursday").trim().equalsIgnoreCase("no")) {
                                alldayThurs.setChecked(false);
                            }
                            if(jo.getString("Friday").trim().equalsIgnoreCase("no")) {
                                allDayFri.setChecked(false);
                            }
                            if(jo.getString("Saturday").trim().equalsIgnoreCase("no")) {
                                allDaySat.setChecked(false);
                            }
                            insertIntoDB = false;


                        } catch (JSONException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "You have not set your availability yet", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "Cannot connect to database"+error);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mAuth.getCurrentUser().getEmail());
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


    public void setAvailabilityButtonClick() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, server_url_set,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        //restart the activity
//                        Intent intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Error..."+error.toString(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sunday", getCheckboxText(alldaySun));
                params.put("monday", getCheckboxText(alldayMon));
                params.put("tuesday", getCheckboxText(alldayTue));
                params.put("wednesday", getCheckboxText(alldayWed));
                params.put("thursday", getCheckboxText(alldayThurs));
                params.put("friday", getCheckboxText(allDayFri));
                params.put("saturday", getCheckboxText(allDaySat));
                params.put("insert", String.valueOf(insertIntoDB).toLowerCase());
                params.put("email",mAuth.getCurrentUser().getEmail());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);



    }

    public String getCheckboxText(CheckBox checkBox) {
        if (checkBox.isChecked()){
            return "yes";
        }
        else {
            return "no";
        }
    }

}
