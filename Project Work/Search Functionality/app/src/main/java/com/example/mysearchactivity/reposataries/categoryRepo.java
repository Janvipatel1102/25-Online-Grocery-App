package com.example.mysearchactivity.reposataries;

import android.app.Application;
import android.os.AsyncTask;

import com.example.mysearchactivity.model.categories;
import com.example.mysearchactivity.persistant.categoryDao;
import com.example.mysearchactivity.persistant.categoryDatabase;

import java.util.List;
import java.util.Locale;

import androidx.lifecycle.LiveData;

public class categoryRepo<firebaseDatabase> {

    private categoryDao mCategoryDao;
    private LiveData<List<categories>> mAllCategories,categoriesByName;



    public categoryRepo(Application application) {
        categoryDatabase db = categoryDatabase.getDatabase(application);
        mCategoryDao = db.categoryDao();
        mAllCategories = mCategoryDao.getAllCategories();

    }

    public LiveData<List<categories>> getmAllCategories() {
        return mAllCategories;
    }

    public LiveData<List<String>> getCategoryByName(String query) {
        return  mCategoryDao.getCategoryByName(query);

    }

    public LiveData<List<String>> getAllCategoriesNames()
    {
        return  mCategoryDao.getAllCategoriesNames();
    }

    public void insert (categories categories) {
        new insertAsyncTask(mCategoryDao).execute(categories);
    }

    private static class insertAsyncTask extends AsyncTask<categories, Void, Void> {

        private categoryDao mAsyncTaskDao;

        insertAsyncTask(categoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final categories... params) {
            mAsyncTaskDao.insertCategory(params[0]);
            return null;
        }
    }



    public void deleteAll () {
        new deleteAllAsyncTask(mCategoryDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private categoryDao mAsyncTaskDao;

        deleteAllAsyncTask(categoryDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAllCategories();
            return null;
        }
    }


}