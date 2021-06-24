package in.ekatta.sony_blood_bank_adminapp.DataClasses;

import in.ekatta.sony_blood_bank_adminapp.Activities.ManageBloodBank_A;


public class BloodBankData {


    String name;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String arealine;
    String landmark;
    String city;
    String district;

    public void setName(String name) {
        this.name = name;
    }

    public void setArealine(String arealine) {
        this.arealine = arealine;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    String mobile;
    String pin;

    public BloodBankData() {
    }

    public String getName() {
        return name;
    }

    public String getArealine() {
        return arealine;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPin() {
        return pin;
    }


}

