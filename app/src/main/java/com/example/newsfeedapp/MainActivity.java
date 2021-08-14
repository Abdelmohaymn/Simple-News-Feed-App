package com.example.newsfeedapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String MY_URL = "https://content.guardianapis.com/search";

    TextView emptyTV;
    ProgressBar progressBar;
    ArticlesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyTV = findViewById(R.id.emptyTV);
        ListView listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        adapter = new ArticlesAdapter(this,new ArrayList<Article>());
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyTV);

        LoaderManager loaderManager = getSupportLoaderManager();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netwotkInfo = connectivityManager.getActiveNetworkInfo();

        if (netwotkInfo == null || !netwotkInfo.isConnected()){
            progressBar.setVisibility(View.GONE);
            emptyTV.setText("No internet connection");
        }else {
            loaderManager.initLoader(1,null,this);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = adapter.getItem(position);
                Uri uri = Uri.parse(article.getWebUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,uri);
                if (webIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(webIntent);
                }
            }
        });


    }

    @NonNull
    @Override
    public Loader<List<Article>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String search = sharedPreferences.getString("search","technology");
        String section = sharedPreferences.getString("section","technology");
        String orderBy = sharedPreferences.getString("orderBy","newest");
        String limit = sharedPreferences.getString("limit","10");
        Uri baseUri =  Uri.parse(MY_URL);
        Uri.Builder uri = baseUri.buildUpon();
        uri.appendQueryParameter("q",search);
        uri.appendQueryParameter("section",section);
        uri.appendQueryParameter("order-by",orderBy);
        uri.appendQueryParameter("page-size", limit);
        uri.appendQueryParameter("show-tags", "contributor");
        uri.appendQueryParameter("api-key","8ce61369-ce29-4d80-a4cc-7d31717f524c");
        return new ArticleLoader(this,uri.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Article>> loader, List<Article> articles) {
        progressBar.setVisibility(View.GONE);
        emptyTV.setText("No news found");
        if (articles != null && !articles.isEmpty()){
            adapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Article>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

         getMenuInflater().inflate(R.menu.main,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}