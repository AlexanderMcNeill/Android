package bit.mcnear1.agilemanager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.ScrumItem;
import bit.mcnear1.agilemanager.SwappablePage;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 18/05/15.
 */
public class SearchScrumFragment extends Fragment {

    private final String URLBASE = "http://alexandermcneill.nz:443/scrums/?";
    private ListView scrumList;
    private int userID;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Search Scrums");

        View v = inflator.inflate(R.layout.fragment_search_scrum, container, false);

        //Getting the user id from the shared preferences
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);
        userID = sharedPrefs.getInt("userID",-1);

        //Setting up the show filters buttons on click listener
        Button btnShowFilters = (Button)v.findViewById(R.id.btnShowFilters);
        btnShowFilters.setOnClickListener(new BtnShowFiltersHandler());

        scrumList = (ListView) v.findViewById(R.id.listScrums);

        //Getting the scrums
        ScrumDataFetcher scrumDataFetcher = new ScrumDataFetcher();
        scrumDataFetcher.execute(URLBASE+ "user_id=" + userID);
        return v;
    }

    public class BtnShowFiltersHandler implements View.OnClickListener
    {


        @Override
        public void onClick(View view) {
            View filterContainer = getView().findViewById(R.id.filterContainer);

            //Toggling on and off the filter container
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

    public class ScrumDataFetcher extends WebDataFetcher
    {

        public ScrumDataFetcher() {
            super(null);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try{
                //Getting the response data
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                //Checking the the request was a success
                JSONObject response_json = fetchedJson.getJSONObject("add_scrum_response");
                if(response_json.getInt("response_code") == 0) {

                    //Getting the array of scrums
                    JSONArray scrums = fetchedJson.getJSONArray("scrums");

                    //Creating a array of scrum items for the adapter
                    ScrumItem[] scrumItems = new ScrumItem[scrums.length()];

                    //Creating objects out of each scrum json
                    for (int i = 0; i < scrums.length(); i++)
                    {
                        JSONObject currentScrum = scrums.getJSONObject(i);

                        scrumItems[i] = new ScrumItem(currentScrum.getString("date"), currentScrum.getString("team_name"), currentScrum.getString("comment"), currentScrum.getInt("scrum_id"));
                    }

                    //Creating an adapter to display the scrum data
                    ScrumItemAdapter scrumItemAdapter = new ScrumItemAdapter(getActivity(), R.layout.scrum_list_item, scrumItems);

                    scrumList.setAdapter(scrumItemAdapter);
                    scrumList.setOnItemClickListener(new ScrumItemClickHandler());
                }
                else
                {
                    //Displaying the error to the user
                    String errorMessage = fetchedJson.getString("error");
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
                //Displaying there was a problem to the user
                Toast toast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public class ScrumItemAdapter extends ArrayAdapter<ScrumItem>
    {

        public ScrumItemAdapter(Context context, int resource, ScrumItem[] scrums)
        {
            super(context, resource, scrums);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            // Inflate custom_list_view and store the returned View in a variable
            View v = inflater.inflate(R.layout.scrum_list_item, container, false);



            // Get references to the elements in scrum list item
            TextView lblTeamName = (TextView) v.findViewById(R.id.lblTeamName);
            TextView lblDate = (TextView) v.findViewById(R.id.lblDate);
            TextView lblComment = (TextView) v.findViewById(R.id.lblComment);

            // Get the current item from array handed in
            ScrumItem scrum = getItem(position);

            // Fill the elements with there data
            lblTeamName.setText(scrum.getTeamName());
            lblDate.setText(scrum.getDate());
            lblComment.setText(scrum.getComment());

            return v;
        }
    }

    public class ScrumItemClickHandler implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Getting the selected scrum
            ScrumItem selectedScrum = (ScrumItem)adapterView.getItemAtPosition(i);

            //Getting a view scrum fragment
            SwappablePage swappablePageActivity = (SwappablePage)getActivity();
            ViewScrumFragment viewScrumFragment = new ViewScrumFragment();

            //Creating a bundle to give it the scrums id
            Bundle bundle = new Bundle();
            bundle.putInt("scrum_id", selectedScrum.getScrumID());

            //Passing the data to the new fragment
            viewScrumFragment.setArguments(bundle);

            //Setting it to the current page
            swappablePageActivity.updateCurrentPage(viewScrumFragment);
        }
    }
}
