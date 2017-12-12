package jaypatel.co.in.bvmalumni.Profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import jaypatel.co.in.bvmalumni.DatePickerFragment;
import jaypatel.co.in.bvmalumni.Info;
import jaypatel.co.in.bvmalumni.R;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditProfile extends AppCompatActivity {

    CircleImageView profilePic;
    Button changePhoto;
    EditText name, address, mobile, residence, office,year,branch,nativ,job,company,designation;
    TextView dobTxt, anniversaryTxt;
    ImageView dob, anniversary;
    Context context;
    Info info;
    String picturePath = "";

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);
        setTitle("Edit Profile");

        context = this;
        info = new Info(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        profilePic = (CircleImageView) findViewById(R.id.ep_user_profile_photo);
        changePhoto = (Button) findViewById(R.id.ep_changePic);
        name = (EditText) findViewById(R.id.ep_name);
        address = (EditText) findViewById(R.id.ep_address);
        mobile = (EditText) findViewById(R.id.ep_mobile);
        residence = (EditText) findViewById(R.id.ep_resi_no);
        office = (EditText) findViewById(R.id.ep_office_no);
        dob = (ImageView) findViewById(R.id.ep_dob);
        dobTxt = (TextView) findViewById(R.id.ep_dobTxt);
        anniversary = (ImageView) findViewById(R.id.ep_anniversery);
        anniversaryTxt = (TextView) findViewById(R.id.ep_anniverseryTxt);
        year = (EditText) findViewById(R.id.ep_year);
        branch = (EditText) findViewById(R.id.ep_branch);
        nativ = (EditText) findViewById(R.id.ep_native);
        job = (EditText) findViewById(R.id.ep_job);
        company = (EditText) findViewById(R.id.ep_company);
        designation = (EditText) findViewById(R.id.ep_designation);

        loadData();

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }

        });

        if(Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateFragment(dobTxt);
            }
        });

        dobTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateFragment(dobTxt);
            }
        });

        anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateFragment(anniversaryTxt);
            }
        });

        anniversaryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateFragment(anniversaryTxt);
            }
        });

    }

    private void showDateFragment(TextView textView) {
        new DatePickerFragment(textView).show(getSupportFragmentManager(), "date");
    }

    private void loadData() {
        name.setText(info.dataBase.getCurrentUserName());
        mobile.setText(info.dataBase.getMobile());
        if(info.dataBase.isBitmapSet())
            profilePic.setImageBitmap(info.dataBase.getBitmap());
        dobTxt.setText(info.dataBase.getDOB());
        address.setText(info.dataBase.getAddress());
        residence.setText(info.dataBase.getResidence());
        office.setText(info.dataBase.getOffice());
        year.setText(info.dataBase.getYear());
        branch.setText(info.dataBase.getBranch());
        nativ.setText(info.dataBase.getNative());
        job.setText(info.dataBase.getJob());
        company.setText(info.dataBase.getCompany());
        designation.setText(info.dataBase.getDesignation());
        anniversaryTxt.setText(info.dataBase.getAnniversary());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                Picasso.with(this).load(data.getData()).resize(300,300).into(target);
            }
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            getImage(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    public void getImage(Bitmap bm) {
        int id = (int) Info.dataBase.insertBitmap(bm);
        //Info.showToast(getApplicationContext(), id+"");
        profilePic.setImageBitmap(Info.dataBase.getBitmap());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 654){
            Info.showSnackBar(this.getCurrentFocus(), "You must have to give the storage permission");
        }
    }

    public void saveEdit(View view) {
        if(Info.isNetworkAvailable(context)) {
            String email = info.dataBase.getCurrentEmail();
            String name = this.name.getText().toString();
            String mobile = this.mobile.getText().toString();
            String office = this.office.getText().toString();
            String residence = this.residence.getText().toString();
            String dob = this.dobTxt.getText().toString();
            String address = this.address.getText().toString();
            String year = this.year.getText().toString();
            String branch = this.branch.getText().toString();
            String nativ = this.nativ.getText().toString();
            String job = this.job.getText().toString();
            String company = this.company.getText().toString();
            String designation = this.designation.getText().toString();
            String anniversary = this.anniversaryTxt.getText().toString();

            info.dataBase.setUserName(name);
            info.dataBase.setMobile(mobile);
            info.dataBase.setOffice(office);
            info.dataBase.setResidence(residence);
            info.dataBase.setDOB(dob);
            info.dataBase.setAddress(address);
            info.dataBase.setYear(year);
            info.dataBase.setBranch(branch);
            info.dataBase.setNative(nativ);
            info.dataBase.setJob(job);
            info.dataBase.setCompany(company);
            info.dataBase.setDesignation(designation);
            info.dataBase.setAnniversary(anniversary);

            Uri file = Uri.fromFile(new File(picturePath));
            StorageReference riversRef = mStorageRef.child("profilePics/"+email+".jpg");

            riversRef.putFile(file) .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    // ...
                }
            });

            UploadToServer obj = new UploadToServer();
            obj.execute(email, name, mobile, office, residence, dob, address,year,branch,nativ,job,company,designation,anniversary);
        }else {
            Info.showNetworkFailuireToast(context);
        }
    }

    class UploadToServer extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String strEmail;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog = progressDialog.show(context, context.getString(R.string.PleaseWait), "Saving data and Updating Profile", true, false);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String email = params[0];
                String name = params[1];
                String mobile = params[2];
                String office = params[3];
                String residence = params[4];
                String dob = params[5];
                String address = params[6];
                String year = params[7];
                String branch = params[8];
                String nativ = params[9];
                String job = params[10];
                String company = params[11];
                String designation = params[12];
                String anniversary = params[13];

                strEmail = email;

                if(email == null)
                    email = "";
                if(name == null)
                    name = "";
                if(mobile == null)
                    mobile = "";
                if(office == null)
                    office = "";
                if(residence == null)
                    residence = "";
                if(dob == null)
                    dob = "";
                if(address == null)
                    address = "";
                if(year == null)
                    year = "";
                if(branch == null)
                    branch = "";
                if(nativ == null)
                    nativ = "";
                if(job == null)
                    job = "";
                if(company == null)
                    company = "";
                if(designation == null)
                    designation = "";
                if(anniversary == null)
                    anniversary = "";

                URL url = new URL(Info.url + "SyncData.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postdata = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8")+"&"+
                        URLEncoder.encode("office","UTF-8")+"="+URLEncoder.encode(office,"UTF-8")+"&"+
                        URLEncoder.encode("resi","UTF-8")+"="+URLEncoder.encode(residence,"UTF-8")+"&"+
                        URLEncoder.encode("dob","UTF-8")+"="+URLEncoder.encode(dob,"UTF-8")+"&"+
                        URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8")+"&"+
                        URLEncoder.encode("branch","UTF-8")+"="+URLEncoder.encode(branch,"UTF-8")+"&"+
                        URLEncoder.encode("native","UTF-8")+"="+URLEncoder.encode(nativ,"UTF-8")+"&"+
                        URLEncoder.encode("job","UTF-8")+"="+URLEncoder.encode(job,"UTF-8")+"&"+
                        URLEncoder.encode("company","UTF-8")+"="+URLEncoder.encode(company,"UTF-8")+"&"+
                        URLEncoder.encode("designation","UTF-8")+"="+URLEncoder.encode(designation,"UTF-8")+"&"+
                        URLEncoder.encode("anniversary","UTF-8")+"="+URLEncoder.encode(anniversary,"UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line,result = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Log.d("hehe",s);
            if (s != null && s.equals("Done")) {
                Info.showToast(context, "Data Saved!");
            } else {
                Info.showToast(context, "ERROR");
            }
            onBackPressed();
        }
    }

}