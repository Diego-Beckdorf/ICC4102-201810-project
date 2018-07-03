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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.diego.handwritingnotes.database.AppDatabase;
import com.example.diego.handwritingnotes.database_interface.DaoAccess;
import com.example.diego.handwritingnotes.database_orm.Document;
import com.example.diego.handwritingnotes.layout_helpers.DocumentListAdapter;
import com.example.diego.handwritingnotes.utils.APIManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexActivity extends AppCompatActivity {
    private static final String DATABASE_NAME = "app_db";
    private AppDatabase appDatabase;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Integer> doc_list;

    private static final String subscriptionKey = "93b117483ad447c4bf14969976c7a7d1";

    private static final String uriBase =
            "https://southcentralus.api.cognitive.microsoft.com/vision/v1.0/recognizeText";

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

        Button newButton = findViewById(R.id.new_note);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNoteIntent("", -1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //TODO uploadBitmap(imageBitmap);

            newNoteIntent("caca", -1);
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void newNoteIntent(String text, int doc_id) {
        Intent newNoteIntent = new Intent(IndexActivity.this, DocumentActivity.class);
        newNoteIntent.putExtra("text", text);
        newNoteIntent.putExtra("doc_id", doc_id);
        startActivity(newNoteIntent);
        finish();
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
        documentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newNoteIntent("", doc_list.get((int)id));
            }
        });
    }

    public void getDocuments() {
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Document> documents = (List<Document>) msg.obj;
                doc_list = new ArrayList<Integer>();
                for (int i=0; i<documents.size(); i++){
                    doc_list.add((int)documents.get(i).getDocumentId());
                }
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
