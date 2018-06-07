package mx.novaterra.mobile.com.appmoviles;

import android.Manifest;
import android.content.Intent;
import mx.novaterra.mobile.com.logic.*;
//
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mx.novaterra.mobile.com.adapter.ChannelAdapter;
import mx.novaterra.mobile.com.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Description.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Description#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Description extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String eventId;

    private OnFragmentInteractionListener mListener;

    Service service;
    TextView txvTitle, txvSch, txvDetails;
    Event event;
    ImageView imgType;
    ProgressBar pb;
    Thread tr;
    FrameLayout flBasicInfo;
    ListView lvChannelList;
    FloatingActionButton fbtnAddToCalendar;

    public Description() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Description.
     */
    // TODO: Rename and change types and number of parameters
    public static Description newInstance(String param1, String param2) {
        Description fragment = new Description();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        service = new Service();
        eventId = String.valueOf(getArguments().getSerializable("id"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        txvTitle = (TextView)view.findViewById(R.id.txvEventTitle);
        txvTitle.setText("");
        txvSch = (TextView)view.findViewById(R.id.txvSchedule);
        txvSch.setText("");
        txvDetails = (TextView)view.findViewById(R.id.txvDetails);
        txvDetails.setText("");
        imgType = (ImageView)view.findViewById(R.id.imgType);
        pb = (ProgressBar)view.findViewById(R.id.pbProgress);
        flBasicInfo = (FrameLayout)view.findViewById(R.id.flBasicInfo);
        lvChannelList = (ListView)view.findViewById(R.id.lvChannelList);
        fbtnAddToCalendar = (FloatingActionButton)view.findViewById(R.id.fbtnAddToCalendar);
        fbtnAddToCalendar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(v.getContext(), "Agregar a calendario", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.VISIBLE);
                if(setCalendarPermission())
                {
                    addToCalendar();
                    pb.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ha habido un problema al intentar agregar este evento a tu calendario",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        try
        {
            tr = new Thread(setLoadingThread(eventId));
            tr.start();
        } catch (Exception ex)
        {
            Log.d("OnCreateView", "Error al iniciar el nuevo hilo");
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if(tr.isAlive())
        {
            try
            {
                tr.wait();
                Log.w("Tr join", "Se espero al hilo " + tr.getName());
            } catch (Exception ex)
            {
                Log.d("Tr wait", "Hilo interrumpido");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Runnable setLoadingThread(final String evId)
    {
        Runnable rn = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    List<Event> temp = new ArrayList<Event>();
                    temp = service.getData(getActivity().getApplicationContext(), evId, 0);
                    event = temp.get(0);

                    switch (event.getType().getId())
                    {
                        case 1:
                            event.getType().setImageResource(R.drawable.americanogrande_rect);
                            event.getType().setColorHex("555EA7");
                            break;
                        case 2:
                            event.getType().setImageResource(R.drawable.soccergrande_rect);
                            event.getType().setColorHex("469234");
                            break;
                        case 3:
                            event.getType().setImageResource(R.drawable.basquetgrande_rect);
                            event.getType().setColorHex("E56C0C");
                            break;
                        case 4:
                            event.getType().setImageResource(R.drawable.baseballgrande_rect);
                            event.getType().setColorHex("E52420");
                            break;
                        case 5:
                            event.getType().setImageResource(R.drawable.musicagrande_rect);
                            event.getType().setColorHex("41BAC1");
                            break;
                        case 6:
                            event.getType().setImageResource(R.drawable.premiosgrande_rect);
                            event.getType().setColorHex("D1C103");
                            break;
                        case 7:
                            event.getType().setColorHex("BBBBBB");
                            break;
                        default:
                            break;
                    }

                    event.setChannelList(service.getData(getActivity().getApplicationContext(), evId));
                    Log.d("Canales econtrados", String.valueOf(event.getChannelList().size()));
                }
                catch (Exception ex)
                {
                    Log.e("Obtención de datos", "No se pudieron obtener los datos");
                    Log.e("Error", ex.getMessage());
                }

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            txvTitle.setText(event.getName().toString());
                            txvDetails.setText(event.getDescription().toString());
                            txvSch.setText(String.valueOf(android.text.format.DateFormat.format("dd MMMM yyyy hh:mm a",
                                    event.getDate())));
                            imgType.setImageResource(event.getType().getImageResource());
                            flBasicInfo.setBackgroundColor(Color.parseColor("#" + event.getType().getColorHex()));

                            getActivity().setTitle("Detalles de evento");

                            ChannelAdapter ca = new ChannelAdapter(getActivity().getApplicationContext(),
                                    event.getChannelList());
                            lvChannelList.setAdapter(ca);

                            pb.setVisibility(View.GONE);
                        }
                        catch (Exception ex)
                        {
                            pb.setVisibility(View.GONE);
                            String msg = "";
                            if(event == null)
                                msg = "Ha habido un problema al obtener los datos." +
                                        "Verifica tu conexión a internet.";
                            else
                                msg = "Error al asignar datos";

                            Toast.makeText(getActivity().getApplicationContext(),
                                    msg, Toast.LENGTH_LONG).show();
                            Log.d("Obtención de datos", "Error al obtener datos");
                            Log.e("Obtención de datos", ex.getMessage());
                        }
                    }
                });
            }
        };

        return rn;
    }

    private void addToCalendar() {
        try {
            Calendar beginTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();

            beginTime.setTime(event.getDate());
            endTime.setTime(event.getDateEnd());

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, event.getName())
                    .putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription());
            startActivity(intent);
        }
        catch(Exception ex)
        {
            Toast.makeText(getContext(), "Ha habido un problema al agregar el evento al calendario",
                    Toast.LENGTH_LONG).show();
            Log.e("Add to calendar", ex.getMessage());
        }
    }

    private boolean setCalendarPermission()
    {
        try
        {
            if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                            Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_CALENDAR))
                {

                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] {Manifest.permission.WRITE_CALENDAR}, 0x1);
                }

                return true;
            }
            else
                return true;
        }
        catch (Exception ex)
        {
            Log.e("setCalendarPermission", "Ha habido un problema");
            Log.e("Mensaje", ex.getMessage());
            return false;
        }
    }
}
