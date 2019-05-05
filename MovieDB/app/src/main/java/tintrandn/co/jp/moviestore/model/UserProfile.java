package tintrandn.co.jp.moviestore.model;


public class UserProfile {
    //private variables
    private int _id;
    private String user_image;
    private String user_name;
    private String user_mail;
    private String user_birthday;
    private String user_gender;

    public UserProfile(int _id, String user_image, String user_name, String user_mail, String user_birthday, String user_gender) {
        this._id = _id;
        this.user_image = user_image;
        this.user_name = user_name;
        this.user_mail = user_mail;
        this.user_birthday = user_birthday;
        this.user_gender = user_gender;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public String getUser_gender() {
        return user_gender;
    }

}
