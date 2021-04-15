package com.example.mysearchactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysearchactivity.adapters.categoryAdapter;
import com.example.mysearchactivity.adapters.categoryWiseAdapter;
import com.example.mysearchactivity.model.CategoryWiseItems;
import com.example.mysearchactivity.model.categories;
import com.example.mysearchactivity.persistant.categoryDao;
import com.example.mysearchactivity.reposataries.categoryRepo;
import com.example.mysearchactivity.viewModels.categoryViewModel;
import com.example.mysearchactivity.viewModels.categoryWiseViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class searchresult<async> extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private  categoryWiseViewModel categoryWiseViewModel;
    private  categoryViewModel categoryViewModel;
     categoryWiseAdapter adapter;
     List<CategoryWiseItems> categoryWiseItems = new ArrayList<CategoryWiseItems>(),tempCategoryWiseItems = new ArrayList<>()
             ,filterCategoryItems = new ArrayList<>();
     List<String> allCategories = new ArrayList<>();
     RecyclerView recyclerView;
    TextView emptyView,search_bar;
    int offset = 0;
    int limit = Constants.Limit;
    private boolean loading = false;
    int scroll_out_items, visibleItemCount, totalItemCount;
    String queryWithFlag,query;
    LinearLayoutManager mLayoutManager;
    boolean shouldLoad=true;
    ProgressBar progressBar;
    LinearLayout searchBox;


    BottomSheetDialog bottomSheetDialog,bottomSheetDialogFilter;
    RadioButton price_low,price_high,relevance,popularity;
    View sheetView,filterSheetView;

    Button sort_button,filter_button;

    boolean isCategory=false,isFiltered=false;



    List<Pair<Float,Float>> price_filters_list = new ArrayList<>();


    List<Float> discount_filters_list = new ArrayList<>(),
            rating_filters_list = new ArrayList<>();
    List<Boolean> isStock_filters_list = new ArrayList<>();


    String columnName[] = {"category_of_item","item_name","discount","rating","price_with_discount","in_stock","item_id"};

    SupportSQLiteQuery query1;
    String column,order = "ASC",sortColumnName = columnName[6];

    TextView price_filter,discount_filter,rating_filter,avaliability_filter,clear_filter;
    LinearLayout price_filter_layout,discount_filter_layout,rating_filter_layout,avaibility_filter_layout;

    CheckBox below_250,between_251_500,between_501_1000,between_1001_2000
            ,between_2001_5000,between_5001_10000,above_10001,more_then_70,more_then_60,more_then_50,more_then_40
            ,more_then_30,below_30,above_4,above_3,above_2,above_1,exclude_out_of_stock;
    Button apply_filter;
    boolean ischecked= false,dis_below_30 = false;

    boolean isPrice_filtered=false,isDiscount_filtered=false,isAvailiability_filtered=false,isCustomoerRating_filtered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView = findViewById(R.id.search_result_list);
        adapter = new categoryWiseAdapter(this);
        recyclerView.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter.setCategory(categoryWiseItems);


        categoryWiseViewModel = new ViewModelProvider(this).get(categoryWiseViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(categoryViewModel.class);


        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        search_bar = (TextView)findViewById(R.id.search_product_name);
        searchBox = (LinearLayout)findViewById(R.id.search);

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchresult.this, searchDialog.class);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra("query", search_bar.getText());
                startActivity(intent);
                finish();
            }
        });

       //  getAllCategories();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryWithFlag = intent.getStringExtra("query");

            String tag = queryWithFlag.split(" ")[0];
            query = queryWithFlag.substring(queryWithFlag.indexOf(" ")+1,queryWithFlag.length());
            search_bar.setText(query);
            if(tag.equals("categories"))
                column = columnName[0];
            else
                column = columnName[1];


            applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);
           // Log.d(String.valueOf(searchresult.this),"column name = "+column);
        }
        fetchDataOnScrolling();

        sort_button = (Button) findViewById(R.id.sort_button);
        filter_button = (Button)findViewById(R.id.filter_button);

        bottomSheetDialog = new BottomSheetDialog(searchresult.this,R.style.BottomSheetTheme);
        sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sort_bottom_sheet,
                findViewById(R.id.bottom_sheet));

        bottomSheetDialogFilter = new BottomSheetDialog(searchresult.this,R.style.BottomSheetTheme);
        filterSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.filter_bottom_sheet,
                findViewById(R.id.bottom_filter_sheet_main));

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterItems();
            }
        });

        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortItems();
            }
        });


    }



    public void fetchDataOnScrolling()
       {
           Log.d(String.valueOf(searchresult.this),"Offset Value =  "+String.valueOf(offset));


           recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
               @Override
               public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                   super.onScrollStateChanged(recyclerView, newState);
                   if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                   {
                       loading=true;
                   }
               }

               @Override
               public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                   super.onScrolled(recyclerView, dx, dy);
                   visibleItemCount = mLayoutManager.getChildCount();//visible items;
                   totalItemCount = mLayoutManager.getItemCount();//total items;
                   scroll_out_items = mLayoutManager.findFirstVisibleItemPosition();// scrooled out  items;

                   if(shouldLoad && loading && scroll_out_items+visibleItemCount >= totalItemCount)
                   {
                       loading=false;
                       offset+=limit;
                       applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);
                   }
               }
           });

       }


        public void sortItems()
        {

            price_low = (RadioButton)sheetView.findViewById(R.id.price_low);
            price_high = (RadioButton)sheetView.findViewById(R.id.price_high);
            relevance = (RadioButton)sheetView.findViewById(R.id.relevance);
            popularity = (RadioButton)sheetView.findViewById(R.id.popularity);
            offset =0;
            shouldLoad = true;


            price_low.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(price_low.isChecked())
                    {
                        order = "ASC";
                        categoryWiseItems.clear();

                        sortColumnName = columnName[4];
                        applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);
                        bottomSheetDialog.dismiss();
                    }
                }
            });


            price_high.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    if(price_high.isChecked())
                    {
                        order = "DESC";
                        categoryWiseItems.clear();
                        sortColumnName = columnName[4];
                        applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);
                        bottomSheetDialog.dismiss();
                    }
                }
            });

            relevance.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    if(relevance.isChecked())
                    {
                        order = "ASC";
                        categoryWiseItems.clear();
                        sortColumnName = columnName[6];
                        applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);
                        bottomSheetDialog.dismiss();

                    }
                }
            });

            popularity.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    if(popularity.isChecked())
                    {
                        order = "DESC";
                        sortColumnName = columnName[3];
                        categoryWiseItems.clear();

                        // callFunctions();
                        applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);

                        bottomSheetDialog.dismiss();
                    }
                }
            });

         //   fetchDataOnScrolling();


            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();


        }

        public  void filterItems()
        {
        price_filter = filterSheetView.findViewById(R.id.price_filter);
        discount_filter = filterSheetView.findViewById(R.id.discount_filter);
        avaliability_filter = filterSheetView.findViewById(R.id.avaliability_filter);
        rating_filter = filterSheetView.findViewById(R.id.rating_filter);

        price_filter_layout = filterSheetView.findViewById(R.id.price_filter_layout);
        discount_filter_layout = filterSheetView.findViewById(R.id.discount_filter_layout);
        avaibility_filter_layout = filterSheetView.findViewById(R.id.outof_stock_filter_layout);
        rating_filter_layout = filterSheetView.findViewById(R.id.customer_filter_layout);

        apply_filter = filterSheetView.findViewById(R.id.apply_button);
        clear_filter = filterSheetView.findViewById(R.id.clear_filters);

        below_250 = filterSheetView.findViewById(R.id.below_250);
        between_251_500 =filterSheetView.findViewById(R.id.between_251_500);
        between_501_1000 = filterSheetView.findViewById(R.id.between_501_1000);
        between_1001_2000 = filterSheetView.findViewById(R.id.between_1001_2000);
        between_2001_5000 = filterSheetView.findViewById(R.id.between_2001_5000);
        between_5001_10000 = filterSheetView.findViewById(R.id.between_5001_10000);
        above_10001 = filterSheetView.findViewById(R.id.above_10001);

        more_then_30 = filterSheetView.findViewById(R.id.more_than_30);
        more_then_40 = filterSheetView.findViewById(R.id.more_than_40);
        more_then_50 = filterSheetView.findViewById(R.id.more_than_50);
        more_then_60 = filterSheetView.findViewById(R.id.more_than_60);
        more_then_70 = filterSheetView.findViewById(R.id.more_than_70);
        below_30 = filterSheetView.findViewById(R.id.below_30);

        above_1 = filterSheetView.findViewById(R.id.more_than_1);
        above_2 = filterSheetView.findViewById(R.id.more_than_2);
        above_3 = filterSheetView.findViewById(R.id.more_than_3);
        above_4 = filterSheetView.findViewById(R.id.more_than_4);


      //  include_out_of_stock = filterSheetView.findViewById(R.id.include_out_of_stock);
        exclude_out_of_stock = filterSheetView.findViewById(R.id.exclude_out_of_stock);

        price_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price_filter.setBackgroundColor(Color.WHITE);
                discount_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));
                avaliability_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));
                rating_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));



                price_filter_layout.setVisibility(View.VISIBLE);
                discount_filter_layout.setVisibility(View.GONE);
                avaibility_filter_layout.setVisibility(View.GONE);
                rating_filter_layout.setVisibility(View.GONE);


            }
        });



        discount_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discount_filter.setBackgroundColor(Color.WHITE);
                price_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));
                avaliability_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));
                rating_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));


                discount_filter_layout.setVisibility(View.VISIBLE);
                price_filter_layout.setVisibility(View.GONE);
                avaibility_filter_layout.setVisibility(View.GONE);
                rating_filter_layout.setVisibility(View.GONE);


            }
        });


        avaliability_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avaliability_filter.setBackgroundColor(Color.WHITE);
                discount_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));
                price_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));
                rating_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));


                avaibility_filter_layout.setVisibility(View.VISIBLE);
                discount_filter_layout.setVisibility(View.GONE);
                price_filter_layout.setVisibility(View.GONE);
                rating_filter_layout.setVisibility(View.GONE);


            }
        });

        rating_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_filter.setBackgroundColor(Color.WHITE);
                discount_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));
                avaliability_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));
                price_filter.setBackgroundColor(getResources().getColor(R.color.lightest_grey));


                rating_filter_layout.setVisibility(View.VISIBLE);
                discount_filter_layout.setVisibility(View.GONE);
                price_filter_layout.setVisibility(View.GONE);
                avaibility_filter_layout.setVisibility(View.GONE);
            }
        });

        apply_filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                offset = 0;
                isFiltered=true;
                shouldLoad = true;
                categoryWiseItems.clear();
                price_filters_list.clear();
                discount_filters_list.clear();
                isStock_filters_list.clear();
                rating_filters_list.clear();

                if(below_250.isChecked())
                {
                    Pair p = new Pair(0F,250F);
                    price_filters_list.add(p);
                }
                if(between_251_500.isChecked())
                {
                    Pair p = new Pair(251F,500F);
                    price_filters_list.add(p);

                }
                if(between_501_1000.isChecked())
                {
                    Pair p = new Pair(501F,1000F);
                    price_filters_list.add(p);

                }
                if(between_1001_2000.isChecked())
                {
                    Pair p = new Pair(1001F,2000F);
                    price_filters_list.add(p);

                }

                if(between_2001_5000.isChecked())
                {
                    Pair p = new Pair(2001F,5000F);
                    price_filters_list.add(p);
                }

                if(between_5001_10000.isChecked())
                {
                    Pair p = new Pair(5001F,10000F);
                    price_filters_list.add(p);

                }
                if(above_10001.isChecked())
                {
                    Pair p = new Pair(10001F, Float.MAX_VALUE);
                    price_filters_list.add(p);

                }



                //discount

                if(more_then_70.isChecked())
                {
                    discount_filters_list.add(70F);

                }
                if(more_then_60.isChecked())
                {
                    discount_filters_list.add(60F);

                }
                if(more_then_50.isChecked())
                {
                    discount_filters_list.add(50F);

                }
                if(more_then_40.isChecked())
                {
                    discount_filters_list.add(40F);

                }
                if(more_then_30.isChecked())
                {
                    discount_filters_list.add(30F);

                }

                if(below_30.isChecked())
                {
                    discount_filters_list.add(29F);

                }

                    //rating filters


                if(above_1.isChecked())
                {
                    rating_filters_list.add(1F);

                }

                if(above_2.isChecked())
                {
                    rating_filters_list.add(2F);

                }

                if(above_3.isChecked())
                {
                    rating_filters_list.add(3F);

                }

                if(above_4.isChecked())
                {
                    rating_filters_list.add(4F);

                }

                    //instock filters
                if(exclude_out_of_stock.isChecked())
                {
                    isStock_filters_list.add(false);

                }

                applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);
                bottomSheetDialogFilter.dismiss();
               // categoryWiseItems.clear();

            }
        });

        clear_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                offset = 0;
             //   isFiltered=false;
                bottomSheetDialogFilter.dismiss();
                categoryWiseItems.clear();
                price_filters_list.clear();
                discount_filters_list.clear();
                rating_filters_list.clear();
                isStock_filters_list.clear();
                applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);
                //  applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);
                allCheckBoxFalse();
              //  fetchDataOnScrolling();

            }
        });



        bottomSheetDialogFilter.setContentView(filterSheetView);
        bottomSheetDialogFilter.show();

    }

    public  void doMySearch(String Query,String column,String sortColumnName,String order,int limit,int offset)
    {
        categoryWiseViewModel.getProductsFromDatabse("%"+Query+"%",column,sortColumnName,order,limit,offset).observe(this, new Observer<List<CategoryWiseItems>>() {
            @Override
            public void onChanged(List<CategoryWiseItems> categoryWiseItems1) {

                fetchDataFromRoom(categoryWiseItems1,limit,offset);
            }
        });
    }


    public  void applyFilters(String Query,List<Pair<Float,Float>> price_filters,List<Float> discount_filters,List<Float> rating_filters,List<Boolean> isStockFilters,String column,String sortColumnName,String order,int limit,int offset)
    {
        categoryWiseViewModel.applyFilters("%"+Query+"%",price_filters,discount_filters,rating_filters,isStockFilters,column,sortColumnName,order,limit,offset).observe(this, new Observer<List<CategoryWiseItems>>() {
            @Override
            public void onChanged(List<CategoryWiseItems> categoryWiseItems1) {

                fetchDataFromRoom(categoryWiseItems1,limit,offset);
            }
        });
    }



    public void allCheckBoxFalse()
    {
        filterSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.filter_bottom_sheet,
                findViewById(R.id.bottom_filter_sheet_main));

    }

    public void fetchDataFromRoom(List<CategoryWiseItems> Items,int limit,int offset)
    {

        Log.d(String.valueOf(searchresult.this),"Size of category  = "+String.valueOf(Items.size()));
        if(Items.size()<limit)
        {
            shouldLoad=false;

        }
        int index = offset;
        for(int i=0;i<Items.size();i++)
        {
            if(categoryWiseItems.size()>=index)
            {
                categoryWiseItems.add(Items.get(i));
            }
            else
            {
                categoryWiseItems.set(index,Items.get(i));
            }
            index++;
        }
        if(Items.size()==0 && categoryWiseItems.size()==0)
        {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

    }
    /*public void callFunctions()
    {
        if(isFiltered)
        {
            applyFilters(query,price_filters_list,discount_filters_list,rating_filters_list,isStock_filters_list,column,sortColumnName,order,limit,offset);

        }
        else
        {
            doMySearch(query,column,sortColumnName,order,limit,offset);

        }
    }
*/

}