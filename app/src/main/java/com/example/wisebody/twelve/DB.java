package com.example.wisebody.twelve;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;



public class DB {
    String link;
    public DB(String php)
    {
        link = "http://girim2.ga/twelve/"+php;
    }
    protected String insert(String id, String pass)
    {
        try {
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    protected  String starInsert(String id, String pass, String name, String nickname, String entertainment, String filename)
    {
        try {
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
            data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("nickname", "UTF-8") + "=" + URLEncoder.encode(nickname, "UTF-8");
            data += "&" + URLEncoder.encode("entertainment", "UTF-8") + "=" + URLEncoder.encode(entertainment, "UTF-8");
            data += "&" + URLEncoder.encode("filename", "UTF-8") + "=" + URLEncoder.encode(filename, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    protected String search(String id, String pass)
    {
        try {
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    public String dupli(String id)
    {
        try {
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    public String postList(String id)
    {
        try {
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    protected String insertPost(String id, String video, String text)
    {
        try {
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("video", "UTF-8") + "=" + URLEncoder.encode(video, "UTF-8");
            data += "&" + URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    protected String smi(String filename)
    {
        String link = "http://girim2.ga/twelve/uploads/read.php";
        try {

            String data = URLEncoder.encode("filename", "UTF-8") + "=" + URLEncoder.encode(filename+".txt", "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                if(sb.toString().trim().equals("자막이 없다"))
                    break;
                sb.append('\n');
                Log.d("sub",line);
                //break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    protected String writeSub(String text, String filename)
    {
        try {
            String tfilename = "uploads/"+filename+".txt";
            Log.d("파일이름2",tfilename);
            String data = URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8");
            data += "&" + URLEncoder.encode("filename", "UTF-8") + "=" + URLEncoder.encode(tfilename, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    protected String mergeVideo(String text, String filename, String mergefilename)
    {
        try {
            String tfilename = "uploads/"+filename+".txt";
            Log.d("파일이름2",tfilename);
            String data = URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8");
            data += "&" + URLEncoder.encode("filename", "UTF-8") + "=" + URLEncoder.encode(tfilename, "UTF-8");
            data += "&" + URLEncoder.encode("mergefilename", "UTF-8") + "=" + URLEncoder.encode(mergefilename, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    public String countDown(String video)
    {
        try {
            String data = URLEncoder.encode("video", "UTF-8") + "=" + URLEncoder.encode(video, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    public String addLatestBookmar(String id, String video)
    {
        try {
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("video", "UTF-8") + "=" + URLEncoder.encode(video, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    public String followstar(String fan, String star)
    {
        try {
            String data = URLEncoder.encode("fan", "UTF-8") + "=" + URLEncoder.encode(fan, "UTF-8");
            data += "&" + URLEncoder.encode("star", "UTF-8") + "=" + URLEncoder.encode(star, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
}






