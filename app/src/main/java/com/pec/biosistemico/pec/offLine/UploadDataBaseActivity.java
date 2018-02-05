package com.pec.biosistemico.pec.offLine;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.pec.biosistemico.pec.main;
import com.pec.biosistemico.pec.util.TreatDate;
import com.pec.biosistemico.pec.util.TreatFile;
import com.pec.biosistemico.pec.R;
import java.io.File;
import java.util.Date;
import java.util.Iterator;



public class UploadDataBaseActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = "UploadDataBaseActivity";
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final String PATH_DATABASE = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private static final String FOLDER_ID_UPLOAD_DATABASE = "0ByuWZKF0EONhaHhDNlF3dEpvNjg";
    private String mNameConsultor = null;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        mNameConsultor = extras.getString("NAME_CONSULTOR");
    }

    private void saveFileToDrive() {
        Log.i(TAG, "Creating new contents.");
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveContentsResult>() {

            @Override
            public void onResult(DriveContentsResult result) {
                if (!result.getStatus().isSuccess()) {
                    Log.i(TAG, "Failed to create new contents.");
                    return;
                }
                Log.i(TAG, "New contents created.");
                File data = Environment.getDataDirectory();
                File file = new File(PATH_DATABASE + "IbsPEC.db");

                final DriveContents content = result.getDriveContents();
                if (content != null && TreatFile.writeOutputStreamFromFile(content.getOutputStream(), file)) {

                    Drive.DriveApi.fetchDriveId(mGoogleApiClient, FOLDER_ID_UPLOAD_DATABASE).setResultCallback(new ResultCallback<DriveApi.DriveIdResult>() {
                        @Override
                        public void onResult(@NonNull final DriveApi.DriveIdResult driveIdResult) {
                            Log.i("Drive ID", "Drive ID " + driveIdResult.getDriveId().getResourceId());
                            if(driveIdResult.getDriveId().getResourceType() == DriveId.RESOURCE_TYPE_FOLDER){
                                uploadFile(driveIdResult.getDriveId().asDriveFolder(), mNameConsultor + TreatDate.format("ddMMyyyy", new Date()) + System.currentTimeMillis(), content);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }

        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
        saveFileToDrive();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    private void uploadFile(DriveFolder driveFolder, String nameFileToUpload, DriveContents content){
        MetadataChangeSet metaDataBase = new MetadataChangeSet.Builder().setTitle(nameFileToUpload).setMimeType("application/x-sqlite3").build();
        driveFolder.createFile(mGoogleApiClient, metaDataBase, content).setResultCallback(
                new ResultCallback<DriveFolder.DriveFileResult>() {
                    @Override
                    public void onResult(@NonNull DriveFolder.DriveFileResult fileResult) {
                        if (!fileResult.getStatus().isSuccess()) {
                            Log.e("Error result file", "Error while trying to upload the file");
                            return;
                        }
                        Log.i("File created", "DriveId " + fileResult.getDriveFile().getDriveId().getResourceId());
                        Toast.makeText(getApplicationContext(), "Upload realizado com sucesso", Toast.LENGTH_LONG).show();
                        restartApp();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(UploadDataBaseActivity.this,main.class);
        startActivity(intent);
    }

    private void restartApp(){
        Intent intent = new Intent(this, main.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
}