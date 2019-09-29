package com.finalproject.kitchenhelper.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateRosterFragment extends Fragment {

    private ProgressBar progressBar;
    private Button buttonSetAvailability;
    private TextView sunFrom,monFrom,tueFrom,wedFrom,thuFrom,friFrom,satFrom;
    private TextView sunTo,monTo,tueTo,wedTo,thuTo,friTo,satTo;
    private TextView userName;
    private String server_url_get = "https://everestelectricals.com.au/kitchen_helper/get_availability.php";
    private String name,email;
    private Bundle args;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_roaster, container, false);
        linkDesignItems();
        args = this.getArguments();
        if (args != null) {
            if (!args.getString("name").isEmpty()){
                name = args.getString("name");
                userName.setText("Creating Roster For: "+name.toUpperCase());
            }
            if (!args.getString("email").isEmpty()){
                email = args.getString("email");
            }
        }
        getCurrentAvailability();
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void getCurrentAvailability() {
        StringRequest request = new StringRequest(Request.Method.POST, server_url_get,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("Create Roster", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("availability");
                            JSONObject jo = array.getJSONObject(0);
                            if(jo.getString("Sunday").trim().equalsIgnoreCase("no")) {
                                    sunFrom.setClickable(false);
                                    sunFrom.setText("N/A");
                                    sunTo.setClickable(false);
                                    sunTo.setText("N/A");
                            }
                            if(jo.getString("Monday").trim().equalsIgnoreCase("no")) {
                                monFrom.setClickable(false);
                                monFrom.setText("N/A");
                                monTo.setClickable(false);
                                monTo.setText("N/A");
                            }
                            if(jo.getString("Tuesday").trim().equalsIgnoreCase("no")) {
                                tueFrom.setClickable(false);
                                tueFrom.setText("N/A");
                                tueTo.setClickable(false);
                                tueTo.setText("N/A");
                            }
                            if(jo.getString("Wednesday").trim().equalsIgnoreCase("no")) {
                                wedFrom.setClickable(false);
                                wedFrom.setText("N/A");
                                wedTo.setClickable(false);
                                wedTo.setText("N/A");
                            }
                            if(jo.getString("Thursday").trim().equalsIgnoreCase("no")) {
                                thuFrom.setClickable(false);
                                thuFrom.setText("N/A");
                                thuTo.setClickable(false);
                                thuTo.setText("N/A");
                            }
                            if(jo.getString("Friday").trim().equalsIgnoreCase("no")) {
                                friFrom.setClickable(false);
                                friFrom.setText("N/A");
                                friTo.setClickable(false);
                                friTo.setText("N/A");
                            }
                            if(jo.getString("Saturday").trim().equalsIgnoreCase("no")) {
                                satFrom.setClickable(false);
                                satFrom.setText("N/A");
                                satTo.setClickable(false);
                                satTo.setText("N/A");
                            }

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.INVISIBLE);
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
                params.put("email", email);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public void linkDesignItems() {
        sunFrom = view.findViewById(R.id.sundayStartAdmin);
        sunTo = view.findViewById(R.id.sundayEndAdmin);
        monFrom = view.findViewById(R.id.mondayStartAdmin);
        monTo = view.findViewById(R.id.mondayEndAdmin);
        tueFrom = view.findViewById(R.id.tuesdayStartAdmin);
        tueTo = view.findViewById(R.id.tuesdayEndAdmin);
        wedFrom = view.findViewById(R.id.wednesdayStartAdmin);
        wedTo = view.findViewById(R.id.wednesdayEndAdmin);
        thuFrom = view.findViewById(R.id.thursdayStartAdmin);
        thuTo = view.findViewById(R.id.thursdayEndAdmin);
        friFrom = view.findViewById(R.id.fridayStartAdmin);
        friTo = view.findViewById(R.id.fridayEndAdmin);
        satFrom = view.findViewById(R.id.saturdayStartAdmin);
        satTo = view.findViewById(R.id.saturdayEndAdmin);
        userName = view.findViewById(R.id.textViewCreateRoasterUserName);
        progressBar = view.findViewById(R.id.progressBarCreateRoster);
    }
}
