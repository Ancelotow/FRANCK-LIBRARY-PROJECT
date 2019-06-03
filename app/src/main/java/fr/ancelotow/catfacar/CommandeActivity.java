package fr.ancelotow.catfacar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import fr.ancelotow.catfacar.technique.Session;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CommandeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "owen.ancelot.sio";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };

    TextView tvError;
    EditText etNom;
    EditText etAuteur1;
    EditText etAuteur2;
    EditText etEdition;
    Button btnRetour;
    Button btnReserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);
        setTitle(Session.getSession().getUser().getNom().toUpperCase()  +
                Session.getSession().getUser().getPrenom());
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        final String error = "Tout les champs avec l'étoile doivent être remplis.";
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Sheets API ...");
        mOutputText = (TextView) findViewById(R.id.tvError);
        etNom = (EditText) findViewById(R.id.etNom);
        etAuteur1 = (EditText) findViewById(R.id.etAuteur1);
        etAuteur2 = (EditText) findViewById(R.id.etAuteur2);
        etEdition = (EditText) findViewById(R.id.etEdition);
        btnRetour = (Button) findViewById(R.id.btnRetour);
        btnReserver = (Button) findViewById(R.id.btnReserver);
        View.OnClickListener reserver = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNom.getText().toString().equals("")){
                    mOutputText.setText(error);
                }
                else if(etAuteur1.getText().toString().equals("")){
                    mOutputText.setText(error);
                }
                else if(etEdition.getText().toString().equals("")){
                    mOutputText.setText(error);
                }
                else{
                    System.out.println("////////// MAIN //////////////////");
                    getResultsFromApi();
                }
            }
        };
        View.OnClickListener retourner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommandeActivity.this,
                        MainActivity.class);
                startActivity(i);
            }
        };
        btnReserver.setOnClickListener(reserver);
        btnRetour.setOnClickListener(retourner);
    }


    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText("Aucun connection internet.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "Cette application as besoin d'un compte Gmail (via les contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                CommandeActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }


    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                int numRes = getNumRes();
                writeReservation(numRes);
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            String spreadsheetId = "1I6Hvtclv3avAQndP7jbTtl2bp67bNucuahPESdLzYn4";
            String range = "A1:A2";
            List<String> results = new ArrayList<String>();
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values != null) {
                for (List row : values) {
                    results.add("OK");
                }
            }
            return results;
        }

        private int getNumRes() throws IOException {
            String spreadsheetId = "1I6Hvtclv3avAQndP7jbTtl2bp67bNucuahPESdLzYn4";
            System.out.println("///// API ////////////");
            String range = "A2:I";
            ValueRange response = this.mService.spreadsheets()
                    .values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            System.out.println("///// API22 ////////////");
            int max = 0;
            if (values != null) {
                for (List row : values) {
                    if(((Integer) Integer.valueOf((String)row.get(0))) >= max){
                        max = ((Integer) Integer.valueOf((String)row.get(0))) + 1;
                    }
                }
            }
            return max;
        }

        private void writeReservation(int numRes) throws IOException {
            String spreadsheetId = "1I6Hvtclv3avAQndP7jbTtl2bp67bNucuahPESdLzYn4";
            String range = "A:I";
            Object A = numRes;
            Object B = Session.getSession().getUser().getNom().toUpperCase();
            Object C = Session.getSession().getUser().getPrenom();
            Object D = Session.getSession().getUser().getTel();
            Object E = Session.getSession().getUser().getEmail();
            Object F = etNom.getText().toString();
            Object G = etAuteur1.getText().toString();
            Object H = etAuteur2.getText().toString();
            Object I = etEdition.getText().toString();
            List<List<Object>> values = Arrays.asList(Arrays.asList(A, B, C, D, E, F, G, H, I));
            ValueRange body = new ValueRange().setValues(values);
            String valueInputOption = "RAW";
            AppendValuesResponse result = mService.spreadsheets()
                    .values()
                    .append(spreadsheetId, range, body)
                    .setValueInputOption(valueInputOption)
                    .execute();
        }

        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            Toast.makeText(CommandeActivity.this,
                    "Votre réservation à été prise en compte.",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(CommandeActivity.this,
                    MainActivity.class);
            startActivity(i);
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            CommandeActivity.REQUEST_AUTHORIZATION);
                } else {
                    System.out.println("/////////// Erreur ///////////");
                    mOutputText.setText("L'erreur suivante s'est produite:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }


}
