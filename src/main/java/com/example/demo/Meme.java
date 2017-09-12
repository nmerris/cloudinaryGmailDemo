package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Meme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String imageUrl;

    private String artisticFilter;

    private String borderType;

    private String cropType;

    private long width;

    private long height;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtisticFilter() {
        return artisticFilter;
    }

    public void setArtisticFilter(String artisticFilter) {
        this.artisticFilter = artisticFilter;
    }

    public String getBorderType() {
        return borderType;
    }

    public void setBorderType(String borderType) {
        this.borderType = borderType;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
