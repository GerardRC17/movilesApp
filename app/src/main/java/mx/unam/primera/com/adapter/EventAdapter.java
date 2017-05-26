package mx.unam.primera.com.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.unam.primera.com.appmoviles.Description;
import mx.unam.primera.com.appmoviles.R;
import mx.unam.primera.com.model.Event;

/**
 * Created by Samuel on 25/05/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{
    private List<Event> _events;

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imagen;
        public TextView general_description;
        public RelativeLayout rvEvent;

        public EventViewHolder(View v)
        {
            super(v);
            imagen = (ImageView)v.findViewById(R.id.Recycler_images);
            general_description =(TextView)v.findViewById(R.id.txtGeneral_description);
            rvEvent = (RelativeLayout)v.findViewById(R.id.rvEvent);
        }

        /*@Override
        public void onClick(View v)
        {
            Toast.makeText(v.getContext(), String.valueOf(v.getId()),Toast.LENGTH_SHORT).show();
        }*/
    }
    public EventAdapter(List<Event> items)
    {
        this._events = items;
    }

    @Override
    public int getItemCount()
    {
       return  _events.size();

    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.events_card, viewGroup, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder viewHolder,int i)
    {
        int resource = 0;
        switch (_events.get(i).getType().getId())
        {
            case 1:
                resource = R.drawable.americano2;
                break;
            case 2:

                break;

            default:
                resource = R.drawable.americano2;
                break;
        }

        viewHolder.imagen.setImageResource(resource);
        viewHolder.general_description.setText(_events.get(i).getName());
        viewHolder.rvEvent.setTag(_events.get(i).getId());
        viewHolder.rvEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(), String.valueOf(v.getTag()),Toast.LENGTH_SHORT).show();
                //fragment=new americano();
                //fragmentosSelec=true;
                Fragment fragment = new Description();
                String id = String.valueOf(v.getTag());
                //fragment.
            }
        });
    }

}

