package com.example.wisebody.twelve.video_list_demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wisebody.twelve.AsyncTask.FollowStar;
import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by danylo.volokh on 9/20/2015.
 */
public class StardomAdapter extends RecyclerView.Adapter<StardomAdapter.ViewHolder> {

    private List<postData> items;
    String id;
    private int itemLayout;
    Context context;
    FollowStar followStar;

    public StardomAdapter(List<postData> items, String id, int itemLayout) {
        this.items = items;
        this.id = id;
        this.itemLayout = itemLayout;

    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
        final postData item = items.get(position);
        Picasso.with(context.getApplicationContext()).load("http://girim2.ga/twelve/myimg/" + item.getImage() + ".jpg").fit().into(holder.profile);
        holder.starindex.setText(Integer.toString(position+1));
        holder.nickname.setText(item.getNickname());
        holder.entertainment.setText(item.getEntertainment());
        if(item.fan.equals("fan"))
            holder.addStar.setBackgroundResource(R.drawable.addedstar);
        holder.addStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.fan.equals("fan")) {
                    holder.addStar.setBackgroundResource(R.drawable.btn_add_pp_star);
                    item.fan = "notfan";
                }
                else {
                    holder.addStar.setBackgroundResource(R.drawable.addedstar);
                   item.fan = "fan";
                }
                followStar = new FollowStar(id,item.getImage());
                followStar.searchToDatabase();
            }
        });
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public void setFilter(List<postData> searchitems) {
        items = new ArrayList<>();
        items.addAll(searchitems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView starindex;
        TextView nickname;
        TextView entertainment;
        Button addStar;

        public ViewHolder(View itemView) {
            super(itemView);
            starindex = (TextView) itemView.findViewById(R.id.starindex);
            profile = (CircleImageView) itemView.findViewById(R.id.starimage);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            entertainment = (TextView) itemView.findViewById(R.id.entertainment);
            addStar = (Button) itemView.findViewById(R.id.addStar);
        }
    }
}