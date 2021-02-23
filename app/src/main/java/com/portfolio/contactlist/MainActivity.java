package com.portfolio.contactlist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    MainFragment fragment = new MainFragment();
    private EditText contactFirstName;
    private EditText contactLastName;
    private EditText contactPhone;
    private EditText contactEmail;
    private EditText contactAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commitNow();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //OVERFLOW MENU
    public boolean onOptionsItemSelected(MenuItem item)
    {
        contactFirstName = (EditText)findViewById(R.id.contact_first_name);
        contactLastName = (EditText)findViewById(R.id.contact_last_name);
        contactPhone = (EditText)findViewById(R.id.contact_phone);
        contactEmail = (EditText)findViewById(R.id.contact_email);
        contactAddress = (EditText)findViewById(R.id.contact_address);
        String first_name = contactFirstName.getText().toString();
        String last_name = contactLastName.getText().toString();
        String phone = contactPhone.getText().toString();
        String email = contactEmail.getText().toString();
        String address = contactAddress.getText().toString();
        //OVERFLOW SWITCHBOARD
        switch (item.getItemId())
        {
            case R.id.add_contact:
                if (!first_name.equals("") && !last_name.equals("") && !phone.equals("")  && !email.equals("") && !address.equals(""))
                {
                    Contact contact = new Contact(first_name, last_name, phone, email, address);
                    fragment.insertContact(contact);
                    clearFields();
                }
                else { toaster(this, "Enter contact info to add"); }
                return true;

            case R.id.sort_az:
                fragment.sort(false);
                return true;

            case R.id.sort_za:
                fragment.sort(true);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Calling fragment.clearFields() causes the application to crash
    private void clearFields()
    {
        contactFirstName.setText("");
        contactLastName.setText("");
        contactPhone.setText("");
        contactEmail.setText("");
        contactAddress.setText("");
        contactFirstName.requestFocus();
    }

    //TOASTER
    public static void toaster(Context context, String msg)
    {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        View toastView = toast.getView();

        TextView toastMessage = (TextView)toastView.findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setCompoundDrawablePadding(16);
        toastMessage.setGravity(Gravity.LEFT);
        toastMessage.setPadding(25,0,25,05);
        toast.setGravity(Gravity.TOP, 0, 280);
        toast.show();
    }
}