package com.gamerequirements.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by v3rt1ag0 on 8/3/18.
 */




public class InternetConnectionUtil {
    private Context context;

    public InternetConnectionUtil(Context context) {
        this.context = context;
    }

    public String Check() {
        ConnectivityManager cn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = null;
        if (cn != null)
        {
            nf = cn.getActiveNetworkInfo();
        }

        if (nf != null && nf.isConnected()) {
            return "Connection timed out. Please try again after some time";
        } else {
            return "Check your connection and try again";
        }
    }
}
