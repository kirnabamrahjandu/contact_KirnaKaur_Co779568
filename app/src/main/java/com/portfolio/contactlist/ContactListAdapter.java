package com.portfolio.contactlist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>
{
    private int contactItemLayout;
    private static List<Contact> contactCardList;
    OnDeleteClickListener deleteListener;
    OnDeleteLongClickListener onDeleteLongClickListener;
    Context activityContext;

    //CONSTRUCTOR
    public ContactListAdapter(int layoutId)
    {
        contactItemLayout = layoutId;
    }

    public void getActivityContext(Context context){
        this.activityContext = context;
    }

    public void setContactList(List<Contact> contacts)
    {
        contactCardList = contacts;
        notifyDataSetChanged();
    }

    //SORT
    public void sort(final boolean reverse)
    {
        Collections.sort(contactCardList, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs)
            {
                int dif = lhs.getContactName().toUpperCase().compareTo(rhs.getContactName().toUpperCase());
                if (reverse) return -1 * dif;
                else return dif;
            }
        });
        setContactList(contactCardList);
    }

    @Override
    public int getItemCount() { return contactCardList == null ? 0 : contactCardList.size(); }

    //VIEW HOLDER METHODS
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(contactItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition)
    {
        TextView firstNameView = holder.firstNameView;
        TextView lastNameView = holder.lastNameView;
        TextView phoneView = holder.phoneView;
        TextView emailView = holder.emailView;
        TextView addressView = holder.addressView;
        ImageButton deleteButton = holder.deleteButton;

        firstNameView.setText(contactCardList.get(listPosition).getContactFirstName());
        lastNameView.setText(contactCardList.get(listPosition).getContactLastName());
        phoneView.setText(contactCardList.get(listPosition).getContactPhone());
        emailView.setText(contactCardList.get(listPosition).getContactEmail());
        addressView.setText(contactCardList.get(listPosition).getContactAddress());

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = contactCardList.get(listPosition).getContactFirstName();
                deleteListener.onClick(name);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(activityContext, "Long click success", Toast.LENGTH_SHORT).show();
                onDeleteLongClickListener.onLongClick(contactCardList.get(listPosition));
                return false;
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView firstNameView;
        TextView lastNameView;
        TextView phoneView;
        TextView emailView;
        TextView addressView;
        ImageButton deleteButton;

        ViewHolder(View itemView)
        {
            super(itemView);
            firstNameView = itemView.findViewById(R.id.first_name_view);
            lastNameView = itemView.findViewById(R.id.last_name_view);
            phoneView = itemView.findViewById(R.id.phone_view);
            emailView = itemView.findViewById(R.id.email_view);
            addressView = itemView.findViewById(R.id.address_view);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    //DELETE CLICK LISTENERS
    public void setOnDeleteClickListener(ContactListAdapter.OnDeleteClickListener deleteListener)
    {
        this.deleteListener = deleteListener;
    }
    public void setOnDeleteLongClickListener(ContactListAdapter.OnDeleteLongClickListener onDeleteLongClickListener){
        this.onDeleteLongClickListener = onDeleteLongClickListener;
    }
    public interface OnDeleteClickListener { void onClick(String name);}
    public interface OnDeleteLongClickListener{ void onLongClick(Contact contact);}
}