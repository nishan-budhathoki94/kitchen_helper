package com.finalproject.kitchenhelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.kitchenhelper.Fragments.CreateRosterFragment;
import com.finalproject.kitchenhelper.Fragments.SetAvailabilityFragment;

import java.util.ArrayList;
import java.util.List;

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

        holder.viewAvailbility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAvailabilityFragment viewAvailabilityFragment = new SetAvailabilityFragment();
                AppCompatActivity activityViewAvailability = (AppCompatActivity) v.getContext();
                Bundle argsViewOnly = new Bundle();
                argsViewOnly.putBoolean("isViewOnly",true);
                argsViewOnly.putString("name",user.getName());
                viewAvailabilityFragment.setArguments(argsViewOnly);
                activityViewAvailability.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, viewAvailabilityFragment, "viewAvailabilityFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });

        holder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.singleUserLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder{
        private TextView name,email;
        private ConstraintLayout singleUserLayout;
        private Button deleteUser,createRoster,viewAvailbility;
        private ProgressBar progressBar;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewUserNameRow);
            email = itemView.findViewById(R.id.textViewEmailRow);
            deleteUser = itemView.findViewById(R.id.buttonDeleteUser);
            createRoster = itemView.findViewById(R.id.buttonCreateRoster);
            progressBar = itemView.findViewById(R.id.progressBarRow);
            viewAvailbility = itemView.findViewById(R.id.buttonViewAvailability);
            singleUserLayout = itemView.findViewById(R.id.singleUserLayout);
        }
    }
}
