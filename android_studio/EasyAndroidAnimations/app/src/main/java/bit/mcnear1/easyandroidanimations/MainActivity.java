package bit.mcnear1.easyandroidanimations;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ExplodeAnimation;


public class MainActivity extends Activity {

    ExplodeAnimation explodeCageImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgCage = (ImageView)findViewById(R.id.imgCage);

        explodeCageImage = new ExplodeAnimation(imgCage);
        explodeCageImage.setExplodeMatrix(ExplodeAnimation.MATRIX_3X3);
        explodeCageImage.setInterpolator(new DecelerateInterpolator());
        explodeCageImage.setDuration(500);
        explodeCageImage.setListener(new ExplodeCageAnimationHandler());

        imgCage.setOnClickListener(new CageClickHandler());
    }

    public class CageClickHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            explodeCageImage.animate();
        }
    }

    public class ExplodeCageAnimationHandler implements AnimationListener
    {

        @Override
        public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
        }
    }
}
