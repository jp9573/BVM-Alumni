package jaypatel.co.in.bvmalumni.SignIn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import jaypatel.co.in.bvmalumni.Info;
import jaypatel.co.in.bvmalumni.MainActivity;
import jaypatel.co.in.bvmalumni.R;

import static jaypatel.co.in.bvmalumni.R.id.passwordInputLayout;

public class SignIn extends AppCompatActivity {

    EditText email,pass;
    static Context context;
    Info info;
    ProgressDialog progressDialog;
    Animation animBounce, animSlideInRight, animSlideInLeft;
    Button btnSignIn, btnForgotPass;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = (EditText) findViewById(R.id.si_email);
        pass = (EditText) findViewById(R.id.si_pass);
        btnSignIn = (Button) findViewById(R.id.si_signIn);
        btnForgotPass = (Button) findViewById(R.id.forgotpass);
        heading = (TextView) findViewById(R.id.signInHeading);
        context = this;
        info = new Info(context);

        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animSlideInRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        animSlideInLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);

        btnForgotPass.setAnimation(animSlideInLeft);
        btnSignIn.setAnimation(animSlideInRight);
        heading.setAnimation(animBounce);

    }

    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    public void signIn(View view) {
        InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        String strEmail = email.getText().toString().toLowerCase();
        String strPass = pass.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            email.setError("Please enter valid Email ID");
        }else if(strPass.length() < 8) {
            pass.setError("Please enter minimum 8 character long password");
        }else {
            if(Info.isNetworkAvailable(context)) {
                AuthUser obj = new AuthUser();
                obj.execute(strEmail, strPass);
            }else {
                Info.showNetworkFailuireToast(context);
            }
        }
    }

    public void successfullyLoggedIn(String name) {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_executed", true);
        edt.commit();

        Toast.makeText(context,"Welcome " + name, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }

        startActivity(intent);
    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    class AuthUser extends AsyncTask<String, Void, String> {
        String stremail = "", strpass = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog = progressDialog.show(context, "Please wait...", "Authenticating User", true, false);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Info.url + "VerifyUser.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                stremail = params[0];
                strpass = params[1];
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postdata = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(stremail,"UTF-8")+"&"+
                        URLEncoder.encode("pass","UTF-8")+"="+ URLEncoder.encode(strpass,"UTF-8");
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
            if(s.equals(null)){
                Toast.makeText(context,"Data Not Loading",Toast.LENGTH_SHORT).show();
            }else{
                if(!s.equals("NAN")){
                    String email,name,pass,mobile;
                    String[] arr = s.split("&");
                    email = arr[0];
                    name = arr[1];
                    mobile = arr[2];
                    pass = arr[3];

                    info.dataBase.insertUser(email, name, mobile, pass);

                    GetAllInfo obj = new GetAllInfo();
                    obj.execute(email, pass);

                }else if(s.equals("NAN")){
                    Info.showSnackBar(email, "Invalid Credentials");
                }else {
                    Info.showSnackBar(email, "Something Wrong" + s);
                }
            }
        }
    }

    void showProgressDialog(){
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog = progressDialog.show(context, "Please wait...", "Requesting user profile", true, false);
        }
    }

    void dismissProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    class GetAllInfo extends AsyncTask<String, Void, String> {
        String stremail = "", strpass = "";

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Info.url + "GetAllInfo.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                stremail = params[0];
                strpass = params[1];
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postdata = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(stremail,"UTF-8")+"&"+
                        URLEncoder.encode("pass","UTF-8")+"="+ URLEncoder.encode(strpass,"UTF-8");
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
            /*if(MyProfile.this.isDetached()) {
                return;
            }*/
            dismissProgressDialog();
            if(s.equals(null)){
                Toast.makeText(context,"Data Not Loading",Toast.LENGTH_SHORT).show();
            }else{
                if(!s.equals("NAN")){
                    String email,name,pass,mobile,office,resi,dob,address,year,branch,nativ,job,company,designation,anniversary;
                    String[] arr = s.split("&");
                    //email = arr[0];
                    name = arr[1];
                    mobile = arr[2];
                    //pass = arr[3];
                    office= arr[4];
                    resi = arr[5];
                    dob = arr[6];
                    address = arr[7];
                    year = arr[8];
                    branch = arr[9];
                    nativ = arr[10];
                    job = arr[11];
                    company = arr[12];
                    designation = arr[13];
                    anniversary = arr[14];

                    info.dataBase.setUserName(name);
                    info.dataBase.setMobile(mobile);
                    info.dataBase.setOffice(office);
                    info.dataBase.setResidence(resi);
                    info.dataBase.setDOB(dob);
                    info.dataBase.setAddress(address);
                    info.dataBase.setYear(year);
                    info.dataBase.setBranch(branch);
                    info.dataBase.setNative(nativ);
                    info.dataBase.setJob(job);
                    info.dataBase.setCompany(company);
                    info.dataBase.setDesignation(designation);
                    info.dataBase.setAnniversary(anniversary);

                    successfullyLoggedIn(name);
                }
            }
        }
    }
}