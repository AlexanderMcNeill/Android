package bit.mcnear1.androidviewanimations;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgNinja = (ImageView) findViewById(R.id.imgNinja);
        imgNinja.setOnClickListener(new NinjaClickHandler());

    }

    public class NinjaClickHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            YoYo.with(Techniques.FlipOutX)
                    .duration(300)
                    .playOn(findViewById(R.id.imgNinja));
        }
    }
}
