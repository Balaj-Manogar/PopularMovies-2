package baali.nano.utils;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpUtils
{
    public HttpUtils()
    {
    }

    @NonNull
    public HttpURLConnection getConnection(String location)
    {
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(location);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
        }
        catch (MalformedURLException | ProtocolException e ) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection(HttpURLConnection connection)
    {
        if (connection != null) {

            connection.disconnect();
        }
    }

    public InputStream getInputStream(HttpURLConnection connection)
    {
        InputStream is = null;
        try {
            is = connection.getInputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return is;
    }

    @NonNull
    public BufferedReader getReader(InputStream is)
    {
        return new BufferedReader(new InputStreamReader(is));
    }

    public void closeReader(BufferedReader reader)
    {
        try {
            if (reader != null) {
                reader.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public static OkHttpClient.Builder getDebugBuilder()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        return httpClient;
    }

}