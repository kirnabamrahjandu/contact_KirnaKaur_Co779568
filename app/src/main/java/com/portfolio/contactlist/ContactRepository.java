package com.portfolio.contactlist;

import android.os.AsyncTask;
import java.util.List;
import android.arch.lifecycle.MutableLiveData;
import android.app.Application;
import android.arch.lifecycle.LiveData;

public class ContactRepository
{
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";

    private MutableLiveData<List<Contact>> searchResults = new MutableLiveData<>();
    private LiveData<List<Contact>> allContacts;
    private ContactDao contactDao;

    //CONSTRUCTOR
    public ContactRepository(Application application)
    {
        ContactRoomDatabase db;
        db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
    }

    //ASYNC CALLS (insert, delete, find)
    public void insertContact(Contact newContact)
    {
        InsertAsyncTask task = new InsertAsyncTask(contactDao);
        task.execute(newContact);
    }
    public void deleteContact(String name)
    {
        DeleteAsyncTask task = new DeleteAsyncTask(contactDao);
        task.execute(name);
    }
    public void findFirstName(String name)
    {
        QueryAsyncTask task = new QueryAsyncTask(contactDao, FIRST_NAME);
        task.delegate = this;
        task.execute(name);
    }

    public void findLastName(String name)
    {
        QueryAsyncTask task = new QueryAsyncTask(contactDao, LAST_NAME);
        task.delegate = this;
        task.execute(name);
    }

    public void findPhone(String phone)
    {
        QueryAsyncTask task = new QueryAsyncTask(contactDao, PHONE);
        task.delegate = this;
        task.execute(phone);
    }
    public void findEmail(String email)
    {
        QueryAsyncTask task = new QueryAsyncTask(contactDao, EMAIL);
        task.delegate = this;
        task.execute(email);
    }

    public void findAddress(String address)
    {
        QueryAsyncTask task = new QueryAsyncTask(contactDao, ADDRESS);
        task.delegate = this;
        task.execute(address);
    }

    //GETTERS & SETTERS
    public LiveData<List<Contact>> getAllContacts() { return allContacts;}
    public MutableLiveData<List<Contact>> getSearchResults() { return searchResults; }
    private void asyncFinished(List<Contact> results) { searchResults.setValue(results); }


    //ASYNC QUERY (Find)
    private static class QueryAsyncTask extends AsyncTask<String, Void, List<Contact>>
    {
        private String criterion;
        private ContactDao asyncTaskDao;
        private static ContactRepository delegate = null;

        //Constructor
        QueryAsyncTask(ContactDao dao, String criterion)
        {
            this.criterion= criterion;
            asyncTaskDao = dao;
        }

        @Override
        protected List<Contact> doInBackground(final String... params)
        {
            if (criterion == LAST_NAME) return asyncTaskDao.findLastName(params[0]);

            else if (criterion == LAST_NAME) return asyncTaskDao.findLastName(params[0]);

            else if (criterion == PHONE) return asyncTaskDao.findPhone(params[0]);

            else if (criterion == ADDRESS) return asyncTaskDao.findAddress(params[0]);

            else return asyncTaskDao.findEmail(params[0]);
        }

        @Override
        protected void onPostExecute(List<Contact> result) { delegate.asyncFinished(result); }
    }

    //ASYNC INSERT
    private static class InsertAsyncTask extends AsyncTask< Contact, Void, Void>
    {
        private  ContactDao asyncTaskDao;
        InsertAsyncTask( ContactDao dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Contact... params)
        {
            asyncTaskDao.insertContact(params[0]);
            return null;
        }
    }

    //ASYNC DELETE
    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void>
    {
        private ContactDao asyncTaskDao;
        DeleteAsyncTask(ContactDao dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final String... params)
        {
            asyncTaskDao.deleteContact(params[0]);
            return null;
        }
    }
}