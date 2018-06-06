package com.example.diego.handwritingnotes.database_interface;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.diego.handwritingnotes.database_orm.Document;

import java.util.List;

/**
 * Created by diego on 27-05-2018.
 */

@Dao
public interface DaoAccess {
    //region Single Insertion
    @Insert
    long insertDocument(Document document);
    //endregion

    //region multi Insertion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDocuments(List<Document> documents);
    //endregion

    //region Get all
    @Query("SELECT * FROM Document")
    List<Document> getDocuments();
    //endregion

    //region Get by id
    @Query ("SELECT * FROM Document WHERE documentId = :documentId")
    Document getDocument(long documentId);

    //region Update
    @Update
    void updateDocument(Document document);
    //endregion

    //region Delete
    @Delete
    void deleteDocument(Document document);
    //endregion
}
