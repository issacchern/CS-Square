package com.chernyee.cssquare;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chernyee.cssquare.FlingSwipe.SwipeFlingAdapterView;
import com.chernyee.cssquare.model.DaoMaster;
import com.chernyee.cssquare.model.DaoSession;
import com.chernyee.cssquare.model.NoteDao;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FlashCardActivity extends AppCompatActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener{


 //   this is lock for synchronization
//    try {
//        synchronized (MyActivity.sDataLock) {
//            File dataFile = new File(getFilesDir(), TOP_SCORES);
//            RandomAccessFile raFile = new RandomAccessFile(dataFile, "rw");
//            raFile.writeInt(score);
//        }
//    } catch (IOException e) {
//        Log.e(TAG, "Unable to write to file");
//    }

    public static final Object sDataLock = new Object();


    private int cardWidth;
    private int cardHeight;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private NoteDao noteDao;
    private SwipeFlingAdapterView swipeView;
    private InnerAdapter adapter;
    private boolean isFirst = true;
    private FloatingActionButton fab;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shuffle:
                // shuffle
                Toast.makeText(FlashCardActivity.this, "List is shuffled!",Toast.LENGTH_SHORT).show();
                Collections.shuffle(adapter.objs);
                return true;
            case R.id.reset:
                Toast.makeText(FlashCardActivity.this, "List is reset!", Toast.LENGTH_SHORT).show();
                loadData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "db_file",null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        noteDao = daoSession.getNoteDao();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FlashCardActivity.this, "This is not functioning yet!", Toast.LENGTH_LONG).show();
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.edit_text2, null);
                final MaterialEditText title = (MaterialEditText) dialoglayout.findViewById(R.id.add_title);
                final MaterialEditText desc = (MaterialEditText) dialoglayout.findViewById(R.id.add_description);


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FlashCardActivity.this);
                alertDialog.setView(dialoglayout);

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.cancel();
                    }
                });

                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do something with it()

                        Toast.makeText(FlashCardActivity.this,
                                title.getText().toString() + " is added!",Toast.LENGTH_LONG).show();



                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });


        initView();
        loadData();

    }

    private void initView() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));


        swipeView = (SwipeFlingAdapterView) findViewById(R.id.swipe_view);
        //swipeView.setIsNeedSwipe(true);
        swipeView.setFlingListener(this);
        swipeView.setOnItemClickListener(this);



        adapter = new InnerAdapter();
        swipeView.setAdapter(adapter);
    }


    public int i = 0;

    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {
        if (v.getTag() instanceof ViewHolder) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            final ViewHolder vh = (ViewHolder) v.getTag();
            View child = vh.textView;
            Rect outRect = new Rect();
            child.getGlobalVisibleRect(outRect);
            if (outRect.contains(x, y)) {
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                vh.textView.setText(vh.inviTextView.getText().toString());
                vh.textView.setTextSize(20); // set the font size

                vh.textView.animate().alpha(1.0f);

                int colorFrom = ContextCompat.getColor(this, R.color.half_black);
                int colorTo = ContextCompat.getColor(this, R.color.nice_green);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(250); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        vh.textView.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();

            }
        }
    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {

    }

    @Override
    public void onRightCardExit(Object dataObject) {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if(isFirst){
            isFirst = false;
            return;
        }

        if(itemsInAdapter == 0){
            Toast.makeText(FlashCardActivity.this, "You have reached the end of the stack! Click reset on the menu to start over!", Toast.LENGTH_LONG).show();

        }

//        if (itemsInAdapter == 3) {
//            loadData();
//        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {
    }

    private void loadData() {
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, List<Talent>>() {
            @Override
            protected List<Talent> doInBackground(Void... params) {
                ArrayList<Talent> list = new ArrayList<>();
                list.add(new Talent("Abstract class", "A class that cannot be directly constructed but only be constructed through its subclasses.","Orange"));
                list.add(new Talent("Functional Programming", "A programming paradigm that treats computation as the evaluation of mathematical functions, " +
                        "avoid state and mutable data.","Orange"));
                list.add(new Talent("Interpreter", "A program that executes another program written in a programming language other than machine code.","Orange"));
                list.add(new Talent("Ad Hoc", "Something that was made up on the fly to deal with a particular situation, as apposed to tome systematic approach to solving problems.","Orange"));
                list.add(new Talent("Ad Hoc", "Something that was made up on the fly to deal with a particular situation, as apposed to tome systematic approach to solving problems.","Orange"));
                list.add(new Talent("Address Space", "An amount of memory allocated for all possible addresses for a computational entity, such as device, a file or a server.","Orange"));
                list.add(new Talent("Agile", "A set of principles for software development in which requirements and solutions evolve through collaboration " +
                        "between self-organizing, cross-functional teams.","Orange"));
                list.add(new Talent("AJAX", "Asynchronous JavaScript and XML is a method to build interative applications for Web that process user requests immediately.","Orange"));


                return list;
            }

            @Override
            protected void onPostExecute(List<Talent> list) {
                super.onPostExecute(list);
                adapter.addAll(list);
            }
        });
    }


    private class InnerAdapter extends BaseAdapter {

        ArrayList<Talent> objs;

        public InnerAdapter() {
            objs = new ArrayList<>();
        }

        public void addAll(Collection<Talent> collection) {
            if (isEmpty()) {
                objs.addAll(collection);
                notifyDataSetChanged();
            } else {
                objs.addAll(collection);
            }
        }

        public void clear() {
            objs.clear();
            notifyDataSetChanged();
        }

        public boolean isEmpty() {
            return objs.isEmpty();
        }

        public void remove(int index) {
            if (index > -1 && index < objs.size()) {
                objs.remove(index);
                notifyDataSetChanged();
            }
        }


        @Override
        public int getCount() {
            return objs.size();
        }

        @Override
        public Talent getItem(int position) {
            if(objs==null ||objs.size()==0) return null;
            return objs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // TODO: getView
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Talent talent = getItem(position);
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            ViewHolder holder = new ViewHolder();
            convertView.setTag(holder);
            convertView.getLayoutParams().width = cardWidth;
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.yesView = (TextView) convertView.findViewById(R.id.yes);
            holder.noView = (TextView) convertView.findViewById(R.id.no);
            holder.deleteView = (TextView) convertView.findViewById(R.id.delete);
            holder.inviTextView = (TextView) convertView.findViewById(R.id.inviText);

            holder.textView.setText(talent.getFrontText());
            holder.inviTextView.setText(talent.getBackText());
            holder.inviTextView.setVisibility(View.GONE);

            holder.yesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FlashCardActivity.this, "Got it!", Toast.LENGTH_SHORT).show();
                }
            });

            holder.noView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FlashCardActivity.this, "Again", Toast.LENGTH_SHORT).show();
                }
            });

            holder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FlashCardActivity.this, "Card deleted" + position, Toast.LENGTH_SHORT).show();
                    v.animate().alpha(0.0f);
                    swipeView.removeFirstItem();
                }
            });



            return convertView;
        }

    }

    private static class ViewHolder {
        TextView textView;
        TextView inviTextView;
        TextView yesView;
        TextView noView;
        TextView deleteView;


    }

    public class Talent {

        private String frontText;
        private String backText;
        private String colorCode;

        public Talent(String frontText, String backText, String colorCode){
            this.frontText = frontText;
            this.backText = backText;
            this.colorCode = colorCode;
        }


        public String getFrontText() {
            return frontText;
        }

        public String getBackText() {
            return backText;
        }

        public String getColorCode() {
            return colorCode;
        }

    }


}
