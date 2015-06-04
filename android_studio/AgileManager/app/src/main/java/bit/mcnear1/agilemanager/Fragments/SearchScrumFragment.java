package bit.mcnear1.agilemanager.Fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bit.mcnear1.agilemanager.R;

/**
 * Created by alexmcneill on 18/05/15.
 */
public class SearchScrumFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        View v = inflator.inflate(R.layout.fragment_search_scrum, container, false);

        Button btnShowFilters = (Button)v.findViewById(R.id.btnShowFilters);
        btnShowFilters.setOnClickListener(new BtnShowFiltersHandler());
        return v;
    }

    public class BtnShowFiltersHandler implements View.OnClickListener
    {


        @Override
        public void onClick(View view) {
            View filterContainer = getView().findViewById(R.id.filterContainer);

            if(filterContainer.getVisibility() == View.VISIBLE)
            {
                filterContainer.setVisibility(View.GONE);
            }
            else
            {
                filterContainer.setVisibility(View.VISIBLE);
            }
        }
    }
}
