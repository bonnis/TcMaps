package com.example.tcmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.idGo:sembunyikanKeyboard(view);
                    gotoLokasi();break;
                case R.id.idCari:goCari();break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.norm :mMap.setMapType((GoogleMap.MAP_TYPE_NORMAL));
            case R.id.terrain :mMap.setMapType((GoogleMap.MAP_TYPE_TERRAIN));
            case R.id.satellite :mMap.setMapType((GoogleMap.MAP_TYPE_SATELLITE));
            case R.id.hybrid :mMap.setMapType((GoogleMap.MAP_TYPE_HYBRID));
            case R.id.no :mMap.setMapType((GoogleMap.MAP_TYPE_NONE));
        }
        return super.onOptionsItemSelected(item);
    }

    private void sembunyikanKeyboard(View v){
        InputMethodManager a = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private void gotoLokasi(){
        EditText lat = (EditText) findViewById(R.id.idLokasiLat);
        EditText lng = (EditText) findViewById(R.id.idLokasiLng);
        EditText zoom = (EditText) findViewById(R.id.idZoom);

        Double dbllat = Double.parseDouble(lat.getText().toString());
        Double dbllng = Double.parseDouble(lng.getText().toString());
        Float dblzoom = Float.parseFloat(zoom.getText().toString());

        Toast.makeText(this,"Move to lat:" +dbllat + " Long:"+dbllng,Toast.LENGTH_LONG).show();
        gotoPeta(dbllat,dbllng,dblzoom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button go = (Button) findViewById(R.id.idGo);
        Button cari = (Button) findViewById(R.id.idCari);
        go.setOnClickListener(op);
        cari.setOnClickListener(op);
    }

    private void hitungJarak(double latAsal, Double lngAsal, double latTujuan, double lngTujuan){
        Location asal = new Location("asal");
        Location tujuan = new Location("tujuan");

        tujuan.setLatitude(latTujuan);
        tujuan.setLongitude(lngTujuan);
        asal.setLatitude(latAsal);
        asal.setLongitude(lngTujuan);
        float jarak = (float) asal.distanceTo(tujuan)/1000;
        String jaraknya = String.valueOf(jarak);
        Toast.makeText(getBaseContext(),"Jarak : " + jaraknya + " km",Toast.LENGTH_LONG).show();
    }

    private void goCari(){
        EditText tempat = (EditText) findViewById(R.id.idDaerah);
        Geocoder g = new Geocoder(getBaseContext());
        try {
            List<android.location.Address> daftar = g.getFromLocationName(tempat.getText().toString(),1);
            Address alamat = daftar.get(0);

            String nemuAlamat = alamat.getAddressLine(0);
            Double lintang = alamat.getLatitude();
            Double bujur = alamat.getLongitude();

            Toast.makeText(getBaseContext(),"Ketemu " + nemuAlamat,Toast.LENGTH_LONG).show();

            EditText zoom = (EditText) findViewById(R.id.idZoom);
            Float dblzoom = Float.parseFloat(zoom.getText().toString());
            Toast.makeText(this,"Move to " +nemuAlamat+" Lat:" +lintang+ " Long:"+bujur,Toast.LENGTH_LONG).show();
            gotoPeta(lintang,bujur,dblzoom);

            EditText lat = (EditText) findViewById(R.id.idLokasiLat);
            EditText lng = (EditText) findViewById(R.id.idLokasiLng);

            Double dbllat = Double.parseDouble(lat.getText().toString());
            Double dbllng = Double.parseDouble(lng.getText().toString());
            hitungJarak(dbllat,dbllng,lintang,bujur);

            lat.setText(lintang.toString());
            lng.setText(bujur.toString());
        } catch (IOException e) {
            e.printStackTrace();
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

        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.2819705, 112.795323);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS,15));

        EditText lat = (EditText) findViewById(R.id.idLokasiLat);
        EditText lng = (EditText) findViewById(R.id.idLokasiLng);
        EditText zoom = (EditText) findViewById(R.id.idZoom);

        lat.setText("-7.2819705");
        lng.setText("112.795323");
        zoom.setText("15");
    }

    private void gotoPeta(Double lat, Double lng, float z) {
        LatLng Lokasibaru = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(Lokasibaru).title("Marker in " +lat+":"+lng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Lokasibaru,z));
    }
}
