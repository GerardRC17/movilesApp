package mx.unam.primera.com.appmoviles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.util.DebugUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import mx.unam.primera.com.logic.DataAsync;
import mx.unam.primera.com.logic.Service;
import mx.unam.primera.com.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link soccer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link soccer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class soccer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    List<Event> events;
    Service service;

    public soccer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment soccer.
     */
    // TODO: Rename and change types and number of parameters
    public static soccer newInstance(String param1, String param2) {
        soccer fragment = new soccer();
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
        events = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_soccer, container, false);
        getData(null, 1);

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

    public void getData(String id, int type)
    {
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
            Toast.makeText(getActivity().getApplicationContext(), "Exito :D", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity().getApplicationContext(), json, Toast.LENGTH_SHORT).show();
            Log.d("JSON ", json);
        }
        catch (TimeoutException tex)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Tiempo excedido al conectar", Toast.LENGTH_SHORT).show();
        }
        catch (CancellationException cex)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Error con tarea asíncrona", Toast.LENGTH_SHORT).show();
        }
        finally
        {
            try
            {
                if (json.trim() != "")
                    events = service.getEventsList(json);
                else
                    Toast.makeText(getActivity().getApplicationContext(), "No se encontraron datos", Toast.LENGTH_SHORT).show();
            } catch (Exception ex)
            {
                Log.d("JSON", "Error al leer JSON/Agregar objetos a la lista de eventos");
            }
            Log.d("Long lista de eventos", String.valueOf(events.size()));
        }
    }
}
