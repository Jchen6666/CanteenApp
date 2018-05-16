package com.example.jerrychen.canteenapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jerrychen on 4/14/17.
 */

public class RatingTask extends AsyncTask<String,Void,CharSequence> {
    @Override
    protected CharSequence doInBackground(String... params) {
        String urlString = params[0];
        String jsonDocumet = params[1];
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(jsonDocumet);
            osw.flush();
            osw.close();
            int responseCode = connection.getResponseCode();
            if (responseCode / 100 != 2) {
                String responseMessage = connection.getResponseMessage();
                throw new IOException("HTTP response code: " + responseCode + " " + responseMessage);
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            return line;
        } catch (MalformedURLException ex) {
            cancel(true);
            String message = ex.getMessage() + " " + urlString;
            //Log.e("Rating", message);
            return message;
        } catch (IOException ex) {
            cancel(true);
            //Log.e("Rating", ex.getMessage());
            return ex.getMessage();
        }
    }

    @Override
    protected void onPostExecute(CharSequence charSequence) {
        super.onPostExecute(charSequence);
        Log.d("MESSAGE","Thank you");


    }

    @Override
    protected void onCancelled(CharSequence charSequence) {
        super.onCancelled(charSequence);
        Log.d("MESSAGE",charSequence.toString());
    }


}
