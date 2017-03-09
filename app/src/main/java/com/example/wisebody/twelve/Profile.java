package com.example.wisebody.twelve;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wisebody on 2016. 6. 29..
 */
public class Profile {
    String id;
    String imgURL;
    Bitmap myImg;
    CircleImageView imgView;
    ImageView normalImg;
    Boolean noraml = false;
    Boolean circle = false;
    public Profile(String id, CircleImageView imgView)
    {
        this.id = id;
        imgURL = "http://girim2.ga/twelve/myimg/"+id+".jpg";
        this.imgView = imgView;
        circle = true;
    }
    public Profile(String id, ImageView normalImg)
    {
        this.id = id;
        imgURL = "http://girim2.ga/twelve/myimg/"+id+".jpg";
        this.normalImg = normalImg;
        noraml = true;
    }
    public void loadImg()
    {
        loadImg task = new loadImg();
        task.execute(imgURL);
    }
    private class loadImg extends AsyncTask<String, Integer,Bitmap>{


        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();

                myImg = BitmapFactory.decodeStream(is);


            }catch(IOException e){
                e.printStackTrace();
            }
            return myImg;
        }

        protected void onPostExecute(Bitmap img){
            if(circle)
                imgView.setImageBitmap(myImg);
            if(noraml)
                normalImg.setImageBitmap(myImg);
        }

    }
}

