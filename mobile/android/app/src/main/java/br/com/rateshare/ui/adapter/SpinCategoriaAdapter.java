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


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_item_spiner_categoria, parent, false);
        }

        if(values.get(position).nome !=null){
            TextView label = (TextView) convertView.findViewById(R.id.tituloText);
            TextView label2 = (TextView) convertView.findViewById(R.id.categoriaText);
            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(values.get(position).nome);
            // And finally return your dynamic (or custom) view for each spinner item
            return convertView;

        }
        else {
            return convertView;
        }

    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_item_spiner_categoria, parent, false);
        }

        if(values.get(position).nome !=null){
            TextView label = (TextView) convertView.findViewById(R.id.tituloText);
            TextView label2 = (TextView) convertView.findViewById(R.id.categoriaText);
            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(values.get(position).nome);
            // And finally return your dynamic (or custom) view for each spinner item
            return convertView;

        }
        else {
            return convertView;
        }

    }
}