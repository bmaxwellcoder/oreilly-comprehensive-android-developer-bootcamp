package com.example.babyneeds.adapter;

package com.example.contactmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.R;
import com.example.babyneeds.model.Item;


import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override // where views are being created and implementing the recycling
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row, viewGroup, false);

        return new ViewHolder(view);
    }

    // binding the contact row with the data as well as RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Item item = itemList.get(position); // each contact object inside of list
        viewHolder.itemName.setText(item.getName());
        viewHolder.itemQuantity.setText(item.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView itemQuantity;
        public ImageView iconButton;


        // where info is fetched and acted on
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemName = itemView.findViewById(R.id.name_textView);
            itemQuantity = itemView.findViewById(R.id.phone_number_textView);
            iconButton = itemView.findViewById(R.id.icon_button_imageView);

            iconButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Item item = itemList.get(position);

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
