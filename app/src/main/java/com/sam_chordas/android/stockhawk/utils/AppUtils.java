package com.sam_chordas.android.stockhawk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by satyanarayana.avv on 12-05-2016.
 */
public class AppUtils {

  public static boolean isInternetConnectiionAvailable(Context context){
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null &&
        activeNetwork.isConnectedOrConnecting();
  }

}
