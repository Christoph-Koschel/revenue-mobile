package com.koschel.revenue.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.koschel.revenue.mobile.R;
import com.koschel.revenue.mobile.model.ActionModel;

public class ActionAdapter extends ArrayAdapter<ActionModel> {

    public ActionAdapter(@NonNull Context context, ActionModel[] list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.action_card, parent, false);
        }

        ActionModel model = getItem(position);
        assert model != null;
        ImageView image = view.findViewById(R.id.icon);
        TextView title = view.findViewById(R.id.title);

        image.setImageResource(model.drawable);
        title.setText(model.name);

        if (!view.hasOnClickListeners()) {
            view.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), model.target);
                getContext().startActivity(intent);
            });
        }

        return view;
    }
}
