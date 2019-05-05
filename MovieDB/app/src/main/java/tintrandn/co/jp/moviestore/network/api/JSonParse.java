package tintrandn.co.jp.moviestore.network.api;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import tintrandn.co.jp.moviestore.model.Cast;
import tintrandn.co.jp.moviestore.model.MovieBase;

public class JSonParse {

    private static final String API_KEY = "api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=";//{pageNumber}
//    private static final String API_BASE = "http://api.themoviedb.org/3/configuration?";
    private static final String API_MOVIE_LIST_POPULAR = "http://api.themoviedb.org/3/movie/popular?";
    private static final String API_MOVIE_LIST_TOP_RATE = "http://api.themoviedb.org/3/movie/top_rated?";
    private static final String API_MOVIE_LIST_UPCOMING = "http://api.themoviedb.org/3/movie/upcoming?";
    private static final String API_MOVIE_LIST_NOW_PLAYING = "http://api.themoviedb.org/3/now_playing?";
//    private static final String API_MOVIE_DESTAIL = "http://api.themoviedb.org/3/movie/";//{movieId}?api_key=e7631ffcb8e766993e5ec0c1f4245f93
    private static final String API_CAST = "http://api.themoviedb.org/3/movie/";//{movieId}/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93"


    private static final String TAG = "JsonParse";

    public JSonParse(){

    }
    //=====================================================================
    private static String readUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        BufferedInputStream bis = null;
        try {
             bis = new BufferedInputStream(url.openStream());
        }
       catch (FileNotFoundException e){
            e.printStackTrace();
       }
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int bytesRead;
        if (bis != null) {
            while ((bytesRead = bis.read(buffer)) > 0) {
                String text = new String(buffer, 0, bytesRead);
                sb.append(text);
            }
            bis.close();
        }
        return sb.toString();
    }
    //=====================================================================
    private static String readFile(String file_Name) throws Exception {
        File sdcard = Environment.getExternalStorageDirectory();
//Get the text file
        File file = new File(sdcard,file_Name);
//Read text from file
        StringBuilder json = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                json.append(line);
                json.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return json.toString();
    }
    //=====================================================================
    public static Page startParse_Popular(String page){
        String json = null;
        try {
            Log.d(TAG,"Read from api "+API_MOVIE_LIST_POPULAR+API_KEY+ page);
//            json = readUrl("http://api.themoviedb.org/3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page="+mPage);
            String url = API_MOVIE_LIST_POPULAR+API_KEY+ page;
            json = readUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json == null){
            try{
                Log.d(TAG,"Read local file");
                json = readFile("page_"+ page +".txt");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Log.d(TAG,json);
        Gson gson = new Gson();
        return gson.fromJson(json, Page.class);
    }
    //=====================================================================
    public static Page startParse_Top_Rate(String page){
        String json = null;
        try {
            Log.d(TAG,"Read from api "+API_MOVIE_LIST_TOP_RATE+API_KEY+ page);
            json = readUrl(API_MOVIE_LIST_TOP_RATE+API_KEY+ page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json == null){
            try{
                Log.d(TAG,"Read local file");
                json = readFile("page_"+ page +".txt");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
//        Log.d(TAG,json);
        Gson gson = new Gson();
        return gson.fromJson(json, Page.class);
    }
    //=====================================================================
    public static Page startParse_Upcoming(String page){
        String json = null;
        try {
            Log.d(TAG,"Read from api "+API_MOVIE_LIST_UPCOMING+API_KEY+ page);
            json = readUrl(API_MOVIE_LIST_UPCOMING+API_KEY+ page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json == null){
            try{
                Log.d(TAG,"Read local file");
                json = readFile("page_"+ page +".txt");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
//        Log.d(TAG,json);
        Gson gson = new Gson();
        return gson.fromJson(json, Page.class);
    }
    //=====================================================================
    public static Page startParse_Now_Playing(String page){
        String json = null;
        String mPage;
        mPage = page;
        try {
            Log.d(TAG,"Read from api "+API_MOVIE_LIST_NOW_PLAYING+API_KEY+mPage);
            json = readUrl(API_MOVIE_LIST_NOW_PLAYING+API_KEY+mPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json == null){
            try{
                Log.d(TAG,"Read local file");
                json = readFile("page_"+mPage+".txt");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
//        Log.d(TAG,json);
        Gson gson = new Gson();
        return gson.fromJson(json, Page.class);
    }

    //=====================================================================
    public static Cast_Id startParse_Cast(String movie_id){
        String json = null;
        try {
            Log.d(TAG,"Read from api Cast_Id");
//            json = readUrl(api.themoviedb.org/3/movie/{movieId}/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93);
            json = readUrl(API_CAST+movie_id+"/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93");
            Log.d(TAG,"Cast json:" +json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json == null){
            try{
                Log.d(TAG,"Read local file");
                json = readFile("page_cast.txt");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
//        Log.d(TAG,json);
        Gson gson = new Gson();
        return gson.fromJson(json, Cast_Id.class);
    }
    //=====================================================================
    public class Cast_Id {
        @SerializedName("id")
        private int id;
        @SerializedName("cast")
        private ArrayList<Cast> mCastArrayList;

        public ArrayList<Cast> getmCastArrayList() {
            return mCastArrayList;
        }

//        public void setmCastArrayList(ArrayList<Cast> mCastArrayList) {
//            this.mCastArrayList = mCastArrayList;
//        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
    //=====================================================================
    public class Page {
        @SerializedName("page")
        private int page_number;

//        public int getPage_number() {
//            return page_number;
//        }

//        public void setPage_number(int page_number) {
//            this.page_number = page_number;
//        }

        @SerializedName("results")
        private ArrayList<MovieBase> movieList;

        public ArrayList <MovieBase> get_movieList(){
            return movieList;
        }
    }
}
