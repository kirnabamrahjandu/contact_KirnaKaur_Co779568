package com.portfolio.contactlist;

import android.support.annotation.NonNull;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "contact_list")
public class Contact
{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "contactId")
    private int id;

    @ColumnInfo(name = "contactFirstName")
    private String contactFirstName;

    @ColumnInfo(name = "contactLastName")
    private String contactLastName;

    @ColumnInfo(name = "contactPhone")
    private String contactPhone;

    @ColumnInfo(name = "contactEmail")
    private String contactEmail;

    @ColumnInfo(name = "contactAddress")
    private String contactAddress;

    //CONSTRUCTOR (Note: parameter names must match column names exactly)
    public Contact(String contactFirstName, String contactLastName, String contactPhone, String contactEmail, String contactAddress)
    {
        this.id = id;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.contactAddress = contactAddress;
    }

    //GETTERS
    public int getId() {return this.id; }
    public String getContactName () {return this.contactFirstName + this.contactLastName;}
    public String getContactFirstName() { return this.contactFirstName; }
    public String getContactLastName() { return this.contactLastName; }
    public String getContactPhone() { return this.contactPhone; }
    public String getContactEmail() { return this.contactEmail; }
    public String getContactAddress() { return this.contactAddress; }

    //SETTERS
    public void setId(int id) { this.id = id; }
    public void setContactFirstName(String name) { this.contactFirstName = name; } //Unused
    public void setContactLastName(String name) { this.contactLastName = name; } //Unused
    public void setContactPhone(String phone) { this.contactPhone = phone; } //Unused
    public void setContactEmail(String email) { this.contactEmail = email; } //Unused
    public void setContactAddress(String address) { this.contactAddress = address; } //Unused
}