package com.example.wisebody.twelve.video_list_demo.Adapter.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by wisebody on 2016. 7. 26..
 */
public class postData implements Serializable, Parcelable {
    public String  nickname;
    public String  entertainment;
    public String  text;
    public String  video;
    public String  image;
    public String  gold;
    public String  authrization;
    public String fan;
    public  postData(String nickname, String entertainment, String text, String video, String image, String gold, String authrization, String fan)
    {
        this.nickname = nickname;
        this.entertainment = entertainment;
        this.text = text;
        this.video = video;
        this.image = image;
        this.gold = gold;
        this.authrization = authrization;
        this.fan = fan;
    }
    public  postData(String nickname, String entertainment, String text, String video, String image, String gold, String authrization)
    {
        this.nickname = nickname;
        this.entertainment = entertainment;
        this.text = text;
        this.video = video;
        this.image = image;
        this.gold = gold;
        this.authrization = authrization;
    }

    public  postData(String nickname, String entertainment, String text, String video, String image, String gold)
    {
        this.nickname = nickname;
        this.entertainment = entertainment;
        this.text = text;
        this.video = video;
        this.image = image;
        this.gold = gold;
    }
    public  postData(String nickname, String entertainment, String text, String video, String image)
    {
        this.nickname = nickname;
        this.entertainment = entertainment;
        this.text = text;
        this.video = video;
        this.image = image;
    }
    public postData(String nickname, String entertainment, String image)
    {
        this.nickname = nickname;
        this.entertainment = entertainment;
        this.image = image;
    }
    public postData(String image, String nickname)
    {
        this.image = image;
        this.nickname = nickname;
    }
    public postData(String video, String text, String gold, String authrization)
    {
        this.video = video;
        this.text = text;
        this.gold = gold;
        this.authrization = authrization;
    }

    protected postData(Parcel in) {
        nickname = in.readString();
        entertainment = in.readString();
        text = in.readString();
        video = in.readString();
        image = in.readString();
        gold = in.readString();
        authrization = in.readString();
        fan = in.readString();
    }

    public static final Creator<postData> CREATOR = new Creator<postData>() {
        @Override
        public postData createFromParcel(Parcel in) {
            return new postData(in);
        }

        @Override
        public postData[] newArray(int size) {
            return new postData[size];
        }
    };

    public String getEntertainment() {
        return entertainment;
    }

    public String getImage() {
        return image;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }

    public String getVideo() {
        return video;
    }
    public String getGold() { return gold;}
    public String getAuthrization() { return authrization;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeString(entertainment);
        dest.writeString(text);
        dest.writeString(video);
        dest.writeString(image);
        dest.writeString(gold);
        dest.writeString(authrization);
        dest.writeString(fan);
    }
}
