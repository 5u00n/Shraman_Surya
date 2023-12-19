package in.devedits.shramansurya.model;

public class Profiles {

    String Uid, name, img_url, is_married, birthday, anniversary,gender,email,UType;

    public Profiles(String uid, String name, String img_url, String is_married, String birthday, String anniversary, String gender, String email, String UType) {
        Uid = uid;
        this.name = name;
        this.img_url = img_url;
        this.is_married = is_married;
        this.birthday = birthday;
        this.anniversary = anniversary;
        this.gender = gender;
        this.email = email;
        this.UType = UType;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIs_married() {
        return is_married;
    }

    public void setIs_married(String is_married) {
        this.is_married = is_married;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUType() {
        return UType;
    }

    public void setUType(String UType) {
        this.UType = UType;
    }
}
