package com.finalproject.kitchenhelper.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.finalproject.kitchenhelper.Constants;
import com.finalproject.kitchenhelper.Login;
import com.finalproject.kitchenhelper.R;
import com.finalproject.kitchenhelper.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout textInputEmail,textInputPassword,textInputConfirmEmail,textInputPhone,textInputName;
    private String name,phone,email,password;
    ProgressBar progressbar;
    Button buttonSignUp;
    private FirebaseAuth mAuth;
    private View view;
    private String server_url = "https://everestelectricals.com.au/kitchen_helper/signup.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();
        textInputEmail = view.findViewById(R.id.textInputLayoutSignUpEmail);
        textInputPassword = view.findViewById(R.id.textInputLayoutSignUpPassword);
        textInputConfirmEmail = view.findViewById(R.id.textInputLayoutSignUpEmailRepeat);
        textInputPhone = view.findViewById(R.id.textInputLayoutSignUpPhone);
        textInputName = view.findViewById(R.id.textInputLayoutSignUpName);
        progressbar = view.findViewById(R.id.progressBarSignUp);
        buttonSignUp = view.findViewById(R.id.buttonCreateUser);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpBtnClick();
            }
        });
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //call php file from the server to create new user
    public void signUpBtnClick() {
        if(validatePhone()  && validatePassword() && validateConfirmEmail() && validateEmail()  &&  validateName()  )  {
            progressbar.setVisibility(View.VISIBLE);
            name = textInputName.getEditText().getText().toString().trim();
            email = textInputEmail.getEditText().getText().toString().trim();
            password = textInputPassword.getEditText().getText().toString().trim();
            phone = textInputPhone.getEditText().getText().toString().trim();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Firebase", "createUserWithEmail:success"+mAuth.getCurrentUser().getEmail());
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(),"Registration Successful, Please check your email for verification link!",Toast.LENGTH_LONG).show();
                                            //Inject Sql with the data using post method
                                            StringRequest request = new StringRequest(Request.Method.POST, server_url,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            progressbar.setVisibility(View.INVISIBLE);
                                                            textInputName.getEditText().setText("");
                                                            textInputEmail.getEditText().setText("");
                                                            textInputConfirmEmail.getEditText().setText("");
                                                            textInputPassword.getEditText().setText("");
                                                            textInputPhone.getEditText().setText("");
                                                        }
                                                    },

                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            progressbar.setVisibility(View.INVISIBLE);
                                                            Toast.makeText(getContext(), "Error..."+error.toString(), Toast.LENGTH_SHORT).show();
                                                            error.printStackTrace();
                                                        }
                                                    }) {

                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("email", email);
                                                    params.put("type", Constants.TYPE_EMPLOYEE.toLowerCase());
                                                    params.put("phone",phone);
                                                    params.put("name",name);
                                                    return params;
                                                }
                                            };

                                            VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
                                        }
                                        else {
                                            progressbar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getActivity(), "Something went wrong please check your email address again", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                progressbar.setVisibility(View.INVISIBLE);
                                // If sign in fails, display a message to the user.
                                Log.w("Firebase", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Email "+email+" is already registered.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });


        }
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

    //validate email
    public boolean validateConfirmEmail() {
        String confirmEmailInput = textInputConfirmEmail.getEditText().getText().toString().trim();
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if(confirmEmailInput.isEmpty()) {
            textInputConfirmEmail.setError("Email cannot be empty");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(confirmEmailInput).matches()) {
            textInputConfirmEmail.setError("Invalid Email Address");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        else if (!emailInput.equals(confirmEmailInput)) {
            textInputConfirmEmail.setError("Email mismatched");
            progressbar.setVisibility(View.INVISIBLE);
            return false;
        }
        else {
            textInputConfirmEmail.setError(null);
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
        else if (passwordInput.length()<6) {
            textInputPassword.setError("Password too weak(at least 6 characters)");
            return false;
        }
        else {
            textInputPassword.setError(null);
            return true;
        }
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

    @Override
    public void onClick(View v) {

    }
}
