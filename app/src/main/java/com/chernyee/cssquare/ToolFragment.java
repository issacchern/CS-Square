package com.chernyee.cssquare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ToolFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Button button;
    private EditText binaryEdit;
    private EditText decimalEdit;
    private EditText hexaEdit;
    private EditText hashEditText;
    private TextView hashText;
    private String output = "";

    private boolean parallelBin = false;
    private boolean parallelDec = false;
    private boolean parallelHex = false;

    private OnFragmentInteractionListener mListener;

    public ToolFragment() {
        // Required empty public constructor
    }

    public static ToolFragment newInstance(String param1, String param2) {
        ToolFragment fragment = new ToolFragment();
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
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_tool, container, false);

        button = (Button) v.findViewById(R.id.emailButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"issac.chua12@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "CS-Square App Suggestion");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });




        binaryEdit = (EditText) v.findViewById(R.id.binaryEditText);
        decimalEdit = (EditText) v.findViewById(R.id.decimalEditText);
        hexaEdit = (EditText) v.findViewById(R.id.hexadecimalEditText);

        binaryEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                parallelBin = (hasFocus) ? true : false;
            }
        });

        decimalEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                parallelDec = (hasFocus) ? true : false;
            }
        });

        hexaEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                parallelHex = (hasFocus) ? true : false;
            }
        });



        binaryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (parallelBin) {
                    binaryEdit.removeTextChangedListener(this);
                    decimalEdit.removeTextChangedListener(this);
                    hexaEdit.removeTextChangedListener(this);

                    try {

                        if (s.toString().length() == 0) {
                            binaryEdit.setText("");
                            decimalEdit.setText("");
                            hexaEdit.setText("");
                        } else if (s.toString().matches("[01]+")) {

                            BigInteger bi = new BigInteger(s.toString(), 2);
                            String decimalStr = bi.toString();
                            decimalEdit.setText(decimalStr);
                            BigInteger bi2 = new BigInteger(decimalStr);
                            String hexStr = bi2.toString(16);
                            hexaEdit.setText(hexStr.toUpperCase());

                        } else {
                            binaryEdit.setText("");
                            decimalEdit.setText("");
                            hexaEdit.setText("");
                            Toast.makeText(getContext(), "Invalid binary input! ", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        Log.v("Exception from binary", e.toString());
                    }

                    binaryEdit.addTextChangedListener(this);
                    decimalEdit.addTextChangedListener(this);
                    hexaEdit.addTextChangedListener(this);

                }

            }
        });


        decimalEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(parallelDec){

                    binaryEdit.removeTextChangedListener(this);
                    decimalEdit.removeTextChangedListener(this);
                    hexaEdit.removeTextChangedListener(this);

                    try{

                        if(s.toString().length() == 0){
                            binaryEdit.setText("");
                            decimalEdit.setText("");
                            hexaEdit.setText("");
                        }

                        else if(s.toString().matches(".*\\d+.*")){
                            BigInteger bi = new BigInteger(s.toString());
                            String binaryStr = bi.toString(2);
                            binaryEdit.setText(binaryStr);
                            String hexStr = bi.toString(16);
                            hexaEdit.setText(hexStr.toUpperCase());

                        }else{
                            binaryEdit.setText("");
                            decimalEdit.setText("");
                            hexaEdit.setText("");
                            Toast.makeText(getContext(), "Invalid decimal input! ", Toast.LENGTH_SHORT).show();
                        }


                    } catch(Exception e){
                        Log.v("Exception from dec", e.toString());

                    }

                    binaryEdit.addTextChangedListener(this);
                    decimalEdit.addTextChangedListener(this);
                    hexaEdit.addTextChangedListener(this);

                }
            }
        });


        hexaEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(parallelHex){

                    binaryEdit.removeTextChangedListener(this);
                    decimalEdit.removeTextChangedListener(this);
                    hexaEdit.removeTextChangedListener(this);

                    try{

                        if(s.toString().length() == 0){
                            binaryEdit.setText("");
                            decimalEdit.setText("");
                            hexaEdit.setText("");
                        }

                        else if (s.toString().matches("^[0-9a-fA-F]+$")){


                            BigInteger bi2 = new BigInteger(s.toString().toLowerCase(), 16);
                            String decimalString = bi2.toString();
                            decimalEdit.setText(decimalString);

                            BigInteger bi = new BigInteger(decimalString);
                            String binaryStr = bi.toString(2);
                            binaryEdit.setText(binaryStr);



                        }else{
                            binaryEdit.setText("");
                            decimalEdit.setText("");
                            hexaEdit.setText("");
                            Toast.makeText(getContext(), "Invalid hexadecimal input! ", Toast.LENGTH_SHORT).show();
                        }


                    } catch(Exception e){
                        Log.v("Exception from hex", e.toString());

                    }

                    binaryEdit.addTextChangedListener(this);
                    decimalEdit.addTextChangedListener(this);
                    hexaEdit.addTextChangedListener(this);

                }
            }
        });

        hashEditText = (EditText) v.findViewById(R.id.hashEditText);
        hashText = (TextView) v.findViewById(R.id.hashTextView);

        hashText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", output);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "String copied!", Toast.LENGTH_SHORT).show();

            }
        });

        hashText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    v.setBackgroundResource(R.color.half_black);
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    v.setBackgroundResource(R.color.nice_green);
                return false;
            }
        });


        hashEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    byte[] bytesOfMessage = s.toString().getBytes();
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    byte[] thedigest = md.digest(bytesOfMessage);
                    output = String.format("%032X", new BigInteger(1, thedigest));
                    hashText.setText(output);

                }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
