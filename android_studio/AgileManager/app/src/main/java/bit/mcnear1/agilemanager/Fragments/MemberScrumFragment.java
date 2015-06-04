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

    protected TextView mLblMemberName;
    protected LinearLayout mMemberContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_member_scrum, container, false);
        Bundle args = getArguments();
        String memberScrumJsonString = args.getString("memberScumJsonString");
        try {
            JSONObject memberScrumJson = new JSONObject(memberScrumJsonString);
            String memberFirstName = memberScrumJson.getString("first_name");
            String memberLastName = memberScrumJson.getString("first_name");

            JSONArray memberGoals = memberScrumJson.getJSONArray("goals");
            String memberGoalsString = "";

            for(int i = 0; i < memberGoals.length(); i++)
            {
                memberGoalsString += "-" + memberGoals.getString(i) + "\n";
            }

            TextView lblMemberHeader = (TextView)v.findViewById(R.id.lblMemberHeader);
            lblMemberHeader.setText(memberFirstName + " " + memberLastName);
            TextView lblMemberGoals = (TextView)v.findViewById(R.id.lblUserGoals);
            lblMemberGoals.setText(memberGoalsString);
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
