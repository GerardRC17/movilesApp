package mx.unam.primera.com.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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
                    Log.w("Tipo de Ev", "Excepción al agregar el tipo de evento");
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
            if(da.getStatus() != AsyncTask.Status.FINISHED)
                da.cancel(true);
            if(json.length() > 0)
                Log.d("Estado", "Exito :D");
            else
                Log.d("Estado", "No hay datos en la cadena JSON");
            Log.d("JSON ", json);
        }
        catch (TimeoutException tex)
        {
            Log.e("TimeoutException", "Tiempo excedido al conectar");
        }
        catch (CancellationException cex)
        {
            Log.e("CancellationException", "Error al conectar con el servidor");
        }
        catch (Exception ex)
        {
            Log.e("Exception", "Error con tarea asíncrona");
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
                    Log.d("Datos JSON", "No se encontraron datos");
                    return null;
                }
            } catch (Exception ex)
            {
                Log.e("JSON Exception", "Error al leer JSON/Agregar objetos a la lista de eventos");
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
            if(da.getStatus() != AsyncTask.Status.FINISHED)
                da.cancel(true);
            if(json.length() > 0)
                Log.d("Estado", "Exito :D");
            else
                Log.d("Estado", "No hay datos en la cadena JSON");
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
                    Log.d("Datos JSON", "No se encontraron datos");
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
