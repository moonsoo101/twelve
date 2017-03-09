package com.example.wisebody.twelve.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.wisebody.twelve.DB;

import java.util.concurrent.ExecutionException;

/**
 * Created by wisebody on 2016. 8. 5..
 */
public class AddsaveLatestTask {

    Context context;
    String php;

    public AddsaveLatestTask(Context context, String php)
    {
        this.context = context;
        this.php = php;
    }


    public void addToDatabase(String id, String video){
        class searchData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(context.getApplicationContext(), "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
               // Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_LONG).show();

            }
            @Override
            protected String doInBackground(String... params) {

                String id = (String) params[0];
                String video = (String) params[1];
                DB db = new DB(php);
                String result = db.addLatestBookmar(id,video);
                Log.d(result,"result");
                return result;
            }
        }
        //searchData task = new searchData(Application.this);
        searchData task = new searchData();
        task.execute(id, video);
        try {

            String temp = task.get();

        } catch (InterruptedException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (ExecutionException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();
        }
    }
}
