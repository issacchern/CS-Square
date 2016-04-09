package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chernyee.cssquare.ApiData.JokesApi;
import com.chernyee.cssquare.ApiData.JokesData;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JokesActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private static final String BASE_URL = "http://www.chernyee.com/api/";
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<JokesData.Image> listImage;
    private FloatingActionButton fab;
    private String tempHoldStr = "";
    private int spanCount = 1; // 1 column
    private int spacing = 50; // 50px




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.edit_text, null);
                final MaterialEditText filterText = (MaterialEditText) dialoglayout.findViewById(R.id.edit);
                final TextView text = (TextView) dialoglayout.findViewById(R.id.text);
                text.setText("Search image");
                filterText.setHint("Type keywords like joke, java...");
                filterText.setFloatingLabelText("Keywords");
                if (tempHoldStr.length() > 0) {
                    filterText.setText(tempHoldStr);
                }


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(JokesActivity.this);
                alertDialog.setView(dialoglayout);

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.cancel();
                    }
                });

                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do something with it()

                        if (listImage == null) {


                        }


                        String filter = filterText.getText().toString().toLowerCase().trim();
                        String[] filterTag = filter.split(" ");


                        List<JokesData.Image> iList = new ArrayList<JokesData.Image>();
                        for (int i = 0; i < listImage.size(); i++) {
                            for (int j = 0; j < filterTag.length; j++) {

                                if (listImage.get(i).getTitle().toLowerCase().contains(filterTag[j])) {
                                    iList.add(listImage.get(i));
                                    break;
                                }
                            }

                        }
                        tempHoldStr = filter;

                        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 1);
                        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

                        if (iList.size() == 0) {
                            Toast.makeText(JokesActivity.this, "No result is found!", Toast.LENGTH_SHORT).show();
                            mRecyclerView.setAdapter(adapter);
                            adapter.swap(listImage);

                        } else {
                            Toast.makeText(JokesActivity.this, "Filter result", Toast.LENGTH_LONG).show();
                            mRecyclerView.setAdapter(adapter);
                            adapter.swap(iList);

                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                            }
                        });

                        dialog.cancel();
                    }
                });

                alertDialog.show();


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));


        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);


        if (MainActivity.isNetworkAvailable(this)) {
            final ProgressDialog progress = ProgressDialog.show(this, "Loading",
                    "Please wait", true);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JokesApi jokeApi = retrofit.create(JokesApi.class);
            Call<JokesData> call = jokeApi.getJokesApi();
            call.enqueue(new Callback<JokesData>() {
                @Override
                public void onResponse(Call<JokesData> call, Response<JokesData> response) {

                    if (response.isSuccessful()) {

                        listImage = new ArrayList<JokesData.Image>(response.body().getImages());
                        adapter = new RecyclerViewAdapter(JokesActivity.this, response.body().getImages());
                        mRecyclerView.setAdapter(adapter);

                    } else {
                        Toast.makeText(JokesActivity.this, "Error on the server side!", Toast.LENGTH_LONG).show();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<JokesData> call, Throwable t) {
                    call.cancel();
                    progress.dismiss();

                }
            });

        } else {
            Toast.makeText(JokesActivity.this, "Please enable Internet to load data", Toast.LENGTH_LONG).show();
        }


    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<FeedListRowHolder> {


        private List<JokesData.Image> feedItemList;

        private Context mContext;

        public RecyclerViewAdapter(Context context, List<JokesData.Image> feedItemList) {
            this.feedItemList = feedItemList;
            this.mContext = context;
        }

        public void swap(List<JokesData.Image> datas) {
            feedItemList.clear();
            feedItemList.addAll(datas);
        }

        @Override
        public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_row, null);
            FeedListRowHolder mh = new FeedListRowHolder(v);

            return mh;
        }

        @Override
        public void onBindViewHolder(final FeedListRowHolder feedListRowHolder, final int i) {
            final JokesData.Image feedItem = feedItemList.get(i);

            Picasso.with(mContext).load(feedItem.getBackground())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(feedListRowHolder.thumbnail);

            feedListRowHolder.title.setText(feedItem.getTitle());

            feedListRowHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(JokesActivity.this, ImageActivity.class);
                    intent.putExtra("URL", feedItem.getBackground());
                    intent.putExtra("TITLE", feedItem.getTitle());
                    intent.setAction(Intent.ACTION_VIEW);
                    startActivity(intent);


                }
            });

            feedListRowHolder.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Picasso.with(JokesActivity.this).load(feedItem.getBackground()).into(
                            new Target() {
                                @Override
                                public void onPrepareLoad(Drawable arg0) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {

                                    String fileName = feedItem.getTitle();

                                    File file = new File(SplashActivity.IMAGES_PATH + "/" + fileName + ".jpg");
                                    try {
                                        file.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                        ostream.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        Toast.makeText(JokesActivity.this, "Image downloaded to" + SplashActivity.CS_FOLDER_PATH
                                                + "images", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onBitmapFailed(Drawable arg0) {
                                    Toast.makeText(JokesActivity.this, "Image downloaded failed", Toast.LENGTH_SHORT).show();
                                    // TODO Auto-generated method stub

                                }
                            }
                    );
                }
            });

            feedListRowHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, feedItem.getTitle() + " Link: " + feedItem.getBackground());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share image with"));
                }
            });

        }

        @Override
        public int getItemCount() {
            return (null != feedItemList ? feedItemList.size() : 0);
        }


    }

    public class FeedListRowHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView title;
        protected ImageView shareButton;
        protected ImageView saveButton;

        public FeedListRowHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title);
            this.shareButton = (ImageView) view.findViewById(R.id.shareButton);
            this.saveButton = (ImageView) view.findViewById(R.id.saveButton);
        }


    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
