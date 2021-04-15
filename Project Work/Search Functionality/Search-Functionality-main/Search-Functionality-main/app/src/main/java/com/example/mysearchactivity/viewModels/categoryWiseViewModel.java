package com.example.mysearchactivity.viewModels;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import com.example.mysearchactivity.Constants;
import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.reposataries.categoryWiseRepo;

import java.nio.channels.FileLock;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

public class categoryWiseViewModel extends AndroidViewModel {

    private categoryWiseRepo mRepository;

    private LiveData<List<CategoryWiseItems>> mAllCategoryWiseItems,mItemByCategories;

    public categoryWiseViewModel (Application application) {

        super(application);
        mRepository = new categoryWiseRepo(application);
        mAllCategoryWiseItems = mRepository.getmAllCategoryWiseItems();
    }

    public LiveData<List<CategoryWiseItems>> getAllCategoryWiseItems() { return mAllCategoryWiseItems; }

    public void insert(CategoryWiseItems category) { mRepository.insert(category); }

    public void delete(CategoryWiseItems category) { mRepository.delete(category); }


    public void deletAllCategoryWiseItems() { mRepository.deleteAll(); }


    public LiveData<List<String>> getNameByName(String query)
    {
        return  mRepository.getNameByName(query);
    }

    public  void deleteCategoryByName (String category_name)
    {
        mRepository.deleteByCategoryName(category_name);
    }

    //get products from database;

    public LiveData<List<CategoryWiseItems>> getProductsFromDatabse(String queryName,String columnName,String sortColumnName,String order,int limit,int offset) {

        SupportSQLiteQuery query;
        String queryString= String.format("SELECT * FROM  %s WHERE %s LIKE '%s' ORDER BY %s %s LIMIT %d  OFFSET %d "
                ,Constants.CATEGORIESWISE_TABLE_NAME ,columnName,queryName,sortColumnName,order,limit,offset);

        Log.d(String.valueOf(categoryWiseViewModel.this),queryString);

        query = new SimpleSQLiteQuery(queryString);
        return mRepository.getProductsFromDatabse(query);
    }


    public LiveData<List<CategoryWiseItems>> applyFilters(String Query, List<Pair<Float,Float>> price_filters_list, List<Float> discount_filters_list, List<Float> rating_filters_list, List<Boolean> isStockFilters_list, String column, String sortColumnName, String order, int limit, int offset)
    {

        SupportSQLiteQuery query;
        String filter ="";

        for(int i=0;i<price_filters_list.size();i++)
        {
            if(i==0)
            {
                filter +=" and ((";
            }
            else
            {
                filter +=" or ( ";
            }
            Pair<Float,Float> p= price_filters_list.get(i);
            String minValue = String.valueOf(p.first);
            String maxValue = String.valueOf(p.second);
            filter += Constants.CATEGORIESWISE_price_with_discount+" >= "+minValue +" and ";
            filter += Constants.CATEGORIESWISE_price_with_discount+" <= "+maxValue +" )";
            if(i==price_filters_list.size()-1)
                filter+=")";
        }

      //  Log.d(String.valueOf(categoryWiseViewModel.this),filter);

        for(int i=0;i<discount_filters_list.size();i++)
        {
            if(i==0)
            {
                filter +=" and ((";
            }
            else
            {
                filter +=" or (";
            }
            Float f = discount_filters_list.get(i);

            if(f==29F)
            {
                filter = filter + Constants.CATEGORIESWISE_discount+" <= "+String.valueOf(f) +") ";
            }
            else
                filter = filter + Constants.CATEGORIESWISE_discount+" >= "+String.valueOf(f)+" ) ";

            if(i==discount_filters_list.size()-1)
            {
                filter+=")";
            }


        }


        for(int i=0;i<rating_filters_list.size();i++)
        {
            if(i==0)
            {
                filter += " and ((";
            }
            else
            {
                filter +=" or ( ";
            }
            String f = String.valueOf(rating_filters_list.get(i));

            filter = filter + Constants.CATEGORIESWISE_rating+" >= "+f +") ";

            if(i==rating_filters_list.size()-1)
                filter+=")";

        }

        for(int i=0;i<isStockFilters_list.size();i++)
        {

            String f = String.valueOf(isStockFilters_list.get(i));
            filter = filter +" and (("+ Constants.CATEGORIESWISE_in_stock+" = 0 "+" ))";

        }


        String queryString= String.format("SELECT * FROM  %s WHERE  ( %s LIKE '%s' %s )  ORDER BY %s %s LIMIT %d  OFFSET %d "
                ,Constants.CATEGORIESWISE_TABLE_NAME ,column,Query,filter,sortColumnName,order,limit,offset);

        Log.d(String.valueOf(categoryWiseViewModel.this),queryString);

        query = new SimpleSQLiteQuery(queryString);
        return mRepository.filterProducts(query);
    }

}


