package com.example.wisebody.twelve.video_list_demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wisebody.twelve.AsyncTask.LoadCheck;
import com.example.wisebody.twelve.PagerMenu.MeFragment;
import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.Util.BitmapDownloaderTask;
import com.example.wisebody.twelve.Util.CreateThumbnail;
import com.example.wisebody.twelve.player.VideoPlayer;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wisebody on 2016. 8. 2..
 */
public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {

    private List<postData> items;
    private int itemLayout;
    Context context;
    ViewHolder viewHolder;
    CreateThumbnail createThumbnail = new CreateThumbnail();



    public MyVideoAdapter(List<postData> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        context = parent.getContext();
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final postData item = items.get(position);
        holder.parent.setTag(holder);
        holder.text.setText(item.getText());
        double num = Double.parseDouble(item.getGold());
        DecimalFormat df = new DecimalFormat("#,##0");
        holder.gold.setText("Gold " + df.format(num).toString());
        holder.thumb.setVisibility(View.VISIBLE);
        holder.play.setVisibility(View.VISIBLE);
        holder.videoPlayer.setVisibility(View.GONE);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.videoPlayer.play = true;
                holder.videoPlayer.loadVideo("http://girim2.ga/twelve/uploads/"+item.getVideo());
                holder.play.setVisibility(View.INVISIBLE);
                holder.videoPlayer.setVisibility(View.VISIBLE);
                MeFragment.playingPlayer.add(holder.videoPlayer);
                Log.d("customplayer","addList");
            }
            });
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.bitmapDownloaderTask = new BitmapDownloaderTask(holder.thumb,holder.progressBar,context);
        holder.bitmapDownloaderTask.download("http://girim2.ga/twelve/uploads/"+item.getVideo()+".jpg",holder.thumb);
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        TextView gold;
        TextView countdown;
        ImageView thumb;
        public Button play;
        public FrameLayout videocontainer;
        ProgressBar progressBar;
        public View parent;
        VideoPlayer videoPlayer;
        BitmapDownloaderTask bitmapDownloaderTask;
        public ViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            text = (TextView) itemView.findViewById(R.id.text);
            gold = (TextView) itemView.findViewById(R.id.gold);
            countdown = (TextView) itemView.findViewById(R.id.countdown);
            play = (Button) itemView.findViewById(R.id.playbtn);
            videocontainer = (FrameLayout) itemView.findViewById(R.id.videocontainer);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            videoPlayer = new VideoPlayer(parent.getContext(),play, videocontainer);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            videocontainer.addView(videoPlayer);
        }
    }
}