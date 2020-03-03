package ir.technopedia.covino.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import ir.technopedia.covino.R;
import ir.technopedia.covino.activity.MainActivity;
import ir.technopedia.covino.model.Badge;

public class AchivementAdapter extends RecyclerView.Adapter<AchivementAdapter.ViewHolder> {

    private ArrayList<Badge> myArr = new ArrayList<>();
    Context mCOntext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AchivementAdapter(ArrayList<Badge> myArr, Context mCOntext) {
        this.myArr = myArr;
        this.mCOntext = mCOntext;
    }

    public void updateAdapter(ArrayList<Badge> myArr) {
        this.myArr = myArr;
        this.notifyDataSetChanged();
    }

    @Override
    public AchivementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_row, parent, false);
        return new AchivementAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AchivementAdapter.ViewHolder viewHolder, int position) {
        final Badge model = myArr.get(position);
        Log.wtf("model get gotten", position + " " + model.getGotten());


        if (model.getGotten().equalsIgnoreCase("1")) {
            Log.wtf("success", model.getName());
//            viewHolder.image.setBackgroundResource(R.drawable.input_bg_white);
//            viewHolder.image.setColorFilter(ContextCompat.getColor(viewHolder.image.getContext(), R.color.colorPrimary));
            Glide.with(mCOntext).load(getImage(model.getName())).crossFade().into(viewHolder.image);
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Crouton.makeText(((MainActivity)mCOntext),"شما این نشان را به دست آورده اید", Style.CONFIRM).show();

                }
            });

        } else {

            Glide.with(mCOntext)
                    .load(getImage(model.getName()))
                    .into(viewHolder.image);

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            viewHolder.image.setColorFilter(filter);
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Crouton.makeText(((MainActivity)mCOntext),model.getCondition(), Style.ALERT).show();
                }
            });

//                Glide.with(mCOntext).load(R.drawable.a).crossFade().into(viewHolder.image);
        }

    }

    public int getImage(String imageName) {

        int drawableResourceId = mCOntext.getResources().getIdentifier(imageName, "drawable", mCOntext.getPackageName());

        return drawableResourceId;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return myArr.size();
    }
}