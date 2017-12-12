package jaypatel.co.in.bvmalumni.Register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jaypatel.co.in.bvmalumni.Info;
import jaypatel.co.in.bvmalumni.R;
import jaypatel.co.in.bvmalumni.StartActivity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Register2 extends AppCompatActivity {

    TextView fileDetail;
    String filePath;
    private int serverResponseCode = 0;
    Context context;
    String strEmail, strName, strMobile, strPass;
    TextView heading;
    Button back,register;
    Animation animBounce, animSlideInLeft, animSlideInRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        fileDetail = (TextView) findViewById(R.id.filePath);
        heading = (TextView) findViewById(R.id.register2Head);
        back = (Button) findViewById(R.id.back);
        register = (Button) findViewById(R.id.register);

        filePath = new String();
        context = this;

        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animSlideInRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        animSlideInLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);

        register.setAnimation(animSlideInRight);
        back.setAnimation(animSlideInLeft);
        heading.setAnimation(animBounce);

        strEmail = getIntent().getStringExtra("email");
        strName = getIntent().getStringExtra("name");
        strMobile = getIntent().getStringExtra("mobile");
        strPass = getIntent().getStringExtra("pass");
    }

    public void browseFile(View view) {
        if(Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + File.separator);
        intent.setDataAndType(uri, "text/csv");
        startActivityForResult(Intent.createChooser(intent, "Browse file"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    filePath = getRealPathFromURI(getApplicationContext(), uri);
                    fileDetail.setText(myFile.getName());
                }
                break;
        }
    }

    public String getRealPathFromURI(Context cntx, Uri uri) {
        Cursor cursor = null;
        try {
            String[] arr = { MediaStore.Images.Media.DATA };
            cursor = cntx.getContentResolver().query(uri,  arr, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    public void registerMe(View view) {
        if(Info.isNetworkAvailable(context)) {
            if (!filePath.equals("") && checkForType()) {
                UploadFileAsync obj = new UploadFileAsync();
                obj.execute(filePath);
            }else {
                Info.showToast(context, "Please select a IMAGE or PDF file");
            }
        }else{
            Info.showNetworkFailuireToast(context);
        }
    }

    public boolean checkForType() {
        String file = filePath.toLowerCase();
        String[] fineFileExtension = new String[] {"jpg", "png", "gif", "jpeg", "pdf"};
        for(String extension : fineFileExtension) {
            if (file.endsWith(extension))
                return true;
        }
        return false;
    }

    private class UploadFileAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog = progressDialog.show(context, "Please wait...", "Uploading Files and Sending Data", true, false);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String sourceFileUri = params[0];

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);

                if (sourceFile.isFile()) {

                    try {
                        String upLoadServerUri = Info.url + "RegisterUser.php?email=" + strEmail + "&name=" + strName +
                                "&mobile=" + strMobile + "&pass=" + strPass + "&";

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", sourceFileUri);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"jay\";filename=\""
                                + sourceFileUri + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        }

                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();

                        if (serverResponseCode == 200) {
//                            Toast.makeText(context, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }

                        fileInputStream.close();
                        dos.flush();
                        dos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if((this.progressDialog != null) && this.progressDialog.isShowing())
                this.progressDialog.dismiss();
            this.progressDialog = null;

            if(result.equals("Executed")) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Registration Successful");
                alertDialog.setMessage("You're successfully registered at BVM Alumni! You will receive an email regarding your registration.\nAfter that you will be able to login!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent i = new Intent(context, StartActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                        startActivity(i);
                    }
                });
            }else {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Registration Failed");
                alertDialog.setMessage("Registration process could not complited successfully please try again later!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent i = new Intent(context, StartActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                        startActivity(i);
                    }
                });
            }
        }
    }
}
