package com.francis.cryptocompare.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.francis.cryptocompare.network.CheckNetworkConnState;
import com.francis.cryptocompare.Currency;
import com.francis.cryptocompare.network.Network;
import com.francis.cryptocompare.R;
import com.francis.cryptocompare.main.ExchangeRateActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Francis on 02/11/2017.
 *
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    // Store a member variable for the currency
    private List<Currency> mCurrency;

    // Store the context for easy access
    private Context mContext;

    //selected currency string
    private String selected_currency;

    //Loaded exchange rate value
    private double rate;

    // an array of 20 local currencies
    private String[] local;

    //Base url to build upon
    private static final String BASE_URL =
            "https://min-api.cryptocompare.com/data/pricehistorical";

    private String selected_crypto;
    private AsyncTask<String, Void, String> task;

    public class ViewHolder extends RecyclerView.ViewHolder{

        //Declare views to hold
        ImageView avatar;
        TextView rate_text;
        Spinner chooser;
        ImageButton delete;

        ViewHolder(final Context context, View itemView) {
            super(itemView);

            //set views
            avatar = (ImageView)itemView.findViewById(R.id.coin_avatar);
            rate_text = (TextView)itemView.findViewById(R.id.exchange_rate_text);
            chooser = (Spinner) itemView.findViewById(R.id.currency_picker);
            delete = (ImageButton) itemView.findViewById(R.id.delete);

            //Attach onClickListener to open ExchangeRateActivity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (rate != 0.0 && selected_currency != null) {
                        Intent intent = new Intent(context, ExchangeRateActivity.class);
                        intent.putExtra("crypto", selected_crypto);
                        intent.putExtra("currency", selected_currency);
                        intent.putExtra("rate", String.valueOf(rate));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    /**
     *
     * @param context application context
     * @param currencies A list of currencies
     */
    public CurrencyAdapter(Context context, List<Currency> currencies) {
        this.mCurrency = currencies;
        this.mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate conversion_card xml to the screen
        Context context = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View card = inflater.inflate(R.layout.conversion_card, parent, false);

        return new ViewHolder(getContext(), card);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Get a currency object at the current position
        Currency currentCurrency = mCurrency.get(position);

        //set selected crypto currency to the varible
        selected_crypto = currentCurrency.getCrypto_currency();

        //get a string array of currencies
        local = getContext().getResources().getStringArray(R.array.currency);

        //Declare an ArrayAdapter for the spinner view
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, local);

        //Declare the avatar image view from the holder class and set an image resource
        ImageView imageView = holder.avatar;
        if (selected_crypto.equals(getContext().getString(R.string.btc_text))){
            imageView.setImageResource(R.drawable.ic_bitcoin);
        }else {
            imageView.setImageResource(R.drawable.ic_ether);
        }

        //Declare the rate text view from the holder class
        final TextView textView = holder.rate_text;

        //Declare delete button view and attach onClickListener
        ImageButton button = holder.delete;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrency.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        //Declare a spinner view for the local currencies
        final Spinner spinner = holder.chooser;

        //set the ArrayAdapter to the spinner view and attach onItemSelectedListener
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get a local currency
                selected_currency = local[position];

                //set the base url for network query and build upon
                Uri baseUri = Uri.parse(BASE_URL);
                Uri.Builder uri_builder = baseUri.buildUpon();
                uri_builder.appendQueryParameter("fsym", selected_crypto);
                uri_builder.appendQueryParameter("tsyms", selected_currency);

                //An Async task to query for data on the network
                 task = new AsyncTask<String, Void, String>() {
                     @Override
                     protected String doInBackground(String... urls) {

                         //start network connection
                         return Network.fetchJSONData(urls[0]);
                     }

                     @Override
                     protected void onPreExecute() {
                         //set a loading text to text view before loading rate
                         textView.setText(R.string.loading_text);
                     }

                     @Override
                     protected void onPostExecute(String s) {
                         //check to see if s is empty
                         if (TextUtils.isEmpty(s)) {
                             textView.setText(R.string.loaded_text);
                             return;
                         }

                         //get rate
                         rate = getExchangeRate(s);
                         textView.setText(String.valueOf(rate));

                         //set spinner to cease selection
                         spinner.setEnabled(false);
                     }
                 };

                //Check for network connection state and execute async task
                if (CheckNetworkConnState.isConnected(getContext())) {
                    if (spinner.getSelectedItemPosition() > 0) {
                        task.execute(uri_builder.toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText(R.string.loaded_text);
            }
        });
    }

    @Override
    public int getItemCount() {
        //return size of currency array list
        return mCurrency.size();
    }

    /**
     *
     * @param response data obtained from network query
     * @return rate
     */
    private double getExchangeRate(String response){
        double rate = 0.0;
        try {
            JSONObject root = new JSONObject(response);
            JSONObject currency = root.getJSONObject(selected_crypto);
            rate = currency.getDouble(selected_currency);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return rate;
    }

}
