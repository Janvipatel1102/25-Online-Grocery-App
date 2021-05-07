package com.example.mysearchactivity.model;

import com.example.mysearchactivity.Constants;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Constants.CATEGORIES_TABLE_NAME)
public class categories {

    
    @PrimaryKey(autoGenerate = true)
    private Integer category_id;

    @ColumnInfo(name = "category_name")
    private String name;

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }


    @Ignore
        public categories()
        {

        }

    public categories(String name) {
        this.name = name;

    }

    @NonNull
    public Integer getCategory_id() {
        return category_id;
    }


    public void setName(String name) {
        this.name = name;
    }



    public String getName() {
        return name;
    }



}
