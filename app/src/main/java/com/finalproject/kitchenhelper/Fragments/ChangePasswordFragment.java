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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    Button btnResetPswd;
    ProgressBar progressBar;
    private TextInputLayout textInputLayoutOldPassword,textInputLayoutOldPasswordRepeat,textInputLayoutNewPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        btnResetPswd = view.findViewById(R.id.buttonChangePassword);
        progressBar = view.findViewById(R.id.progressBarChangePassword);
        textInputLayoutOldPassword = view.findViewById(R.id.textInputLayoutOldPassword);
        textInputLayoutOldPasswordRepeat = view.findViewById(R.id.textInputLayoutOldPasswordRepeat);
        textInputLayoutNewPassword = view.findViewById(R.id.textInputLayoutNewPassword);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        btnResetPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePassword() && validateRepeatPassword()) {
                    final String oldPassword = textInputLayoutOldPasswordRepeat.getEditText().getText().toString().trim();
                    final String newPassword = textInputLayoutNewPassword.getEditText().getText().toString().trim();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(mUser.getEmail(), oldPassword );
                    progressBar.setVisibility(View.VISIBLE);
                    mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "Password Has Been Changed",
                                                    Toast.LENGTH_LONG).show();
                                            Intent loginActivity = new Intent(getActivity(), Login.class);
                                            getActivity().finish();
                                            startActivity(loginActivity);
                                        }
                                        else{
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "Error!! Password Not Updated",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });

                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Something went wrong, please check your old password again.",
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

    //validate password
    public boolean validatePassword() {
        String passwordInput = textInputLayoutNewPassword.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()) {
            textInputLayoutNewPassword.setError("Password cannot be empty");
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
        else if (passwordInput.length()<6) {
            textInputLayoutNewPassword.setError("Password too weak(at least 6 characters)");
            return false;
        }
        else {
            textInputLayoutNewPassword.setError(null);
            return true;
        }
    }

    //validate repeat password
    public boolean validateRepeatPassword() {
        String oldPasswordInput = textInputLayoutOldPasswordRepeat.getEditText().getText().toString().trim();
        String oldPasswordInputRepeat = textInputLayoutOldPasswordRepeat.getEditText().getText().toString().trim();


        if(oldPasswordInput.isEmpty()) {
            textInputLayoutOldPassword.setError("Password cannot be empty");
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
        else if (oldPasswordInputRepeat.isEmpty()) {
            textInputLayoutOldPasswordRepeat.setError("Password cannot be empty");
            return false;
        }
        else if (!


                oldPasswordInput.equals(oldPasswordInputRepeat)){
            textInputLayoutOldPasswordRepeat.setError("Password does not match");
            return false;
        }
        else {
            textInputLayoutNewPassword.setError(null);
            return true;
        }
    }
}
