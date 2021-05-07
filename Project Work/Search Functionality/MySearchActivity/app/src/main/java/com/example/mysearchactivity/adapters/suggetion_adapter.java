package com.example.mysearchactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mysearchactivity.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class suggetion_adapter extends RecyclerView.Adapter<suggetion_adapter.suggestion_viewholder> {

    private final LayoutInflater mInflater;

    private List<String> suggestions; // Cached copy of words

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public suggetion_adapter(Context context) {
        mInflater = LayoutInflater.from(context); }

    @Override
    public suggestion_viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.suggestion_activity, parent, false);
        return new suggestion_viewholder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(suggestion_viewholder holder, int position) {
        if (suggestions != null) {
            String suggestion = suggestions.get(position);
            holder.suggestions_text.setText(suggestion);
        } else {
            // Covers the case of data not being ready yet.
            holder.suggestions_text.setText("No Word");
        }
    }

    public  void setSuggestions(List<String> suggestions){
        this.suggestions= suggestions;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (suggestions != null)
            return suggestions.size();
        else return 0;
    }



    class suggestion_viewholder extends RecyclerView.ViewHolder {

        private final TextView suggestions_text;

        private suggestion_viewholder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            suggestions_text = itemView.findViewById(R.id.suggestions_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}