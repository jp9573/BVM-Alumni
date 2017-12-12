package jaypatel.co.in.bvmalumni.EventNotification;

import android.graphics.Color;
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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
private ArrayList<String> message, body, date;

public NotificationAdapter(ArrayList<String> heading, ArrayList<String> body, ArrayList<String> detail) {
        this.message = heading;
        this.body = body;
        this.date = detail;
        }

public static class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView mHeding;
    public TextView mBody;
    public TextView mDate;
    public CardView cardView;

    public MyViewHolder(View v) {
        super(v);

        cardView = (CardView) v.findViewById(R.id.messages_cv);
        mHeding = (TextView) v.findViewById(R.id.msg_Message);
        mDate = (TextView) v.findViewById(R.id.msg_dt);
        mBody = (TextView) v.findViewById(R.id.msg_Body);
    }
}

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(position % 2 == 0) {
            holder.cardView.setCardBackgroundColor(Color.rgb(9,130,164));
            holder.mHeding.setTextColor(Color.WHITE);
            holder.mBody.setTextColor(Color.WHITE);
            holder.mDate.setTextColor(Color.WHITE);
        }
        holder.mHeding.setText(message.get(position));
        holder.mBody.setText(body.get(position));
        holder.mDate.setText(date.get(position));
    }

    @Override
    public int getItemCount() {
        return message.size();
    }
}
