package com.tsolution.excontact;

public class MyContacts {
    private String name;
    private String phone;
    private int groupId;
    private String groupTitle;
    private String starred;

    public MyContacts(String name, String phone, String starred) {
        this.name = name;
        this.phone = phone;
        this.starred = starred;
        groupId = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }
}
