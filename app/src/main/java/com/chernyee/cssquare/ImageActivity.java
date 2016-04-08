package com.chernyee.cssquare;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.chernyee.cssquare.UI.ZoomImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;


public class ImageActivity extends AppCompatActivity{

    private ImageViewTouch fullImage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.saveMenu:
                Picasso.with(this).load(getIntent().getStringExtra("URL")).into(
                        new Target() {
                            @Override
                            public void onPrepareLoad(Drawable arg0) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {

                                String fileName = getIntent().getStringExtra("TITLE");

                                File file = new File(SplashActivity.IMAGES_PATH + "/" + fileName + ".jpg");
                                try {
                                    file.createNewFile();
                                    FileOutputStream ostream = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                    ostream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    Toast.makeText(ImageActivity.this, "Image downloaded to" + SplashActivity.CS_FOLDER_PATH
                                            + "images", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onBitmapFailed(Drawable arg0) {
                                Toast.makeText(ImageActivity.this, "Image downloaded failed", Toast.LENGTH_SHORT).show();
                                // TODO Auto-generated method stub

                            }
                        }
                );

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6d000000")));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#6d000000")));

        getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));

        fullImage = (ImageViewTouch) findViewById(R.id.fullImage);
        fullImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        String s = getIntent().getStringExtra("URL");
        Picasso.with(this).load(s)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(fullImage);


    }

}
