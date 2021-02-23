package com.portfolio.contactlist;

import java.util.List;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface ContactDao
{
    @Insert
    void insertContact(Contact product);

    @Query("SELECT * FROM contact_list WHERE contactFirstName LIKE ('%'||:first_name||'%') ORDER BY contactFirstName")
    List<Contact> findFirstName(String first_name);

    @Query("SELECT * FROM contact_list WHERE contactLastName LIKE ('%'||:last_name||'%') ORDER BY contactLastName")
    List<Contact> findLastName(String last_name);

    @Query("SELECT * FROM contact_list WHERE contactPhone LIKE ('%'||:phone||'%') ORDER BY contactPhone")
    List<Contact> findPhone(String phone);

    @Query("SELECT * FROM contact_list WHERE contactEmail LIKE ('%'||:email||'%') ORDER BY contactEmail")
    List<Contact> findEmail(String email);

    @Query("SELECT * FROM contact_list WHERE contactAddress LIKE ('%'||:address||'%') ORDER BY contactAddress")
    List<Contact> findAddress(String address);

    @Query("DELETE FROM contact_list WHERE contactFirstName = :name")
    void deleteContact(String name);

    @Query("SELECT * FROM contact_list ORDER BY contactFirstName")
    LiveData<List<Contact>> getAllContacts();
}