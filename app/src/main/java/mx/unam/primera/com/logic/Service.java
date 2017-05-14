package mx.unam.primera.com.logic;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            String strUrl =
                    "http://192.168.1.64/MovilesWebService/scripts/service/req_events.php?evId="
                    //"Dirección web"
                    //+ ev_id;
                    + "null";
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

    public List<Event> getEventsList(String strJson)
    {
        List<Event> events = new ArrayList<>();
        try
        {
            JSONArray json = new JSONArray(strJson);
            Event event;
            for (int i = 0; i < json.length(); i++)
            {
                event = new Event();
                JSONObject ob = json.getJSONObject(i);
                event.setId(ob.getString("ev_id"));
                event.setName(ob.getString("ev_name"));
                String strDate = ob.getString("ev_sch");
                strDate = strDate.replace("-", "/");
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                event.setDate(format.parse(strDate));
                events.add(event);
            }
        }
        catch (Exception ex)
        {
            Log.d("Error al deserializar", "Algo salio mal :S -- ");
            Log.e("Deserializar", ex.getMessage().toString());
        }
        return events;
    }
}
