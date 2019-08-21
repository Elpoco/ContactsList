package com.tsolution.excontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<MyContacts> contacts = new ArrayList<>();
    ArrayList<String> groupTitles = new ArrayList<>();
    HashMap<String, String> groups = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
    }

    public void getContactsList() {
        contacts.clear();
        groupTitles.clear();
        groups.clear();
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
                contacts.add(new MyContacts(groupCursor.getString(0), groupCursor.getString(1), groupCursor.getString(2)));
            } catch (Exception e) {
                return;
            }
        }
        groupCursor.close();
        getGroupId();
    }

    public void getGroupId() {
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
                settingId(groupCursor.getString(0), groupCursor.getString(1));
            } catch (Exception e) {
                return;
            }
        }
        groupCursor.close();
        setGroupId();
        getGroupTitle();
    }

    public void settingId(String name, String num) {
        if (groups.containsKey(name)) return;
        groups.put(name, num);
    }

    public void setGroupId() {
        for (MyContacts tmp : contacts) {
            tmp.setGroupId(Integer.parseInt(groups.get(tmp.getName())));
        }
    }

    public void getGroupTitle() {
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
        groupCursor.close();
        matching();
    }

    public void matching() {
        for (MyContacts tmp : contacts) {
            if (tmp.getGroupId() != 0) {
                tmp.setGroupTitle(groupTitles.get(tmp.getGroupId() - 1) + "");
            } else {
                tmp.setGroupTitle("기본그룹");
            }
        }
        Log();
    }

    public void Log() {
        for (MyContacts tmp : contacts) {
            Log.v("MyContacts", String.format("Name: %s, Phone: %s, GroupID: %s, GroupTitle: %s, star: %s", tmp.getName(), tmp.getPhone(), tmp.getGroupId(), tmp.getGroupTitle(), tmp.getStarred()));
        }
    }

    public void getContacts(View view) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
            return;
        getContactsList();
        StringBuffer sb = new StringBuffer();
        for (MyContacts tmp : contacts) {
            sb.append("Name: " + tmp.getName() + ", Phone: " + tmp.getPhone() + ", GroupID: " + tmp.getGroupId() + ", GroupTitle: " + tmp.getGroupTitle() + ", star: " + tmp.getStarred() + "\n\n");
        }
        new AlertDialog.Builder(this).setMessage(sb.toString()).show();
    }

}
