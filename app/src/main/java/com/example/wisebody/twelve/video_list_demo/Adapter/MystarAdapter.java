package com.example.wisebody.twelve.video_list_demo.Adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wisebody on 2016. 8. 2..
 */
public class MystarAdapter extends RecyclerView.Adapter<MystarAdapter.ViewHolder> {

    private List<postData> items;
    private int itemLayout;
    ViewPager viewPager;
    Context context;


    public MystarAdapter(List<postData> items, int itemLayout, ViewPager viewPager) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.viewPager = viewPager;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final postData item = items.get(position);

        if (item.getImage().equals("add"))
            Picasso.with(context.getApplicationContext()).load(R.drawable.icn_me_add_copy).fit().into(holder.profile);
        else
            Picasso.with(context.getApplicationContext()).load("http://girim2.ga/twelve/myimg/" + item.getImage() + ".jpg").fit().into(holder.profile);
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==items.size()-1)
                viewPager.setCurrentItem(2, true);
            }
        });
        holder.nickname.setText(item.getNickname());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setFilter(List<postData> searchitems) {
        items = new ArrayList<>();
        items.addAll(searchitems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView nickname;

        public ViewHolder(View itemView) {
            super(itemView);
            profile = (CircleImageView) itemView.findViewById(R.id.mystarImage);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
        }
    }
}
