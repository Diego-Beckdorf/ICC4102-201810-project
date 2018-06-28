package com.example.diego.handwritingnotes;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diego.handwritingnotes.database.AppDatabase;
import com.example.diego.handwritingnotes.database_interface.DaoAccess;
import com.example.diego.handwritingnotes.database_orm.Document;
import com.example.diego.handwritingnotes.layout_helpers.DocumentListAdapter;
import com.example.diego.handwritingnotes.utils.APIManager;

import java.text.Normalizer;
import java.util.List;

public class IndexActivity extends AppCompatActivity {
    private static final String DATABASE_NAME = "app_db";
    private AppDatabase appDatabase;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        setContentView(R.layout.activity_main);

        getDocuments();

        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Toast.makeText(this, "Sending image to API", Toast.LENGTH_LONG).show();
            //TODO async task required
            APIManager apiManager = new APIManager();
            //apiManager.requestAPIProcess(imageBitmap);
            //TODO parse API response
            String text = apiManager.processAPIResponse();
            saveDocument(text);
        }
        getDocuments();
    }

    public void saveDocument(String text){
        final String st = text;
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaoAccess dao = appDatabase.daoAccess();
                Document d = new Document(st, "01", "01");
                dao.insertDocument(d);
            }
        }).start();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void fillDocumentsList(List<Document> documents) {
        ListAdapter listAdapter = new DocumentListAdapter(this, documents);
        ListView documentsListView = findViewById(R.id.documents_list);
        documentsListView.setAdapter(listAdapter);
    }

    public void getDocuments() {
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Document> documents = (List<Document>) msg.obj;
                fillDocumentsList(documents);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaoAccess dao = appDatabase.daoAccess();
                List<Document> forms = dao.getDocuments();
                Message msg = new Message();
                msg.obj = forms;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
