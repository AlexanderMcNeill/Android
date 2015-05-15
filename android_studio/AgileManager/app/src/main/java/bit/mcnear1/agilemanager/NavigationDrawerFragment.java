/*
The navigation drawer was made by following slide nerds tutorial.
I added commenting and made the code more modular as i went through the tutorial
https://www.youtube.com/user/slidenerd
 */

package bit.mcnear1.agilemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class NavigationDrawerFragment extends Fragment {

    //Constant string for the file name the drawer preferences will be stored under
    public static final String PREF_FILE_NAME = "drawerPrefs";
    public static final String KEY_LEARNED_DRAWER = "learnedDrawer";


    //The class that will manage the drawers toggle events
    private ActionBarDrawerToggle mDrawerToggle;

    //The layout that contains the drawer
    private DrawerLayout mDrawerLayout;

    //Bool for knowing if the user is already familiar with the drawer
    private boolean mUserLearnedDrawer;

    //Variable for saving the drawer state when rotating
    private boolean mFromSavedInstanceState;


    private View containerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting whether the user has opened the drawer from the drawers shared prefs
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_LEARNED_DRAWER, "false"));

        if(savedInstanceState != null)
        {
            mFromSavedInstanceState = true;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

    }


    public void setup(int drawerID, Toolbar toolbar, DrawerLayout drawerLayout) {

        containerView = getActivity().findViewById(drawerID);
        setupDrawerContent();

        mDrawerLayout = drawerLayout;

        mDrawerToggle = new CustomDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Displaying the drawer to user if they haven't see it before. And not opening it if the app has been rotated
        if(!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
            mDrawerLayout.openDrawer(containerView);
        }
    }

    public void setupDrawerContent()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        Button btnLogout = (Button)containerView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new BtnLogoutHandler());

        TextView txtCurrentUser = (TextView)containerView.findViewById(R.id.txtCurrentUser);
        txtCurrentUser.setText(prefs.getString("userName", "Unknown User"));

        ListView listNavigation = (ListView)containerView.findViewById(R.id.listNavigation);
        String[] navigationItems = containerView.getResources().getStringArray(R.array.nav_items);
        ArrayAdapter<String> navigationAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                navigationItems);
        listNavigation.setAdapter(navigationAdapter);
        listNavigation.setOnItemClickListener(new NavItemClickHandler());
    }

    //Used to update the drawers state variables when being recreated throughout the use of the app
    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    //Method for reading the preferences of the drawer
    public static String readFromPreferences(Context context, String preferenceName, String defaultValue)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(preferenceName, defaultValue);
    }

    //Class for managing how the drawer handle toggling on and off
    public class CustomDrawerToggle extends ActionBarDrawerToggle
    {

        public CustomDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        public CustomDrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView){
            super.onDrawerOpened(drawerView);

            //Checking if the user has seen the drawer open before
            if(!mUserLearnedDrawer)
            {
                //The user hasn't seen it open, saving that they have to the drawer preferences
                mUserLearnedDrawer = true;
                saveToPreferences(getActivity(), KEY_LEARNED_DRAWER, "true");
            }

            getActivity().invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View drawerView){
            super.onDrawerClosed(drawerView);
            getActivity().invalidateOptionsMenu();
        }
    }

    public class BtnLogoutHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            SharedPreferences prefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("userID");
            editor.remove("userName");
            editor.remove("fullName");
            editor.apply();

            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(loginIntent);
        }
    }

    public class NavItemClickHandler implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String[] navigationItems = containerView.getResources().getStringArray(R.array.nav_items);
            Intent goToIntent;

            switch (navigationItems[i])
            {
                case "Dashboard":
                    goToIntent = new Intent(getActivity(), DashboardActivity.class);
                    break;
                case "Create Scrum Meeting":
                    goToIntent = new Intent(getActivity(), AddScrumActivity.class);
                    break;
                case "Search Scrum Meetings":
                    goToIntent = new Intent(getActivity(), DashboardActivity.class);
                    break;
                default:
                    goToIntent = new Intent(getActivity(), getActivity().getClass());
                    break;
            }

            getActivity().startActivity(goToIntent);
        }
    }

}
