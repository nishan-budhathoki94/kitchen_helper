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
import com.finalproject.kitchenhelper.UserListAdapter;
import com.finalproject.kitchenhelper.Users;
import com.finalproject.kitchenhelper.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserListFragment extends Fragment {

    private RecyclerView recyclerViewUserLIst;
    private View view;
    private UserListAdapter adapter;
    private ArrayList<Users> userList;
    private Users singleUser;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_userlist, container, false);
        progressBar = view.findViewById(R.id.progressBarUserList);
        recyclerViewUserLIst = view.findViewById(R.id.recyclerViewUSerList);
        recyclerViewUserLIst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new UserListAdapter(userList,this.getContext());
        recyclerViewUserLIst.setAdapter(adapter);
        if (userList.size()>0) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fetchUserList();
        super.onCreate(savedInstanceState);
    }


    //fetch all the users from database
    public void fetchUserList(){
        String server_url = "https://everestelectricals.com.au/kitchen_helper/getuser_list.php";
        userList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, server_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("users");
                            Log.d("jsonResponse",response.toString());
                            for(int i=0;i<jsonArray.length();i++) {
                                singleUser = new Users();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                singleUser.setName(jsonObject.getString("Name").trim());
                                singleUser.setEmail(jsonObject.getString("Email").trim());
                                singleUser.setType(jsonObject.getString("type").trim());
                                userList.add(singleUser);
                            }

                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("Volley", "Cannot connect to database");
                    }
                }) {

        };

        VolleySingleton.getInstance(this.getContext()).addToRequestQueue(request);
    }


}
