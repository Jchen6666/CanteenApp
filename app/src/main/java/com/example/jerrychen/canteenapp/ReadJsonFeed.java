package com.example.jerrychen.canteenapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jerrychen on 4/7/17.
 */

public class ReadJsonFeed {
    public static String readJSonFeed(String urlString )throws IOException {
        StringBuilder stringBuilder =new StringBuilder();
        final InputStream content =openHttpConnection(urlString);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        while (true){
            final String line=reader.readLine();
            if (line==null)
                break;
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
    public static   InputStream openHttpConnection(final String urlString)
            throws IOException{
        final URL url = new URL(urlString);
        final URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        final HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setAllowUserInteraction(false);
        // No user interaction like dialog boxes, etc.
        httpConn.setInstanceFollowRedirects(true);
        // follow redirects, response code 3xx
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        final int response = httpConn.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            throw new IOException("HTTP response not OK");
        }

    }
}
