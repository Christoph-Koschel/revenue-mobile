package com.koschel.revenue.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.koschel.revenue.mobile.R;
import com.koschel.revenue.mobile.SendActivity;
import com.koschel.revenue.mobile.model.TagModel;

public class TagAdapter extends ArrayAdapter<TagModel> {
    public TagAdapter(@NonNull Context context, @NonNull TagModel[] objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.tag_card, parent, false);
        }


        final TagModel model = getItem(position);
        assert model != null;

//        if (!view.hasOnClickListeners()) {
//            view.setOnClickListener(new View.OnClickListener() {
//                private final TagModel _model = model;
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }

        TextView title = view.findViewById(R.id.title);
        TextView type = view.findViewById(R.id.type);

        title.setText(model.name);
        type.setText(model.income ? "Einkommen" : "Ausgabe");
        return view;
    }
}
