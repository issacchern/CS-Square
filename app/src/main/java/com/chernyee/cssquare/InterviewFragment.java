package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InterviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InterviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InterviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private CheckedTextView checkedTextView1;
    private CheckedTextView checkedTextView2;
    private CheckedTextView checkedTextView3;
    private CheckedTextView checkedTextView4;

    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;

    private SeekBar seekBar;
    private TextView seekValue;

    private Button startButton;

    private OnFragmentInteractionListener mListener;

    public InterviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InterviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterviewFragment newInstance(String param1, String param2) {
        InterviewFragment fragment = new InterviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("cscheck1", true);
        editor.putBoolean("cscheck2", true);
        editor.putBoolean("cscheck3", true);
        editor.commit();
        editor.putInt("csradio", 1);
        editor.commit();
        editor.putInt("csseek", 10);
        editor.commit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_interview, container, false);

        seekBar = (SeekBar) v.findViewById(R.id.seekbar);
        seekValue = (TextView) v.findViewById(R.id.seekvalue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekValue.setText(progress + " mins");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startButton = (Button) v.findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkedTextView1.isChecked() && !checkedTextView2.isChecked() && !checkedTextView3.isChecked() && !checkedTextView3.isChecked()) {
                    Toast.makeText(getContext(), "Please select at least of the categories", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(seekBar.getProgress() == 0){
                    Toast.makeText(getContext(), "Length of interview must not be 0!", Toast.LENGTH_SHORT).show();
                    return;
                }

                editor = sharedPreferences.edit();

                if(radioButton1.isChecked()){
                    editor.putInt("csradio", 1);
                } else if(radioButton2.isChecked()){
                    editor.putInt("csradio",2);
                } else if(radioButton3.isChecked()){
                    editor.putInt("csradio",3);
                }



                editor.putInt("csseek", seekBar.getProgress());
                editor.commit();

                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                startActivity(intent);

            }
        });


        checkedTextView1 = (CheckedTextView) v.findViewById(R.id.checkedTextView1);
        checkedTextView2 = (CheckedTextView) v.findViewById(R.id.checkedTextView2);
        checkedTextView3 = (CheckedTextView) v.findViewById(R.id.checkedTextView3);
        checkedTextView4 = (CheckedTextView) v.findViewById(R.id.checkedTextView4);

        checkedTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextView1.isChecked()){
                    checkedTextView1.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck1", false);
                    editor.commit();
                } else{
                    checkedTextView1.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck1", true);
                    editor.commit();
                }
            }
        });

        checkedTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextView2.isChecked()){
                    checkedTextView2.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck2", false);
                    editor.commit();
                } else{
                    checkedTextView2.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck2", true);
                    editor.commit();
                }
            }
        });

        checkedTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextView3.isChecked()){
                    checkedTextView3.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck3", false);
                    editor.commit();
                } else{
                    checkedTextView3.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck3", true);
                    editor.commit();
                }
            }
        });

        checkedTextView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextView4.isChecked()){
                    checkedTextView1.setChecked(false);
                    checkedTextView2.setChecked(false);
                    checkedTextView3.setChecked(false);
                    checkedTextView4.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck1", false);
                    editor.putBoolean("cscheck2", false);
                    editor.putBoolean("cscheck3", false);
                    editor.commit();
                } else{
                    checkedTextView1.setChecked(true);
                    checkedTextView2.setChecked(true);
                    checkedTextView3.setChecked(true);
                    checkedTextView4.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck1", true);
                    editor.putBoolean("cscheck2", true);
                    editor.putBoolean("cscheck3", true);
                    editor.commit();

                }
            }
        });

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) radioGroup.findViewById(R.id.radio1);
        radioButton2 = (RadioButton) radioGroup.findViewById(R.id.radio2);
        radioButton3 = (RadioButton) radioGroup.findViewById(R.id.radio3);

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Function Not Supported")
                        .setMessage("Video recording is still at early development stage and is unavailable now." +
                                " Meanwhile, you can try audio recording and see how it turns out.")
                        .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.android)
                        .show();

                radioButton2.setChecked(false);
                radioButton1.setChecked(true);
            }
        });




        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}