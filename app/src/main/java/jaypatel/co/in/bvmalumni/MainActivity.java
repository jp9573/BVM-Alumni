package jaypatel.co.in.bvmalumni;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;

import jaypatel.co.in.bvmalumni.AboutUs.AboutUs;
import jaypatel.co.in.bvmalumni.DevelopedBy.CyeCoders;
import jaypatel.co.in.bvmalumni.DevelopedBy.Maintainers;
import jaypatel.co.in.bvmalumni.Folder.FolderMainActivity;
import jaypatel.co.in.bvmalumni.Profile.MyProfile;
import jaypatel.co.in.bvmalumni.News.News;
import jaypatel.co.in.bvmalumni.EventNotification.Notifications;
import jaypatel.co.in.bvmalumni.Suggestions.Suggestions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    Class fragmentClass = null;
    Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        info = new Info(this);
        FirebaseMessaging.getInstance().subscribeToTopic("general");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();

        if (savedInstanceState == null) {
            if (bundle != null) {
                navigationView.getMenu().getItem(2).setChecked(true);
                fragmentClass = Notifications.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                setTitle("Event Notifications");
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
//            } else {
//                navigationView.getMenu().getItem(0).setChecked(true);
//                fragmentClass = MyProfile.class;
//                try {
//                    fragment = (Fragment) fragmentClass.newInstance();
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                setTitle("My Profile");
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
//            }
            } else {
                navigationView.getMenu().getItem(0).setChecked(true);
                fragmentClass = Home.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                setTitle("Home");
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            System.exit(0);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            if (getTitle().toString().equals("Home")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure do you want to exit?").setPositiveButton("YES", dialogClickListener)
                        .setNegativeButton("NO", dialogClickListener).show();
            }
            else
                goHome();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClass = Home.class;
            setTitle("Home");
        } else if (id == R.id.nav_profile) {
            fragmentClass = MyProfile.class;
            setTitle("My Profile");
        } else if (id == R.id.nav_news) {
            fragmentClass = News.class;
            setTitle("Event News");
        } else if (id == R.id.nav_notifications) {
            fragmentClass = Notifications.class;
            setTitle("Event Notifications");
        } else if (id == R.id.nav_gallery) {
            //fragmentClass = Gallery.class;
            //setTitle("Gallery");
            Intent intent = new Intent(this, FolderMainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_suggestions) {
            fragmentClass = Suggestions.class;
            setTitle("Suggestions");
        } else if (id == R.id.nav_aboutUs) {
            fragmentClass = AboutUs.class;
            setTitle("About Us");
        } else if (id == R.id.nav_logout) {
            info.dataBase.logout();
            SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = pref.edit();
            edt.putBoolean("activity_executed", false);
            edt.commit();

            Intent intent = new Intent(this, StartActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
            startActivity(intent);
        } else if (id == R.id.nav_DevelopedBy) {
            Intent intent = new Intent(this, CyeCoders.class);
            startActivity(intent);
        } else if (id == R.id.nav_MaintainedBy) {
            Intent intent = new Intent(this, Maintainers.class);
            startActivity(intent);
        }
        try {
            if (fragmentClass != null)
                fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ProfClick(View view) {
        try {
            fragment = MyProfile.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setTitle("My Profile");
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void newsClick(View view) {
        try {
            fragment = News.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setTitle("Event News");

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void notiClick(View view) {
        try {
            fragment = Notifications.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setTitle("EventNotifications");

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void galClick(View view) {
        setTitle("Gallery");
        Intent intent = new Intent(this, FolderMainActivity.class);
        startActivity(intent);
    }

    public void goHome() {
        try {
            fragment = Home.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setTitle("Home");

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
