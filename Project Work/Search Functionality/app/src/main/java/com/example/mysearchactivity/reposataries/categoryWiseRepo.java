package com.example.mysearchactivity.reposataries;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.persistant.categoryDatabase;
import com.example.mysearchactivity.persistant.categoryWiseDao;
import com.example.mysearchactivity.persistant.categoryWiseDatabase;

import java.util.Currency;
import java.util.List;

import androidx.lifecycle.LiveData;
public class categoryWiseRepo<firebaseDatabase> {

    private categoryWiseDao mcategoryWiseDao;

    private LiveData<List<CategoryWiseItems>> mAllCategoryWiseItems,nameWiseItems,mItemByCategory;


    public categoryWiseRepo(Application application) {
        categoryWiseDatabase db = categoryWiseDatabase.getDatabase(application);
        mcategoryWiseDao = db.categoryWiseDao();
        mAllCategoryWiseItems = mcategoryWiseDao.getAllCategoryWiseItem();

    }

    public LiveData<List<CategoryWiseItems>> getmAllCategoryWiseItems() {
        return mAllCategoryWiseItems;
    }
    public LiveData<List<CategoryWiseItems>> getItemByName(String query) {
        nameWiseItems  = mcategoryWiseDao.getItemByName(query);
        return  nameWiseItems;
    }
    public LiveData<List<CategoryWiseItems>> getItemByCategories(String query) {
         mItemByCategory  = mcategoryWiseDao.getItemByCategories(query);
        return  mItemByCategory;
    }

    public LiveData<List<String>> getNameByName(String query) {
        return mcategoryWiseDao.getNameByName(query);

    }



    public void insert (CategoryWiseItems CategoryWiseItems) {
        new insertAsyncTask(mcategoryWiseDao).execute(CategoryWiseItems);
    }

    private static class insertAsyncTask extends AsyncTask<CategoryWiseItems, Void, Void> {

        private categoryWiseDao mAsyncTaskDao;

        insertAsyncTask(categoryWiseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CategoryWiseItems... params) {
            mAsyncTaskDao.insertCatogoryWiseItem(params[0]);
            return null;
        }
    }



    public void deleteAll () {
        new deleteAllAsyncTask(mcategoryWiseDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private categoryWiseDao mAsyncTaskDao;

        deleteAllAsyncTask(categoryWiseDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAllCatogoryWiseItem();
            return null;
        }
    }


}