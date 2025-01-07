package com.example.googlemaps_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform geocoding and update map
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng vehicleLocation = new LatLng(12.992281142011239, 77.53512432366614);
        LatLng routeStartPoint = new LatLng(12.990430252502582, 77.53775963900824);
        LatLng routeEndPoint = new LatLng(12.987088451605654, 77.54035312356125);

        mMap.addMarker(new MarkerOptions().position(vehicleLocation).title("Vehicle Location"));
        mMap.addMarker(new MarkerOptions().position(routeStartPoint).title("Start Point"));
        mMap.addMarker(new MarkerOptions().position(routeEndPoint).title("End Point"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vehicleLocation, 15f));

        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .add(vehicleLocation, routeStartPoint)
                .width(5)
                .color(android.graphics.Color.BLUE));

        Polyline polyline2 = mMap.addPolyline(new PolylineOptions()
                .add(routeStartPoint, routeEndPoint)
                .width(5)
                .color(android.graphics.Color.RED));
    }

    private void searchLocation(String location) {
        if (mMap == null) return; // Ensure map is initialized

        if (location == null || location.isEmpty()) return;

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(location, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
