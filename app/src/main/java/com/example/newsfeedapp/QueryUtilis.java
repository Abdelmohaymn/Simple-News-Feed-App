package com.example.newsfeedapp;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtilis {

    private QueryUtilis(){

    }

    public static List<Article> fetchArticlesData(String stUrl){
        List<Article> articles ;
        URL url = createUrl(stUrl);
        String jsonRes = null;

        try {
            jsonRes = makeHTTPConnection(url);
        } catch (IOException e) {

        }
        articles = extractArticles(jsonRes);
        return articles;
    }


    private static URL createUrl(String st){

        URL url = null;
        try {
            url = new URL(st);
        }catch (MalformedURLException e){

        }
        return url;
    }

    private static String makeHTTPConnection(URL url) throws IOException {
        if (url==null){
            return null;
        }
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {

        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Article> extractArticles(String jsonRes) {
        if (TextUtils.isEmpty(jsonRes)){
            return null;
        }
        List<Article> articles = new ArrayList<>();

        try {
            JSONObject baseJson = new JSONObject(jsonRes);
            JSONObject response = baseJson.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i=0; i<results.length(); i++){
                JSONObject currentArticle = results.getJSONObject(i);

                String section = currentArticle.getString("sectionName");
                String date = currentArticle.getString("webPublicationDate");
                String title = currentArticle.getString("webTitle");
                String url = currentArticle.getString("webUrl");

                JSONArray tags = currentArticle.getJSONArray("tags");
                JSONObject tag = tags.getJSONObject(0);

                 String author = "By ";
                 author += tag.getString("webTitle");

                 String imageUrl = tag.getString("bylineImageUrl");

                Article article = new Article(section,date,title,url,author,imageUrl);
                articles.add(article);
            }

        } catch (JSONException e) {

        }

        return articles;
    }

}
