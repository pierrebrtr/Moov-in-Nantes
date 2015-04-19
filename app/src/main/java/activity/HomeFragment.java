package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pierre.tan.R;

import java.util.List;

import adapter.CustomListAdapter;
import model.Arrets;
import util.Spfav;


public class HomeFragment extends Fragment {

    ListView favoriteList;
    Spfav sharedPreference;
    List<Arrets> favorites;

    Activity activity;
    CustomListAdapter productListAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        sharedPreference = new Spfav();
        favorites = sharedPreference.getFavorites(activity);

        if (favorites == null) {
            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
        } else {

            if (favorites.size() == 0) {
                Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();


            }


            favoriteList = (ListView) view.findViewById(R.id.list);
            if (favorites != null) {
                productListAdapter = new CustomListAdapter(activity, favorites);
                favoriteList.setAdapter(productListAdapter);
            }


            favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    TextView textView = (TextView) view.findViewById(R.id.lieu);
                    String text = textView.getText().toString();


                    TextView textView2 = (TextView) view.findViewById(R.id.arret);
                    String libelle = textView2.getText().toString();

                    Intent i = new Intent(getActivity(), TempsActivity.class);
                    i.putExtra("text", text);
                    i.putExtra("libelle", libelle);
                    startActivity(i);


                }
            });

            // Inflate the layout for this fragment

        }

        return view;
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public void onDetach() {
        super.onDetach();
    }
}