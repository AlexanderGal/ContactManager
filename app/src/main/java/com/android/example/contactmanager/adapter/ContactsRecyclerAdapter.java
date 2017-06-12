package com.android.example.contactmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.contactmanager.entity.Contact;
import com.android.example.contactmanager.R;
import com.android.example.contactmanager.ui.ContactActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple recyclerview adapter impl
 *
 * @author Galochkin A.
 */

public class ContactsRecyclerAdapter extends RecyclerView.Adapter {
    private List<Contact> mContacts;

    public ContactsRecyclerAdapter() {
        this.mContacts = new ArrayList<>();
    }

    public void setContacts(List<Contact> data) {
        mContacts = null;
        mContacts = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).fillView(
                mContacts.get(position).getmLabel(),
                mContacts.get(position).getmNumber());
        ((ViewHolder) holder).mContactId = mContacts.get(position).getmId();
    }

    @Override
    public int getItemCount() {
        return mContacts == null ? 0 : mContacts.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mContactImage;
        TextView mContactName;
        TextView mContactNumber;
        long mContactId;

        ViewHolder(final View itemView) {
            super(itemView);
            mContactImage = (ImageView) itemView.findViewById(R.id.contact_imageview);
            mContactName = (TextView) itemView.findViewById(R.id.contact_name);
            mContactNumber = (TextView) itemView.findViewById(R.id.contact_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                Context context = itemView.getContext();

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ContactActivity.class);
                    intent.putExtra(ContactActivity.EXTRA_CONTACT_NAME, mContactName.getText().toString());
                    intent.putExtra(ContactActivity.EXTRA_CONTACT_NUMBER, mContactNumber.getText().toString());
                    intent.putExtra(ContactActivity.EXTRA_CONTACT_ID, mContactId);
                    intent.setAction(ContactActivity.ACTION_EDIT_CONTACT);
                    context.startActivity(intent);
                }
            });
        }

        void fillView(String name, String number) {
            mContactName.setText(name);
            mContactNumber.setText(number);
        }
    }
}
