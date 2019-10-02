package com.finalproject.kitchenhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RosterAdapter extends RecyclerView.Adapter<RosterAdapter.RosterViewHolder> {

    private ArrayList<RosterData> rosterDataList;
    private Context mContext;

    public RosterAdapter(ArrayList<RosterData> rosterDataList, Context context) {
        this.rosterDataList = rosterDataList;
        mContext = context;
    }
    @NonNull
    @Override
    public RosterAdapter.RosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_create_roster,parent,false);
        return new RosterAdapter.RosterViewHolder(view);
    }

    public void splitStartEnd(TextView start, TextView end, String wholeString) {
        if (wholeString.equalsIgnoreCase(Constants.NOT_APPLICABLE) || wholeString.equalsIgnoreCase(Constants.DAY_OFF) || wholeString.equalsIgnoreCase(Constants.SELECT_TIME)){
            start.setText(wholeString);
            end.setText(wholeString);
        }
        else {
            start.setText(wholeString.substring(0,8));
            end.setText(wholeString.substring(9,17));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RosterViewHolder holder, int position) {
        final RosterData rdata = rosterDataList.get(position);
        holder.buttonCreateRoster.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.GONE);
        holder.email.setText("Email:"+rdata.getEmail());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        holder.layout.setLayoutParams(params);
        splitStartEnd(holder.sunFrom,holder.sunTo,rdata.getSun());
        splitStartEnd(holder.monFrom,holder.monTo,rdata.getMon());
        splitStartEnd(holder.tueFrom,holder.tueTo,rdata.getTue());
        splitStartEnd(holder.wedFrom,holder.wedTo,rdata.getWed());
        splitStartEnd(holder.thuFrom,holder.thuTo,rdata.getThu());
        splitStartEnd(holder.friFrom,holder.friTo,rdata.getFri());
        splitStartEnd(holder.satFrom,holder.satTo,rdata.getSat());


    }


    @Override
    public int getItemCount() {
        return rosterDataList.size();
    }

    public class RosterViewHolder extends RecyclerView.ViewHolder{
        private TextView email,sunFrom,monFrom,tueFrom,wedFrom,thuFrom,friFrom,satFrom;
        private TextView sunTo,monTo,tueTo,wedTo,thuTo,friTo,satTo,userName;
        private ProgressBar progressBar;
        private Button buttonCreateRoster;
        private ConstraintLayout layout;
        public RosterViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.textViewCreateRoasterUserName);
            sunFrom = itemView.findViewById(R.id.sundayStartAdmin);
            sunTo = itemView.findViewById(R.id.sundayEndAdmin);
            monFrom = itemView.findViewById(R.id.mondayStartAdmin);
            monTo = itemView.findViewById(R.id.mondayEndAdmin);
            tueFrom = itemView.findViewById(R.id.tuesdayStartAdmin);
            tueTo = itemView.findViewById(R.id.tuesdayEndAdmin);
            wedFrom = itemView.findViewById(R.id.wednesdayStartAdmin);
            wedTo = itemView.findViewById(R.id.wednesdayEndAdmin);
            thuFrom = itemView.findViewById(R.id.thursdayStartAdmin);
            thuTo = itemView.findViewById(R.id.thursdayEndAdmin);
            friFrom = itemView.findViewById(R.id.fridayStartAdmin);
            friTo = itemView.findViewById(R.id.fridayEndAdmin);
            satFrom = itemView.findViewById(R.id.saturdayStartAdmin);
            satTo = itemView.findViewById(R.id.saturdayEndAdmin);
            userName = itemView.findViewById(R.id.textViewCreateRoasterUserName);
            progressBar = itemView.findViewById(R.id.progressBarCreateRoster);
            buttonCreateRoster = itemView.findViewById(R.id.buttonCreateRoster);
            layout = itemView.findViewById(R.id.layoutCreateRosterAdmin);
        }
    }
}
