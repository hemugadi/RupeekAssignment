package com.amit.mvvmnews.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="favoritelist")
public class FavoriteList {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "place")
    private String place;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "rate")
    private int rate;

    @ColumnInfo(name = "description")
    private String description;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}

