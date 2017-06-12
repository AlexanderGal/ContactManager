package com.android.example.contactmanager.ui;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.example.contactmanager.R;

import java.util.ArrayList;

/**
 * ContactActivity
 *
 * @author Galochkin A.
 */

//todo 7. Сделать рефактроинг - вынести CRUD методы в отдельный класс
public class ContactActivity extends AppCompatActivity {
    public static final String TAG = ContactActivity.class.getCanonicalName();
    //todo 8. Вынести все константы в отдельный класс?
    public static final String ACTION_NEW_CONTACT = TAG + "_ADD_NEW_ACCOUNT";
    public static final String ACTION_EDIT_CONTACT = TAG + "_EDDIT_ACCOUNT";
    public static final String EXTRA_CONTACT_NUMBER = "extra_contact_number";
    public static final String EXTRA_CONTACT_NAME = "extra_contact_namne";
    public static final String EXTRA_CONTACT_ID = "extra_contact_id";

    private EditText mContactNameET;
    private EditText mContactNumberET;
    private View mSaveButton;
    private View mDeleteButton;
    private ContentResolver mContentResolver;
    private long mContactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mContentResolver = getContentResolver();
        initView();
        fillView();
    }

    private void initView() {
        mContactNameET = (EditText) findViewById(R.id.contact_name_edittext);
        mContactNumberET = (EditText) findViewById(R.id.contact_number_edittext);
        mSaveButton = findViewById(R.id.contact_save_button);
        mDeleteButton = findViewById(R.id.contact_delete_button);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkField(mContactNameET, mContactNumberET)) {
                    if (getIntent().getAction().equals(ACTION_EDIT_CONTACT)) {
                        updateContact();
                    } else {
                        saveContact();
                    }
                    finish();
                }
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
                finish();
            }
        });
    }

    private void fillView() {
        Intent intent = getIntent();
        if (intent.getAction().equals(ACTION_EDIT_CONTACT)) {
            String name = intent.getStringExtra(EXTRA_CONTACT_NAME);
            String number = intent.getStringExtra(EXTRA_CONTACT_NUMBER);
            mContactId = intent.getLongExtra(EXTRA_CONTACT_ID, 0);
            mContactNameET.setText(name);
            mContactNumberET.setText(number);
        }

        if (intent.getAction().equals(ACTION_NEW_CONTACT)) {
            mSaveButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mDeleteButton.setVisibility(View.GONE);
        }
    }

    private boolean checkField(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.all_fields_is_req, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void saveContact() {
        Toast.makeText(ContactActivity.this, R.string.not_supported, Toast.LENGTH_SHORT).show();
        //todo 1. Реализовать сохранение
    }

    private void updateContact() {
        //todo 3. При изменении в Name добавляется номер - разобраться почему
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        //Name
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(mContactId)})
                .withValue(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, mContactNameET.getText().toString())
                .build());
        //Number
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(mContactId)})
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mContactNumberET.getText().toString())
                .build());
        try {
            mContentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }

    }

    private void deleteContact() {
        Toast.makeText(ContactActivity.this, R.string.not_supported, Toast.LENGTH_SHORT).show();
        //todo 2. Реализовать удаление
    }
}
