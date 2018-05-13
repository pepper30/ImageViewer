package com.example.megha.imageviewer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Details> detailsList;
    private FirebaseFirestore db;
    private Context context;
    private ClickHandler clickHandler;

    public RecyclerViewAdapter(List<Details> detailsList, FirebaseFirestore db, Context context,ClickHandler clickHandler) {
        this.detailsList = detailsList;
        this.db = db;
        this.context = context;
        this.clickHandler = clickHandler;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new RecyclerViewAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.clickHandler = this.clickHandler;
        int itemPosition=position;
        Details details=detailsList.get(itemPosition);
        holder.title.setText(details.getTitle());
        holder.likes_count.setText(String.valueOf(details.getLikes()));
//        Glide.with(context).load(details.getImgUrl()).into(holder.imageView);
        Picasso.with(context).load(details.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.like_button)
        ImageButton like_button;
        @BindView(R.id.likes)
        TextView likes_count;

        private ClickHandler clickHandler;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            like_button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickHandler != null) {
                clickHandler.onMyButtonClicked(getAdapterPosition());
            }
        }
    }
}

 interface ClickHandler {
    void onMyButtonClicked(final int position);
}
