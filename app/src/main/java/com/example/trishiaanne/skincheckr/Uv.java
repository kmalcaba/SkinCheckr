package com.example.trishiaanne.skincheckr;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Uv extends AppCompatActivity implements LocationListener {
    private TextView uvIndex, uvDate, uvLoc;
    private LocationManager locationManager;
    double latitude;
    double longitutde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uv);

        uvIndex = findViewById(R.id.uvIndex);
        uvDate = findViewById(R.id.uvDate);
        uvLoc = findViewById(R.id.uvLoc);

        findUvIndex();
    }

    private void findUvIndex() {
        getGPS();
        String url = "http://api.openweathermap.org/data/2.5/uvi?appid=9acd5cafe1888d79a02ca97c69497737" +
                "&lat=" + latitude + "&lon=" + longitutde;

        displayMessage(getApplicationContext(), "User Lat and Long: " + longitutde + "\n" + latitude);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Get the UV Index
                    Double uvI = response.getDouble("value");

                    displayMessage(getApplicationContext(),"UV INDEX: " + uvI);
                    uvIndex.setText(uvI.toString());

                    //Get the City and Country Name
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitutde, 1);
                    String cityName = addresses.get(0).getAddressLine(0);

                    String address = cityName;

                    uvLoc.setText(address);

                    //Get Date
                    Calendar dateToday = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(dateToday.getTime());

                    uvDate.setText(currentDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    private void getGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(loc);
    }

    @Override
    public void onLocationChanged(Location location) {
        longitutde = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
