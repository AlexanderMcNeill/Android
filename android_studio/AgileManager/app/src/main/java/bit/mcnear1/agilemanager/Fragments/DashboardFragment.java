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
    protected LinearLayout recentScrumContainer;
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflator.inflate(R.layout.fragment_dashboard, container, false);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        TextView lblFullName = (TextView)v.findViewById(R.id.lblFullname);
        lblFullName.setText(sharedPrefs.getString("firstName", "Unknown") + " " + sharedPrefs.getString("lastName", "Unknown"));

        recentScrumContainer = (LinearLayout)v.findViewById(R.id.recentScrumContainer);

        FetchRecentScrums fetchRecentScrums = new FetchRecentScrums(null);
        fetchRecentScrums.execute("http://alexandermcneill.nz/mobileapi/userRecentScrums.php");
        return v;
    }

    public void addRecentScrum(Fragment newRecentScrum, JSONObject scrumData)
    {
        //Creating a bundle to give it the json data it needs
        Bundle bundle = new Bundle();
        bundle.putString("scumJsonString", scrumData.toString());

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

    public class FetchRecentScrums extends WebDataFetcher
    {

        public FetchRecentScrums(List<NameValuePair> params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData)
        {
            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                if(fetchedJson.getInt("success") == 1) {

                    JSONArray scrumMeetings = fetchedJson.getJSONArray("scrums");
                    for(int i = 0; i < scrumMeetings.length(); i++)
                    {

                        //Creating a new fragment to hold recent scrum data
                        Fragment recentScrum = new RecentScrumFragment();
                        addRecentScrum(recentScrum, scrumMeetings.getJSONObject(i));
                    }
                }
                else
                {
                    String errorMessage = fetchedJson.getString("error");
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
                Toast toast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
