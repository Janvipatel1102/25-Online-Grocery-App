package com.example.mysearchactivity;


import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysearchactivity.adapters.categoryAdapter;
import com.example.mysearchactivity.adapters.categoryWiseAdapter;
import com.example.mysearchactivity.adapters.suggetion_adapter;
import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.viewModels.categoryViewModel;
import com.example.mysearchactivity.viewModels.categoryWiseViewModel;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class searchDialog extends AppCompatActivity {


   private categoryWiseViewModel categoryWiseViewModel;
    private ListView listView;
    private categoryViewModel categoryViewModel;
    SearchManager searchManager;
    SearchView searchView;
    SearchView.SearchAutoComplete searchAutoComplete;
    long count = 0;
    private CompositeDisposable disposable = new CompositeDisposable();
    Handler handler = new Handler(Looper.getMainLooper());
    Subscription subscription;

    EditText editText;
    Observable<String> observable;

    List<String> categories = new ArrayList<>(),itemNames = new ArrayList<>(),totalSuggestion = new ArrayList<>();

    suggetion_adapter suggetion_adapter;
    RecyclerView recyclerView;
    Intent intent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchdiolog);
        recyclerView = (RecyclerView) findViewById(R.id.suggestions_recyclerview);
        suggetion_adapter = new suggetion_adapter(this);
        recyclerView.setAdapter(suggetion_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryWiseViewModel = new ViewModelProvider(this).get(categoryWiseViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(categoryViewModel.class);
     //   suggetion_adapter.setSuggestions(null);

         intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchview, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setQueryHint("Search Product ,Categories Here..");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify t

        editText = ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        editText.setHintTextColor(getResources().getColor(R.color.grey3));
        editText.setTextColor(getResources().getColor(R.color.dark_grey));



        ImageView searchCloseIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchCloseIcon.setImageResource(R.drawable.ic_clear_black_24dp);
        searchCloseIcon.setColorFilter(getResources().getColor(R.color.grey3));

        ImageView searchIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.ic_search_black_24dp);
        searchIcon.setColorFilter(getResources().getColor(R.color.grey3));

        searchView.setMaxWidth(Integer.MAX_VALUE);

        observableFunction();

        if (Intent.ACTION_GET_CONTENT.equals(intent.getAction())) {
            String s  = intent.getStringExtra("query");
            Log.d(String.valueOf(searchDialog.this),s);

        }

        return super.onCreateOptionsMenu(menu);
    }

    public void observableFunction()
    {
         observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {

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

                        totalSuggestion.clear();
                        Intent intent = new Intent(getApplicationContext(), searchresult.class);
                        intent.setAction(Intent.ACTION_SEARCH);
                        intent.putExtra("query", searchQuery);
                        searchView.setQuery(null,false);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        if(!emitter.isDisposed())
                        {
                            emitter.onNext(newText);
                        }
                        return true;
                    }
                });

            }
        }).debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());

        observable.subscribe(new io.reactivex.Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);

            }

            @Override
            public void onNext(@NonNull String s) {

                Log.d(String.valueOf(searchDialog.this),"String = "+(s.isEmpty()==false && s!=null) );
                if(s != null && (s.isEmpty()==false))
                {

                    //  handler.removeCallbacks(runnable);
                    String searchQuery = "%"+s+"%";
                    handler.post(new Runnable() {
                        public void run() {
                            itemNames(searchQuery);
                            categoryNames(searchQuery);

                        }
                    });
                }
                else
                {
                    totalSuggestion.clear();

                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

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
        suggetion_adapter.setSuggestions(totalSuggestion);

        suggetion_adapter.setOnItemClickListener(new suggetion_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String s = totalSuggestion.get(position);
                Log.d(String.valueOf(searchDialog.this),"String = "+ s);
                String searchQuery = "";
               /* if(categories.contains(s))
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
        disposable.dispose();

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(searchDialog.this,MainActivity.class);
        startActivity(startMain);
        finish();
    }

}
