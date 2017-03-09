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

import com.example.wisebody.twelve.PagerMenu.MeFragment;
import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.Util.BitmapDownloaderTask;
import com.example.wisebody.twelve.Util.CreateThumbnail;
import com.example.wisebody.twelve.player.VideoPlayer;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by wisebody on 2016. 8. 2..
 */
public class LatestSaveAdapter extends RecyclerView.Adapter<LatestSaveAdapter.ViewHolder> {

    private List<postData> items;
    private int itemLayout;
    Context context;
    public ViewHolder viewHolder;


    public LatestSaveAdapter(List<postData> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;

    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(itemLayout, parent, false);
        context = parent.getContext();
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
        final postData item = items.get(position);

        Picasso.with(context).load("http://girim2.ga/twelve/myimg/" + item.getImage() + ".jpg").fit().into(holder.profile);
        holder.nickname.setText(item.getNickname());
        holder.entertainment.setText(item.getEntertainment());
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
                Log.d("save","click");
                holder.videoPlayer.play = true;
                holder.videoPlayer.loadVideo("http://girim2.ga/twelve/uploads/"+item.getVideo());
                holder.play.setVisibility(View.INVISIBLE);
                holder.videoPlayer.setVisibility(View.VISIBLE);
                MeFragment.playingPlayer.add(holder.videoPlayer);
            }
        });
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.bitmapDownloaderTask = new BitmapDownloaderTask(holder.thumb,holder.progressBar,context);
        holder.bitmapDownloaderTask.download("http://girim2.ga/twelve/uploads/"+item.getVideo()+".jpg",holder.thumb);
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
        TextView nickname;
        TextView entertainment;
        TextView text;
        TextView gold;
        Button play;
        ImageView thumb;
        public FrameLayout videoContainer;
        public View parent;
        ProgressBar progressBar;
        public VideoPlayer videoPlayer;
        BitmapDownloaderTask bitmapDownloaderTask;

        public ViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            profile = (CircleImageView) itemView.findViewById(R.id.profileImage);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            entertainment = (TextView) itemView.findViewById(R.id.entertainment);
            play = (Button) itemView.findViewById(R.id.playbtn);
            text = (TextView) itemView.findViewById(R.id.text);
            gold = (TextView) itemView.findViewById(R.id.gold);
            videoContainer = (FrameLayout) itemView.findViewById(R.id.videocontainer);
            videoPlayer = new VideoPlayer(parent.getContext(),play, videoContainer);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            videoContainer.addView(videoPlayer);
        }
    }

}