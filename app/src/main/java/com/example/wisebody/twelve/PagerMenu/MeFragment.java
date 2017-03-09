package com.example.wisebody.twelve.PagerMenu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wisebody.twelve.AsyncTask.LoadCheck;
import com.example.wisebody.twelve.DB;
import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.ShowsaveLatestTask;
import com.example.wisebody.twelve.User;
import com.example.wisebody.twelve.player.VideoPlayer;
import com.example.wisebody.twelve.video_list_demo.Adapter.LatestSaveAdapter;
import com.example.wisebody.twelve.video_list_demo.Adapter.MyVideoAdapter;
import com.example.wisebody.twelve.video_list_demo.Adapter.MystarAdapter;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by gh on 2016-07-15.
 */
public class MeFragment extends Fragment {
    User loginUser;
    RecyclerView myStarView;
    RecyclerView latestView;
    RecyclerView saveView;
    RecyclerView myvideoView;
    TextView numMystar;
    TextView numMyvideo;
    TextView numLatest;
    TextView numSave;
    MystarAdapter mystarAdapter;
    MyVideoAdapter myVideoAdapter;
    LatestSaveAdapter latestAdapter;
    LatestSaveAdapter saveAdapter;
    ImageView profile;
    String mystarJSON;
    String myvideoJSON;

    ShowsaveLatestTask showsaveList;
    ShowsaveLatestTask showLatesList;

    LinearLayoutManager myStarManager;
    LinearLayoutManager latestManager;
    LinearLayoutManager saveManager;
    LinearLayoutManager myvideoManager;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar mainLoad;
    View view;
    Toolbar toolbar;
    Boolean loading;
    Boolean firstLoad;
    Thread loadThread;
    LoadCheck[] loadCheck;
    static Boolean shownpage;
    Boolean loadData;

    private  ArrayList<postData> myStarList = new ArrayList<>();
    private  ArrayList<postData> latestList = new ArrayList<>();
    private  ArrayList<postData> saveList = new ArrayList<>();
    private  ArrayList<postData> myvideoList = new ArrayList<>();
    public static ArrayList<VideoPlayer> playingPlayer = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_me, container, false);
        loginUser = (User) (getArguments().getSerializable("loginUser"));
        toolbar = (Toolbar) view.findViewById(R.id.me_toolbar);
        setHasOptionsMenu(true);
        myStarView = (RecyclerView) view.findViewById(R.id.mystarView);
        latestView = (RecyclerView) view.findViewById(R.id.latesetView);
        saveView = (RecyclerView) view.findViewById(R.id.saveView);
        myvideoView = (RecyclerView) view.findViewById(R.id.myvideoView);
        numMystar = (TextView) view.findViewById(R.id.numMystar);
        numMyvideo = (TextView) view.findViewById(R.id.numMyvideo);
        numLatest = (TextView) view.findViewById(R.id.numLatest);
        numSave = (TextView) view.findViewById(R.id.numSave);
        profile = (ImageView) view.findViewById(R.id.profileImage);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.setEnabled(false);
        myVideoAdapter = new MyVideoAdapter(myvideoList,R.layout.myvideo_me);
        mainLoad = (ProgressBar) view.findViewById(R.id.mainload);
        loadCheck = new LoadCheck[4];
        for(int i =0; i<4;i++)
        loadCheck[i] = new LoadCheck(false);
        shownpage = false;
        loadData = false;

        Picasso.with(getContext()).load("http://girim2.ga/twelve/myimg/"+loginUser.id+".jpg").fit().into(profile);

        myStarManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        latestManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        saveManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myvideoManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myStarView.setLayoutManager(myStarManager);
        latestView.setLayoutManager(latestManager);
        saveView.setLayoutManager(saveManager);
        myvideoView.setLayoutManager(myvideoManager);
        mystarAdapter = new MystarAdapter(myStarList,R.layout.mystar_me,((MainActivity)getActivity()).getViewPager());
        latestAdapter = new LatestSaveAdapter(latestList,R.layout.latestvideo_me);
        saveAdapter = new LatestSaveAdapter(saveList, R.layout.latestvideo_me);
        showsaveList = new ShowsaveLatestTask("bookmarkList.php", saveAdapter, numSave,"Save ・", saveList,loadCheck[2]);
        showLatesList = new ShowsaveLatestTask("latestList.php", latestAdapter, numLatest, "Latest ・", latestList,loadCheck[3]);

        myStarView.setAdapter(mystarAdapter);
        myStarView.setHasFixedSize(true);
        latestView.setAdapter(latestAdapter);
        latestView.setHasFixedSize(true);
        saveView.setAdapter(saveAdapter);
        saveView.setHasFixedSize(true);
        myvideoView.setAdapter(myVideoAdapter);
        myvideoView.setHasFixedSize(true);
        firstLoad = true;
        makeLoadThread();

        final myStarList myStartask = new myStarList();
        myStartask.execute();

        showLatesList.searchToDatabase(loginUser.id);

        showsaveList.searchToDatabase(loginUser.id);

        final myVideoList myVideotask = new myVideoList();
        myVideotask.execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainLoad.setVisibility(View.VISIBLE);
                loadData = false;
                for(int i = 0; i<4;i++)
                loadCheck[i].load = false;
                makeLoadThread();
                new myStarList().execute();
                new myVideoList().execute();
                new ShowsaveLatestTask("bookmarkList.php", saveAdapter, numSave,"Save ・", saveList,loadCheck[2]).searchToDatabase(loginUser.id);
                new ShowsaveLatestTask("latestList.php", latestAdapter, numLatest, "Latest ・", latestList,loadCheck[3]).searchToDatabase(loginUser.id);
                    }
        });

        Log.d("생성","리미트");
        return view;
    }
    protected void makeLoadThread()
    {
        Log.d("loadThread","생성");
        loading = true;
        loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (loading) {
                    for (int i = 0; i < 4; i++) {
                        if (loadCheck[i].load) {
                            if (i == 3) {
                                if(firstLoad) {
                                    MainActivity.loadcount++;
                                    firstLoad = false;
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainLoad.setVisibility(View.GONE);
                                        swipeRefreshLayout.setEnabled(true);
                                        Log.d("loadThread","로딩성공");
                                        if(swipeRefreshLayout.isRefreshing())
                                            swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                                loading = false;
                                loadData = true;
                                if (loadThread != null)
                                    loadThread = null;
                                break;
                            }
                        } else {
                            i = -1;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Log.d("loadThread","로딩");
                }
            }
        });
        loadThread.start();


    }
   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

    }
    public void restFragment()
    {
        if(!shownpage) {
            if(playingPlayer!=null) {
                Log.d("Size", Integer.toString(playingPlayer.size()));
                for (int i = 0; i < playingPlayer.size(); i++) {
                    if (playingPlayer.get(i).isPlaying())
                        playingPlayer.get(i).stopPlay();
                }
                    playingPlayer.clear();
                    playingPlayer = null;
            }
     }
        else {
            if (playingPlayer == null)
                playingPlayer = new ArrayList<>();
        }
    }
    protected void showMystar(){
        try {
            String[] id = null;
            String[] nickname = null;
            if (myStarList != null)
                myStarList.clear();
            JSONObject jsonObj = new JSONObject(mystarJSON);
            JSONArray posts = jsonObj.getJSONArray("result");
            if(posts.length()>0) {
                id = new String[posts.length()];
                nickname = new String[posts.length()];
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject c = posts.getJSONObject(i);
                    nickname[i] = c.getString("starnickname");
                    id[i] = c.getString("star");
                    Log.d("add", id[i]);
                    myStarList.add(new postData(id[i], nickname[i]));
                }
            }
            myStarList.add(new postData("add", "add"));
            numMystar.setText("My Stars ・"+Integer.toString(myStarList.size()-1));
            mystarAdapter.notifyDataSetChanged();
            loadCheck[0].load = true;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    protected void showMyvideo(){
        try {
            String[] video = null;
            String[] text = null;
            String[] gold = null;
            String[] active = null;
            if (myvideoList != null)
                myvideoList.clear();
            JSONObject jsonObj = new JSONObject(myvideoJSON);
            JSONArray posts = jsonObj.getJSONArray("result");
            if(posts.length()>0) {
                video = new String[posts.length()];
                text = new String[posts.length()];
                gold = new String[posts.length()];
                active = new String[posts.length()];
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject c = posts.getJSONObject(i);
                    video[i] = c.getString("video");
                    text[i] = c.getString("text");
                    gold[i] = c.getString("gold");
                    active[i] = c.getString("active");
                    myvideoList.add(new postData(video[i], text[i], gold[i], active[i]));
                    Log.d("내비디오", "http://girim2.ga/twelve/uploads/" + video[i]);
                }
               /* for(int i =0;i<8;i++)
                    myvideoList.add(new postData(video[0], text[0], gold[0], active[0]));*/
            }
            numMyvideo.setText("My video ・"+myvideoList.size());
            myVideoAdapter.notifyDataSetChanged();
            loadCheck[1].load = true;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    class myStarList extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params) {
            DB db = new DB("mystarList.php");
            mystarJSON  = db.dupli(loginUser.id);
            Log.d("스타리스트",mystarJSON);
            return mystarJSON;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showMystar();
            mainLoad.setVisibility(View.GONE);
        }
    }
    class myVideoList extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... params) {
            DB db = new DB("myvideoList.php");
            myvideoJSON  = db.dupli(loginUser.id);
            Log.d("마이비디오",myvideoJSON);
            return myvideoJSON;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showMyvideo();
        }
    }

}