package mx.unam.primera.com.logic;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.model.*;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Samuel on 29/05/2017.
 */

public class Calendar implements EasyPermissions.PermissionCallbacks
{
    Context context;
    public GoogleAccountCredential mCredential;

    private static final String[] SCOPES = {CalendarScopes.CALENDAR};
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;


    public Calendar(Context _cont)
    {
        context = _cont;
        mCredential = GoogleAccountCredential.usingOAuth2(
                context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        Log.i("GAC", mCredential.toString());
    }

    private void getResultsFromApi() {
        /**
         * Attempt to call the API, after verifying that all the preconditions are
         * satisfied. The preconditions are: Google Play Services installed, an
         * account was selected and the device currently has online access. If any
         * of the preconditions are not satisfied, the app will prompt the user as
         * appropriate.
         */
        if (!isGooglePlayServicesAvailable())
            acquireGooglePlayServices();

        else if (mCredential.getSelectedAccountName()== null)
            chooseAccount();

        else if (!isDeviceOnline())
            Log.i("getResultsFromApi", "conexion no disponible");

        else
            new MakeRequestTask(mCredential).execute();

    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount()
    {
        if (EasyPermissions.hasPermissions(context,
                Manifest.permission.GET_ACCOUNTS))
        {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME,null);

            if (accountName !=null)
            {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            }

            else
                startActivityForResult(mCredential.newChooseAccountIntent(),REQUEST_ACCOUNT_PICKER);
        }
        else
        {
            EasyPermissions.requestPermissions(context,"esta app necesita accesar a tu cuenta de google",
                    REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
        }
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)

    {

    }
    */

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms)
    {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms)
    {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

    }
}
