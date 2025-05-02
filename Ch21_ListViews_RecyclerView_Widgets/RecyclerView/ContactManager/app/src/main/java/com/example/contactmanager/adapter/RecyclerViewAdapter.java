package com.example.contactmanager.adapter;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactmanager.R;
import com.example.contactmanager.DetailsActivity;
import com.example.contactmanager.model.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Contact> contactList;

    public RecyclerViewAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override // where views are being created and implementing the recycling
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_row, viewGroup, false);

        return new ViewHolder(view);
    }

    // binding the contact row with the data as well as RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Contact contact = contactList.get(position); // each contact object inside of list
        viewHolder.contactName.setText(contact.getName());
        viewHolder.phoneNumber.setText(contact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView contactName;
        public TextView phoneNumber;
        public ImageView iconButton;


        // where info is fetched and acted on
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            contactName = itemView.findViewById(R.id.name_textView);
            phoneNumber = itemView.findViewById(R.id.phone_number_textView);
            iconButton = itemView.findViewById(R.id.icon_button_imageView);

            iconButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Contact contact = contactList.get(position);

//            if (view.getId() == R.id.icon_button_imageView) {
//            Log.d("Clicked", "onClick: " + contact.getName());
//        }

            Intent intent = new Intent(context, DetailsActivity.class); // Create an Intent to start ShowContactChoice activity
            intent.putExtra("name", contact.getName()); // Add the guess to the Intent
            intent.putExtra("number", contact.getPhoneNumber()); // Add a name to the Intent

            context.startActivity(intent);
            }
        }
}
