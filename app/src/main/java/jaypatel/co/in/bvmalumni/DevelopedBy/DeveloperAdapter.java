package jaypatel.co.in.bvmalumni.DevelopedBy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jaypatel.co.in.bvmalumni.R;

/**
 * Created by Abhishek on 07-Dec-17.
 */

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.MyViewHolder> {

    private Context mContext;
    private List<Developer> mDeveloperList;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView mobile, mail, github;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.devName);
            mobile = (ImageView) view.findViewById(R.id.mobile);
            mail = (ImageView) view.findViewById(R.id.mail);
            github = (ImageView) view.findViewById(R.id.github);
            photo = (ImageView) view.findViewById(R.id.photo);
        }
    }

    public DeveloperAdapter(Context mContext, List<Developer> mDeveloperList) {
        this.mContext = mContext;
        this.mDeveloperList = mDeveloperList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.developer_card_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Developer developer = mDeveloperList.get(position);
        holder.name.setText(developer.getName());

        if (developer.getMobile().equals(""))
            holder.mobile.setVisibility(View.INVISIBLE);
        else{
            holder.mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent call_dev = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+developer.getMobile()));

                    if(Build.VERSION.SDK_INT >= 23) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 10);
                        }else {
                            mContext.startActivity(call_dev);
                        }
                    }else {
                        mContext.startActivity(call_dev);
                    }
                }
            });
        }


        if (developer.getMail().equals(""))
            holder.mail.setVisibility(View.INVISIBLE);
        else{
            holder.mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",developer.getMail(), null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    mContext.startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
        }


        if (developer.getGithub().equals(""))
            holder.github.setVisibility(View.INVISIBLE);
        else{
            holder.github.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/"+developer.getGithub())));
                }
            });
        }

        Glide.with(mContext).load(developer.getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return mDeveloperList.size();
    }

}