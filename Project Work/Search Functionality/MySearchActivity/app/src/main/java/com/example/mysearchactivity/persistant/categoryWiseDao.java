package com.example.mysearchactivity.persistant;

import android.database.Cursor;
import android.util.Pair;

import com.example.mysearchactivity.Constants;
import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.model.categories;

import java.nio.channels.FileLock;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.SkipQueryVerification;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import static android.icu.text.MessagePattern.ArgType.SELECT;

@Dao
@SkipQueryVerification
public interface categoryWiseDao {

    @Insert
    void insertCatogoryWiseItems(CategoryWiseItems...categoryWiseItems);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCatogoryWiseItem(CategoryWiseItems categoryWiseItem);

    @Update
    void updateCatogoryWiseItem(CategoryWiseItems categoryWiseItem);

    @Delete
    void deleteCatogoryWiseItem(CategoryWiseItems categoryWiseItem);

    @Query("DELETE FROM "+ Constants.CATEGORIESWISE_TABLE_NAME)
    void deleteAllCatogoryWiseItem();

    @Query("SELECT * FROM "+ Constants.CATEGORIESWISE_TABLE_NAME)
    LiveData<List<CategoryWiseItems>> getAllCategoryWiseItem();

    @Query("SELECT item_name FROM " + Constants.CATEGORIESWISE_TABLE_NAME + " WHERE item_name LIKE :query ORDER BY item_name")
    LiveData<List<String>> getNameByName(String query);


    @Query("DELETE  FROM " + Constants.CATEGORIESWISE_TABLE_NAME + " WHERE category_of_item = :category_name ")
    void deleteByCategoryName(String category_name);




    //Fttch data queries;
    @RawQuery(observedEntities = CategoryWiseItems.class)
    LiveData<List<CategoryWiseItems>> getProductsFromDatabse(SupportSQLiteQuery query);

    //sort products

    @RawQuery(observedEntities = CategoryWiseItems.class)
    LiveData<List<CategoryWiseItems>> doMySearch(SupportSQLiteQuery query);


}