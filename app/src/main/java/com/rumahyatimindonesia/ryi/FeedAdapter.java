package com.rumahyatimindonesia.ryi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wahyudhzt on 26/04/2016.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private Context context;
    private List<FeedList> feedList;

    public FeedAdapter(Context context, List<FeedList> feedList) {
        this.context = context;
        this.feedList = feedList;
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.Feed_Date.setText(feedList.get(position).getDate());
        holder.Feed_Title.setText(feedList.get(position).getTitle());
//        holder.Feed_Image.setImageResource(feedList.get(position).getImage());
        Picasso.with(context).load(feedList.get(position).getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.Feed_Image);

    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.feed_list, viewGroup, false);

        return new FeedViewHolder(itemView);
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        protected TextView Feed_Title;
        protected ImageView Feed_Image;
        protected TextView Feed_Date;

        public FeedViewHolder(View v) {
            super(v);
            Feed_Title =  (TextView) v.findViewById(R.id.feed_title);
            Feed_Image = (ImageView) v.findViewById(R.id.feed_image);
            Feed_Date =  (TextView) v.findViewById(R.id.feed_date);
        }
    }
}
