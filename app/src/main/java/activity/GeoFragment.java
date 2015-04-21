package activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pierre.tan.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GeoFragment extends Fragment{



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_geo, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View searchContainer = getActivity().findViewById(R.id.search_container);
        final EditText toolbarSearchView = (EditText) getActivity().findViewById(R.id.search);
        ImageView searchClearButton = (ImageView) getActivity().findViewById(R.id.search_clear);
        searchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchView.setText("");
            }
        });
        searchContainer.setVisibility(View.GONE);

        

    }

}
