package com.francis.cryptocompare.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.francis.cryptocompare.R;

public class ExchangeRateActivity extends AppCompatActivity {

    //Declare views
    TextView exchangeRate, calculatedRate;
    ImageView avatar;
    EditText input;
    ImageButton button;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exchanget_rate_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.string.close_text){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);

        //get intent object with extras
        Intent intent = getIntent();
        final String rate_value = intent.getStringExtra("rate");
        String coin = intent.getStringExtra("crypto");
        final String local_currency = intent.getStringExtra("currency");

        //initialize exchangeRate text view
        exchangeRate = (TextView) findViewById(R.id.display_rate);

        //set rate value to exchangeRate text view
        exchangeRate.setText(rate_value);

        //initialize crypto currency image view and set image
        avatar = (ImageView) findViewById(R.id.crypto_avatar);
        if (coin.equals(getString(R.string.btc_text))){
            avatar.setImageResource(R.drawable.ic_bitcoin);
        }else{
            avatar.setImageResource(R.drawable.ic_ether);
        }

        //initialize calculate exchange rate text view
        calculatedRate = (TextView)findViewById(R.id.exchange_rate_text);

        //initialize EditView
        input = (EditText) findViewById(R.id.enter_text);

        //initialize calculate button view and set onClickListener
        button = (ImageButton) findViewById(R.id.calc_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int entered = Integer.valueOf(input.getText().toString());
                double calc = entered * Double.valueOf(rate_value);
                calculatedRate.setText(local_currency + ": " + calc);
            }
        });
    }
}
