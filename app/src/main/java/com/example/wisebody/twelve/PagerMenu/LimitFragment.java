package com.example.wisebody.twelve.PagerMenu;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.wisebody.twelve.player.LimitPlay;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;
import com.example.wisebody.twelve.widget.AddsaveLatestTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by gh on 2016-07-15.
 */
public class LimitFragment extends Fragment {


    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;
    static VerticalViewPager verticalViewPager;
    String myJSON;
    int position;
    JSONArray posts=null;
    String[]  nickname;
    String[]  entertainment;
    String[]  text;
    String[]  video;
    String[]  image;
    String[]  gold;
    String[]  authorization;
    String[] fan;
    static ArrayList<postData> mList;
    static User loginUser;
    SwipeRefreshLayout swipeRefreshLayout;
    View limitView;
    ProgressBar mainLoad;
    static LimitAdapter limitAdapter;
    static Boolean shownpage;
    Boolean loadData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        limitView = inflater.inflate(R.layout.fragment_limit, container, false);
        verticalViewPager = (VerticalViewPager)limitView.findViewById(R.id.verticalviewpager);
        loginUser = (User) (getArguments().getSerializable("loginUser"));
        shownpage = false;
        loadData = false;
        Button record  = (Button) limitView.findViewById(R.id.rec);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Record.class);
                intent.putExtra("loginUser", loginUser);
                startActivity(intent);
            }
        });
        mainLoad = (ProgressBar) limitView.findViewById(R.id.mainload);
        swipeRefreshLayout = (SwipeRefreshLayout) limitView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData getData = new GetData();
                getData.execute();
            }
        });
        mList = new ArrayList<>();
        limitAdapter = new LimitAdapter(getChildFragmentManager(),mList);
        GetData task = new GetData();
        task.execute();
        Log.d("생성","리미트");
        return limitView;
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
                    nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                    nextFrag.unselectedFrag();
                    nextFrag = null;
                }
            } else if (position == mList.size() - 1) {
                if (mList.size() > 1) {
                    nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                    nextFrag.unselectedFrag();
                    nextFrag = null;
                }
            } else {
                if (mList.size() > 2) {
                    prevFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                    prevFrag.unselectedFrag();
                    nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                    nextFrag.unselectedFrag();
                }
            }
            ((PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem())).selectedFrag();
        }
    }

    protected void showList(){
        try {
            nickname = null;
            entertainment = null;
            text = null;
            video = null;
            image = null;
            gold = null;
            fan = null;
            authorization = null;
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
                authorization = new String[posts.length()];
                fan = new String[posts.length()];
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject c = posts.getJSONObject(i);
                    nickname[i] = c.getString("nickname");
                    entertainment[i] = c.getString("entertainment");
                    text[i] = c.getString("text");
                    video[i] = c.getString("video");
                    image[i] = c.getString("id");
                    gold[i] = c.getString("gold");
                    authorization[i] = c.getString("authorization");
                    fan[i] = c.getString("fan");
                    mList.add(new postData(nickname[i], entertainment[i], text[i], video[i], image[i], gold[i], authorization[i], fan[i]));
                }
            }
        }
        catch (JSONException e)
        {
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
            verticalViewPager.setAdapter(limitAdapter);
            loadData = true;
            MainActivity.loadcount++;
            mainLoad.setVisibility(View.GONE);
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
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(Void... params) {

            DB db = new DB("limitList.php");
            String result = db.postList(loginUser.id);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            myJSON = result;
            showList();
            Log.d("제이슨리미트",myJSON);
            return result;
        }
    }
    public class LimitAdapter extends FragmentStatePagerAdapter {
        ArrayList<postData> mList;

        public LimitAdapter(FragmentManager fm, ArrayList<postData> mList) {
            super(fm);
            this.mList = mList;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(mList.get(position),position,loginUser.id);
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
        ImageView profile;
        TextView nickname;
        TextView entertainment;
        Button play;
        Button yes;
        TextView text;
        TextView goldoryes;
        LinearLayout limitcontainer;
        View rootView;
        Boolean prepare;
        Boolean cur;
        postData postData;
        TextView cover;
        TextView title;
        TextView countdown;
        Thread count;
        String countText;
        Boolean thread;
        String id;
        ProgressBar loading;
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
            args.putSerializable("postData",postData);
            args.putString("id",id);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("크리에이드","create");
            postData = (com.example.wisebody.twelve.video_list_demo.Adapter.items.postData)getArguments().getSerializable("postData");
            prepare = false;
            cur = false;
            thread  = false;
            id  = getArguments().getString("id");
            rootView = inflater.inflate(R.layout.limitcontent, container, false);
            video = (EMVideoView) rootView.findViewById(R.id.preview);
            loading = (ProgressBar) rootView.findViewById(R.id.loading);
            play = (Button) rootView.findViewById(R.id.playbtn);
            play.setVisibility(View.INVISIBLE);
            text = (TextView) rootView.findViewById(R.id.text);
            cover = (TextView) rootView.findViewById(R.id.cover);
            title = (TextView) rootView.findViewById(R.id.title);
            limitcontainer = (LinearLayout) rootView.findViewById(R.id.limitcontainer);
            yes = (Button) rootView.findViewById(R.id.yesbtn);
            nickname = (TextView) rootView.findViewById(R.id.nickname);
            entertainment = (TextView) rootView.findViewById(R.id.entertainment);
            bookmark = (Button) rootView.findViewById(R.id.bookmark);
            addStar = (Button) rootView.findViewById(R.id.addStar);
            if(postData.fan.equals("fan"))
                addStar.setBackgroundResource(R.drawable.addedstar);
            profile = (ImageView)rootView.findViewById(R.id.profileImage);
            goldoryes = (TextView) rootView.findViewById(R.id.goldoryes);
            countdown = (TextView) rootView.findViewById(R.id.countdown);
            double num = Double.parseDouble(postData.getGold());
            DecimalFormat df = new DecimalFormat("#,##0");
            if(Integer.parseInt(postData.getAuthrization())==1) {
                yes.setVisibility(View.INVISIBLE);
                bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSaveList.addToDatabase(id,postData.getVideo());
                    }
                });
                Picasso.with(getContext().getApplicationContext()).load("http://girim2.ga/twelve/myimg/" + postData.getImage() + ".jpg").fit().into(profile);
                nickname.setText(postData.getNickname());
                entertainment.setText(postData.getEntertainment());
                cover.setBackgroundColor(Color.parseColor("#9c6d64de"));
                text.setText(postData.getText());
                goldoryes.setText("Gold "+df.format(num).toString());
                title.setText("스타 영상의 첫 번째 자막의 주인공이 되어주세요!");
            }
            else {
                limitcontainer.setVisibility(View.INVISIBLE);
                goldoryes.setText("YES "+df.format(num).toString());
                cover.setBackgroundColor(Color.parseColor("#9ccc64de"));
                text.setText("이 사람은 스타\n"+postData.getNickname()+"이 맞나요?");
                title.setText("스타의 인증 영상입니다. 영상 속 인물이 우리가 아는 스타가 맞다면\n" +
                        "아래 버튼을 눌러 여러분의 스타가 Golden twelve에서 활동 할 수 있게 도와주세요!");
            }
            video.setScaleType(ScaleType.CENTER_CROP);
            video.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/"+postData.getVideo()));
            video.requestFocus();
            video.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    prepare = true;
                    loading.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                    video.setScaleType(ScaleType.CENTER_CROP);
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
                    video.setVideoURI(Uri.parse("http://girim2.ga/twelve/uploads/"+postData.getVideo()));
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
            play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addLatestList.addToDatabase(id,postData.getVideo());
                                Intent intent = new Intent(getActivity(), LimitPlay.class);
                                intent.putExtra("URL", postData.getVideo());
                                intent.putExtra("authorization",postData.getAuthrization());
                                intent.putExtra("id",loginUser.id);
                                startActivity(intent);
                        }
                    });
            addStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  checkAddstar();
                    followStar = new FollowStar(loginUser.id, postData.getImage());
                }
            });

            return rootView;
        }
        public void checkAddstar()
        {
            PlaceholderFragment nextFrag;
            if(postData.fan.equals("fan")) {
                addStar.setBackgroundResource(R.drawable.btn_add_star);
                postData.fan = "notfan";
                for(int i = 0 ; i<mList.size(); i ++) {
                    if (mList.get(i).getNickname().equals(postData.getNickname())) {
                        mList.get(i).fan = "notfan";
                        if(verticalViewPager.getCurrentItem()==0) {
                            if(i==1) {
                                nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.btn_add_star);
                                nextFrag = null;
                            }
                        }
                        else if (verticalViewPager.getCurrentItem()+1==mList.size())
                        {
                            if(i==verticalViewPager.getCurrentItem()-1) {
                                nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.btn_add_star);
                                nextFrag = null;
                            }
                        }
                        else
                        {
                            if(i==verticalViewPager.getCurrentItem()-1)
                            {
                                nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.btn_add_star);
                                nextFrag = null;
                            }
                            else if(i==verticalViewPager.getCurrentItem()+1)
                            {
                                nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.btn_add_star);
                                nextFrag = null;
                            }
                        }
                        Log.d("mList","edit"+mList.get(i).fan);
                        Log.d("notfan",Integer.toString(i));

                    }
                }
                limitAdapter.notifyDataSetChanged();
            }
            else
            {
                addStar.setBackgroundResource(R.drawable.addedstar);
                postData.fan = "fan";
                for(int i = 0 ; i<mList.size(); i ++)
                {
                    if(mList.get(i).getNickname().equals(postData.getNickname())) {
                        mList.get(i).fan= "fan";
                        if(verticalViewPager.getCurrentItem()==0) {
                            if(i==1) {
                                nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.addedstar);
                                nextFrag = null;
                            }
                        }
                        else if (verticalViewPager.getCurrentItem()+1==mList.size())
                        {
                            if(i==verticalViewPager.getCurrentItem()-1) {
                                nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.addedstar);
                                nextFrag = null;
                            }
                        }
                        else
                        {
                            if(i==verticalViewPager.getCurrentItem()-1)
                            {
                                nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() - 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.addedstar);
                                nextFrag = null;
                            }
                            else if(i==verticalViewPager.getCurrentItem()+1)
                            {
                                nextFrag = (PlaceholderFragment) limitAdapter.instantiateItem(verticalViewPager, verticalViewPager.getCurrentItem() + 1);
                                nextFrag.addStar.setBackgroundResource(R.drawable.addedstar);
                                nextFrag = null;
                            }
                        }
                    }
                }
                limitAdapter.notifyDataSetChanged();
            }
        }
        public void startTimerThread()
        {
            count = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(thread)
                    {
                        try {
                            Log.d("돈다", Boolean.toString(thread));
                            CountDown(postData.getVideo());
                            Thread.sleep(900);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread = true;
            count.start();
        }

        @Override
        public void onResume()
        {
            super.onResume();
        }
        @Override
        public  void onDestroyView()
        {
            super.onDestroyView();
            thread = false;
            if(count!=null)
            count = null;
                Log.d("파괴", Boolean.toString(Thread.currentThread().isInterrupted()));

        }
        public void selectedFrag()
        {
            cur = true;
            if(shownpage) {
                startTimerThread();
                loading.setVisibility(View.VISIBLE);
            }
            else
            {
                if(count!=null)
                {
                    thread = false;
                    count = null;
                }
            }
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
            thread = false;
            if(count!=null)
                count = null;
            cur = false;
            if(video !=null)
                video.pause();
        }
        private void CountDown(String video) {

            class CountDown extends AsyncTask<String, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    countText = s;
                    countdown.setText(s);
                    Log.d("표기",s);
                }

                @Override
                protected String doInBackground(String... params) {

                    String video = (String)params[0];
                    DB db = new DB("sendTime.php");
                    String result = db.countDown(video);
                    Log.d("sub", result);
                    return result;
                }
            }
            CountDown task = new CountDown();
            task.execute(video);
        }
    }

}