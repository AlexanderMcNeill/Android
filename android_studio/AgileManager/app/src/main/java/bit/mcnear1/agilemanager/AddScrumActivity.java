package bit.mcnear1.agilemanager;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class AddScrumActivity extends NavigationActivity {

    protected View page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        page = getLayoutInflater().inflate(R.layout.fragment_add_scrum, null);
        pageContainer.addView(page);

    }
}
