package com.example.mysearchactivity.persistant;

import android.content.Context;

import com.example.mysearchactivity.model.CategoryWiseItems;

import java.util.List;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CategoryWiseItems.class}, version = 1, exportSchema = false)
public abstract class categoryWiseDatabase extends RoomDatabase {

    public abstract categoryWiseDao categoryWiseDao();
    private static categoryWiseDatabase INSTANCE;

   // private static List<CategoryWiseItems> categoriesList;

    public static categoryWiseDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (categoryWiseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            categoryWiseDatabase.class, "categoryWiseDatabase")
                            .fallbackToDestructiveMigration()
                            // .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}