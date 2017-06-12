package com.android.example.contactmanager.entity;

import android.support.annotation.NonNull;

/**
 * Simple entity class
 *
 * @author Galochkin A.
 */
public class Contact implements Comparable<Contact> {
    private long mId;
    private String mImageUri;
    private String mNumber;
    private String mLabel;

    public Contact() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (mId != contact.mId) return false;
        if (mImageUri != null ? !mImageUri.equals(contact.mImageUri) : contact.mImageUri != null)
            return false;
        if (mNumber != null ? !mNumber.equals(contact.mNumber) : contact.mNumber != null)
            return false;
        return mLabel != null ? mLabel.equals(contact.mLabel) : contact.mLabel == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (mNumber != null ? mNumber.hashCode() : 0);
        result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
        result = 31 * result + (mImageUri != null ? mImageUri.hashCode() : 0);
        return result;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "mId=" + mId +
                ", mNumber='" + mNumber + '\'' +
                ", mLabel='" + mLabel + '\'' +
                ", mImageUri='" + mImageUri + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Contact o) {
        return mLabel.compareTo(o.mLabel);
    }
}
