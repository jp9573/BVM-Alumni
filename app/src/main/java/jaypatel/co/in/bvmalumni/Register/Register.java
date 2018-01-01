package jaypatel.co.in.bvmalumni.Register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

public class Register extends AppCompatActivity {

    EditText email, name, mobile, pass, confPass;
    Context context;
    TextView heading;
    Button btnRegisterNext;
    Animation animBounce, animSlideInRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.mobile);
        pass = (EditText) findViewById(R.id.pass);
        confPass = (EditText) findViewById(R.id.confPass);
        heading = (TextView) findViewById(R.id.register1Head);
        btnRegisterNext = (Button) findViewById(R.id.reg_next);
        context = this;

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    pass.setHint("Password must be 8 character long");
                }
            }
        });

        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animSlideInRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);

        btnRegisterNext.setAnimation(animSlideInRight);
        heading.setAnimation(animBounce);
    }

    public void next(View view) {
        InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        String strEmail = email.getText().toString().toLowerCase();
        String strName = name.getText().toString().trim();
        String strMobile = mobile.getText().toString();
        String strPass = pass.getText().toString();
        String strConfPass = confPass.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            email.setError("Please enter valid Email ID");
        }else if(strName.length() <= 0) {
            name.setError("Please enter valid Name");
        }else if(strMobile.length() <= 0) {
            mobile.setError("Plase enter a valid mobile no");
        }else if(strPass.length() < 8) {
            pass.setError("Please enter minimum 8 character long password");
        }else if(!strConfPass.equals(strPass)) {
            confPass.setError("Passwords doesn't match");
        }else {
            if(Info.isNetworkAvailable(context)) {
                AuthUser obj = new AuthUser();
                obj.execute(strEmail);
            }else {
                Info.showNetworkFailuireToast(context);
            }
        }
    }

    class AuthUser extends AsyncTask<String, Void, String> {
        String stremail = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog = progressDialog.show(context, "Please wait...", "Checking Information", true, false);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Info.url + "isUserRegisterd.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                stremail = params[0];
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postdata = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(stremail,"UTF-8");
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
                if(s.equals("NAN")){
                    String strEmail = email.getText().toString().toLowerCase();
                    String strName = name.getText().toString().trim();
                    String strMobile = mobile.getText().toString();
                    String strPass = pass.getText().toString();

                    Intent i = new Intent(context, Register2.class);
                    i.putExtra("email", strEmail);
                    i.putExtra("name", strName);
                    i.putExtra("mobile", strMobile);
                    i.putExtra("pass", strPass);
                    startActivityForResult(i, 1);
                }else if(s.equals("Registered")){
                    Info.showSnackBar(email, "This email id is already registered!");
                }else {
                    Info.showSnackBar(email, "Something Wrong" + s);
                }
            }
        }
    }
}
