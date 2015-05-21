package bit.mcnear1.agilemanager.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bit.mcnear1.agilemanager.R;

/**
 * Created by alexmcneill on 18/05/15.
 */
public class SearchScrumFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflator.inflate(R.layout.fragment_search_scrum, container, false);

        return v;
    }
}
