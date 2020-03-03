package ir.technopedia.covino.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.R;
import ir.technopedia.covino.model.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> restaurantList;
    List<String> poked = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poke)
        ImageView poke;

        @BindView(R.id.count)
        TextView count;

        @BindView(R.id.name)
        TextView name;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ContactAdapter(List<Contact> restaurantList, List<String> poked) {
        this.restaurantList = restaurantList;
        this.poked = poked;
    }

    public void updateAdapter(List<Contact> restaurantList, List<String> poked) {
        this.restaurantList = restaurantList;
        this.poked = poked;
        this.notifyDataSetChanged();
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder viewHolder, int position) {
        Contact model = restaurantList.get(position);
        viewHolder.count.setText(model.getCount());
        viewHolder.name.setText(model.getName());
        if (poked.get(position).equals("")) {
            viewHolder.poke.setBackgroundResource(R.drawable.input_bg_white);
            viewHolder.poke.setColorFilter(ContextCompat.getColor(viewHolder.poke.getContext(), R.color.colorPrimary));
        } else {
            viewHolder.poke.setBackgroundResource(R.drawable.btn_primary);
            viewHolder.poke.setColorFilter(ContextCompat.getColor(viewHolder.poke.getContext(), R.color.md_white_1000));
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}