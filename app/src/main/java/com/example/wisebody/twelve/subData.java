package com.example.wisebody.twelve;

/**
 * Created by wisebody on 2016. 7. 12..
 */
class subData {
    long time;
    String text;
    long endTime;
    subData(long time, String text, long endTime) {
        this.time = time;
        this.text = text;
        this.endTime = endTime;
    }

    public long gettime() {
        return time;
    }
    public long getEndTime() {return  endTime;}

    public String gettext() {
        return text;
    }
    public void settext(String text){this.text=text;}
}

