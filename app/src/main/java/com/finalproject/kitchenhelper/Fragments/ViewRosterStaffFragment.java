package com.finalproject.kitchenhelper.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.finalproject.kitchenhelper.Constants;
import com.finalproject.kitchenhelper.R;
import com.finalproject.kitchenhelper.VolleySingleton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewRosterStaffFragment extends Fragment {

    private ProgressBar progressBar;
    private Button buttonCreateRoster;
    private TextView sunFrom,monFrom,tueFrom,wedFrom,thuFrom,friFrom,satFrom;
    private TextView sunTo,monTo,tueTo,wedTo,thuTo,friTo,satTo;
    private TextView userName;
    private TableLayout viewRosterLayout;
    private String server_url_get_roster = "https://everestelectricals.com.au/kitchen_helper/get_roster.php";
    private FirebaseAuth mAuth;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_roster, container, false);
        mAuth = FirebaseAuth.getInstance();
        linkDesignItems();
        getCurrentRoster();
        //disable the admin privilege items
        buttonCreateRoster.setVisibility(View.GONE);
        if (viewRosterLayout.getVisibility() == View.VISIBLE) {
            userName.setVisibility(View.GONE);
        }
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        buttonCreateRoster = view.findViewById(R.id.buttonCreateRoster);
        viewRosterLayout = view.findViewById(R.id.tableLayout2);
    }




    public void splitStartEnd(TextView start, TextView end, String wholeString) {
        if (wholeString.equalsIgnoreCase(Constants.NOT_APPLICABLE) || wholeString.equalsIgnoreCase(Constants.SELECT_TIME) || wholeString.contains(Constants.DAY_OFF)){
            start.setText(wholeString);
            end.setText(wholeString);
        }
        else {
            start.setText(wholeString.substring(0,8));
            end.setText(wholeString.substring(8,17));
            start.setTextColor(getResources().getColor(R.color.colorAccent));
            end.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }


    public void getCurrentRoster() {
        StringRequest request = new StringRequest(Request.Method.POST, server_url_get_roster,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("Get Roster", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("roster");
                            JSONObject jo = array.getJSONObject(0);

                            splitStartEnd(sunFrom,sunTo,jo.getString("Sunday").trim());
                            splitStartEnd(monFrom,monTo,jo.getString("Monday").trim());
                            splitStartEnd(tueFrom,tueTo,jo.getString("Tuesday").trim());
                            splitStartEnd(wedFrom,wedTo,jo.getString("Wednesday").trim());
                            splitStartEnd(thuFrom,thuTo,jo.getString("Thursday").trim());
                            splitStartEnd(friFrom,friTo,jo.getString("Friday").trim());
                            splitStartEnd(satFrom,satTo,jo.getString("Saturday").trim());


                        } catch (JSONException e) {
                            viewRosterLayout.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            userName.setText("Your roster has not been set yet");
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
}
