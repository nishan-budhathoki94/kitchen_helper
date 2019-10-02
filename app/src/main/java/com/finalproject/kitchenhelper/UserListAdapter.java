package com.finalproject.kitchenhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.finalproject.kitchenhelper.Fragments.CreateRosterFragment;
import com.finalproject.kitchenhelper.Fragments.SetAvailabilityFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {
    private List<Users> users;
    private Context mContext;

    public UserListAdapter(ArrayList<Users> users, Context context) {
        this.users = users;
        mContext = context;
    }
    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, int position) {
        final Users user = users.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.createRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRosterFragment createRosterFragment = new CreateRosterFragment();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle args = new Bundle();
                args.putString("email",user.getEmail());
                args.putString("name",user.getName());
                createRosterFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, createRosterFragment, "createRoasterFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.viewAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAvailabilityFragment viewAvailabilityFragment = new SetAvailabilityFragment();
                AppCompatActivity activityViewAvailability = (AppCompatActivity) v.getContext();
                Bundle argsViewOnly = new Bundle();
                argsViewOnly.putBoolean("isViewOnly",true);
                argsViewOnly.putString("name",user.getName());
                viewAvailabilityFragment.setArguments(argsViewOnly);
                activityViewAvailability.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, viewAvailabilityFragment, Constants.DialogFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        //disable the remove button if the user is admin
        if (user.getType().equalsIgnoreCase(Constants.TYPE_ADMIN)) {
            holder.deleteUser.setVisibility(View.GONE);
        }
        else {
            holder.deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    holder.progressBar.setVisibility(View.VISIBLE);
                                        String server_url = "https://everestelectricals.com.au/kitchen_helper/delete_user.php";
                                        StringRequest request = new StringRequest(Request.Method.POST, server_url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.d("DeleteUser",response);
                                                        if (response.contains("Success")) {
                                                            holder.singleUserLayout.setVisibility(View.INVISIBLE);
                                                        }
                                                        else {
                                                            holder.progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                },

                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("Volley", "Cannot connect to database" + error);
                                                        holder.progressBar.setVisibility(View.INVISIBLE);
                                                    }
                                                }) {

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("email", user.getEmail());
                                                return params;
                                            }
                                        };

                                        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder{
        private TextView name,email,deleteUser;
        private ConstraintLayout singleUserLayout;
        private Button createRoster,viewAvailability;
        private ProgressBar progressBar;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewUserNameRow);
            email = itemView.findViewById(R.id.textViewEmailRow);
            deleteUser = itemView.findViewById(R.id.textViewDeleteUser);
            createRoster = itemView.findViewById(R.id.buttonCreateRoster);
            progressBar = itemView.findViewById(R.id.progressBarRow);
            viewAvailability = itemView.findViewById(R.id.buttonViewAvailability);
            singleUserLayout = itemView.findViewById(R.id.singleUserLayout);
        }
    }

}
