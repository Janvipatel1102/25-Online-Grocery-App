
package com.example.mysearchactivity.model;

import android.content.Intent;

import com.example.mysearchactivity.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Constants.CATEGORIESWISE_TABLE_NAME)
public class CategoryWiseItems {

    @NonNull
    @PrimaryKey
    private String item_id;

    @ColumnInfo(name = "price_with_discount")
    private Float price_with_discount;


    @ColumnInfo(name = "item_name")
    private String item_name;

    @ColumnInfo(name = "category_of_item")
    private  String category_of_item;

    @ColumnInfo(name = "discount")
    private Float discount;

    @ColumnInfo(name = "rating_count")
    private String ratingCount;

    @ColumnInfo(name = "rating")
    private Float rating;

    @ColumnInfo(name = "price")
    private String price;


    @ColumnInfo(name = "image_url")
    private String image_url;

    @ColumnInfo(name = "in_stock")
    private boolean in_stock;


    public boolean isIn_stock() {
        return in_stock;
    }

    public void setIn_stock(boolean in_stock) {
        this.in_stock = in_stock;
    }

    @Ignore
    public CategoryWiseItems()
    {

    }

    public CategoryWiseItems( String item_id,String item_name, Float discount, String ratingCount, Float rating, String price,String category_of_item ,String image_url,Float price_with_discount,boolean in_stock)
    {
        this.item_id =item_id;
        this.item_name = item_name;
        this.discount = discount;
        this.ratingCount = ratingCount;
        this.rating = rating;
        this.price = price;
        this.category_of_item = category_of_item;
        this.image_url =image_url;
        this.price_with_discount = price_with_discount;
        this.in_stock = in_stock;

    }

    @NonNull
    public Float getPrice_with_discount() {
        return price_with_discount;
    }

    public void setPrice_with_discount(Float price_with_discount) {
        this.price_with_discount = price_with_discount;
    }


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public String getCategory_of_item() {
        return category_of_item;
    }

    public void setCategory_of_item(String category_of_item) {
        this.category_of_item = category_of_item;
    }



    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }



    public void setName(String name) {
        item_name = name;
    }


    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float  discount) {
        this.discount = discount;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(
            String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
