package com.pec.biosistemico.pec.offLine;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.adapter.ResultsDriveAdapter;
import com.pec.biosistemico.pec.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class DownloadDataBaseActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "DownloadDBActivity";
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private ResultsDriveAdapter mResultsFileAdapter;
    private ListView mResultsFilesListView;
    private String mFolderId;

    private GoogleApiClient mGoogleApiClient;

      /*0ByuWZKF0EONhYTVkWlR3MTJLREE DOWNLOAD_DATABASE
        0ByuWZKF0EONhaHhDNlF3dEpvNjg CRIATF
        0ByuWZKF0EONha3RzQ2ZGRlFoSW8 RUFIAO
        0ByuWZKF0EONhbjNpbU9ZRm9MUnc VACA_MOVEL
        0ByuWZKF0EONhcnpZMFRNZENWUUk UPLOAD_DATABASE*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
         mFolderId = extras.getString("FOLDER_ID");

        setContentView(R.layout.activity_listfiles);

        mResultsFilesListView = (ListView) findViewById(R.id.listViewResults);
        mResultsFileAdapter = new ResultsDriveAdapter(this);
        mResultsFilesListView.setAdapter(mResultsFileAdapter);

        mResultsFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Metadata metaData = (Metadata) mResultsFilesListView.getItemAtPosition(position);

                if (metaData.getDriveId().getResourceType() == DriveId.RESOURCE_TYPE_FILE) {
                    downloadFile(metaData.getDriveId());
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
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "API client connected.");
        saveFileFromDrive();
    }

    private void saveFileFromDrive() {
        Log.i(TAG, "Creating new contents.");

        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    Log.i(TAG, "Failed to create new contents.");
                    return;
                }

                Drive.DriveApi.fetchDriveId(mGoogleApiClient, mFolderId).setResultCallback(new ResultCallback<DriveApi.DriveIdResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveIdResult driveIdResult) {
                        Log.i(TAG, "Drive ID " + driveIdResult.getDriveId().getResourceId());
                        if (driveIdResult.getDriveId().getResourceType() == DriveId.RESOURCE_TYPE_FOLDER) {

                            driveIdResult.getDriveId().asDriveFolder().listChildren(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                @Override
                                public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                                    MetadataBuffer result = metadataBufferResult.getMetadataBuffer();

                                    mResultsFileAdapter.clear();
                                    mResultsFileAdapter.append(result);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void downloadFile(DriveId driveId) {
        DriveFile file = driveId.asDriveFile();
        file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    return;
                }

                DriveContents driveContents = driveContentsResult.getDriveContents();
                BufferedInputStream bis = new BufferedInputStream(driveContents.getInputStream());
                byte[] buffer = new byte[1024];
                int bytesread = 0;
                FileOutputStream outStream;

                File sourceDbFile = getApplicationContext().getDatabasePath("IbsPEC.db");

                if (sourceDbFile != null) {
                    sourceDbFile.delete();
                }

                Log.i(TAG, "Write File in database");

                try {
                    outStream = new FileOutputStream(getApplicationContext().getDatabasePath("IbsPEC.db"));
                    while ((bytesread = bis.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesread);
                    }

                    outStream.flush();
                    bis.close();
                    outStream.close();
                } catch (FileNotFoundException e) {
                    Log.i(TAG, e.getMessage());
                } catch (IOException e) {
                    Log.i(TAG, e.getMessage());
                } finally {
                    Toast.makeText(getApplicationContext(), "Os dados do Google Drive foram restaurados com sucesso.", Toast.LENGTH_LONG).show();
                }
                driveContents.discard(mGoogleApiClient);
                restart();
            }
        });
    }

    private void restart() {
        Intent intent = new Intent(this, main.class);
        this.startActivity(intent);
        this.finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DownloadDataBaseActivity.this,main.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());

        if (!connectionResult.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            return;
        }

        try {
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }
}
