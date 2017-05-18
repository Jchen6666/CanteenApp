package com.example.jerrychen.canteenapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {
    public static final String PREF_FILE_NAME = "loginPref";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    private SharedPreferences preferences;
    private EditText usernameField;
    private EditText passwordField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences=getSharedPreferences(PREF_FILE_NAME,MODE_PRIVATE);
        usernameField=(EditText)findViewById(R.id.username);
        passwordField=(EditText)findViewById(R.id.password);
        String username = preferences.getString(USERNAME, null);
        String password = preferences.getString(PASSWORD, null);
        if (username != null && password != null) {
            usernameField.setText(username);
            passwordField.setText(password);
        }
    }


    public void ButtonClicked(View view) {

        String username= usernameField.getText().toString();
        String password =passwordField.getText().toString();
        CheckBox checkBox=(CheckBox)findViewById(R.id.remember) ;
        SharedPreferences.Editor editor=preferences.edit();
        if(checkBox.isChecked()){
            editor.putString(USERNAME,username);
            editor.putString(PASSWORD,password);
        }
        else {
            editor.remove(USERNAME);
            editor.remove(PASSWORD);
        }
        editor.apply();
        LoginTask task =new LoginTask();
        task.execute("http://anbo-canteen.azurewebsites.net/Service1.svc/customers/"+username+"/"+password);


    }
   private class LoginTask extends AsyncTask<String,Void,String> {
       @Override

       protected  String doInBackground(String ...urls){
           try{
               String result=ReadJsonFeed.readJSonFeed(urls[0]);
               return  result;
               //return readJSonFeed(urls[0]);
           }catch (IOException ex){
               Log.e("Error", ex.toString());
               cancel(true);
               return ex.toString();
           }
       }

       @Override
       protected  void onPostExecute(String result){

           TextView textView=(TextView)findViewById(R.id.login_message);
           try{
               JSONObject jsonObject=new JSONObject(result);
               final String firstname=jsonObject.getString("Firstname");
               final String lastname=jsonObject.getString("Lastname");
               final String email=jsonObject.getString("Email");
               final String password=jsonObject.getString("Password");
               final int id =jsonObject.getInt("Id");
               User user=new User(id,firstname,lastname,email,password);
               Intent intent=new Intent(getBaseContext(),MainActivity.class);
               intent.putExtra("USER",id);
                startActivity(intent);
               textView.setText("succeeded");
           }catch (JSONException ex){
               //TextView textView=(TextView)findViewById(R.id.login_message);
               textView.setText("Wrong Username or Password");

           }
       }
       @Override
       protected void onCancelled(String message){
           super.onCancelled(message);
           TextView textView=(TextView)findViewById(R.id.login_message);
           textView.setText(message);
       }
   }

}

