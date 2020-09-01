package com.example.task7.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class StoryList implements Serializable {
    @SerializedName("articles")
    @Expose
    public List<Story> articles = null;

    public StoryList(List<Story> articles) {
        this.articles = articles;
    }

    public List<Story> getArticles() {
        return articles;
    }

    public void setArticles(List<Story> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoryList storyList = (StoryList) o;
        return Objects.equals(articles, storyList.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articles);
    }

    @Override
    public String toString() {
        return "StoryList{" +
                "articles=" + articles +
                '}';
    }
}
