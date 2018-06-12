package mx.novaterra.mobile.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Button btnGoTo = (Button)convertView.findViewById(R.id.link_btn);

        // Lead actual.... ??
        // Objeto actual
        final Channel channel = getItem(position);

        // Setup
        Glide.with(getContext()).load(channel.getImageUrl()).into(ivChannel);
        txvChannelName.setText(channel.getName());

        if(channel.getBroadcastUrl() != null)
        {
            if(channel.getBroadcastUrl().toString().trim() != "")
            {
                btnGoTo.setVisibility(View.VISIBLE);

                btnGoTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(v.getContext(), channel.getBroadcastUrl().toString(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(channel.getBroadcastUrl().toString()));

                        // Starts Implicit Activity
                        getContext().startActivity(i);
                    }
                });
            }
        } else {
            btnGoTo.setVisibility(View.GONE);
        }

        return convertView;
    }
}
