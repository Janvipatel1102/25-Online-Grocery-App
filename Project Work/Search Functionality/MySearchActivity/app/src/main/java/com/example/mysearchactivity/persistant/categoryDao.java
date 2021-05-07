package com.example.mysearchactivity.persistant;

import com.example.mysearchactivity.Constants;
import com.example.mysearchactivity.model.categories;

import java.util.List;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface categoryDao {

    @Insert
    void insertcategories(categories...categories);

    @Insert
    void insertCategory(categories category);

    @Update
    void updateCategory(categories category);

    @Delete
    void deleteCategory(categories category);

    @Query("DELETE FROM "+ Constants.CATEGORIES_TABLE_NAME)
    void deleteAllCategories();

    @Query("SELECT * FROM "+ Constants.CATEGORIES_TABLE_NAME+" ORDER BY category_id")
    LiveData<List<categories>> getAllCategories();

    @Query("SELECT category_name FROM "+ Constants.CATEGORIES_TABLE_NAME + " ORDER BY category_name")
    LiveData<List<String>> getAllCategoriesNames();


    @Query("SELECT category_name FROM "+ Constants.CATEGORIES_TABLE_NAME +" WHERE category_name LIKE :Query ORDER BY category_name")
    LiveData<List<String>> getCategoryByName(String Query);



}