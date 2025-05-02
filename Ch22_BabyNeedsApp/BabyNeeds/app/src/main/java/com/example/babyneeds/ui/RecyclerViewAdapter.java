package com.example.babyneeds.ui;


import android.content.Context;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.R;
import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override // where views are being created and implementing the recycling
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    // binding the contact row with the data as well as RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Item item = itemList.get(position); // each contact object inside of list
        viewHolder.name.setText(MessageFormat.format("Item: {0}", item.getItemName()));
        viewHolder.quantity.setText(MessageFormat.format("Qty: {0}", String.valueOf(item.getItemQuantity())));
        viewHolder.color.setText(MessageFormat.format("Color: {0}", item.getItemColor()));
        viewHolder.size.setText(MessageFormat.format("Size: {0}", String.valueOf(item.getItemSize())));
        viewHolder.dateAdded.setText(MessageFormat.format("Added on: {0}", item.getDateItemAdded()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // create the instance variables pertaining to the list row
        public TextView name;
        public TextView color;
        public TextView quantity;
        public TextView size;
        public TextView dateAdded;

        public Button editButton;
        public Button deleteButton;

        public int id;

        // where info is fetched and acted on
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            RecyclerViewAdapter.this.context = context;
            itemView.setOnClickListener(this);


            name = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            color = itemView.findViewById(R.id.item_color);
            size = itemView.findViewById(R.id.item_size);
            dateAdded = itemView.findViewById(R.id.date_added);


            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position;
            position = getAdapterPosition();
            Item item = itemList.get(position);

            if (view.getId() == R.id.edit_button) {
                // edit item
                 editItem(item);
            } else if (view.getId() == R.id.delete_button) {
                // delete item
                deleteItem(item.getId());
            }
        }

        private void editItem(Item  itemToUpdate) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_entry_popup, null);

            EditText itemName;
            EditText itemQuantity;
            EditText itemColor;
            EditText itemSize;
            TextView title;
            Button saveButton;

            title = view.findViewById(R.id.title);
            itemName = view.findViewById(R.id.babyItem);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemColor = view.findViewById(R.id.itemColor);
            itemSize = view.findViewById(R.id.itemSize);
            saveButton = view.findViewById(R.id.saveButton);

            title.setText(R.string.edit_item);
            itemName.setText(itemToUpdate.getItemName());
            itemQuantity.setText(String.valueOf(itemToUpdate.getItemQuantity()));
            itemColor.setText(itemToUpdate.getItemColor());
            itemSize.setText(String.valueOf(itemToUpdate.getItemSize()));
            saveButton.setText(R.string.update_text);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseHandler sqLiteDatabase = new DatabaseHandler(context);

                    // TODO: update each baby item to db
                    itemToUpdate.setItemName(itemName.getText().toString());
                    itemToUpdate.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                    itemToUpdate.setItemColor(itemColor.getText().toString());
                    itemToUpdate.setItemSize(Integer.parseInt(itemSize.getText().toString()));

                    if (
                            !itemName.getText().toString().isEmpty()
                                    && !itemQuantity.getText().toString().isEmpty()
                                    && !itemColor.getText().toString().isEmpty()
                                    && !itemSize.getText().toString().isEmpty()
                    ) {
                    sqLiteDatabase.updateItem(itemToUpdate);
                    notifyItemChanged(getAdapterPosition(), itemToUpdate);
                    } else {
                        Snackbar.make(view, "Empty Fields not Allowed",
                                Snackbar.LENGTH_SHORT).show();
                    }

                    Snackbar.make(view, "Item Updated", Snackbar.LENGTH_SHORT)
                            .show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }, 1200); //  1.2sec
                }
            });
        }

        private void deleteItem(int id) {
            builder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_popup, null);

            Button noButton = view.findViewById(R.id.no_button);
            Button yesButton = view.findViewById(R.id.yes_button);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler sqLiteDatabase = new DatabaseHandler(context);
                    sqLiteDatabase.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });
        }
    }
}
