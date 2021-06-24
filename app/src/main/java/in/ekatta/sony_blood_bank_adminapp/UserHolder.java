package in.ekatta.sony_blood_bank_adminapp;

public class UserHolder {
    private static UserHolder userHolder = null;

    private UserHolder() {
    }

    public static UserHolder getInstance() {
        if (userHolder == null) {
            userHolder = new UserHolder();
        }
        return userHolder;
    }

    private Boolean OPositive, APositive, BPositive, ABPositive, ONegative, ANegative, BNegative, ABNegative;
    private String arealine, city, district, email, landmark, mobile, name, pass, pin;

    public Boolean getOPositive() {
        return OPositive;
    }

    public void setOPositive(Boolean OPositive) {
        this.OPositive = OPositive;
    }

    public Boolean getAPositive() {
        return APositive;
    }

    public void setAPositive(Boolean APositive) {
        this.APositive = APositive;
    }

    public Boolean getBPositive() {
        return BPositive;
    }

    public void setBPositive(Boolean BPositive) {
        this.BPositive = BPositive;
    }

    public Boolean getABPositive() {
        return ABPositive;
    }

    public void setABPositive(Boolean ABPositive) {
        this.ABPositive = ABPositive;
    }

    public Boolean getONegative() {
        return ONegative;
    }

    public void setONegative(Boolean ONegative) {
        this.ONegative = ONegative;
    }

    public Boolean getANegative() {
        return ANegative;
    }

    public void setANegative(Boolean ANegative) {
        this.ANegative = ANegative;
    }

    public Boolean getBNegative() {
        return BNegative;
    }

    public void setBNegative(Boolean BNegative) {
        this.BNegative = BNegative;
    }

    public Boolean getABNegative() {
        return ABNegative;
    }

    public void setABNegative(Boolean ABNegative) {
        this.ABNegative = ABNegative;
    }

    public String getArealine() {
        return arealine;
    }

    public void setArealine(String arealine) {
        this.arealine = arealine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}
