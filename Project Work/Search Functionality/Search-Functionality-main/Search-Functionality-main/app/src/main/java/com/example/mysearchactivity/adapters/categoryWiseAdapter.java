package com.example.mysearchactivity.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mysearchactivity.R;
import com.example.mysearchactivity.model.CategoryWiseItems;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class categoryWiseAdapter extends RecyclerView.Adapter<categoryWiseAdapter.categoryViewHolder> {

    private final LayoutInflater mInflater;

    private List<CategoryWiseItems> mWords;
    Context context;// Cached copy of words

    public categoryWiseAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public categoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.result_item, parent, false);
        return new categoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(categoryViewHolder holder, int position) {
        if (mWords != null) {
            CategoryWiseItems current = mWords.get(position);

            String productname = current.getItem_name();
            String ratingcount = current.getRatingCount();
            String totalexpense ="\u20B9 " +current.getPrice();
            Float ratings = current.getRating();
            String discount_expense = "\u20B9 "+String.valueOf(current.getPrice_with_discount());
        //    Log.d(String.valueOf(context),discount_expense);
            String url = current.getImage_url();

            holder.product_name.setText(productname);
            holder.rating_count.setText(ratingcount);
            holder.ratingbar.setRating(ratings);
            holder.total_expense.setText(totalexpense);
            holder.total_expense.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.discount_expense.setText(discount_expense);

        //    Log.d(String.valueOf(context),url);
            Glide.with(context).load(url)
                    .placeholder(R.drawable.ic_launcher_background).into(holder.imageView);



        } else {
            // Covers the case of data not being ready yet.
            holder.product_name.setText("No Word");
        }
    }

    public  void setCategory(List<CategoryWiseItems> words){
        mWords = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }



    class categoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView product_name,total_expense,discount_expense,rating_count;
        private final RatingBar ratingbar;
        private final ImageView imageView;

        private categoryViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            total_expense = itemView.findViewById(R.id.expense1);
            discount_expense = itemView.findViewById(R.id.expense2);
            ratingbar = itemView.findViewById(R.id.ratingBar);
            rating_count = itemView.findViewById(R.id.rating_count);
            imageView = itemView.findViewById(R.id.product_image);


        }
    }
}