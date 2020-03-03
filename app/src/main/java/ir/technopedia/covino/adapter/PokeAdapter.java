package ir.technopedia.covino.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.R;
import ir.technopedia.covino.model.Contact;
import ir.technopedia.covino.model.Poke;

public class PokeAdapter extends RecyclerView.Adapter<PokeAdapter.ViewHolder> {

    private List<Poke> restaurantList;
    List<Contact> contactList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.background)
        LinearLayout background;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.message)
        TextView message;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public PokeAdapter(List<Poke> restaurantList, List<Contact> contactList) {
        this.restaurantList = restaurantList;
        this.contactList = contactList;
    }

    public void updateAdapter(List<Poke> restaurantList, List<Contact> contactList) {
        this.restaurantList = restaurantList;
        this.contactList = contactList;
        this.notifyDataSetChanged();
    }

    @Override
    public PokeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new PokeAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PokeAdapter.ViewHolder viewHolder, int position) {
        Poke model = restaurantList.get(position);
        String[] date = model.getDate().split(" ");
        viewHolder.date.setText(date[0]);
        String[] time = date[1].split(":");
        viewHolder.time.setText(time[1] + " : " + time[0]);

        viewHolder.message.setText("شخص ناشناس با شماره " + model.getPokerPhone() + " به شما برای شستن دستانتان تلنگر زده است");
        for (int i = 0; i < contactList.size(); i++) {
            String pokerPhone = model.getPokerPhone();
            pokerPhone = pokerPhone.replace(" ", "");
            pokerPhone = pokerPhone.replace("+1", "");
            pokerPhone = pokerPhone.replace("+98", "");
            pokerPhone = pokerPhone.replace("+44", "");
            String phone = contactList.get(i).getPhone();
            phone = phone.replace(" ", "");
            phone = phone.replace("+1", "");
            phone = phone.replace("+98", "");
            phone = phone.replace("+44", "");
            if (phone.equals(pokerPhone)) {
                viewHolder.message.setText(contactList.get(i).getName() + " به شما برای شستن دستانتان تلنگر زده است");
                break;
            }
        }
        if (model.getIsNew().equals("1")) {
            viewHolder.background.setBackgroundColor(viewHolder.background.getContext().getResources().getColor(R.color.md_red_500));
            viewHolder.message.setTextColor(viewHolder.background.getContext().getResources().getColor(R.color.md_white_1000));
            viewHolder.date.setTextColor(viewHolder.background.getContext().getResources().getColor(R.color.md_white_1000));
            viewHolder.time.setTextColor(viewHolder.background.getContext().getResources().getColor(R.color.md_white_1000));
        } else {
            viewHolder.background.setBackgroundColor(viewHolder.background.getContext().getResources().getColor(R.color.md_white_1000));
            viewHolder.message.setTextColor(viewHolder.background.getContext().getResources().getColor(R.color.md_grey_900));
            viewHolder.date.setTextColor(viewHolder.background.getContext().getResources().getColor(R.color.md_grey_900));
            viewHolder.time.setTextColor(viewHolder.background.getContext().getResources().getColor(R.color.md_grey_900));
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