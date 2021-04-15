package com.example.mysearchactivity.viewModels;


import android.app.Application;

import com.example.mysearchactivity.model.categories;
import com.example.mysearchactivity.reposataries.categoryRepo;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class categoryViewModel extends AndroidViewModel {

    private categoryRepo mRepository;

    private LiveData<List<categories>> mAllCategories;

    public categoryViewModel (Application application) {

        super(application);
        mRepository = new categoryRepo(application);
        mAllCategories = mRepository.getmAllCategories();
    }

    public LiveData<List<categories>> getAllCategories() { return mAllCategories; }

    public void insert(categories category) { mRepository.insert(category); }

    public void deletAllCategories() { mRepository.deleteAll(); }

    public LiveData<List<String>> getCategoryByName(String query)
    {
        return  mRepository.getCategoryByName(query);
    }


    public LiveData<List<String>> getAllCategoriesNames()
    {
        return  mRepository.getAllCategoriesNames();
    }




}