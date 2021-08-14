package com.example.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ArticlesAdapter extends ArrayAdapter<Article> {

    public ArticlesAdapter(Context context, List<Article> articles) {
        super(context,0,articles);
    }

    public View getView(int position, View listItemView, ViewGroup parent){
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.article_item,parent,false);
        }

        Article currentArticle = getItem(position);

        TextView title = listItemView.findViewById(R.id.article_title);
        TextView section = listItemView.findViewById(R.id.article_section);
        TextView date = listItemView.findViewById(R.id.article_date);
        TextView time = listItemView.findViewById(R.id.article_time);
        TextView author = listItemView.findViewById(R.id.article_author);
        ImageView imageArticle = listItemView.findViewById(R.id.article_image);

        title.setText(currentArticle.getTitle());
        section.setText(currentArticle.getSection());

        String finDate = formatDate(currentArticle.getDate());
        String finTime = formatTime(currentArticle.getDate());

        date.setText(finDate);
        time.setText(finTime);

        author.setText(currentArticle.getAuthor());
        Picasso.get().load(currentArticle.getImageUrl()).into(imageArticle);

        return listItemView;
    }

    private String formatDate(String dateObj) {
        String dateFormatted = "";
        SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputDate = new SimpleDateFormat("d MMM,yyyy", Locale.getDefault());
        try {
            Date newDate = inputDate.parse(dateObj);
            return outputDate.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatted;
    }

    private String formatTime(String dateObj) {
        String dateFormatted = "";
        SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputDate = new SimpleDateFormat("h:mm a", Locale.getDefault());
        try {
            Date newDate = inputDate.parse(dateObj);
            return outputDate.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatted;
    }


}
