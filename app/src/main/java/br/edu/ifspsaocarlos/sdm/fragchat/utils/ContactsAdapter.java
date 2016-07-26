package br.edu.ifspsaocarlos.sdm.fragchat.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;

/**
 * Created by LeonardoAlmeida on 23/07/16.
 */

public class ContactsAdapter extends ArrayAdapter<UserModel> implements Filterable
{

    private List<UserModel>  contacts;
    private List<UserModel> contactsFiltered;
    private Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView labelName;
        TextView labelDetails;
        TextView labelDate;
        TextView labelIcons;
        ImageView iv;
    }

    public ContactsAdapter(Context context, List contacts) {
        super(context, R.layout.component_msgchat_item, contacts);
        this.context = context;
        this.contacts = contacts;
        this.contactsFiltered = contacts;
    }

    @Override
    public int getCount() {
        return contactsFiltered.size();
    }

    @Override
    public UserModel getItem(int position) {
        return contactsFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* (non-Javadoc)
                 * @see android.widget.RecycleViewAdapter#getView(int, android.view.View, android.view.ViewGroup)
                 */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.component_msgchat_item, parent, false);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserModel contact = (UserModel) contactsFiltered.get(position);

        if (contact != null){

            viewHolder.labelName = (TextView) convertView.findViewById(R.id.contact_name);

            if (viewHolder.labelName != null) {
                viewHolder.labelName.setText(contact.getNome());
                viewHolder.labelName.setCompoundDrawablesWithIntrinsicBounds(
                        contact.isOnline() ? android.R.drawable.presence_online
                                : android.R.drawable.presence_offline, 0, 0, 0);
            }


            viewHolder.labelDetails = (TextView) convertView.findViewById(R.id.contact_details);
            if (viewHolder.labelDetails != null) {
                viewHolder.labelDetails.setText(contact.getDetalhes());
            }

            viewHolder.labelDate = (TextView) convertView.findViewById(R.id.lastMsgdate);
            if (viewHolder.labelDate != null) {
                viewHolder.labelDate.setText("02/07/2016 10:35");
            }
            viewHolder.labelIcons = (TextView) convertView.findViewById(R.id.iconState);
            if (viewHolder.labelIcons != null) {
                viewHolder.labelIcons.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        contact.isOnline() ? android.R.drawable.sym_action_chat
                                : R.drawable.arrow, 0);
            }

            viewHolder.iv = (ImageView) convertView.findViewById(R.id.listuser_profile_photo);
            if (viewHolder.iv != null) {
                viewHolder.iv.setImageResource(contact.getImgAvatar());
            }

        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = contacts;
                    results.count = contacts.size();
                }
                else {
                    List<UserModel> filteredList= new ArrayList<>();
                    String searchStr = constraint.toString().toUpperCase();
                    for (UserModel data : contacts)
                        if (data.getNome().toUpperCase().contains(searchStr)) filteredList.add(data);

                    results.count = filteredList.size();
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactsFiltered = (ArrayList<UserModel>) results.values;
                notifyDataSetChanged();
            }

        };
    }
}