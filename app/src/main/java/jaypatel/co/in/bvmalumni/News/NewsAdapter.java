package jaypatel.co.in.bvmalumni.News;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jaypatel.co.in.bvmalumni.R;

/**
 * Created by jay on 4/12/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private ArrayList<String> heading, detail;
    static Context context;


    public NewsAdapter(ArrayList<String> heading, ArrayList<String> detail, Context context) {
        this.heading = heading;
        this.detail = detail;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mHeadingTextView;
        //public TextView mDetailTextView;

        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.news_cv);
            mHeadingTextView = (TextView) v.findViewById(R.id.cv_HeadTextView);


           // mDetailTextView = (TextView) v.findViewById(R.id.cv_DetailTextView);
        }
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(position % 2 == 0) {
            holder.mCardView.setCardBackgroundColor(Color.rgb(9,130,164));
            holder.mHeadingTextView.setTextColor(Color.WHITE);
           // holder.mDetailTextView.setTextColor(Color.WHITE);
        }
        holder.mHeadingTextView.setText(heading.get(position));

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(heading.get(position));
                alertDialog.setMessage(detail.get(position));
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });
       // holder.mDetailTextView.setText(detail.get(position));
    }

    @Override
    public int getItemCount() {
        return heading.size();
    }
}