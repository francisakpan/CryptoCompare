package com.francis.cryptocompare.main;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.francis.cryptocompare.Currency;
import com.francis.cryptocompare.adapters.CurrencyAdapter;
import com.francis.cryptocompare.R;
import com.francis.cryptocompare.fragments.AboutFragment;
import com.francis.cryptocompare.fragments.AddCardFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        AddCardFragment.OnCryptoCurrencySelectedListener{

    //Log text string
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Declare views, adapter and ArrayList
    FragmentManager manager;
    ArrayList<Currency> currencies;
    CurrencyAdapter adapter;
    RecyclerView recyclerView;
    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        currencies = new ArrayList<>();
        adapter = new CurrencyAdapter(this, currencies);

        //Initialize recycler view
        recyclerView = (RecyclerView)findViewById(R.id.recycler);

        //set an currency adapter to recycler view
        recyclerView.setAdapter(adapter);

        //define recycler view layout type using layoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //initialize floatingActionButton and set onClickListener
        add = (FloatingActionButton) findViewById(R.id.addAction);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCard();
            }
        });

    }

    //inflate the menu options to action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                //Opens the about fragment
                AboutFragment ab = new AboutFragment();
                ab.setCancelable(false);
                ab.show(getSupportFragmentManager(), "AboutFragment");
                return true;
            case R.id.add:
                //add a new card to screen
                addNewCard();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //function that adds a new card to screen
    private void addNewCard(){
        AddCardFragment fragment = new AddCardFragment();
        fragment.show(manager, LOG_TAG);
    }

    /**
     *
     * @param selected The crypto currency selected
     */
    @Override
    public void onCryptoCurrencySelected(String selected) {
        currencies.add(new Currency(selected));
        adapter.notifyItemInserted(currencies.size()-1);
    }
}
