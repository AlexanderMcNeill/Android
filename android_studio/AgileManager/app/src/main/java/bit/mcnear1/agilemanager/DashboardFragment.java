package bit.mcnear1.agilemanager;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alexmcneill on 15/05/15.
 */
public class DashboardFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        TextView lblFullName = (TextView)v.findViewById(R.id.lblFullname);
        lblFullName.setText(sharedPrefs.getString("firstName", "Unknown") + " " + sharedPrefs.getString("lastName", "Unknown"));

        return v;
    }
}
