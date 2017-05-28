package mx.unam.primera.com.appmoviles;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.onClick;
import mx.unam.primera.com.logic.*;
import mx.unam.primera.com.model.Event;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Description.OnFragmentInteractionListener, AllEvents.OnFragmentInteractionListener
{

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //// Easter Egg de Natalia
        final MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(this,R.raw.blaze);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(100,100);

        Runnable rn = new Runnable()
        {
            @Override
            public void run()
            {
                mediaPlayer.start();
            }
        };

        Thread music = new Thread(rn);
        //music.start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigate(R.id.nav_all);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        return navigate(id);
    }

    private boolean navigate(int id)
    {
        fragment = null;
        fragment = new AllEvents();
        Bundle args = new Bundle();

        switch (id)
        {
            case R.id.nav_all:
                args.putSerializable("EventType", -1);
                break;
            case R.id.nav_americano:
                args.putSerializable("EventType", 1);
                break;
            case R.id.nav_soccer:
                args.putSerializable("EventType", 2);
                break;

            case R.id.nav_bascket:
                args.putSerializable("EventType", 3);
                break;

            case R.id.nav_basebaññ:
                args.putSerializable("EventType", 4);
                break;

            case R.id.nav_conciertos:
                args.putSerializable("EventType", 5);
                break;

            case R.id.nav_event:
                args.putSerializable("EventType", 6);
                break;

            case R.id.nav_fav:
                fragment = new Description();
                break;
        }
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.principal,fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void navigate(Fragment frag)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.principal,frag).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
