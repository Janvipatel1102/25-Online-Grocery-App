package com.example.mysearchactivity;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysearchactivity.adapters.categoryWiseAdapter;
import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.viewModels.categoryViewModel;
import com.example.mysearchactivity.viewModels.categoryWiseViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class searchDialog extends AppCompatActivity {

   private categoryWiseViewModel categoryWiseViewModel;
    private ListView listView;
    private categoryViewModel categoryViewModel;
    SearchManager searchManager;
    SearchView searchView;
    SearchView.SearchAutoComplete searchAutoComplete;

    List<String> categories = new ArrayList<>(),itemNames = new ArrayList<>(),totalSuggestion = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.searchdiolog);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        categoryWiseViewModel = new ViewModelProvider(this).get(categoryWiseViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(categoryViewModel.class);

        //   categoryWiseViewModel = new ViewModelProvider(this).get(categoryWiseViewModel.class);
       // categoryViewModel = new ViewModelProvider(this).get(categoryViewModel.class);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchview, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify t
        searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                String searchQuery = "";
                if(categories.contains(query))
                {
                    searchQuery = "categories "+query;
                }
                else
                {
                    searchQuery = "items "+query;
                }

                Intent intent = new Intent(getApplicationContext(), searchresult.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra("query", searchQuery);
                searchView.setQuery(null,false);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null)
                {
                    Log.d(String.valueOf(searchDialog.this),newText);
                    String searchQuery = "%"+newText+"%";

                    itemNames(searchQuery);
                    categoryNames(searchQuery);

                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void categoryNames(String searchQuery)
    {
        categoryViewModel.getCategoryByName(searchQuery).observe(searchDialog.this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {

                List<String> categoryName = strings;
                categories = categoryName;
                setTotalSuggestion();
            }
        });
    }

    public void itemNames(String searchQuery)
    {
        categoryWiseViewModel.getNameByName(searchQuery).observe(searchDialog.this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> categories) {

                List<String> nameList = categories;
                itemNames = nameList;
                setTotalSuggestion();


            }
        });
    }

    public void setTotalSuggestion()
    {
        totalSuggestion.clear();
        totalSuggestion.addAll(itemNames);
        totalSuggestion.addAll(categories);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.suggestion_item, totalSuggestion);
        searchAutoComplete.setAdapter(adapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Your code for onitemclick
                String s = totalSuggestion.get(position);
                searchAutoComplete.setAdapter(null);
              /*  String searchQuery = "";
                if(categories.contains(s))
                {
                    searchQuery = "categories "+s;
                }
                else
                {
                    searchQuery = "items "+s;
                }*/

                searchView.setQuery(s,true);

            }
        });

    }

}
