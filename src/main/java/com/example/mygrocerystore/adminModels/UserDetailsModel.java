package com.example.mygrocerystore.adminModels;

public class UserDetailsModel {
    String Name;
    String Email;
    String MobileNumber;
    String Address;
    String ProfileImg;
    String DocumentId;

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public UserDetailsModel() {
    }

    public UserDetailsModel(String name, String email, String mobileNumber, String address, String profileImg) {
        Name = name;
        Email = email;
        MobileNumber = mobileNumber;
        Address = address;
        ProfileImg = profileImg;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getProfileImg() {
        return ProfileImg;
    }

    public void setProfileImg(String profileImg) {
        ProfileImg = profileImg;
    }
}
