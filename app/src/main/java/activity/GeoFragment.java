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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import static java.lang.String.valueOf;


public class GeoFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "basic-location-sample";
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    protected TextView latitude_Text;
    protected TextView longitude_Text;
    protected TextView url_Text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_geo, container, false);
        TextView latitude_text= (TextView) rootView.findViewById(R.id.latitude_text);
        TextView longitude_text= (TextView) rootView.findViewById(R.id.longitude_text);
        TextView url_text= (TextView) rootView.findViewById(R.id.url_text);


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


        buildGoogleApiClient();


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        latitude_Text = (TextView) getView().findViewById((R.id.latitude_text));
        longitude_Text = (TextView) getView().findViewById((R.id.longitude_text));
        url_Text = (TextView) getView().findViewById((R.id.url_text));
        String url = null;
        if (mLastLocation != null) {
            latitude_Text.setText(valueOf(mLastLocation.getLatitude()));
            longitude_Text.setText(valueOf(mLastLocation.getLongitude()));
            String lat = valueOf(mLastLocation.getLatitude());
            lat = lat.replace(".", ",");

            String lon = valueOf(mLastLocation.getLongitude());
            lon = lon.replace(".", ",");

            url = "https://open.tan.fr/ewp/arrets.json/" + lat + "/" + lon + " ";
            Log.d("Url", url);
            url_Text.setText(url);
            Toast.makeText(getActivity(), valueOf(url_Text), Toast.LENGTH_LONG).show();
            String love = url_Text.getText().toString();
        } else {
            Log.d("Url", url);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
}
