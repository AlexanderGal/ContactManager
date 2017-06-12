package com.android.example.contactmanager.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.example.contactmanager.loaders.ContactLoader;
import com.android.example.contactmanager.R;
import com.android.example.contactmanager.adapter.ContactsRecyclerAdapter;
import com.android.example.contactmanager.entity.Contact;

import java.util.List;

/**
 * ContactListActivity
 *
 * @author Galochkin A.
 */

public class ContactListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Contact>> {
    private static final String TAG = ContactListActivity.class.getCanonicalName();
    private static final int LOADER_ID = 12321;
    private static final int REQUEST_CODE = 432;

    private RecyclerView mContactRecyclerView;
    private ContactsRecyclerAdapter mContactsRecyclerAdapter;
    private Snackbar mHintSnackBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "protected void onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        if (!hasPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)) {
            ActivityCompat.requestPermissions(ContactListActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE);
        }
    }

    private void initView() {
        Log.e(TAG, "private void initView()");
        mContactRecyclerView = (RecyclerView) findViewById(R.id.contact_recyclerview);
        mContactsRecyclerAdapter = new ContactsRecyclerAdapter();

        mContactRecyclerView.setAdapter(mContactsRecyclerAdapter);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)) {
                    Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
                    intent.setAction(ContactActivity.ACTION_NEW_CONTACT);
                    startActivity(intent);
                } else {
                    showHint();
                }
            }
        });
    }

    private void showHint() {
        Log.e(TAG, "private void showHint()");
        mHintSnackBar = Snackbar.make(mContactRecyclerView, R.string.no_permission, Snackbar.LENGTH_INDEFINITE);
        mHintSnackBar.setAction(R.string.enable, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ContactListActivity.this, new String[]{Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS},
                        REQUEST_CODE);
            }
        });
        mHintSnackBar.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            hasPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
        } else {
            showHint();
        }
    }

    /**
     * Check all needed permission
     *
     * @param permissions - Permissions name array
     * @return true if all needed permission granted
     */
    private boolean hasPermissions(String... permissions) {
        Log.e(TAG, "private boolean hasPermissions(String... permissions)");
        Context context = getApplicationContext();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        if (mHintSnackBar != null) {
            mHintSnackBar.dismiss();
        }
        getSupportLoaderManager().initLoader(LOADER_ID, null, ContactListActivity.this);
        return true;
    }

    @Override
    public Loader<List<Contact>> onCreateLoader(int id, Bundle args) {
        Log.e(TAG, "public Loader<List<Contact>> onCreateLoader(int id, Bundle args)");
        return new ContactLoader(ContactListActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {
        Log.e(TAG, "public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data)");
        mContactsRecyclerAdapter.setContacts(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {
        Log.e(TAG, "public void onLoaderReset(Loader<List<Contact>> loader)");
    }
}
