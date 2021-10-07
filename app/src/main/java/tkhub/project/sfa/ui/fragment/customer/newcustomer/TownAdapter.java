package tkhub.project.sfa.ui.fragment.customer.newcustomer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tkhub.project.sfa.R;
import tkhub.project.sfa.data.model.Towns;

public class TownAdapter  extends ArrayAdapter<Towns> {

    Context context;
    int resource, textViewResourceId;
    List<Towns> items, tempItems, suggestions;

    public TownAdapter(Context context, int resource, int textViewResourceId, List<Towns> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Towns>(items); // this makes the difference.
        suggestions = new ArrayList<Towns>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_towns, parent, false);
        }
        Towns people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getName_en());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Towns) resultValue).getName_en();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Towns people : tempItems) {
                    if (people.getName_en().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Towns> filterList = (ArrayList<Towns>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Towns people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}