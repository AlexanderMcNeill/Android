package bit.mcnear1.agilemanager.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bit.mcnear1.agilemanager.R;

/**
 * Created by alexmcneill on 25/05/15.
 */
public class MemberScrumFragment extends Fragment {
    protected LinearLayout mMemberContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_member_scrum, container, false);
        Bundle args = getArguments();
        String memberScrumJsonString = args.getString("memberScumJsonString");
        try {
            //Getting all the info from the json object passed in
            JSONObject memberScrumJson = new JSONObject(memberScrumJsonString);
            String memberFirstName = memberScrumJson.getString("first_name");
            String memberLastName = memberScrumJson.getString("last_name");
            String achievedGoals = memberScrumJson.getString("achieved_goals");
            String workDone = memberScrumJson.getString("work_done");
            String nextTime = memberScrumJson.getString("next_time");

            //Setting up all the xml elements with the data and event handlers they need
            mMemberContainer = (LinearLayout)v.findViewById(R.id.scrumInfoContainer);

            TextView lblMemberHeader = (TextView)v.findViewById(R.id.lblMemberHeader);
            lblMemberHeader.setText(memberFirstName + " " + memberLastName);
            lblMemberHeader.setOnClickListener(new MemberTitleClickHandler());

            TextView lblNextScrum = (TextView) v.findViewById(R.id.lblNextTime);
            lblNextScrum.setText(nextTime);

            TextView lblWorkDone = (TextView) v.findViewById(R.id.lblWorkDone);
            lblWorkDone.setText(workDone);

            TextView lblAchievedGoals = (TextView) v.findViewById(R.id.lblAchievedGoals);
            lblAchievedGoals.setText(achievedGoals);


        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }

        return v;
    }

    public class MemberTitleClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            //Toggling the members info to collapse
            if(mMemberContainer.getVisibility() == View.VISIBLE)
            {
                mMemberContainer.setVisibility(View.GONE);
            }
            else
            {
                mMemberContainer.setVisibility(View.VISIBLE);
            }
        }
    }
}
