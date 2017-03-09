package com.example.wisebody.twelve.PagerMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.OnSeekCompletionListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.example.wisebody.twelve.AsyncTask.FollowStar;
import com.example.wisebody.twelve.DB;
import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.Record;
import com.example.wisebody.twelve.User;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;
import com.example.wisebody.twelve.widget.AddsaveLatestTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by gh on 2016-07-15.
 */

public class BestFragment extends Fragment {
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;
    static VerticalViewPager verticalViewPager;
    String myJSON;
    JSONArray posts = null;
    String[] nickname;
    String[] entertainment;
    String[] text;
    String[] video;
    String[] image;
    String[] gold;
    String[] fan;
    int position;
    static Boolean shownpage;
    Boolean loadData;
    static ArrayList<postData> mList;
    static User loginUser;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar mainLoad;
    static BestAdapter bestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best, container, false);
        Log.d("크리에이트", "크리에이트2");
        verticalViewPager = (VerticalViewPager) view.findViewById(R.id.verticalviewpager);
        mList = new ArrayList<>();
        Button record = (Button) view.findViewById(R.id.recordbtn);
        loginUser = (User) (getArguments().getSerializable("loginUser"));
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Record.class);
                intent.putExtra("loginUser", loginUser);
                startActivity(intent);
            }
        });
        mainLoad = (ProgressBar) view.findViewById(R.id.mainload);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        bestAdapter = new BestAdapter(getChildFragmentManager(), mList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData task = new GetData();
                task.execute();
            }
        });
        shownpage = false;
        loadData = false;
        GetData task = new GetData();
        task.execute();
        Log.d("생성","베스트");

        return view;
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }
    protected void restFragment()
    {
        PlaceholderFragment nextFrag;
        PlaceholderFragment prevFrag;
        if(mList.size()!=0) {
            if (position == 0) {
                if (mList.size() > 1) {
                    nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                    nextFrag.unselectedFrag();
                    nextFrag = null;
                }
            } else if (position == mList.size() - 1) {
                if (mList.size() > 1) {
                    nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                    nextFrag.unselectedFrag();
                    nextFrag = null;
                }
            } else {
                if (mList.size() > 2) {
                    prevFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                    prevFrag.unselectedFrag();
                    nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                    nextFrag.unselectedFrag();
                }
            }
            ((PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem())).selectedFrag();
        }
    }

    protected void showList() {
        try {
            nickname = null;
            entertainment = null;
            text = null;
            video = null;
            image = null;
            gold = null;
            fan = null;
            if(mList!=null)
                mList.clear();
            JSONObject jsonObj = new JSONObject(myJSON);
            posts = jsonObj.getJSONArray("result");
            if(posts.length()>0) {
                nickname = new String[posts.length()];
                entertainment = new String[posts.length()];
                text = new String[posts.length()];
                video = new String[posts.length()];
                image = new String[posts.length()];
                gold = new String[posts.length()];
                fan = new String[posts.length()];
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject c = posts.getJSONObject(i);
                    nickname[i] = c.getString("nickname");
                    entertainment[i] = c.getString("entertainment");
                    text[i] = c.getString("text");
                    video[i] = c.getString("video");
                    image[i] = c.getString("id");
                    gold[i] = c.getString("gold");
                    fan[i] = c.getString("fan");
                    mList.add(new postData(nickname[i], entertainment[i], text[i], video[i], image[i], gold[i], fan[i]));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class GetData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadData = false;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            verticalViewPager.setAdapter(bestAdapter);
            mainLoad.setVisibility(View.GONE);
            loadData = true;
            MainActivity.loadcount++;
            position = verticalViewPager.getCurrentItem();
            if(position==0)
                swipeRefreshLayout.setEnabled(true);
            else
                swipeRefreshLayout.setEnabled(false);
            if(swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
            restFragment();
            verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int pos) {
                    position = pos;
                    if (position == 0) {
                        swipeRefreshLayout.setEnabled(true);
                    }
                    else{
                        swipeRefreshLayout.setEnabled(false);
                    }
                    restFragment();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {

            DB db = new DB("postList.php");
            String result = db.postList(loginUser.id);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            myJSON = result;
            showList();

            return result;
        }
    }

    public class BestAdapter extends FragmentStatePagerAdapter {
        ArrayList<postData> mList;

        public BestAdapter(FragmentManager fm, ArrayList<postData> mList) {
            super(fm);
            this.mList = mList;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(mList.get(position), position,loginUser.id);
        }


        @Override
        public int getCount() {
            return mList.size();
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        EMVideoView video;
        Button bookmark;
        Button addStar;
        CircleImageView profile;
        TextView nickname;
        TextView entertainment;
        TextView rank;
        Button play;
        TextView text;
        TextView gold;
        ProgressBar loading;

        private static final String ARG_SECTION_NUMBER = "section_number";
        View rootView;
        Boolean prepare;
        Boolean cur;
        postData postData;
        int numRank;
        String id;
        AddsaveLatestTask addSaveList = new AddsaveLatestTask(getContext(),"pressbookmark.php");
        AddsaveLatestTask addLatestList = new AddsaveLatestTask(getContext(),"pressplay.php");
        FollowStar followStar;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(postData postData, int position, String id) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("rank", position);
            args.putSerializable("postData", postData);
            args.putString("id", id);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("크리에이드", "create");
            numRank = getArguments().getInt("rank");
            id = getArguments().getString("id");
            postData = (com.example.wisebody.twelve.video_list_demo.Adapter.items.postData) getArguments().getSerializable("postData");
            prepare = false;
            cur = false;
            rootView = inflater.inflate(R.layout.bestcontent, container, false);
            video = (EMVideoView) rootView.findViewById(R.id.video);
            bookmark = (Button) rootView.findViewById(R.id.bookmark);
            addStar = (Button) rootView.findViewById(R.id.addStar);
            loading = (ProgressBar) rootView.findViewById(R.id.loading);
            if(postData.getAuthrization().equals("fan"))
                addStar.setBackgroundResource(R.drawable.addedstar);
            profile = (CircleImageView) rootView.findViewById(R.id.profileImage);
            nickname = (TextView) rootView.findViewById(R.id.nickname);
            entertainment = (TextView) rootView.findViewById(R.id.entertainment);
            rank = (TextView) rootView.findViewById(R.id.rank);
            play = (Button) rootView.findViewById(R.id.play);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addLatestList.addToDatabase(id,postData.getVideo());
                }
            });
            text = (TextView) rootView.findViewById(R.id.text);
            gold = (TextView) rootView.findViewById(R.id.gold);
            Picasso.with(getContext().getApplicationContext()).load("http://girim2.ga/twelve/myimg/" + postData.getImage() + ".jpg").fit().into(profile);
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSaveList.addToDatabase(id,postData.getVideo());
                }
            });
            rank.setText(Integer.toString(numRank + 1));
            double num = Double.parseDouble(postData.getGold());
            DecimalFormat df = new DecimalFormat("#,##0");
            gold.setText("Gold " + df.format(num).toString());
            nickname.setText(postData.getNickname());
            entertainment.setText(postData.getEntertainment());
            text.setText(postData.getText());
            video.setScaleType(ScaleType.CENTER_CROP);
            video.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/" + postData.getVideo()));
            video.requestFocus();
            video.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    prepare = true;
                    loading.setVisibility(View.GONE);
                    play.setVisibility(View.INVISIBLE);
                    video.setVolume(0);
                    if(cur&&shownpage)
                    video.start();
                }
            });
            video.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion() {
                    prepare = false;
                    loading.setVisibility(View.VISIBLE);
                    video.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/" + postData.getVideo()));
                    video.requestFocus();
                }
            });
            video.setOnSeekCompletionListener(new OnSeekCompletionListener() {
                @Override
                public void onSeekComplete() {
                    loading.setVisibility(View.GONE);
                    video.start();
                }
            });
            addStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              checkAddstar();
                    followStar = new FollowStar(loginUser.id, postData.getImage());
                    followStar.searchToDatabase();
                }
            });
            return rootView;
        }
        public void checkAddstar()
        {
            PlaceholderFragment nextFrag;
            if(postData.getAuthrization().equals("fan")) {
                addStar.setBackgroundResource(R.drawable.btn_add_pp_star);
                postData.authrization = "notfan";
                for(int i = 0 ; i<mList.size(); i ++) {
                    if (mList.get(i).getNickname().equals(postData.getNickname())) {
                        mList.get(i).authrization = "notfan";
                        if(verticalViewPager.getCurrentItem()==0) {
                            if(i==1) {
                                nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.btn_add_pp_star);
                                nextFrag = null;
                            }
                        }
                        else if (verticalViewPager.getCurrentItem()+1==mList.size())
                        {
                            if(i==verticalViewPager.getCurrentItem()-1) {
                                nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.btn_add_pp_star);
                                nextFrag = null;
                            }
                        }
                        else
                        {
                            if(i==verticalViewPager.getCurrentItem()-1)
                            {
                                nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.btn_add_pp_star);
                                nextFrag = null;
                            }
                            else if(i==verticalViewPager.getCurrentItem()+1)
                            {
                                nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.btn_add_pp_star);
                                nextFrag = null;
                            }
                        }
                        Log.d("mList","edit"+mList.get(i).authrization);
                        Log.d("notfan",Integer.toString(i));

                    }
                }
                bestAdapter.notifyDataSetChanged();
            }
            else
            {
                addStar.setBackgroundResource(R.drawable.addedstar);
                postData.authrization = "fan";
                for(int i = 0 ; i<mList.size(); i ++)
                {
                    if(mList.get(i).getNickname().equals(postData.getNickname())) {
                        mList.get(i).authrization = "fan";
                        if(verticalViewPager.getCurrentItem()==0) {
                            if(i==1) {
                                nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.addedstar);
                                nextFrag = null;
                            }
                        }
                        else if (verticalViewPager.getCurrentItem()+1==mList.size())
                        {
                            if(i==verticalViewPager.getCurrentItem()-1) {
                                nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.addedstar);
                                nextFrag = null;
                            }
                        }
                        else
                        {
                            if(i==verticalViewPager.getCurrentItem()-1)
                            {
                                nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.addedstar);
                                nextFrag = null;
                            }
                            else if(i==verticalViewPager.getCurrentItem()+1)
                            {
                                nextFrag = (PlaceholderFragment) bestAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.addedstar);
                                nextFrag = null;
                            }
                        }
                    }
                }
                bestAdapter.notifyDataSetChanged();
            }
        }
        public void selectedFrag()
        {
            cur = true;
            if (shownpage)
            loading.setVisibility(View.VISIBLE);
            if(prepare&&shownpage) {
                if(video.getCurrentPosition()!=0)
                    video.seekTo(0);
                else
                {
                    loading.setVisibility(View.GONE);
                    video.start();
                }
            }
            else if(!prepare)
            {
                video.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/"+postData.getVideo()));
                video.requestFocus();
            }
            else if(!shownpage)
            {
                if(video !=null&&video.isPlaying())
                    video.pause();
            }
        }
        public void unselectedFrag()
        {
            cur = false;
            if(video !=null)
                video.pause();
        }
    }
}



