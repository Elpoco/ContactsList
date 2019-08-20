package com.tsolution.excontact;

public class contacts {
    String name;
    String phone;
    int groupId;
    String groupTitle;
    String starred;

    public contacts(String name, String phone, String starred) {
        this.name = name;
        this.phone = phone;
        this.starred = starred;
        groupId = 0;
    }
}
