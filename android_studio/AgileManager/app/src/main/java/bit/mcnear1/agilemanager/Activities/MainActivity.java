package bit.mcnear1.agilemanager.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import bit.mcnear1.agilemanager.Fragments.DashboardFragment;
import bit.mcnear1.agilemanager.Fragments.NavigationDrawerFragment;
import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.SwappablePage;

public class MainActivity extends ActionBarActivity implements SwappablePage {
    protected Toolbar mToolbar;
    protected FragmentManager mFragmentManager;
    protected FrameLayout mPageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getFragmentManager();
        mPageContainer = (FrameLayout)findViewById(R.id.page_container);

        setupToolbar();
        setupDrawer();

        //Setting to first page is activity is just starting
        if(savedInstanceState == null)
        {
            updateCurrentPage(new DashboardFragment());
        }
    }

    @Override
    public void onBackPressed() {
        //Checking if there is any fragments on the stack
        if(mFragmentManager.getBackStackEntryCount() > 1) {
            //If there are fragments on the stack popping of the last fragment
            mFragmentManager.popBackStack();
        }else
        {
            //If there is no fragments on the stack just calling the normal on back pressed that
            // will go back to the last activity
            super.onBackPressed();
        }
    }

    private void setupToolbar() {
        //Setting toolbar to be my custom toolbar
        mToolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        //Setting the toolbar to show the hamburger
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setupDrawer()
    {

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById((R.id.fragment_navigation_drawer));
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        drawerFragment.setup(R.id.fragment_navigation_drawer,mToolbar, drawerLayout);
    }

    @Override
    public void updateCurrentPage(Fragment newPage) {

        //Starting a transaction to add the new page fragment
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        //Replacing the current page fragment with the new one
        ft.replace(mPageContainer.getId(), newPage, "currentPage");
        //Adding the transaction that add the new page to the stack
        ft.addToBackStack(null);
        //Committing the new changes
        ft.commit();

    }
}
