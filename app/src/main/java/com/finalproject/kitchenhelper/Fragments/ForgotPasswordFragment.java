package com.finalproject.kitchenhelper.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.kitchenhelper.Login;
import com.finalproject.kitchenhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

    Button btnResetPswd;
    ProgressBar progressBar;
    private TextInputLayout textInputEmail;
    private FirebaseAuth mAuth;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_forgot_password, container, false);
        btnResetPswd = view.findViewById(R.id.buttonResetPassword);
        progressBar = view.findViewById(R.id.progressBarForgotPassword);
        textInputEmail = view.findViewById(R.id.textInputLayoutForgotPassword);
        mAuth = FirebaseAuth.getInstance();
        btnResetPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail()) {
                    final String emailInput = textInputEmail.getEditText().getText().toString().trim();
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(emailInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Password reset link has been sent to" + emailInput + ". Please check your email",
                                        Toast.LENGTH_LONG).show();
                                Intent loginActivity = new Intent(getActivity(), Login.class);
                                getActivity().finish();
                                startActivity(loginActivity);
                            }

                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Something went wrong, please check your email address again.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });{

                    }
                }

            }
        });
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //validate email
    public boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {
            textInputEmail.setError("Email cannot be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Invalid Email Address");
            return false;
        }
        else {
            textInputEmail.setError(null);
            return true;
        }
    }
}
