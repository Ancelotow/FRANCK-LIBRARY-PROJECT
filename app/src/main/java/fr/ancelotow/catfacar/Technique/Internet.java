package fr.ancelotow.catfacar.Technique;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class Internet
{
    public static boolean isConnectedInternet(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            State networkState = networkInfo.getState();
            if (networkState.compareTo(State.CONNECTED) == 0)
            {
                return true;
            }
            else return false;
        }
        else return false;
    }

}

