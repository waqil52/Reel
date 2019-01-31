package com.example.user.movie_project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. Se
            //            // here to request the missing permissions, and then overriding
            //            //   public void onRequestPermissionsResult(int requestCode,e the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mMap.clear();
                            cinemaHallList();
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            //List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
                            //String list = address.get(0).getLocality()+",";
                            //list += address.get(0).getCountryName();
                            CircleOptions circleOptions = new CircleOptions();
                            circleOptions.center(latLng);
                            circleOptions.radius(10000);
                            circleOptions.strokeColor(Color.BLACK);
                            circleOptions.fillColor(0x30ff0000);
                            circleOptions.strokeWidth(2);
                            mMap.addCircle(circleOptions);
                            mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title("Current Location"));
//                                mMap.addMarker(new MarkerOptions().position(latLng).title(list).
//                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
        }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mMap.clear();
                            cinemaHallList();
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            //List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
                            //String list = address.get(0).getLocality()+",";
                            //list += address.get(0).getCountryName();
                            CircleOptions circleOptions = new CircleOptions();
                            circleOptions.center(latLng);
                            circleOptions.radius(10000);
                            circleOptions.strokeColor(Color.BLACK);
                            circleOptions.fillColor(0x30ff0000);
                            circleOptions.strokeWidth(2);
                            mMap.addCircle(circleOptions);
                            mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title("Current Location"));
//                                mMap.addMarker(new MarkerOptions().position(latLng).title(list).
//                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        cinemaHallList();

        /*Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));   */
    }
    public void cinemaHallList()
    {
        LatLng balaka = new LatLng(23.733463, 90.385106);
        mMap.addMarker(new MarkerOptions().position(balaka)
                .title("Balaka Cine World"));

        LatLng starcineplex = new LatLng(23.751233, 90.390235);
        mMap.addMarker(new MarkerOptions().position(starcineplex)
                .title("Star Cineplex Basundhara"));

        LatLng blockbuster = new LatLng(23.814128, 90.423529);
        mMap.addMarker(new MarkerOptions().position(blockbuster)
                .title("BlockBuster Cinemas"));

        LatLng bgbsyl = new LatLng(24.910105, 91.840796);
        mMap.addMarker(new MarkerOptions().position(bgbsyl)
                .title("BGB Auditorium Sylhet"));

        LatLng nanditasyl = new LatLng(24.890856, 91.864035);
        mMap.addMarker(new MarkerOptions().position(nanditasyl)
                .title("Nandita Cinema Hall"));

        LatLng mohonhab = new LatLng(24.393021, 91.407553);
        mMap.addMarker(new MarkerOptions().position(mohonhab)
                .title("Mohon Cinema Hall"));

        LatLng nilkanthahab  = new LatLng(24.207733, 91.520297);
        mMap.addMarker(new MarkerOptions().position(nilkanthahab)
                .title("Nil Kantha Cinema Hall"));

        LatLng victoriasree = new LatLng(24.314865, 91.731768);
        mMap.addMarker(new MarkerOptions().position(victoriasree)
                .title("Victoria Movie Theater"));

        LatLng noaphab = new LatLng(24.176330, 91.045333);
        mMap.addMarker(new MarkerOptions().position(noaphab)
                .title("Noapara Cinema Hall"));

        LatLng almasctg = new LatLng(22.351336, 91.825588);
        mMap.addMarker(new MarkerOptions().position(almasctg)
                .title("Almas Cinema Hall"));

        LatLng silverscreenctg = new LatLng(22.367189, 91.824235);
        mMap.addMarker(new MarkerOptions().position(silverscreenctg)
                .title("Silver Screen"));
    }
}