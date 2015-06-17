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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.SwappablePage;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 22/05/15.
 */
public class ViewScrumFragment extends Fragment {

    public final String URLBASE = "http://alexandermcneill.nz:443/scrums/?";
    protected LinearLayout memberScrumContainer;

    protected TextView lblDate;
    protected TextView lblTeamGoals;
    protected TextView lblComment;
    protected TextView lblTeamName;
    protected Button btnJoinScrum;

    protected int scrumID;
    protected int userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("View Scrum");

        View v = inflater.inflate(R.layout.fragment_view_scrum, container, false);

        //Fetching the scrum id that was passed into the fragment
        Bundle args = getArguments();
        scrumID = args.getInt("scrum_id");

        //Getting the user id from the shared preferences
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);
        userID = sharedPrefs.getInt("userID",-1);

        //Linking xml elements to their objects
        lblDate = (TextView)v.findViewById(R.id.lblDate);
        lblTeamGoals = (TextView)v.findViewById(R.id.lblTeamGoals);
        lblComment = (TextView)v.findViewById(R.id.lblComment);
        lblTeamName = (TextView)v.findViewById(R.id.lblTeamName);
        memberScrumContainer = (LinearLayout)v.findViewById(R.id.memberScrumContainer);
        btnJoinScrum = (Button)v.findViewById(R.id.btnJoinScrum);
        btnJoinScrum.setOnClickListener(new BtnJoinScrumHandler());

        if (savedInstanceState == null) {
            //Fetching the scrums data
            ScrumDataFetcher scrumDataFetcher = new ScrumDataFetcher();
            scrumDataFetcher.execute(URLBASE + "scrum_id=" + scrumID);
        }

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

    public class BtnJoinScrumHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            SwappablePage swappablePageActivity = (SwappablePage)getActivity();
            JoinScrumFragment joinScrumFragment = new JoinScrumFragment();

            //Creating a bundle to give it the scrums id
            Bundle bundle = new Bundle();
            bundle.putInt("scrum_id", scrumID);

            //Passing the data to the new fragment
            joinScrumFragment.setArguments(bundle);

            swappablePageActivity.updateCurrentPage(joinScrumFragment);
        }
    }

    public class ScrumDataFetcher extends WebDataFetcher
    {

        public ScrumDataFetcher() {
            super(null);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try{
                //Getting the data
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                //Checking if it was a successful fetch
                JSONObject response_json = fetchedJson.getJSONObject("add_scrum_response");
                if(response_json.getInt("response_code") == 0) {
                    //Getting the scrum data
                    JSONObject scrum_data = fetchedJson.getJSONObject("scrum_data");

                    //Filling the lbls with needed info
                    lblDate.setText(scrum_data.getString("date"));
                    lblComment.setText(scrum_data.getString("comment"));
                    lblTeamName.setText(scrum_data.getString("team_name"));

                    //Getting the goals for the team
                    JSONArray teamGoals = scrum_data.getJSONArray("scrum_goals");

                    //Turning the teams goals into a displayable string
                    String teamGoalsString = "";
                    for(int i = 0; i < teamGoals.length(); i++) {
                        teamGoalsString += teamGoals.getString(i) + "\n";
                    }

                    //Displaying the goals
                    lblTeamGoals.setText(teamGoalsString);

                    //Getting each of the members scrum input
                    JSONArray memberScrums = scrum_data.getJSONArray("member_scrums");

                    //Creating a bool for checking if that logged in user has joined the scrum yet
                    boolean userJoinedScrum = false;

                    for(int i = 0; i < memberScrums.length(); i++)
                    {
                        //Getting the current members scrum info
                        JSONObject memberScrum = memberScrums.getJSONObject(i);

                        //Checking if it belongs to the logged in user
                        if(memberScrum.getInt("user_id") == userID)
                        {
                            userJoinedScrum = true;
                        }

                        //Creating a new fragment to hold the members scrum data and display to the screen
                        Fragment memberScrumFragment = new MemberScrumFragment();
                        addMemberScrum(memberScrumFragment, memberScrum);
                    }

                    //Checking if the current user hasn't joined the scrum
                    if(userJoinedScrum == false)
                    {
                        //If they haven't displaying a button that allows them to do so
                        btnJoinScrum.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    //Displaying the user the error as a toast
                    String errorMessage = response_json.getString("response_message");
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }catch (Exception ex)
            {
                //Informing that user that something has gone wrong
                ex.printStackTrace();
                Toast toast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
