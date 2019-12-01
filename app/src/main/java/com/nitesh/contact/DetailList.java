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

public class DetailList extends ArrayAdapter<Model> {
    private Activity context;
    private List<Model> detaillist;

    public DetailList(Activity context, List<Model> contactlist){
        super(context, R.layout.list_details, contactlist);
        this.context = context;
        this.detaillist = contactlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewitem = inflater.inflate(R.layout.list_details, null, true);

        TextView textViewPhone = listViewitem.findViewById(R.id.textViewPhone);
        TextView textViewEmail = listViewitem.findViewById(R.id.textViewEmail);
        TextView textViewAddress = listViewitem.findViewById(R.id.textViewAddress);

        Model model = detaillist.get(position);

        textViewPhone.setText(model.getPhone());
        textViewEmail.setText(model.getEmail());
        textViewAddress.setText(model.getAddress());

        return  listViewitem;
    }
}
