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

        Button saveButton = findViewById(R.id.document_save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_content = content.getText().toString();
                String text_title = title_doc.getText().toString();
                saveDocument(text_content, text_title);
                indexCall();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO share action.
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void saveDocument(String text, final String title){
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String st = text;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String date = df.format(Calendar.getInstance().getTime());
                DaoAccess dao = appDatabase.daoAccess();
                Document d = new Document(title, st, date);
                dao.insertDocument(d);
            }
        }).start();
    }

    private void indexCall() {
        Intent indexIntent = new Intent(DocumentActivity.this, IndexActivity.class);
        startActivity(indexIntent);
        finish();
    }

}
