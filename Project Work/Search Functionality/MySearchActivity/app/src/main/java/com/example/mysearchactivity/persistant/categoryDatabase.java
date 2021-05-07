package com.example.mysearchactivity.persistant;


import android.content.Context;
import android.os.AsyncTask;

import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.model.categories;

import com.example.mysearchactivity.reposataries.categoryRepo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {categories.class}, version = 1, exportSchema = false)
public abstract class categoryDatabase extends RoomDatabase {

    public abstract categoryDao categoryDao();


    private static categoryDatabase INSTANCE;

    private static  List<categories> categoriesList;
//    private  static  categoryRepo categoryRepo =   new categoryRepo();;

    public static categoryDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (categoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            categoryDatabase.class, "categoryDatabase")
                            .fallbackToDestructiveMigration()
                           // .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }





}