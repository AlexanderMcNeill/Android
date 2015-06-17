package bit.mcnear1.agilemanager.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 18/05/15.
 */
public class DashboardFragment extends Fragment {
    public final static String URLBASE = "http://alexandermcneill.nz:443/members/?";
    protected LinearLayout recentScrumContainer;
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Dashboard");
        View v = inflator.inflate(R.layout.fragment_dashboard, container, false);

        //Getting the apps shared preferences
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        //Filling the name label with the first name and last name stored in the shared preferences
        TextView lblFullName = (TextView)v.findViewById(R.id.lblFullname);
        lblFullName.setText(sharedPrefs.getString("firstName", "Unknown") + " " + sharedPrefs.getString("lastName", "Unknown"));

        //Getting the container that the recent scrum fragments will be displaying in
        recentScrumContainer = (LinearLayout)v.findViewById(R.id.recentScrumContainer);

        //Checking that the page is opening for the first time before getting the recent scrums
        //This stops it duplicating
        if (savedInstanceState == null) {

            //Making a request for the recent scrums the user is part of
            FetchRecentScrums fetchRecentScrums = new FetchRecentScrums(null);
            String url_string = URLBASE + "user_id=" + sharedPrefs.getInt("userID", -1);
            fetchRecentScrums.execute(url_string);
        }

        return v;
    }

    //Method that adds a recent scrum to the recent scrum container
    public void addRecentScrum(Fragment newRecentScrum, JSONObject teamData)
    {
        //Creating a bundle to give it the json data it needs
        Bundle bundle = new Bundle();
        bundle.putString("teamJsonString", teamData.toString());

        //Passing the data to the new fragment
        newRecentScrum.setArguments(bundle);

        //Getting the fragment manager from the activity
        FragmentManager fm = getActivity().getFragmentManager();

        //Starting a transaction to add the new recent scrum fragment
        FragmentTransaction ft = fm.beginTransaction();

        //adding the recent scrum to the container
        ft.add(recentScrumContainer.getId(), newRecentScrum);

        //Committing the new changes
        ft.commit();
    }

    //Class that is used for fetching recent scrum data
    public class FetchRecentScrums extends WebDataFetcher
    {

        public FetchRecentScrums(JSONObject params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData)
        {
            try{
                //Getting the response data
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                //Checking that the request succeeded
                JSONObject response_json = fetchedJson.getJSONObject("member_request_response");
                if(response_json.getInt("response_code") == 0) {

                    //Getting the teams from the user object returned
                    JSONObject userJson = fetchedJson.getJSONObject("user");
                    JSONArray teams = userJson.getJSONArray("teams");

                    //Creating a recent scrum fragments from each of the teams
                    for(int i = 0; i < teams.length(); i++)
                    {
                        JSONObject teamJson = teams.getJSONObject(i);
                        Fragment recentScrum = new RecentScrumFragment();
                        addRecentScrum(recentScrum, teamJson);
                    }
                }
                else
                {
                    //Displaying why the request failed to the user
                    String errorMessage = fetchedJson.getString("error");
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }catch (Exception ex)
            {
                //Displaying that there was a error to the user
                ex.printStackTrace();
                Toast toast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
