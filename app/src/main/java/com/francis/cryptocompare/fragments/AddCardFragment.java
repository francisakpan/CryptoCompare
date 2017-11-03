package com.francis.cryptocompare.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.francis.cryptocompare.R;

/**
 * Created by Francis on 02/11/2017.
 *
 */

public class AddCardFragment extends DialogFragment {

    // Declare onCryptoCurrencySelectedListener object
    OnCryptoCurrencySelectedListener mCallback;

    //Declare a public interface for crypto currency selected
    public interface OnCryptoCurrencySelectedListener{
        void onCryptoCurrencySelected(String selected);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //cast application context to onCryptoCurrencySelectedListener
        mCallback = (OnCryptoCurrencySelectedListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set style for displaying dialog fragment
        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflate the add_card_fragment xml to the screen
        return inflater.inflate(R.layout.add_card_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize bitCoin text view and attach OnClickListener
        final TextView bitCoin = (TextView) view.findViewById(R.id.bitCoin);
        bitCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onCryptoCurrencySelected(bitCoin.getText().toString());
                dismiss();
            }
        });

        //Initialize ethereum text view and attach OnClickListener
        final TextView ether = (TextView) view.findViewById(R.id.ether);
        ether.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onCryptoCurrencySelected(ether.getText().toString());
                dismiss();
            }
        });

        //Initialize cancel button view and attach OnClickListener
        Button cancel = (Button) view.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
