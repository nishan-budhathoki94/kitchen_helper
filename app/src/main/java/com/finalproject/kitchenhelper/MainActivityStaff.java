package com.finalproject.kitchenhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.finalproject.kitchenhelper.Fragments.ChangePasswordFragment;
import com.finalproject.kitchenhelper.Fragments.EditDeatilsFragment;
import com.finalproject.kitchenhelper.Fragments.SetAvailabilityFragment;
import com.finalproject.kitchenhelper.Fragments.ViewRosterAdminFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityStaff extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView displayName, displayEmail;
    private long backPressed;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_staff);
        Toolbar toolbar = findViewById(R.id.toolbarStaff);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_staff);
        NavigationView navigationView = findViewById(R.id.nav_view_staff);
        View navHeader = navigationView.getHeaderView(0);
        displayName = navHeader.findViewById(R.id.displayUserName);
        displayEmail = navHeader.findViewById(R.id.displayEmail);
        if(getIntent().hasExtra("name")) {
            displayName.setText(getIntent().getStringExtra("name"));
        }
        if(getIntent().hasExtra("email")) {
            displayEmail.setText(getIntent().getStringExtra("email"));
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff,new ViewRosterAdminFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_staff);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed + 2000 > System.currentTimeMillis()){
                backToast.cancel();
                super.onBackPressed();
                return;
            }
            else {
                backToast= Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressed = System.currentTimeMillis();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_admin_view_roaster) {
            // Handle the camera action
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff,new ViewRosterAdminFragment()).commit();
        } else if (id == R.id.nav_staff_change_password) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff,new ChangePasswordFragment()).commit();

        } else if (id == R.id.nav_staff_set_availability) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff,new SetAvailabilityFragment()).commit();

        }  else if (id == R.id.nav_staff_edit_details) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new EditDeatilsFragment()).commit();
        }

        else if (id == R.id.nav_staff_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intentLogout = new Intent(this, Login.class);
            intentLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentLogout);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_staff);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
