package com.nyu.cs9033.eta.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;

/**
 * Created by Vismay on 3/14/2015.
 */
public class PersonAdapterView extends ArrayAdapter {
    public static final String TAG = PersonAdapterView.class.getSimpleName();
    private final Context context;
    private Person[] people;

    public PersonAdapterView(Context context, Person[] people) {
        super(context, R.layout.person_view);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.people = people;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = inflater.inflate(R.layout.person_view, parent, false);
            TextView personNameView = (TextView) view.findViewById(R.id.PersonName);
            Log.v(TAG, "" + position);
            personNameView.setText(people[position].getName());
            TextView personLocation = (TextView) view.findViewById(R.id.personLocation);
            personLocation.setText(people[position].getLocation());
            TextView personPhoneView = (TextView) view.findViewById(R.id.personPhone);
            personPhoneView.setText(people[position].getPhone());
        } else {
            view = convertView;
        }
        return view;
    }

    @Override
    public int getCount() {
        return (people != null) ? people.length : 0;
    }

    @Override
    public Person getItem(int idx) {
        return (people != null) ? people[idx] : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int pos) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

}
