package com.finalproject.kitchenhelper.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.finalproject.kitchenhelper.R;
import com.finalproject.kitchenhelper.RosterAdapter;
import com.finalproject.kitchenhelper.RosterData;
import com.finalproject.kitchenhelper.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewRosterAdminFragment extends Fragment {
    private RecyclerView recyclerViewRosterLIst;
    private View view;
    private RosterAdapter adapter;
    private ArrayList<RosterData> rosterDataList;
    private String server_url_get_roster_list = "https://everestelectricals.com.au/kitchen_helper/get_roster_list.php";
    private RosterData rosterDataSingle;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_roster_admin, container, false);
        progressBar = view.findViewById(R.id.progressBarViewRosterAdmin);
        recyclerViewRosterLIst = view.findViewById(R.id.recyclerViewAdminViewRoster);
        recyclerViewRosterLIst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new RosterAdapter(rosterDataList,this.getContext());
        recyclerViewRosterLIst.setAdapter(adapter);
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRosterList();

    }

    public void getRosterList() {
        rosterDataList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, server_url_get_roster_list,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("Get Roster", response.toString());
                            JSONArray jsonArray = response.getJSONArray("roster");
                            for(int i=0;i<jsonArray.length();i++) {
                                rosterDataSingle = new RosterData();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                rosterDataSingle.setEmail(jsonObject.getString("email").trim());
                                rosterDataSingle.setSun(jsonObject.getString("Sunday").trim());
                                rosterDataSingle.setMon(jsonObject.getString("Monday").trim());
                                rosterDataSingle.setTue(jsonObject.getString("Tuesday").trim());
                                rosterDataSingle.setThu(jsonObject.getString("Thursday").trim());
                                rosterDataSingle.setFri(jsonObject.getString("Friday").trim());
                                rosterDataSingle.setSat(jsonObject.getString("Saturday").trim());
                                rosterDataSingle.setWed(jsonObject.getString("Wednesday").trim());
                                rosterDataList.add(rosterDataSingle);
                            }

                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);



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
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}
