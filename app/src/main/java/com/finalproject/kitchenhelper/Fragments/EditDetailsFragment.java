package com.finalproject.kitchenhelper.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditDetailsFragment extends Fragment {

    private TextInputLayout textInputPhone,textInputName;
    String name,phone;
    ProgressBar progressbar;
    Button buttonUpdateDeatils;
    private FirebaseAuth mAuth;
    private View view;
    private String server_url = "https://everestelectricals.com.au/kitchen_helper/getuser.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_details, container, false);
        mAuth = FirebaseAuth.getInstance();
        textInputPhone = view.findViewById(R.id.textInputLayoutEditPhone);
        textInputName = view.findViewById(R.id.textInputLayoutEditName);
        progressbar = view.findViewById(R.id.progressBarEditDetails);
        buttonUpdateDeatils = view.findViewById(R.id.buttonEditDetails);
        getCurrentDetails();
        buttonUpdateDeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
               editUserDetails();
            }
        });
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void editUserDetails(){
        String update_url = "https://everestelectricals.com.au/kitchen_helper/edit_user.php";
        textInputPhone.setError(null);
        textInputName.setError(null);
        if (validateName() && validatePhone() && validateSimilarity()) {
            StringRequest request = new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressbar.setVisibility(View.INVISIBLE);
                    if (response.contains("Details")) {
                        Toast.makeText(getActivity(), "User Details Edited Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong please check your email address again", Toast.LENGTH_LONG).show();
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Volley", "Cannot connect to database" + error);
                    progressbar.setVisibility(View.INVISIBLE);
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", mAuth.getCurrentUser().getEmail());
                    params.put("name", textInputName.getEditText().getText().toString().trim());
                    params.put("phone", textInputPhone.getEditText().getText().toString().trim());
                    return params;
                }
            };

            VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }

    }
    public void getCurrentDetails() {
        StringRequest request = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            progressbar.setVisibility(View.INVISIBLE);
                            Log.d("json response", "type"+response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("user");
                            JSONObject jo = array.getJSONObject(0);
                            name = jo.getString("name").trim();
                            phone = jo.getString("phone").trim();
                            textInputName.getEditText().setText(name);
                            textInputPhone.getEditText().setText(phone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "Cannot connect to database"+error);
                        progressbar.setVisibility(View.INVISIBLE);
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

    //validate phone
    public boolean validatePhone() {
        String phoneInput = textInputPhone.getEditText().getText().toString().trim();

        if(phoneInput.isEmpty()) {
            textInputPhone.setError("Phone Field cannot be empty");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        else {
            textInputPhone.setError(null);
            return true;
        }
    }

    //validate phone
    public boolean validateName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        if(nameInput.isEmpty()) {
            textInputPhone.setError("Name cannot be empty");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        else {
            textInputPhone.setError(null);
            return true;
        }
    }

    //validate phone
    public boolean validateSimilarity() {
        String nameInput = textInputName.getEditText().getText().toString().trim();
        String phoneInput = textInputPhone.getEditText().getText().toString().trim();

        if(nameInput.equalsIgnoreCase(name) && phoneInput.equalsIgnoreCase(phone)) {
            textInputName.setError("Value not changed");
            textInputPhone.setError("Value not changed");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        textInputPhone.setError(null);
        return true;
    }
}
