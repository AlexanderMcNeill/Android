package bit.mcnear1.agilemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by alexmcneill on 15/05/15.
 */
public class NavigationActivity extends ActionBarActivity {
    protected Toolbar toolbar;
    protected FrameLayout pageContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        setupToolbar();
        setupDrawer();

        pageContainer = (FrameLayout) findViewById(R.id.page_container);
    }

    private void setupToolbar() {
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
