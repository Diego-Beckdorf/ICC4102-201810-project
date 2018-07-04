package com.example.diego.handwritingnotes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.diego.handwritingnotes.database.AppDatabase;
import com.example.diego.handwritingnotes.database_interface.DaoAccess;
import com.example.diego.handwritingnotes.database_orm.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DocumentActivity extends AppCompatActivity {

    //TODO instance of document from IndexActivity, save button action

    private static final String DATABASE_NAME = "app_db";
    private AppDatabase appDatabase;

    private Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        setContentView(R.layout.activity_document);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText content = (EditText) findViewById(R.id.document_content);
        final String text = getIntent().getStringExtra("text");
        content.setText(text);

        final EditText title_doc = (EditText) findViewById(R.id.document_title);

        final int doc_id = getIntent().getIntExtra("doc_id", -1);
        if (doc_id != -1){
            getDocument(doc_id);
        }

        Button saveButton = findViewById(R.id.document_save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                String text_content = content.getText().toString();
                String text_title = title_doc.getText().toString();

                final String date = df.format(Calendar.getInstance().getTime());

                if (doc_id != -1){
                    updateDocument(doc_id, text_content, text_title, date);
                } else {
                    saveDocument(text_content, text_title, date);
                }
                indexCall();
            }
        });

        Button cancelButton = findViewById(R.id.document_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexCall();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText content = (EditText) findViewById(R.id.document_content);

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{""});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content.getText().toString());
                getApplicationContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
    }

    public void getDocument(final int doc_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                EditText content = (EditText) findViewById(R.id.document_content);
                EditText title_doc = (EditText) findViewById(R.id.document_title);

                doc = appDatabase.daoAccess().getDocument(doc_id);
                content.setText(doc.getStoragePath());
                title_doc.setText(doc.getName());
            }
        }).start();
    }

    public void saveDocument(String text, final String title, final String date){
        final String st = text;
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaoAccess dao = appDatabase.daoAccess();
                Document d = new Document(title, st, date);
                dao.insertDocument(d);
            }
        }).start();
    }

    public void updateDocument(final int doc_id, final String text, final String title, final String date){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaoAccess dao = appDatabase.daoAccess();

                Document doc = dao.getDocument(doc_id);
                doc.setName(title);
                doc.setCreatedAt(date);
                doc.setStoragePath(text);

                dao.updateDocument(doc);
            }
        }).start();
    }

    private void indexCall() {
        Intent indexIntent = new Intent(DocumentActivity.this, IndexActivity.class);
        startActivity(indexIntent);
        finish();
    }

}
