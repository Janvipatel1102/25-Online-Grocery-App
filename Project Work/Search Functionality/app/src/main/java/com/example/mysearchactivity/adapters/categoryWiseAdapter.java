package com.example.mysearchactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mysearchactivity.R;
import com.example.mysearchactivity.model.CategoryWiseItems;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class categoryWiseAdapter extends RecyclerView.Adapter<categoryWiseAdapter.categoryViewHolder> {

    private final LayoutInflater mInflater;

    private List<CategoryWiseItems> mWords; // Cached copy of words

    public categoryWiseAdapter(Context context) {
        mInflater = LayoutInflater.from(context); }

    @Override
    public categoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new categoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(categoryViewHolder holder, int position) {
        if (mWords != null) {
            CategoryWiseItems current = mWords.get(position);
            holder.wordItemView.setText(current.getItem_name());
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Word");
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

        private final TextView wordItemView;

        private categoryViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }
    }
}