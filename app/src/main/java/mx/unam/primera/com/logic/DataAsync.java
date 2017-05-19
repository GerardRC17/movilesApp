package mx.unam.primera.com.logic;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import mx.unam.primera.com.model.Event;

/**
 * Created by Samuel on 16/05/2017.
 */

public class DataAsync extends AsyncTask<Event, Void, String>
{
    @Override
    protected String doInBackground(Event... params)
    {
        Event event = params[0];
        String line = "";
        int response = 0;
        StringBuilder result = null;

        try
        {
            // Cambiar por dirección web
            String strUrl =
                    //"http://192.168.1.64/MovilesWebService/scripts/service/rest/req_events.php?evId="
                    "http://livebr.esy.es/scripts/service/rest/req_events.php?evId="
                            //"Dirección web"
                            + event.getId().toString()
                            + "&tpId=" + String.valueOf(event.getType().getId());
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            result = new StringBuilder();
            try
            {
                response = connection.getResponseCode();
            }
            catch (Exception e)
            {
                Log.e("Conexion", e.getMessage());
                return null;
            }

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
            Log.d("D", ex.getMessage().toString());
            return null;
        }

        return result.toString();
    }
}
