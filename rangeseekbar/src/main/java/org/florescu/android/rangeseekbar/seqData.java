package org.florescu.android.rangeseekbar;

/**
 * Created by wisebody on 2016. 8. 5..
 */
public class seqData {
    int min;
    int max;
    float startX;
    float endX;
    String text;
    public  seqData(int min, int max, String text, float startX, float endX){
        this.min = min;
        this.max = max;
        this.text = text;
        this.startX = startX;
        this.endX = endX;
    }

    public String getText() {
        return text;
    }

    public int getStarttime() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public float getStartX() {
        return startX;
    }

    public float getEndX() {
        return endX;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        seqData other = (seqData) obj;
       if (min!=other.min)
            return false;
       if (max!=other.max)
            return false;
        return true;
    }
}
