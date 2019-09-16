package com.finalproject.kitchenhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private String name,phone,type= "employee",password,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        textInputEmail = findViewById(R.id.textInputLayoutLoginEmail);
        textInputPassword = findViewById(R.id.textInputLayoutLoginPassword);
        progressbar = findViewById(R.id.progressBarLogin);
    }

    //validate email
    public boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {
            textInputEmail.setError("Email cannot be empty");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Invalid Email Address");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        else {
            textInputEmail.setError(null);
            return true;
        }
    }

    //validate password
    public boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()) {
            textInputPassword.setError("Password cannot be empty");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        else {
            textInputPassword.setError(null);
            return true;
        }
    }

    //forgot password activity
    public void forgotPasswordLink(View v) {
        Intent forgotPassword = new Intent(this, ForgotPassword.class);
        startActivity(forgotPassword);
    }

    public void loginBtnClick(View v) {
        if(validateEmail() && validatePassword()) {
            progressbar.setVisibility(View.VISIBLE);
            password = textInputPassword.getEditText().getText().toString().trim();
            email = textInputEmail.getEditText().getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("login", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //check if user email is verified
                                if(user.isEmailVerified()) {
                                    String server_url = "https://everestelectricals.com.au/kitchen_helper/getuser.php";
                                    StringRequest request = new StringRequest(Request.Method.POST, server_url,
                                            new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        Log.d("json response", "type"+response);
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        JSONArray array = jsonObject.getJSONArray("user");
                                                        JSONObject jo = array.getJSONObject(0);
                                                        name = jo.getString("name").trim();
                                                        phone = jo.getString("phone").trim();
                                                        type = jo.getString("type").trim();
                                                        Log.d("user type", "type"+type);

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                   if (type.equalsIgnoreCase(Constants.TYPE_ADMIN)){
                                                        progressbar.setVisibility(View.GONE);
                                                        Intent adminPanel = new Intent(Login.this, MainActivityAdmin.class);
                                                        adminPanel.putExtra("name",name);
                                                        adminPanel.putExtra("email",email);
                                                        adminPanel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        finish();
                                                        startActivity(adminPanel);
                                                    }
                                                    else {
                                                        progressbar.setVisibility(View.GONE);
                                                        Intent employee = new Intent(Login.this, MainActivityStaff.class);
                                                        employee.putExtra("name",name);
                                                        employee.putExtra("email",email);
                                                        employee.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        finish();
                                                        startActivity(employee);
                                                    }

                                                }
                                            },

                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("Volley", "Cannot connect to database"+error);
                                                    Toast.makeText(Login.this,"Database Error!!",Toast.LENGTH_LONG).show();
                                                    progressbar.setVisibility(View.INVISIBLE);
                                                }
                                            }) {

                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("email", email);
                                            return params;
                                        }
                                    };

                                    VolleySingleton.getInstance(Login.this).addToRequestQueue(request);
                                }
                                else{
                                    progressbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(Login.this, "Email "+email+" not verified. Please verify your email first",
                                            Toast.LENGTH_LONG).show();
                                }
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                progressbar.setVisibility(View.INVISIBLE);
                                Log.w("login", "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, "Incorrect Username or Password",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
