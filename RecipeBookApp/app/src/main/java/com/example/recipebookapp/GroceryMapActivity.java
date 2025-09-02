package com.example.recipebookapp;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * GroceryMapActivity
 * Shows a Google Map centered on Athlone with a marker.
 */
public class GroceryMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // Coordinates for Athlone
    private static final LatLng TARGET_LOCATION = new LatLng(53.4239, -7.9407);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_map);

        // Obtain the SupportMapFragment and get notified when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Called when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker on Athlone and move the camera
        mMap.addMarker(new MarkerOptions().position(TARGET_LOCATION).title("Target Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TARGET_LOCATION, 15));
    }
}
