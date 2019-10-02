package com.finalproject.kitchenhelper.Fragments;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateRosterFragment extends Fragment {

    private ProgressBar progressBar;
    private Button buttonCreateRoster;
    private TextView sunFrom,monFrom,tueFrom,wedFrom,thuFrom,friFrom,satFrom;
    private TextView sunTo,monTo,tueTo,wedTo,thuTo,friTo,satTo;
    private TextView userName;
    private String server_url_get_availability = "https://everestelectricals.com.au/kitchen_helper/get_availability.php";
    private String server_url_get_roster = "https://everestelectricals.com.au/kitchen_helper/get_roster.php";
    private String server_url_create_roster = "https://everestelectricals.com.au/kitchen_helper/create_roster.php";
    private String name,email;
    private boolean insertIntoDB = true;
    private Bundle args;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_roster, container, false);
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
        getCurrentRoster();
        getCurrentAvailability();
        timePickerPopUp(sunFrom);
        timePickerPopUp(monFrom);
        timePickerPopUp(tueFrom);
        timePickerPopUp(wedFrom);
        timePickerPopUp(thuFrom);
        timePickerPopUp(friFrom);
        timePickerPopUp(satFrom);
        timePickerPopUp(sunTo);
        timePickerPopUp(monTo);
        timePickerPopUp(tueTo);
        timePickerPopUp(wedTo);
        timePickerPopUp(thuTo);
        timePickerPopUp(friTo);
        timePickerPopUp(satTo);

        buttonCreateRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (isStartAndEndSet(sunFrom,sunTo) && isStartAndEndSet(monFrom,monTo) && isStartAndEndSet(tueFrom,tueTo)
                            && isStartAndEndSet(wedFrom,wedTo) && isStartAndEndSet(thuFrom,thuTo)&& isStartAndEndSet(friFrom,friTo)
                            && isStartAndEndSet(satFrom, satTo)) {
                        createRosterButtonClick();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please set both start and end time",
                                Toast.LENGTH_LONG).show();
                    }
            }
        });

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void getCurrentAvailability() {
        StringRequest request = new StringRequest(Request.Method.POST, server_url_get_availability,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("Create Roster", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("availability");
                            JSONObject jo = array.getJSONObject(0);

                            //disable the textviews if staff is not available
                            if(jo.getString("Sunday").trim().equalsIgnoreCase("no")) {
                                disableTextView(sunFrom,sunTo);
                            }
                            if(jo.getString("Monday").trim().equalsIgnoreCase("no")) {
                                disableTextView(monFrom,monTo);
                            }
                            if(jo.getString("Tuesday").trim().equalsIgnoreCase("no")) {
                                disableTextView(tueFrom,tueTo);
                            }
                            if(jo.getString("Wednesday").trim().equalsIgnoreCase("no")) {
                                disableTextView(wedFrom,wedTo);
                            }
                            if(jo.getString("Thursday").trim().equalsIgnoreCase("no")) {
                                disableTextView(thuFrom,thuTo);
                            }
                            if(jo.getString("Friday").trim().equalsIgnoreCase("no")) {
                                disableTextView(friFrom,friTo);
                            }
                            if(jo.getString("Saturday").trim().equalsIgnoreCase("no")) {
                                disableTextView(satFrom,satTo);
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
        buttonCreateRoster = view.findViewById(R.id.buttonCreateRoster);
    }

    //disable the textviews
    public void disableTextView(TextView From, TextView To) {
        From.setText(Constants.NOT_APPLICABLE);
        To.setText(Constants.NOT_APPLICABLE);
        From.setClickable(false);
        To.setClickable(false);
    }

    public void timePickerPopUp(final TextView selectTime) {
        if (selectTime.isClickable()) {
            selectTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = getTimeFromText(selectTime);
                    if (mcurrentTime == null) {
                        mcurrentTime = Calendar.getInstance();
                    }
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        String format;
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int selectedMinute) {
                            if (hourOfDay == 0) {

                                hourOfDay += 12;

                                format = Constants.AM;
                            }
                            else if (hourOfDay == 12) {

                                format = Constants.PM;

                            }
                            else if (hourOfDay > 12) {

                                hourOfDay -= 12;

                                format = Constants.PM;

                            }
                            else {

                                format = Constants.AM;
                            }
                            selectTime.setText(String.format("%02d:%02d %s",hourOfDay,selectedMinute,format));
                            selectTime.setTextColor(getResources().getColor(R.color.colorAccent));

                        }

                    }, hour, minute, false);//12 hour clock
                    mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener(){
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(getActivity(), "Cancelled",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });

        }
        else {
            Toast.makeText(getActivity(), name+" is not available.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public Calendar getTimeFromText(TextView selectedTime) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
        Calendar calendar = Calendar.getInstance();
        try {
            Date time = timeFormat.parse(selectedTime.getText().toString());
            calendar.setTime(time);

        } catch (ParseException e) {
            e.printStackTrace();
            calendar = null;

        }
        return calendar;
    }

    public boolean isStartAndEndSet(TextView start, TextView end) {
        Log.d("CreateRoster", String.valueOf(start.getCurrentTextColor()+end.getCurrentTextColor()));
       if (start.getCurrentTextColor() == end.getCurrentTextColor() ) {
           return true;
       }
       return false;
    }

    public String addStartEnd(TextView start, TextView end) {
        if (start.getCurrentTextColor() == end.getCurrentTextColor() && start.getCurrentTextColor() == getResources().getColor(R.color.colorAccent) ) {
            return start.getText().toString()+"-"+end.getText().toString();
        }
        else if (start.getText().toString().equalsIgnoreCase(Constants.SELECT_TIME)) {
            return Constants.DAY_OFF;
        }

        else return Constants.NOT_APPLICABLE;
    }

    public void splitStartEnd(TextView start, TextView end, String wholeString) {
        if (wholeString.equalsIgnoreCase(Constants.NOT_APPLICABLE) || wholeString.equalsIgnoreCase(Constants.DAY_OFF) || wholeString.equalsIgnoreCase(Constants.SELECT_TIME)){
            start.setText(wholeString);
            end.setText(wholeString);
        }
        else {
            start.setText(wholeString.substring(0,8));
            end.setText(wholeString.substring(9,17));
            start.setTextColor(getResources().getColor(R.color.colorAccent));
            end.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }


    public void createRosterButtonClick() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, server_url_create_roster,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

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
                params.put("sunday", addStartEnd(sunFrom,sunTo));
                params.put("monday", addStartEnd(monFrom,monTo));
                params.put("tuesday",addStartEnd(tueFrom,tueTo));
                params.put("wednesday", addStartEnd(wedFrom,wedTo));
                params.put("thursday", addStartEnd(thuFrom,thuTo));
                params.put("friday", addStartEnd(friFrom,friTo));
                params.put("saturday", addStartEnd(satFrom,satTo));
                params.put("insert", String.valueOf(insertIntoDB).toLowerCase());
                params.put("email",email);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

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


                            insertIntoDB = false;

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


}
