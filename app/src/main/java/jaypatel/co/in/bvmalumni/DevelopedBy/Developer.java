package jaypatel.co.in.bvmalumni.DevelopedBy;

/**
 * Created by Abhishek on 07-Dec-17.
 */
public class Developer {


    private String name, mobile, mail, github;
    private int photo;


    public Developer() {
    }

    public Developer(int photo, String name, String mobile, String mail, String github) {
        this.photo = photo;
        this.name = name;
        this.mobile = mobile;
        this.mail = mail;
        this.github = github;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

}
