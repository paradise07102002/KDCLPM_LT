package edu.huflit.kdclpm_lt.Object;

public class User {
    private int id_user;
    private String username_user;
    private String password_user;
    private String role_user;
    private String fullname_user;
    private String email_user;
    private String phone_user;
    private byte[] avartar_user;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUsername_user() {
        return username_user;
    }

    public void setUsername_user(String username_user) {
        this.username_user = username_user;
    }

    public String getPassword_user() {
        return password_user;
    }

    public void setPassword_user(String password_user) {
        this.password_user = password_user;
    }

    public String getRole_user() {
        return role_user;
    }

    public void setRole_user(String role_user) {
        this.role_user = role_user;
    }

    public String getFullname_user() {
        return fullname_user;
    }

    public void setFullname_user(String fullname_user) {
        this.fullname_user = fullname_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getPhone_user() {
        return phone_user;
    }

    public void setPhone_user(String phone_user) {
        this.phone_user = phone_user;
    }

    public byte[] getAvartar_user() {
        return avartar_user;
    }

    public void setAvartar_user(byte[] avartar_user) {
        this.avartar_user = avartar_user;
    }
}
