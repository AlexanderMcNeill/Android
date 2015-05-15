package bit.mcnear1.agilemanager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    protected Toolbar toolbar;
    protected SharedPreferences sharedPrefs;
    protected FrameLayout pageContainer;
    protected int currentPageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupDrawer();

        sharedPrefs = getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);
        pageContainer = (FrameLayout)findViewById(R.id.pageContainer);

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("currentPageID", currentPageId);
    }

    public void setupToolbar()
    {
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setupDrawer()
    {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById((R.id.fragment_navigation_drawer));
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        drawerFragment.setup(R.id.fragment_navigation_drawer,toolbar, drawerLayout);
    }
}
