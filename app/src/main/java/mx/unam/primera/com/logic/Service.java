package mx.unam.primera.com.logic;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import mx.unam.primera.com.model.Channel;
import mx.unam.primera.com.model.Event;

/**
 * Created by Samuel on 12/05/2017.
 */

public class Service
{
    Event event;
    /*public String getEvent(String ev_id, int tp_id)
    {
        event = new Event();
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
                    + ev_id
                    + "&tpId=" + tp_id;
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
    }*/

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
                event.setDescription(ob.getString("ev_des"));
                String strDate = ob.getString("ev_sch");
                strDate = strDate.replace("-", "/");
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                event.setDate(format.parse(strDate));

                try
                {
                    event.getType().setId(Integer.parseInt(ob.getString("tp_id")));
                    event.getType().setName(ob.getString("tp_name"));
                }
                catch (Exception ex)
                {
                    Log.e("Tipo de Ev", "Error al agregar el tipo de evento");
                }

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

    public List<Channel> getChannelList(String strJson)
    {
        List<Channel> channels = new ArrayList<>();
        try
        {
            JSONArray json = new JSONArray(strJson);
            Channel channel;
            for (int i = 0; i < json.length(); i++)
            {
                channel = new Channel();
                JSONObject ob = json.getJSONObject(i);
                channel.setId(Integer.parseInt(ob.getString("ch_id")));
                channel.setName(ob.getString("ch_name"));
                channel.setAbbreviation(ob.getString("ch_abv"));
                channel.setImageUrl(new URL(ob.getString("ch_img")));

                channels.add(channel);
            }
        }
        catch (Exception ex)
        {
            Log.d("Error al deserializar", "Algo salio mal :S -- ");
            Log.e("Deserializar", ex.getMessage().toString());
        }
        return channels;
    }

    public List<Event> getData(Context context, String id, int type)
    {
        List<Event> events = new ArrayList<>();
        if (id == null)
            id = "null";

        Event ev = new Event();
        ev.setId(id);
        ev.getType().setId(type);
        String json = "";

        try
        {
            DataAsync da = new DataAsync();
            da.execute(ev);
            json = da.get(4, TimeUnit.SECONDS);
            if(json.length() > 0)
                Toast.makeText(context, "Exito :D", Toast.LENGTH_SHORT).show();
            Log.d("JSON ", json);
        }
        catch (TimeoutException tex)
        {
            Toast.makeText(context, "Tiempo excedido al conectar", Toast.LENGTH_SHORT).show();
        }
        catch (CancellationException cex)
        {
            Toast.makeText(context, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(context, "Error con tarea asíncrona", Toast.LENGTH_SHORT).show();
            Log.e("Error tarea", ex.getMessage());
        }
        finally
        {
            try
            {
                if (json.trim() != "")
                    events = getEventsList(json);
                else
                {
                    Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_SHORT).show();
                    return null;
                }
            } catch (Exception ex)
            {
                Log.d("JSON", "Error al leer JSON/Agregar objetos a la lista de eventos");
            }
            Log.d("Long lista de eventos", String.valueOf(events.size()));

            return events;
        }
    }

    public List<Channel> getData(Context context, String id)
    {
        List<Channel> channelList = new ArrayList<Channel>();
        String json = "";

        try
        {
            DataChannelsAsync da = new DataChannelsAsync();
            da.execute(id);
            json = da.get(4, TimeUnit.SECONDS);
            if(json.length() > 0)
                Toast.makeText(context, "Exito :D", Toast.LENGTH_SHORT).show();
            Log.d("JSON ", json);
        }
        catch (TimeoutException tex)
        {
            Toast.makeText(context, "Tiempo excedido al conectar", Toast.LENGTH_SHORT).show();
        }
        catch (CancellationException cex)
        {
            Toast.makeText(context, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(context, "Error con tarea asíncrona", Toast.LENGTH_SHORT).show();
            Log.e("Error tarea", ex.getMessage());
        }
        finally
        {
            try
            {
                if (json.trim() != "")
                    channelList = getChannelList(json);
                else
                {
                    Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_SHORT).show();
                    return null;
                }
            } catch (Exception ex)
            {
                Log.d("JSON", "Error al leer JSON/Agregar objetos a la lista de eventos");
            }
            Log.d("Long lista de eventos", String.valueOf(channelList.size()));

            return channelList;
        }
    }
}
