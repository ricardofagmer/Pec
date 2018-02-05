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
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.adapter.ResultsDriveAdapter;
import com.pec.biosistemico.pec.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nelson on 04/11/2016.
 */

public class ListFoldersActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ListFoldersActivity";
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final String FOLDER_ID_DOWNLOAD_DATABASE = "0ByuWZKF0EONhYTVkWlR3MTJLREE";
    private ResultsDriveAdapter mResultsFolderAdapter;
    private ListView mResultsFoldersListView;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listfolders);
        mResultsFoldersListView = (ListView) findViewById(R.id.listViewResultsFolders);
        mResultsFolderAdapter = new ResultsDriveAdapter(this);
        mResultsFoldersListView.setAdapter(mResultsFolderAdapter);

        mResultsFoldersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Metadata metaData = (Metadata) mResultsFoldersListView.getItemAtPosition(position);

                if (metaData.getDriveId().getResourceType() == DriveId.RESOURCE_TYPE_FOLDER) {
                    Intent intent = new Intent(ListFoldersActivity.this, DownloadDataBaseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("FOLDER_ID", metaData.getDriveId().getResourceId());
                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
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

                Drive.DriveApi.fetchDriveId(mGoogleApiClient, FOLDER_ID_DOWNLOAD_DATABASE).setResultCallback(new ResultCallback<DriveApi.DriveIdResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveIdResult driveIdResult) {
                        Log.i(TAG, "Drive ID " + driveIdResult.getDriveId().getResourceId());
                        if (driveIdResult.getDriveId().getResourceType() == DriveId.RESOURCE_TYPE_FOLDER) {
                            driveIdResult.getDriveId().asDriveFolder().listChildren(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                @Override
                                public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                                    MetadataBuffer result = metadataBufferResult.getMetadataBuffer();

                                    mResultsFolderAdapter.clear();
                                    mResultsFolderAdapter.append(result);
                                }
                            });
                        }
                    }
                });
            }
        });
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