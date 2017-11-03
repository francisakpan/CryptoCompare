package com.francis.cryptocompare.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Francis on 02/11/2017.
 *
 */

public class CheckNetworkConnState {

    /**
     *
     * @param context application context
     * @return true if network connection is ON
     */
    public static boolean isConnected(Context context){

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
