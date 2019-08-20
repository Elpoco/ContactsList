package com.tsolution.excontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<contacts> contacts = new ArrayList<>();
    ArrayList<String> groupTitles = new ArrayList<>();
    HashMap<String, String> groups = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

    }

    public void first(View view) {
        Cursor groupCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.STARRED},
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        );
        while (groupCursor.moveToNext()) {
            try {
                contacts.add(new contacts(groupCursor.getString(0), groupCursor.getString(1), groupCursor.getString(2)));
            } catch (Exception e) {
            }
        }
        groupCursor.close();
    }

    public void two(View view) {
        Cursor groupCursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID},
                ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
                null,
                null
        );
        while (groupCursor.moveToNext()) {
            try {
                setting(groupCursor.getString(0), groupCursor.getString(1));
            } catch (Exception e) {
            }
        }
        groupCursor.close();
    }

    public void three(View view) {
        for (contacts tmp : contacts) {
            tmp.groupId = Integer.parseInt(groups.get(tmp.name));
        }
    }

    public void four(View view) {
        Cursor groupCursor = getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI,
                new String[]{ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE},
                null,
                null,
                ContactsContract.Groups._ID
        );
        while (groupCursor.moveToNext()) {
            try {
                groupTitles.add(groupCursor.getString(1));
            } catch (Exception e) {
            }
        }
        matching();
        groupCursor.close();
    }

    public void matching() {
        for (contacts tmp : contacts) {
            if (tmp.groupId != 0) {
                tmp.groupTitle = groupTitles.get(tmp.groupId - 1) + "";
            } else {
                tmp.groupTitle = "기본그룹";
            }
        }
    }

    public void ok(View view) {
        for (contacts tmp : contacts) {
            Log.e("test", "name: " + tmp.name + ", phone: " + tmp.phone + ", groupID: " + tmp.groupId + ", groupTitle: " + tmp.groupTitle + ", star: " + tmp.starred);
        }
    }

    public void setting(String name, String num) {
        if (groups.containsKey(name)) return;
        groups.put(name, num);
    }
}
