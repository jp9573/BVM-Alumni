package jaypatel.co.in.bvmalumni.SignIn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import jaypatel.co.in.bvmalumni.Info;
import jaypatel.co.in.bvmalumni.R;

public class ForgotPassword extends AppCompatActivity {

    EditText email,mobile;
    static Context context;
    ProgressDialog progressDialog;
    Animation animBounce, animSlideInRight;
    Button btnRequestPass;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context = this;

        email = (EditText) findViewById(R.id.fp_email);
        mobile = (EditText) findViewById(R.id.fp_mobile);
        btnRequestPass = (Button) findViewById(R.id.fp_requestPassword);
        heading = (TextView) findViewById(R.id.fp_Heading);

        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animSlideInRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);

        btnRequestPass.setAnimation(animSlideInRight);
        heading.setAnimation(animBounce);
    }

    public void requestPassword(View view) {
        InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        String strEmail = email.getText().toString().toLowerCase();
        String strMobile = mobile.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            email.setError("Please enter valid Email ID");
        }else if(strMobile.length() != 10) {
            mobile.setError("Please enter 10 digit Mobile no");
        }else {
            if(Info.isNetworkAvailable(context)) {
                AuthUser obj = new AuthUser();
                obj.execute(strEmail, strMobile);
            }else {
                Info.showNetworkFailuireToast(context);
            }
        }
    }

    class AuthUser extends AsyncTask<String, Void, String> {
        String stremail = "", strmobile = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog = progressDialog.show(context, "Please wait...", "Requesting Password for you!", true, false);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Info.url + "ForgotPassword.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                stremail = params[0];
                strmobile = params[1];
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postdata = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(stremail,"UTF-8")+"&"+
                        URLEncoder.encode("mobile","UTF-8")+"="+ URLEncoder.encode(strmobile,"UTF-8");
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
                if(s.equals("Done")){
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Check your email!");
                    alertDialog.setMessage("Your password is successfully emailed to "+ stremail);
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
                            onBackPressed();
                        }
                    });
                }else if(s.equals("NAN")){
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Oops!");
                    alertDialog.setMessage("It seems that you are not a member of BVM Alumni!\nPlease register first.");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
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
                        onBackPressed();
                        }
                    });
                }else {
                    Info.showSnackBar(email, "Something Wrong" + s);
                }
            }
        }
    }
}
