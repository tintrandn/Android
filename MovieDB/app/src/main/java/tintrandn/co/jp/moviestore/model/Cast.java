package tintrandn.co.jp.moviestore.model;

import com.google.gson.annotations.SerializedName;


public class Cast {
    @SerializedName("profile_path")
    private String image;
    @SerializedName("name")
    private String name;


    public Cast(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
