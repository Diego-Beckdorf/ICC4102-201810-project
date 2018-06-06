package com.example.diego.handwritingnotes.database_orm;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by diego on 27-05-2018.
 */

@Entity
public class Document {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long documentId;
    private String name;
    private String storagePath;
    private String createdAt;

    public Document(String name, String storagePath, String createdAt) {
        this.name = name;
        this.storagePath = storagePath;
        this.createdAt = createdAt;
    }

    @NonNull
    public long getDocumentId() {return documentId;}
    public void setDocumentId(@NonNull long documentId) {this.documentId = documentId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getStoragePath() {return storagePath;}
    public void setStoragePath(String storagePath) {this.storagePath = storagePath;}

    public String getCreatedAt() {return createdAt;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}
}
