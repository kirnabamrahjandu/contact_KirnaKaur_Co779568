package com.portfolio.contactlist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.arch.lifecycle.Observer;
import java.util.List;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class MainFragment extends Fragment
{
    private Activity parentActivity;
    private static final int REQUEST_CALL = 1000;
    static MainViewModel mViewModel;
    static ContactListAdapter adapter = new ContactListAdapter(R.layout.card_layout);
    private EditText contactFirstName;
    private EditText contactLastName;
    private EditText contactPhone;
    private EditText contactEmail;
    private EditText contactAddress;
    private FloatingActionButton fab;

    //CONSTRUCTOR
    public static MainFragment newInstance() { return new MainFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        parentActivity = getActivity();
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        contactFirstName = (EditText)getView().findViewById(R.id.contact_first_name);
        contactLastName = (EditText)getView().findViewById(R.id.contact_last_name);
        contactPhone = (EditText)getView().findViewById(R.id.contact_phone);
        contactEmail = (EditText)getView().findViewById(R.id.contact_email);
        contactAddress = (EditText)getView().findViewById(R.id.contact_address);
        recyclerSetup(getContext(), getActivity());
        observerSetup();

        adapter.getActivityContext(getActivity());

        fab = getView().findViewById(R.id.searchButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String first_name = contactFirstName.getText().toString();
                String last_name = contactLastName.getText().toString();
                String phone = contactPhone.getText().toString();
                String email = contactEmail.getText().toString();
                String address = contactAddress.getText().toString();

                if (!first_name.equals("")) { mViewModel.findFirstName(first_name); }
                else if (!last_name.equals("")) { mViewModel.findLastName(last_name); }
                else if (!phone.equals("")) { mViewModel.findPhone(phone); }
                else if (!email.equals("")) { mViewModel.findEmail(email); }
                else { MainActivity.toaster(getContext(), "You must enter criteria to search for"); }
                clearFields();
            }
        });
    }

    //CLEAR - Cannot call non-static MainActivity.clearFields() from static Fragment
    public void clearFields()
    {
        contactFirstName.setText("");
        contactLastName.setText("");
        contactPhone.setText("");
        contactEmail.setText("");
        contactFirstName.requestFocus();
    }

    public void insertContact (Contact contact)
    {
        if (!contact.getContactName().equals("") && !contact.getContactPhone().equals(""))
        {
            mViewModel.insertContact(contact);
        }
    }

    //Sorts the display adapter, not the data
    public void sort(boolean reverse) { adapter.sort(reverse); }

    //OBSERVER SETUP
    private void observerSetup()
    {
        mViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contacts)
            {
                adapter.setContactList(contacts);
            }
        });

        mViewModel.getSearchResults().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contacts)
            {
                if (contacts.size() > 0)
                {
                    adapter.setContactList(contacts);
                }
                else
                {
                    mViewModel.findFirstName("");
                    MainActivity.toaster(getContext(),"No matches found");
                }
            }
        });
    }

    //RECYCLER SETUP
    private void recyclerSetup(final Context context, final Activity activity)
    {
        RecyclerView recyclerView;
        recyclerView = getView().findViewById(R.id.contact_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(new ContactListAdapter.OnDeleteClickListener()
        {
            public void onClick(String name)
            {
                mViewModel.deleteContact(name);
            }
        });
        adapter.setOnDeleteLongClickListener(new ContactListAdapter.OnDeleteLongClickListener() {
            @Override
            public void onLongClick(final Contact contact) {
//                Toast.makeText(context, contact.getContactFirstName(), Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                View view = getLayoutInflater().inflate(R.layout.dialog, null);
                alert.setView(view);

                Button call = view.findViewById(R.id.call);
                Button msg = view.findViewById(R.id.msg);
                Button email = view.findViewById(R.id.email);
                
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "call", Toast.LENGTH_SHORT).show();
                        makePhoneCall(contact.getContactPhone(), context, activity);
                    }
                });

                msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "msg", Toast.LENGTH_SHORT).show();
                    }
                });

                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "email", Toast.LENGTH_SHORT).show();
                    }
                });


                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });
    }

    private void makePhoneCall(String phoneNum, Context context, Activity activity){

        if(phoneNum.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }else {
                String dial = "tel:" + phoneNum;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                makePhoneCall();
            } else {
                Toast.makeText(parentActivity, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}