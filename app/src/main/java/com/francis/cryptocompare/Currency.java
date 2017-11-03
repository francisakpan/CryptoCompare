package com.francis.cryptocompare;

/**
 * Created by Francis on 15/10/2017.
 */

public class Currency {

    // a crypto currency
    private String crypto_currency;

    /**
     *
     * @param crypto_currency a crypto currency
     */
    public Currency(String crypto_currency){
        this.crypto_currency = crypto_currency;
    }

    //get selected crypto currency
    public String getCrypto_currency() {
        return crypto_currency;
    }
}
