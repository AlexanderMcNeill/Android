package bit.mcnear1.cameratest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private final int CAMERARETURNID = 1;
    private File imageFile;
    private File appDirectory;

    private ImageView[] imageViews = new ImageView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViews[0] = (ImageView)findViewById(R.id.imageView);
        imageViews[1] = (ImageView)findViewById(R.id.imageView2);
        imageViews[2] = (ImageView)findViewById(R.id.imageView3);
        imageViews[3] = (ImageView)findViewById(R.id.imageView4);

        File imageRootDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        appDirectory = new File(imageRootDirectory.getPath(), "camera_lab");

        if(!appDirectory.exists())
        {
            appDirectory.mkdirs();
        }

        Button btnCamera = (Button)findViewById(R.id.btnImage);
        btnCamera.setOnClickListener(new btnPhotoHandler());

        if(savedInstanceState != null)
        {
            imageFile = new File(savedInstanceState.getString("file_path"));
            displayImage(imageFile);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case CAMERARETURNID:
                    displayImage(imageFile);
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if(imageFile != null)
        {
            savedInstanceState.putString("file_path", imageFile.getPath());
        }
    }

    protected void displayImage(File toDisplayImageFile)
    {
        Bitmap imageBitmap = BitmapFactory.decodeFile(toDisplayImageFile.getPath());

        for(int i = 0; i < imageViews.length; i++)
        {
            imageViews[i].setImageBitmap(imageBitmap);
        }
    }

    protected File createUniqueImageFile()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
        Date d = new Date();
        String dateString = dateFormat.format(d);

        String fileName = "IMG_" + dateString + ".jpg";

        File newImageFile = new File(appDirectory.getPath() + File.separator + fileName);

        return newImageFile;
    }

    protected class btnPhotoHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            imageFile = createUniqueImageFile();

            Uri imageUri = Uri.fromFile(imageFile);

            Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(photoIntent, CAMERARETURNID);
        }
    }
}
