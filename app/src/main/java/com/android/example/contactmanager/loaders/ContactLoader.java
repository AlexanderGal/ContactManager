package com.android.example.contactmanager.loaders;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;

import com.android.example.contactmanager.entity.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact loader
 *
 * @author Galochkin A.
 */

public class ContactLoader extends AsyncTaskLoader<List<Contact>> {
    private ContentResolver mContentResolver;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public ContactLoader(Context context) {
        super(context);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public List<Contact> loadInBackground() {
        List<Contact> list = new ArrayList<>();
        Cursor cursor;
        cursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        if (cursor != null) {
            /*
             * Дефолтная позиция Курсора = -1.
             * Поэтому сразу используем метод moveToNext(Cursor.mPos + 1) без перевода на первое значение резалтсэта
             */
            while (cursor.moveToNext()) {
                Contact contact = new Contact();
                contact.setmId(cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID)));
                contact.setmLabel(getString(cursor, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                getPhoneNumber(cursor, contact);
                list.add(contact);
            }
        }
        return list;
    }

    private void getPhoneNumber(Cursor cursor, Contact contact) {
        Cursor phoneCursor;

        if (Integer.parseInt(getString(cursor, ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
            phoneCursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{getString(cursor, ContactsContract.Contacts._ID)}, null);

            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    @SuppressLint("InlinedApi") String number = getString(phoneCursor, ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                    if (number == null || number.equals("")) {
                        number = getString(phoneCursor, ContactsContract.CommonDataKinds.Phone.NUMBER);
                    }
                    contact.setmNumber(number);
                }
            }
        }
    }

    private String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
