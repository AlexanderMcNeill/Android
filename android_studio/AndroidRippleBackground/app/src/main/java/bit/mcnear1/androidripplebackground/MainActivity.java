package bit.mcnear1.androidripplebackground;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;


public class MainActivity extends Activity {
    private RippleBackground rippleBackground;
    private boolean on = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rippleBackground=(RippleBackground)findViewById(R.id.content);

        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new BtnTickHandler());
    }

    public class BtnTickHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(on)
            {
                rippleBackground.stopRippleAnimation();
            }
            else
            {
                rippleBackground.startRippleAnimation();
            }

            on = !on;
        }
    }
}
