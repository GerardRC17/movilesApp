package mx.unam.primera.com.logic;

import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import mx.unam.primera.com.model.Event;

/**
 * Created by Samuel on 12/05/2017.
 */

public class Service
{
    Event event;
    public String getEvent(String ev_id)
    {
        event = new Event();
        String line = "";
        int response = 0;
        StringBuilder result = null;

        try
        {
            // Cambiar por dirección web
            String strUrl = "http://192.168.1.64/MovilesWebService/scripts/service/req_events.php?evId=" +
                    ev_id;
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            response = connection.getResponseCode();
            result = new StringBuilder();

            if(response == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
            }
        }
        catch (Exception ex)
        {
            //Log.println(1, "", "Error en leer o conectar: ");
            //Log.d("D", ex.getMessage().toString());
        }

        return result.toString();
    }

    public boolean isReqEmpty(String response)
    {
        try
        {
            JSONArray json = new JSONArray(response);
            if(json.length() > 0)
                return true;
        }
        catch (Exception e)
        {
            return false;
        }
        return false;
    }
}
