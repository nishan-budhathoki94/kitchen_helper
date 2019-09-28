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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.kitchenhelper.Fragments.CreateRosterFragment;

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
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder{
        private TextView name,email;
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
        }
    }
}
