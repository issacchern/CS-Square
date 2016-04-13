package com.chernyee.cssquare;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chernyee.cssquare.ApiData.JokesData;
import com.chernyee.cssquare.FlingSwipe.SwipeFlingAdapterView;
import com.chernyee.cssquare.Utility.DaoDBHelper;
import com.chernyee.cssquare.model.DaoMaster;
import com.chernyee.cssquare.model.DaoSession;
import com.chernyee.cssquare.model.Note;
import com.chernyee.cssquare.model.NoteDao;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

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

    private DaoDBHelper daoDBHelper;
    private List<Note> listNote;
    private NoteDao noteDao;
    private SwipeFlingAdapterView swipeView;
    private InnerAdapter adapter;
    private FloatingActionButton fab;
    private TextView noCardTextView;

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
                if(adapter.objs.size() >0){
                    Toast.makeText(FlashCardActivity.this, "List is shuffled!",Toast.LENGTH_SHORT).show();
                    Collections.shuffle(adapter.objs);
                } else{
                    Toast.makeText(FlashCardActivity.this, "Unable to perform data shuffling!", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.reset:
                listNote = noteDao.loadAll();
                if(listNote.size()> 0){
                    Toast.makeText(FlashCardActivity.this, "List is reset!", Toast.LENGTH_SHORT).show();
                    adapter.addAll(listNote);
                } else{
                    Toast.makeText(FlashCardActivity.this, "You have no added data!", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.load:

                adapter.add(new Note(null, "Abstract class", "A class that cannot be directly constructed but only be constructed through its subclasses.", "Orange", false));
                adapter.add(new Note(null,"Functional Programming", "A programming paradigm that treats computation as the evaluation of mathematical functions, " +
                        "avoid state and mutable data.","Orange", false));
                adapter.add(new Note(null,"Interpreter", "A program that executes another program written in a programming language other than machine code.","Orange", false));
                adapter.add(new Note(null,"Ad Hoc", "Something that was made up on the fly to deal with a particular situation, as apposed to tome systematic approach to solving problems.","Orange", false));
                adapter.add(new Note(null,"Address Space", "An amount of memory allocated for all possible addresses for a computational entity, such as device, a file or a server.","Orange", false));
                adapter.add(new Note(null,"Agile", "A set of principles for software development in which requirements and solutions evolve through collaboration " +
                        "between self-organizing, cross-functional teams.","Orange", false));
                noCardTextView.setVisibility(View.GONE);
                Toast.makeText(FlashCardActivity.this, "Sample data loaded!", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.deleteAll:
                noteDao.deleteAll();
                Toast.makeText(FlashCardActivity.this, "All flash cards deleted!", Toast.LENGTH_SHORT).show();
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
        noCardTextView = (TextView) findViewById(R.id.no_card_text);

        daoDBHelper = DaoDBHelper.getInstance(this);

        noteDao = daoDBHelper.getNoteDao();

        listNote = noteDao.loadAll();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.edit_text2, null);
                final MaterialEditText title = (MaterialEditText) dialoglayout.findViewById(R.id.add_title);
                final MaterialEditText desc = (MaterialEditText) dialoglayout.findViewById(R.id.add_description);
                ImageView colorPicker = (ImageView) dialoglayout.findViewById(R.id.colorPicker);


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
                                title.getText().toString() + " is added!", Toast.LENGTH_LONG).show();

                        Note note = new Note(null, title.getText().toString(), desc.getText().toString(), "green", false);
                        // noteDao.insert(note);
                        AsyncSession asyncSession = daoDBHelper.getAsyncSession();
                        asyncSession.insert(note);

                        adapter.add(note);

                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });


        initView();
        adapter.addAll(listNote);


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
            View child = vh.cardLayout;
            Rect outRect = new Rect();
            child.getGlobalVisibleRect(outRect);
            if (outRect.contains(x, y)) {
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);

                if(vh.textView.getVisibility() == View.VISIBLE){
                    vh.inviTextView.setVisibility(View.VISIBLE);
                    vh.textView.setVisibility(View.INVISIBLE);

                } else{
                    vh.inviTextView.setVisibility(View.INVISIBLE);
                    vh.textView.setVisibility(View.VISIBLE);
                }


                int colorFrom = ContextCompat.getColor(this, R.color.half_black);
                int colorTo = ContextCompat.getColor(this, R.color.nice_green);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(250); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        vh.cardLayout.setBackgroundColor((int) animator.getAnimatedValue());
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


        if(adapter.objs.isEmpty()){
            noCardTextView.setVisibility(View.VISIBLE);
        } else{
            noCardTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {
    }




    private class InnerAdapter extends BaseAdapter {

        ArrayList<Note> objs;

        public InnerAdapter() {
            objs = new ArrayList<>();
        }

        public void addAll(Collection<Note> collection) {
            if (isEmpty()) {
                objs.addAll(collection);
                notifyDataSetChanged();
            } else {
                objs.addAll(collection);
            }
            // hide the no card text on the screen
            noCardTextView.setVisibility(View.INVISIBLE);
        }

        public void add(Note n){
            if(isEmpty()){
                objs.add(n);
                notifyDataSetChanged();
            }else{
                objs.add(n);
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
        public Note getItem(int position) {
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
            Note talent = getItem(position);
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            convertView.setTag(holder);
            convertView.getLayoutParams().width = cardWidth;
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.yesView = (TextView) convertView.findViewById(R.id.yes);
            holder.noView = (TextView) convertView.findViewById(R.id.no);
            holder.deleteView = (TextView) convertView.findViewById(R.id.delete);
            holder.inviTextView = (TextView) convertView.findViewById(R.id.inviText);
            holder.cardLayout = (FrameLayout) convertView.findViewById(R.id.card);

            holder.textView.setText(talent.getText());
            holder.inviTextView.setText(talent.getDesc());
            holder.inviTextView.setVisibility(View.INVISIBLE);
     //       holder.inviTextView.setVisibility(View.GONE);

            holder.yesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FlashCardActivity.this, "Got it!", Toast.LENGTH_SHORT).show();

                }
            });

            holder.noView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FlashCardActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                }
            });

            holder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.animate().alpha(0.0f);
                    swipeView.removeFirstItem();
                    QueryBuilder<Note> qb = noteDao.queryBuilder();
                    DeleteQuery<Note> bd = qb.where(NoteDao.Properties.Text.eq(holder.textView.getText())).buildDelete();
                    bd.executeDeleteWithoutDetachingEntities();

                    Toast.makeText(FlashCardActivity.this, "Card deleted!" + position, Toast.LENGTH_SHORT).show();

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
        FrameLayout cardLayout;


    }




}
