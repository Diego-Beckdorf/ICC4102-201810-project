package com.example.diego.handwritingnotes.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.diego.handwritingnotes.database_interface.DaoAccess;
import com.example.diego.handwritingnotes.database_orm.Document;

/**
 * Created by diego on 27-05-2018.
 */

@Database(entities = {Document.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}
