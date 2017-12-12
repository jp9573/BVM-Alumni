package jaypatel.co.in.bvmalumni.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;
import jaypatel.co.in.bvmalumni.Info;
import jaypatel.co.in.bvmalumni.R;

public class MyProfile extends Fragment {

    ImageView editProfile;
    Info info;

    CircleImageView profilePic;
    Context context;
    TextView profileName, dob, mob, office, resi, address,year,branch,nativ,job,company,designation,anniversary;

    private Bitmap bitmap = null;

    public MyProfile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        context = getContext();
        info = new Info(context);

        profilePic = (CircleImageView) v.findViewById(R.id.user_profile_photo);
        editProfile = (ImageView) v.findViewById(R.id.ep_edit);
        office = (TextView) v.findViewById(R.id.pr_office_no);
        resi = (TextView) v.findViewById(R.id.pr_resi_no);
        address = (TextView) v.findViewById(R.id.pr_address);
        profileName = (TextView) v.findViewById(R.id.user_profile_name);
        dob = (TextView) v.findViewById(R.id.pr_dob);
        mob = (TextView) v.findViewById(R.id.pr_mob);
        year = (TextView) v.findViewById(R.id.pr_year);
        branch = (TextView) v.findViewById(R.id.pr_branch);
        nativ = (TextView) v.findViewById(R.id.pr_native);
        job = (TextView) v.findViewById(R.id.pr_job);
        company = (TextView) v.findViewById(R.id.pr_company);
        designation = (TextView) v.findViewById(R.id.pr_designation);
        anniversary = (TextView) v.findViewById(R.id.pr_anniversery);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(context, EditProfile.class);
            startActivity(intent);
            }
        });

        loadData();

        if(!info.dataBase.isBitmapSet()) {
            downloadImage();
        }else {
            profilePic.setImageBitmap(info.dataBase.getBitmap());
        }

        /*if(Info.isNetworkAvailable(getContext())) {
            loadInfo();
        }else{*/

        //}
        return v;
    }

    private void downloadImage() {
        final FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://bvm-alumni.appspot.com");
        String email = info.dataBase.getCurrentEmail();
        storageRef.child("profilePics/"+email+".jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if(bitmap != null) {
                    info.dataBase.insertBitmap(bitmap);
                    profilePic.setImageBitmap(info.dataBase.getBitmap());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(Info.isNetworkAvailable(getContext())) {
            loadInfo();
        }else{*/
            loadData();
        //}
    }

    private void loadData() {
        profileName.setText(info.dataBase.getCurrentUserName());
        mob.setText(info.dataBase.getMobile());
        if(info.dataBase.isBitmapSet())
            profilePic.setImageBitmap(info.dataBase.getBitmap());
        dob.setText(info.dataBase.getDOB());
        address.setText(info.dataBase.getAddress());
        resi.setText(info.dataBase.getResidence());
        office.setText(info.dataBase.getOffice());
        year.setText(info.dataBase.getYear());
        branch.setText(info.dataBase.getBranch());
        nativ.setText(info.dataBase.getNative());
        job.setText(info.dataBase.getJob());
        company.setText(info.dataBase.getCompany());
        designation.setText(info.dataBase.getDesignation());
        anniversary.setText(info.dataBase.getAnniversary());
    }

}
