package br.com.rateshare.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.Categoria;

public class SpinCategoriaAdapter extends ArrayAdapter<Categoria> {

    private Context context;
    private List<Categoria> values;
    private TextView label;

    public SpinCategoriaAdapter(Context context, int textViewResourceId,
                       List<Categoria> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Categoria getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_item_spiner_categoria, parent, false);
        }
        label = convertView.findViewById(R.id.tituloText);

        label.setText(values.get(position).nome);

        return convertView;

    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_item_spiner_categoria, parent, false);
        }
        label = convertView.findViewById(R.id.tituloText);

        label.setText(values.get(position).nome);

        return convertView;

    }

}