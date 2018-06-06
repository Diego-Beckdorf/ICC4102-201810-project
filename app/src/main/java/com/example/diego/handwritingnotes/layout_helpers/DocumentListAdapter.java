package com.example.diego.handwritingnotes.layout_helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.diego.handwritingnotes.R;
import com.example.diego.handwritingnotes.database_orm.Document;

import java.util.List;

/**
 * Created by diego on 27-05-2018.
 */

public class DocumentListAdapter extends ArrayAdapter<Document> {

    public DocumentListAdapter(Context context, List<Document> objects){
        super(context, R.layout.document_template, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        @SuppressLint("ViewHolder") View documentView = inflater.inflate(R.layout.document_template,
                parent, false);

        final Document document= getItem(position);
        TextView name = documentView.findViewById(R.id.name);
        TextView createdAt = documentView.findViewById(R.id.created_at);
        assert document != null;
        name.setText(document.getName());
        createdAt.setText(document.getCreatedAt());

        return documentView;
    }
}
