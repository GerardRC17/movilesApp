package mx.novaterra.mobile.com.appmoviles;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Description.OnFragmentInteractionListener, AllEvents.OnFragmentInteractionListener
{

    Fragment fragment;
    List<Integer> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        history = new ArrayList<Integer>();

        if(!isTablet())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
        {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigate(R.id.nav_all);
    }

    private boolean isTablet()
    {
        return (this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            /*if(getFragmentManager().getBackStackEntryCount() > 2) {
                getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
            }*/

            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rLayoutEventDetails);
            if (rl.getVisibility() == View.VISIBLE)
                rl.setVisibility(View.GONE);
            else
            {
                try
                {
                    if (history.size() > 0) {
                        int _type = history.get(history.size() - 2);
                        int _id = 0;
                        Log.d("Id", String.valueOf(_type));
                        history.remove(history.size() - 1);
                        switch (_type)
                        {
                            case -1:
                                _id = R.id.nav_all;
                                break;
                            case 1:
                                _id=R.id.nav_americano;
                                break;
                            case 2:
                                _id=R.id.nav_soccer;
                                break;
                            case 3:
                                _id=R.id.nav_bascket;
                                break;
                            case 4:
                                _id=R.id.nav_basebaññ;
                                break;
                            case 5:
                                _id=R.id.nav_conciertos;
                                break;
                            case 6:
                                _id=R.id.nav_event;
                                break;
                        }
                        navigate(_id);
                    }
                }
                catch (Exception ex)
                {
                    Log.e("Navegación de historial", ex.getMessage());
                }
            }
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

        int newNav = 0;

        switch (id)
        {
            case R.id.nav_all:
                args.putSerializable("EventType", -1);
                newNav = -1;
                break;
            case R.id.nav_americano:
                args.putSerializable("EventType", 1);
                newNav = 1;
                break;
            case R.id.nav_soccer:
                args.putSerializable("EventType", 2);
                newNav = 2;
                break;

            case R.id.nav_bascket:
                args.putSerializable("EventType", 3);
                newNav = 3;
                break;

            case R.id.nav_basebaññ:
                args.putSerializable("EventType", 4);
                newNav = 4;
                break;

            case R.id.nav_conciertos:
                args.putSerializable("EventType", 5);
                newNav = 5;
                break;

            case R.id.nav_event:
                args.putSerializable("EventType", 6);
                newNav = 6;
                break;
        }
        fragment.setArguments(args);

        navigate(R.id.rLayoutEventList, fragment);

        try
        {
            if (history.get(history.size() - 1) != newNav)
                history.add(newNav);
            Log.d("Historial", String.valueOf(history.size()));
        }
        catch (Exception ex)
        {
            Log.d("History", "Historial vacío");
            history.add(newNav);
        }

        return true;
    }

    public void navigate(int replaced, Fragment frag) {

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rLayoutEventDetails);
        if (replaced == R.id.rLayoutEventList)
            rl.setVisibility(View.GONE);
        else
            rl.setVisibility(View.VISIBLE);

        /*getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(replaced, frag)
                .commit();*/

        getSupportFragmentManager().beginTransaction().replace(replaced, frag)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        /*RelativeLayout rlD =(RelativeLayout)findViewById(R.id.rLayoutEventDetails);
        rlD.setVisibility(View.GONE);*/



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
