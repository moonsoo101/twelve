package com.example.wisebody.twelve.PagerMenu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.wisebody.twelve.DB;
import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.Record;
import com.example.wisebody.twelve.User;
import com.example.wisebody.twelve.video_list_demo.Adapter.StardomAdapter;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gh on 2016-07-15.
 */
public class StardomFragment extends Fragment  implements SearchView.OnQueryTextListener {
    User loginUser;
    RecyclerView starListview;
    private LinearLayoutManager mLayoutManager;
    String myJSON;
    JSONArray posts;
    StardomAdapter stardomAdapter;
    String[] nickname;
    String[] entertainment;
    String[] image;
    String[] fan;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    ProgressBar mainLoad;
    public Toolbar toolbar;
    private  ArrayList<postData> mList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stardom, container, false);
        loginUser = (User) (getArguments().getSerializable("loginUser"));
        toolbar = (Toolbar) view.findViewById(R.id.stardom_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        starListview = (RecyclerView) view.findViewById(R.id.starlist);
        mLayoutManager = new LinearLayoutManager(getActivity());
        starListview.setLayoutManager(mLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mainLoad = (ProgressBar) view.findViewById(R.id.mainload);
        stardomAdapter = new StardomAdapter(mList, loginUser.id, R.layout.recyclerstardom);
        starListview.setAdapter(stardomAdapter);
        starListview.setHasFixedSize(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStarList task = new getStarList();
                task.execute();
            }
        });
        getStarList task = new getStarList();
        task.execute();
        Log.d("생성","스타덤");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        stardomAdapter.setFilter(mList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        final List<postData> filteredModelList = filter(mList, newText);
        stardomAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<postData> filter(List<postData> posts, String query) {
        query = query.toLowerCase();

        final List<postData> filteredpostlList = new ArrayList<>();
        for (postData post : posts) {
            final String text = post.getNickname().toLowerCase();
            if (text.contains(query)) {
                filteredpostlList.add(post);
            }
        }
        return filteredpostlList;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.record) {
            Intent intent = new Intent(getActivity(),Record.class);
            intent.putExtra("loginUser",loginUser);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void showList(){
        try {
            nickname = null;
            entertainment = null;
            image = null;
            fan  = null;
            JSONObject jsonObj = new JSONObject(myJSON);
            posts = jsonObj.getJSONArray("result");
            nickname = new String[posts.length()];
            entertainment = new String[posts.length()];
            image = new String[posts.length()];
            fan = new String[posts.length()];
            if(mList!=null)
                mList.clear();
            for (int i = 0; i < posts.length(); i++) {
                JSONObject c = posts.getJSONObject(i);
                nickname[i] = c.getString("nickname");
                entertainment[i] = c.getString("entertainment");
                image[i] = c.getString("id");
                fan[i] = c.getString("fan");
                mList.add(new postData(nickname[i],entertainment[i],null,null,image[i],null,null,fan[i]));
            }
            stardomAdapter.notifyDataSetChanged();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

        class getStarList extends AsyncTask<Void,Void,String>
        {

            @Override
            protected String doInBackground(Void... params) {
                DB db = new DB("starList.php");
                myJSON  = db.postList(loginUser.id);
                Log.d("스타리스트",myJSON);
                return myJSON;
            }
            @Override
            protected void onPostExecute(String result) {
                showList();
                MainActivity.loadcount++;
                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                mainLoad.setVisibility(View.GONE);
                super.onPostExecute(result);
            }
        }
}
