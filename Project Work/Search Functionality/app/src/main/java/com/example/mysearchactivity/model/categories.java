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

    @ColumnInfo(name = "image_url")
    private String image_url;

    @Ignore
public categories()
{

}

    public categories(String name, String image_url) {
        this.name = name;
        this.image_url = image_url;
    }

    @NonNull
    public Integer getCategory_id() {
        return category_id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

}
