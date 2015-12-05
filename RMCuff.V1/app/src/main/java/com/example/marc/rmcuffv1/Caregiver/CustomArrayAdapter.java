package com.example.marc.rmcuffv1.Caregiver;

/**
 * Created by Davidb on 9/23/15.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.marc.rmcuffv1.R;

import java.util.ArrayList;


public class CustomArrayAdapter extends BaseAdapter {

    private static final String LOG_TAG = CustomArrayAdapter.class.getSimpleName();
    private static ArrayList<Caregiver> searchArrayList;
    private LayoutInflater mInflater;

    public CustomArrayAdapter(Context context, ArrayList<Caregiver> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
        //Log.d(LOG_TAG, "ADAPTER SET: " + searchArrayList.toString());
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.caregiver_row_view, null);
                holder = new ViewHolder();

                holder.txtFirstName = (TextView) convertView.findViewById(R.id.firstName);
                holder.txtLastName = (TextView) convertView.findViewById(R.id.lastName);
                holder.txtEmailAddress = (TextView) convertView.findViewById(R.id.emailAddress);
                holder.txtPhoneNumber = (TextView) convertView.findViewById(R.id.phoneNumber);
                holder.chkSelected = (CheckBox) convertView.findViewById(R.id.caregiverSelectBox);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtFirstName.setText(searchArrayList.get(position).getFirstName());
            holder.txtLastName.setText(searchArrayList.get(position).getLastName());
            holder.txtEmailAddress.setText(searchArrayList.get(position).getEmailAddress());
            holder.txtPhoneNumber.setText(searchArrayList.get(position).getPhoneNum());
            holder.chkSelected.setChecked(searchArrayList.get(position).getNotify());

            holder.txtFirstName.setTypeface(null, Typeface.BOLD);
            holder.txtLastName.setTypeface(null, Typeface.BOLD);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class ViewHolder {
        TextView txtFirstName;
        TextView txtLastName;
        TextView txtEmailAddress;
        TextView txtPhoneNumber;
        CheckBox chkSelected;
    }
}