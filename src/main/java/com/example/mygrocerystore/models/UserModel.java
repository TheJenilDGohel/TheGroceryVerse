package com.example.mygrocerystore.models;

public class UserModel {
    String Name;
    String Email;
    String Password;
    String Address;
    String MobilrNumber;
    String ProfileImg;
    String DocumentId;

    public UserModel() {
    }

    public UserModel(String name, String email, String password, String address, String mobilrNumber, String profileImg) {
        Name = name;
        Email = email;
        Password = password;
        Address = address;
        MobilrNumber = mobilrNumber;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobilrNumber() {
        return MobilrNumber;
    }

    public void setMobilrNumber(String mobilrNumber) {
        MobilrNumber = mobilrNumber;
    }

    public String getProfileImg() {
        return ProfileImg;
    }

    public void setProfileImg(String profileImg) {
        ProfileImg = profileImg;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}
