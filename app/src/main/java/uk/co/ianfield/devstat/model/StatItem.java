package uk.co.ianfield.devstat.model;

/**
 * Created by Ian Field on 20/02/2014.
 */
public class StatItem {
    String title;
    String info;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String toString() {
        return String.format("%s: %s", getTitle(), getInfo());
    }
}
