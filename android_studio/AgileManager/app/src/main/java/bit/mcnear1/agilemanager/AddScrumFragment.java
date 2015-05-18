package bit.mcnear1.agilemanager;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by alexmcneill on 18/05/15.
 */
public class AddScrumFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflator.inflate(R.layout.fragment_add_scrum, container, false);

        return v;
    }
}
