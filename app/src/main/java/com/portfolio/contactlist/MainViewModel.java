package com.portfolio.contactlist;

import android.app.Application;
import java.util.List;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel
{
    private static ContactRepository repository;
    private LiveData<List<Contact>> allContacts;
    private MutableLiveData<List<Contact>> searchResults;

    //CONSTRUCTOR
    public MainViewModel (Application application)
    {
        super(application);
        repository = new ContactRepository(application);
        allContacts = repository.getAllContacts();
        searchResults = repository.getSearchResults();
    }

    //GETTERS
    MutableLiveData<List<Contact>> getSearchResults() { return searchResults; }
    LiveData<List<Contact>> getAllContacts() { return allContacts; }

    //REPOSITORY OPERATIONS (insert, find, delete)
    public void insertContact(Contact contact) { repository.insertContact(contact); }
    public void findLastName(String name) { repository.findLastName(name); }
    public void findFirstName(String name) { repository.findFirstName(name); }
    public void findPhone(String phone) { repository.findPhone(phone); }
    public void findEmail(String email) { repository.findEmail(email); }
    public void deleteContact(String name) { repository.deleteContact(name); }
}