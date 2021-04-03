package com.example.mysearchactivity.persistant;

import android.database.Cursor;

import com.example.mysearchactivity.Constants;
import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.model.categories;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface categoryWiseDao {

    @Insert
    void insertCatogoryWiseItems(CategoryWiseItems...categoryWiseItems);

    @Insert
    void insertCatogoryWiseItem(CategoryWiseItems categoryWiseItem);

    @Update
    void updateCatogoryWiseItem(CategoryWiseItems categoryWiseItem);

    @Delete
    void deleteCatogoryWiseItem(CategoryWiseItems categoryWiseItem);

    @Query("DELETE FROM "+ Constants.CATEGORIESWISE_TABLE_NAME)
    void deleteAllCatogoryWiseItem();

    @Query("SELECT * FROM "+ Constants.CATEGORIESWISE_TABLE_NAME)
    LiveData<List<CategoryWiseItems>> getAllCategoryWiseItem();

    @Query("SELECT * FROM " + Constants.CATEGORIESWISE_TABLE_NAME + " WHERE item_name LIKE :query")
    LiveData<List<CategoryWiseItems>> getItemByName(String query);

    @Query("SELECT item_name FROM " + Constants.CATEGORIESWISE_TABLE_NAME + " WHERE item_name LIKE :query")
    LiveData<List<String>> getNameByName(String query);

    @Query("SELECT * FROM " + Constants.CATEGORIESWISE_TABLE_NAME + " WHERE category_of_item LIKE :query")
    LiveData<List<CategoryWiseItems>> getItemByCategories(String query);
}