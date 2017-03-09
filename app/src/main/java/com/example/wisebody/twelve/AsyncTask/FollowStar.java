package com.example.wisebody.twelve.AsyncTask;

import android.os.AsyncTask;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.TextView;

import com.example.wisebody.twelve.DB;
import com.example.wisebody.twelve.video_list_demo.Adapter.LatestSaveAdapter;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wisebody on 2016. 9. 9..
 */
public class FollowStar {

        String fan;
        String star;
        String php;

        public FollowStar(String fan, String star)
        {
            this.fan = fan;
            this.star = star;
            php = "pressaddstar.php";
        }

        public void searchToDatabase(){
            class searchData extends AsyncTask<String, Void, String> {
                @Override
                protected void onPreExecute() {

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                }
                @Override
                protected String doInBackground(String... params) {

                    String fan = (String) params[0];
                    String star = (String) params[0];
                    DB db = new DB(php);
                    String result = db.followstar(fan, star);
                    Log.d(result,"result");
                    Log.d("Json",result);
                    return result;
                }
            }
            searchData task = new searchData();
            task.execute(fan, star);
        }
    }

