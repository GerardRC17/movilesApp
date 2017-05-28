package mx.unam.primera.com.adapter;

import android.content.Context;
import android.os.Bundle;
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
import mx.unam.primera.com.appmoviles.MainActivity;
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
        public TextView txvTitle, txvDescription;
        public RelativeLayout rvEvent;

        public EventViewHolder(View v)
        {
            super(v);
            imagen = (ImageView)v.findViewById(R.id.Recycler_images);
            txvTitle =(TextView)v.findViewById(R.id.txvTitle);
            txvDescription = (TextView)v.findViewById(R.id.txvDescription);
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
                resource = R.drawable.americano;
                break;
            case 2:
                resource = R.drawable.soccer;
                break;

            case 3:
                resource = R.drawable.basketball;
                break;

            case 4:
                resource = R.drawable.baseball;
                break;

            case 5:
                resource = R.drawable.musica;
                break;

            case 6:
                resource = R.drawable.premios;
                break;

            default:
                resource = R.drawable.gen;
                break;
        }

        viewHolder.imagen.setImageResource(resource);
        viewHolder.txvTitle.setText(_events.get(i).getName());
        viewHolder.txvDescription.setText(_events.get(i).getDescription());
        viewHolder.rvEvent.setTag(_events.get(i).getId());
        viewHolder.rvEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(), String.valueOf(v.getTag()),Toast.LENGTH_SHORT).show();
                String id = String.valueOf(v.getTag());
                navigate(v, id);
            }
        });
    }

    private void navigate(View v, String id)
    {
        Fragment fragment = new Description();
        Bundle args = new Bundle();

        args.putSerializable("id", id);
        fragment.setArguments(args);
        switchContent(R.id.principal, fragment, v);
    }

    private void switchContent(int fragmentId, Fragment fragment, View v)
    {
        if(v.getContext() == null)
            return;

        if(v.getContext() instanceof MainActivity)
        {
            MainActivity main = (MainActivity)v.getContext();
            Fragment frag = fragment;
            main.navigate(frag);
        }
    }

}

