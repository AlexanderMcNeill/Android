package bit.mcnear1.agilemanager.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 22/05/15.
 */
public class ViewScrumFragment extends Fragment {

    public final static String URLBASE = "http://alexandermcneill.nz:443/scrums/?";
    protected LinearLayout memberScrumContainer;

    protected TextView lblDate;
    protected TextView lblTeamGoals;
    protected TextView lblComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_scrum, container, false);

        memberScrumContainer = (LinearLayout)v.findViewById(R.id.memberScrumContainer);

        Bundle args = getArguments();
        int scrumID = args.getInt("scrum_id");

        lblDate = (TextView)v.findViewById(R.id.lblDate);
        lblTeamGoals = (TextView)v.findViewById(R.id.lblTeamGoals);
        lblComment = (TextView)v.findViewById(R.id.lblComment);

        ScrumDataFetcher scrumDataFetcher = new ScrumDataFetcher();
        scrumDataFetcher.execute(URLBASE + "scrum_id=" +scrumID);

        return v;
    }

    public void addMemberScrum(Fragment memberScrumFragment, JSONObject memberScrumJson)
    {
        //Creating a bundle to give it the json data it needs
        Bundle bundle = new Bundle();
        bundle.putString("memberScumJsonString", memberScrumJson.toString());

        //Passing the data to the new fragment
        memberScrumFragment.setArguments(bundle);

        //Getting the fragment manager from the activity
        FragmentManager fm = getActivity().getFragmentManager();

        //Starting a transaction to add the new recent scrum fragment
        FragmentTransaction ft = fm.beginTransaction();

        //adding the recent scrum to the container
        ft.add(memberScrumContainer.getId(), memberScrumFragment);

        //Committing the new changes
        ft.commit();
    }

    public class ScrumDataFetcher extends WebDataFetcher
    {

        public ScrumDataFetcher() {
            super(null);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                JSONObject response_json = fetchedJson.getJSONObject("add_scrum_response");
                if(response_json.getInt("response_code") == 0) {
                    JSONObject scrum_data = fetchedJson.getJSONObject("scrum_data");

                    lblDate.setText(scrum_data.getString("date"));
                    lblComment.setText(scrum_data.getString("comment"));

                    JSONArray teamGoals = scrum_data.getJSONArray("scrum_goals");

                    String teamGoalsString = "";
                    for(int i = 0; i < teamGoals.length(); i++) {
                        teamGoalsString += teamGoals.getString(i) + "\n";
                    }

                    lblTeamGoals.setText(teamGoalsString);

                    JSONArray memberScrums = scrum_data.getJSONArray("member_scrums");
                    for(int i = 0; i < memberScrums.length(); i++)
                    {
                        //Creating a new fragment to hold the members scrum data
                        Fragment memberScrum = new MemberScrumFragment();
                        addMemberScrum(memberScrum, memberScrums.getJSONObject(i));
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
