package bit.mcnear1.agilemanager;

import android.os.Bundle;
import android.view.View;

/**
 * Created by alexmcneill on 15/05/15.
 */
public class SearchScrumActivity extends NavigationActivity{

    protected View page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        page = getLayoutInflater().inflate(R.layout.fragment_search_scrum, null);
        pageContainer.addView(page);

    }
}
