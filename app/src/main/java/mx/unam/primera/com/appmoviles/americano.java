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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mx.unam.primera.com.logic.Service;
import mx.unam.primera.com.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link americano.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link americano#newInstance} factory method to
 * create an instance of this fragment.
 */
public class americano extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView rv;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public americano() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment americano.
     */
    // TODO: Rename and change types and number of parameters
    public static americano newInstance(String param1, String param2) {
        americano fragment = new americano();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_americano, container, false);
        /*btnTest = (Button) view.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(this);
        txvResult = (TextView) view.findViewById(R.id.txvResult);*/

        rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(llm);

        getData(null);

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

    /*@Override
    public void onClick(View v)
    {

    }*/

    public void getData(String id)
    {
        if (id == null)
            id = "null";

        final String value = id;
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final Service service = new Service();
                final String result = service.getEvent(value);
                Log.d("Resultado", String.valueOf(result));

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(result != null)
                        {
                            boolean r = service.isReqEmpty(result);
                            if (r == true)
                            {
                                Log.i("Encontró valores", String.valueOf(r));
                                //txvResult.setText("Exito");
                                List<Event> events = service.getEventsList(result);

                                if (events != null)
                                {
                                    Toast.makeText(getActivity().getApplicationContext(), "Exito!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity().getApplicationContext(), String.valueOf(events.size()), Toast.LENGTH_SHORT).show();
                                } else
                                {
                                    Toast.makeText(getActivity().getApplicationContext(), ":S", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity().getApplicationContext(), String.valueOf(events.size()), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                                Toast.makeText(getActivity().getApplicationContext(), ":S", Toast.LENGTH_SHORT).show();

                            Log.d("D", result.toString());
                        }
                        else
                        {
                            Log.e("Ha habido un problema", "Resultado es nulo");
                            Toast.makeText(getActivity().getApplicationContext(), "No hay conexión con el servico", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }
}
