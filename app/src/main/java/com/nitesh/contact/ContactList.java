package com.nitesh.contact;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContactList extends ArrayAdapter<Model> {

    private Activity context;
    private List<Model> contactlist;

    public ContactList(Activity context, List<Model> contactlist){
        super(context, R.layout.list_view, contactlist);
        this.context = context;
        this.contactlist = contactlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewitem = inflater.inflate(R.layout.list_view, null, true);

        TextView textViewName = listViewitem.findViewById(R.id.textView);
        TextView textViewNum = listViewitem.findViewById(R.id.textViewnumb);
        TextView textViewemail = listViewitem.findViewById(R.id.textViewemail);
        TextView textViewaddress = listViewitem.findViewById(R.id.textViewaddress);

        Model model = contactlist.get(position);

        textViewName.setText(model.getName());
        textViewNum.setText(model.getPhone());
        textViewemail.setText(model.getEmail());
        textViewaddress.setText(model.getAddress());

        return  listViewitem;
    }
}
