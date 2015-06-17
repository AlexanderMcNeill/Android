package bit.mcnear1.agilemanager.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.SwappablePage;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 27/05/15.
 */
public class JoinScrumFragment extends Fragment{
    public final static String URLBASE = "http://alexandermcneill.nz:443/scrums/";

    protected int userID;
    protected int scrumID;
    protected Spinner nextTimeSpinner;
    protected Spinner achievedGoalsSpinner;
    protected Spinner workDoneSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Join Scrum");

        View v = inflater.inflate(R.layout.fragment_join_scrum, container, false);

        //Getting the user id from the shared preferences
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);
        userID = sharedPrefs.getInt("userID",-1);

        //Getting the scrum id that was passed in from the last fragment
        Bundle args = getArguments();
        scrumID = args.getInt("scrum_id");

        setupScreenElements(v);


        return v;
    }

    protected void setupScreenElements(View v)
    {
        Button btnJoinScrum = (Button)v.findViewById(R.id.btnJoinScrum);
        btnJoinScrum.setOnClickListener(new BtnJoinScrumHandler());

        //Linking xml elements to their objects
        nextTimeSpinner = (Spinner) v.findViewById(R.id.nextTimeSpinner);
        achievedGoalsSpinner = (Spinner) v.findViewById(R.id.achievedGoalsSpinner);
        workDoneSpinner = (Spinner) v.findViewById(R.id.workDoneSpinner);
    }

    public class BtnJoinScrumHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            try {
                //Creating a json object for the join scrum request and passing it the needed data
                JSONObject joinScrumData = new JSONObject();
                joinScrumData.put("function", "join_scrum");
                joinScrumData.put("user_id", userID);
                joinScrumData.put("scrum_id", scrumID);
                joinScrumData.put("work_done", workDoneSpinner.getSelectedItem().toString());
                joinScrumData.put("achieved_goals", achievedGoalsSpinner.getSelectedItem().toString());
                joinScrumData.put("next_time", workDoneSpinner.getSelectedItem().toString());

                //Needed for the request to succeed but not used in app
                joinScrumData.put("obstacles", "");

                //Making the request
                JoinScrumRequest joinScrumRequest = new JoinScrumRequest(joinScrumData);
                joinScrumRequest.execute(URLBASE);
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }

        }
    }

    //Class used for requesting to join a scrum
    public class JoinScrumRequest extends WebDataFetcher {

        public JoinScrumRequest(JSONObject params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try {
                //Getting the response data
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                //Creating a toast to display the result of the request
                Toast toast;

                //Displaying if the response was a success
                JSONObject response_json = fetchedJson.getJSONObject("join_scrum_response");
                toast = Toast.makeText(getActivity(), response_json.getString("response_message"), Toast.LENGTH_SHORT);
                toast.show();



            }catch (JSONException ex)
            {
                ex.printStackTrace();

                //Displaying that there was a issue to the user
                Toast toast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
            }

            //Switching to the view scrum page
            SwappablePage swappablePageActivity = (SwappablePage)getActivity();
            ViewScrumFragment viewScrumFragment = new ViewScrumFragment();

            //Creating a bundle to give it the scrums id
            Bundle bundle = new Bundle();
            bundle.putInt("scrum_id", scrumID);

            //Passing the data to the new fragment
            viewScrumFragment.setArguments(bundle);

            swappablePageActivity.updateCurrentPage(viewScrumFragment);
        }
    }
}
