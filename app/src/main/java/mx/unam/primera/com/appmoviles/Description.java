package mx.unam.primera.com.appmoviles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.unam.primera.com.logic.Service;
import mx.unam.primera.com.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Description.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Description#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Description extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    String[] images = {"americanogrande.png", "soccergrande.png", "basquetgrande.png",
            "baseballgrande.png", "musicalgrande.png", "premiogrande.png"};
    Service service;
    TextView txvTitle, txvSch, txvDetails;
    Event event;
    ImageView imgType;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        txvTitle = (TextView)view.findViewById(R.id.txvEventTitle);
        txvSch = (TextView)view.findViewById(R.id.txvSchedule);
        txvDetails = (TextView)view.findViewById(R.id.txvDetails);
        imgType = (ImageView)view.findViewById(R.id.imgType);
        setLoadingThread("1705051a12f");

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

    private void setLoadingThread(final String evId)
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        List<Event> temp = new ArrayList<Event>();
                        try
                        {
                            temp = service.getData(getActivity().getApplicationContext(), evId, 0);
                            event = temp.get(0);
                            txvTitle.setText(event.getName().toString());
                            txvDetails.setText(event.getDescription().toString());


                            txvSch.setText(String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a",
                                    event.getDate())));

                            switch (event.getType().getId())
                            {
                                case 1:
                                    imgType.setImageResource(R.drawable.americanogrande);
                                    break;
                                case 2:
                                    imgType.setImageResource(R.drawable.soccergrande);
                                    break;
                                case 3:
                                    imgType.setImageResource(R.drawable.basquetgrande);
                                    break;
                                case 4:
                                    imgType.setImageResource(R.drawable.baseballgrande);
                                    break;
                                case 5:
                                    imgType.setImageResource(R.drawable.musicalgrande);
                                    break;
                                case 6:
                                    imgType.setImageResource(R.drawable.premiogrande);
                                    break;
                                default:
                                    break;
                            }

                            //pb.setVisibility(View.GONE);
                        }
                        catch (Exception ex)
                        {
                            Log.e("Obtención de datos", "No se pudieron obtener los datos");
                            Log.e("Error", ex.getMessage());
                        }
                    }
                });
            }
        };
        tr.start();
    }
}
