package mx.unam.primera.com.appmoviles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.unam.primera.com.adapter.EventAdapter;
import mx.unam.primera.com.logic.Service;
import mx.unam.primera.com.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link especiales.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link especiales#newInstance} factory method to
 * create an instance of this fragment.
 */
public class especiales extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager IManeger;

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

    public especiales() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment especiales.
     */
    // TODO: Rename and change types and number of parameters
    public static especiales newInstance(String param1, String param2) {
        especiales fragment = new especiales();
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
        View view = inflater.inflate(R.layout.fragment_especiales, container, false);

        recycler =(RecyclerView) view.findViewById(R.id.reciclador);
        //recycler.setHasFixedSize(true);

        IManeger = new LinearLayoutManager(getActivity().getApplicationContext());
        recycler.setLayoutManager(IManeger);


        Thread tr = setLoadingThread();
        tr.start();

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

    private Thread setLoadingThread()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    events = service.getData(getActivity().getApplicationContext(), null, 7);
                    Log.d("Eventos encontrados", String.valueOf(events.size()));
                }
                catch (Exception ex)
                {
                    Log.d("Thread tr", "Ha ocurrido un error al intentar cargar los datos");
                    Log.e("Error Thread", ex.getMessage());
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //pb.setVisibility(View.GONE);
                        try
                        {
                            if(events != null)
                            {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        String.valueOf(events.size()), Toast.LENGTH_SHORT).show();
                                // Aquí va el código para cargar la lista
                                adapter = new EventAdapter(events);
                                recycler.setAdapter(adapter);

                            }
                            else
                            {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception ex)
                        {
                            String msg = "";
                            if(events == null)
                                msg = "Ha habido un problema. Verifica tu conexión a internet";
                            else
                                msg = "Ha habido un problema";

                            Toast.makeText(getActivity().getApplicationContext(),
                                    msg, Toast.LENGTH_LONG).show();
                            Log.e("Mensaje de error", ex.getMessage());
                        }
                    }
                });
            }
        };
        return tr;
    }
}
