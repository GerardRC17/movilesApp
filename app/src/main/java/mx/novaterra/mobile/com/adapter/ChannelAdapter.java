package mx.novaterra.mobile.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import mx.novaterra.mobile.com.appmoviles.R;
import mx.novaterra.mobile.com.model.Channel;

/**
 * Created by Samuel on 25/05/2017.
 */

public class ChannelAdapter extends ArrayAdapter<Channel>
{
    public ChannelAdapter(Context context, List<Channel> channels)
    {
        super(context, 0, channels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Obtener inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Comprueba si existe un view actual
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_row, parent, false);
        }

        // Referencias a UI
        ImageView ivChannel = (ImageView)convertView.findViewById(R.id.ivChannelImage);
        TextView txvChannelName = (TextView)convertView.findViewById(R.id.txvChannelName);

        // Lead actual.... ??
        // Objeto actual
        Channel channel = getItem(position);

        // Setup
        Glide.with(getContext()).load(channel.getImageUrl()).into(ivChannel);
        txvChannelName.setText(channel.getName());

        return convertView;
    }
}
