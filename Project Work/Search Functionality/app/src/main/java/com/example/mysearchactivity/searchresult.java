package com.example.mysearchactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mysearchactivity.adapters.categoryAdapter;
import com.example.mysearchactivity.adapters.categoryWiseAdapter;
import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.model.categories;
import com.example.mysearchactivity.persistant.categoryDao;
import com.example.mysearchactivity.reposataries.categoryRepo;
import com.example.mysearchactivity.viewModels.categoryViewModel;
import com.example.mysearchactivity.viewModels.categoryWiseViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class searchresult<async> extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private  categoryWiseViewModel categoryWiseViewModel;
    private  categoryViewModel categoryViewModel;
     categoryWiseAdapter adapter;
     List<String> allCategories = new ArrayList<>();
    RecyclerView recyclerView;
    TextView emptyView;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        emptyView = (TextView) findViewById(R.id.empty_view);
         recyclerView = findViewById(R.id.itemrecyclerview);
        adapter = new categoryWiseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryWiseViewModel = new ViewModelProvider(this).get(categoryWiseViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(categoryViewModel.class);


       //  getAllCategories();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          String  query = intent.getStringExtra("query");
          Log.d(String.valueOf(searchresult.this),query);

          doMySearch(query);
        }

    }

    public  void doMySearch(String Query)
    {

        String tag = Query.split(" ")[0];
        String query = Query.split(" ")[1];
        if(tag .equals("categories"))
        {

            categoryWiseViewModel.getItemByCategory("%"+query+"%").observe(this, new Observer<List<CategoryWiseItems>>() {
                @Override
                public void onChanged(List<CategoryWiseItems> categoryWiseItems) {

                    if(categoryWiseItems.size()==0)
                    {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                    Log.d(String.valueOf(searchresult.this),"Size of category "+String.valueOf(categoryWiseItems.size()));
                    adapter.setCategory(categoryWiseItems);
                }
            });
        }
        else {
           // Log.d(String.valueOf(searchresult.this),"In Items items ");
            categoryWiseViewModel.getItemByName("%" + query + "%").observe(this, new Observer<List<CategoryWiseItems>>() {
                    @Override
                    public void onChanged(@Nullable final List<CategoryWiseItems> categories) {
                        // Update the cached copy of the words in the adapter.
                        if(categories.size()==0)
                        {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                        adapter.setCategory(categories);

                    }
                });
            }

       }

}