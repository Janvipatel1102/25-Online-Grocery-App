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
    LiveData<List<CategoryWiseItems>> sortProducts(SupportSQLiteQuery query);

    @RawQuery(observedEntities = CategoryWiseItems.class)
    LiveData<List<CategoryWiseItems>> filterProducts(SupportSQLiteQuery query);








    // Filters categoryWiseItems


    @Query("SELECT * FROM " + Constants.CATEGORIESWISE_TABLE_NAME +
            " WHERE :ColumnName = :query and " +
            "("+

            "(price_with_discount >= :price_filter1 and price_with_discount <=:price_filter2) " +
            "or (price_with_discount >= :price_filter3 and price_with_discount <=:price_filter4) " +
            "or (price_with_discount >= :price_filter5 and price_with_discount <=:price_filter6)" +
            " or (price_with_discount >= :price_filter7 and price_with_discount <=:price_filter8) " +
            " or (price_with_discount >= :price_filter9 and price_with_discount <=:price_filter10) " +
            " or (price_with_discount >= :price_filter11 and price_with_discount <=:price_filter12) " +
            "or (price_with_discount >= :price_filter13 and price_with_discount <=:price_filter14)"+

             " or (discount >= :dis_filter1)"
            + "or ( discount >= :dis_filter2)"
            + "or ( discount >= :dis_filter3)"
            + "or ( discount >= :dis_filter4)"
            + "or ( discount >= :dis_filter5)"
            + "or ( discount <= :dis_filter6)"
            +
            " or (rating >= :rating_filter1)"
            + "or ( rating >= :rating_filter2)"
            + "or ( rating >= :rating_filter3)"
            + "or ( rating >= :rating_filter4)"
           +

            " or (in_stock = :availiability_filter1)"+")"+

            "ORDER BY item_id LIMIT :limit OFFSET :offset "

    )
    LiveData<List<CategoryWiseItems>>
    applyfilters(String query,String ColumnName,
                                    Float price_filter1,
                                    Float price_filter2,
                                    Float price_filter3,
                                    Float price_filter4,
                                    Float price_filter5,
                                    Float price_filter6,
                                    Float price_filter7,
                                    Float price_filter8,
                                    Float price_filter9,
                                    Float price_filter10,
                                    Float price_filter11,
                                    Float price_filter12,
                                    Float price_filter13,
                                    Float price_filter14,

                                    Float dis_filter1,
                                    Float dis_filter2,
                                    Float dis_filter3,
                                    Float dis_filter4,
                                    Float dis_filter5,
                                    Float dis_filter6,

                                    Float rating_filter1,
                                    Float rating_filter2,
                                    Float rating_filter3,
                                    Float rating_filter4,

                                    boolean availiability_filter1,

                                    int limit, int offset);
}