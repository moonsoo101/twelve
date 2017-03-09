package com.example.wisebody.twelve;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.wisebody.twelve.AsyncTask.LoadCheck;
import com.example.wisebody.twelve.video_list_demo.Adapter.LatestSaveAdapter;
import com.example.wisebody.twelve.video_list_demo.Adapter.items.postData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wisebody on 2016. 8. 5..
 */
public class ShowsaveLatestTask {

    String taskResult;
    String php;
    LatestSaveAdapter Adapter;
    TextView textView;
    String title;
    ArrayList<postData> list;
    LoadCheck load;

    public ShowsaveLatestTask(String php, LatestSaveAdapter Adapter, TextView textView, String title, ArrayList<postData> list, LoadCheck load)
    {
        this.php = php;
        this.Adapter = Adapter;
        this.textView = textView;
        this.title = title;
        this.list = list;
        this.load = load;
    }

    public void searchToDatabase(String id){
        class searchData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                    taskResult = s;
                    showLatestSavevideo(s,list);
            }
            @Override
            protected String doInBackground(String... params) {

                String id = (String) params[0];
                DB db = new DB(php);
                String result = db.dupli(id);
                Log.d(result,"result");
                Log.d("Json",result);
                return result;
            }
        }
        searchData task = new searchData();
        task.execute(id, php);
    }
    protected void showLatestSavevideo(String json, ArrayList<postData> list){
            try {
                String[] video = null;
                String[] nickname = null;
                String[] text = null;
                String[] gold = null;
                String[] image = null;
                String[] entertainment = null;
                JSONObject jsonObj = new JSONObject(json);
                JSONArray posts = jsonObj.getJSONArray("result");
                if(posts.length()>0) {
                    video = new String[posts.length()];
                    text = new String[posts.length()];
                    gold = new String[posts.length()];
                    nickname = new String[posts.length()];
                    image = new String[posts.length()];
                    entertainment = new String[posts.length()];
                    if (list != null) {
                        list.clear();
                    }
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject c = posts.getJSONObject(i);
                        video[i] = c.getString("video");
                        text[i] = c.getString("text");
                        gold[i] = c.getString("gold");
                        nickname[i] = c.getString("nickname");
                        image[i] = c.getString("id");
                        entertainment[i] = c.getString("entertainment");
                        list.add(new postData(nickname[i], entertainment[i], text[i],
                                video[i], image[i], gold[i]));
                        Log.d("최신", video[i]);
                    }
                }
                Adapter.notifyDataSetChanged();
                load.load = true;
                textView.setText(title + list.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        Log.d("어댑터",title+Integer.toString(list.size()));
    }
}
