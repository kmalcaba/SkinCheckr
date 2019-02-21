package com.example.trishiaanne.skincheckr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trishiaanne.skincheckr.dermaSearch.Dermatologist;
import com.example.trishiaanne.skincheckr.dermaSearch.Finder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Derma extends AppCompatActivity implements LocationListener {
    Spinner spinner;
    private List<Dermatologist> dermaList;
    private List<Dermatologist> alphabetical = new ArrayList<>();
    Finder f;
    RecyclerView recyclerView;
    DermaAdapter adapter;

    LocationManager locationManager;
    double longitude = 0.0;
    double latitude = 0.0;
    private int LOCATION_PERMISSION_CODE = 1;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;

    String locality = "";

    private void init() {
        getGPS();
        context = getApplicationContext();
        f = new Finder(2, this);
        initDermaList();
        alphabetical.addAll(dermaList);
    }

    private void initSpinner() {
        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.sort_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: //sort by distance
                        //TODO: Sort by distance
                        dermaList.clear();
                        dermaList.addAll(alphabetical);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        break;
                    case 1: //sort by price low to high
                        f.findByFee(0);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        break;
                    case 2: //sort by price high to low
                        f.findByFee(1);
                        Log.d("", "Sorted by price from high to low");
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        break;
                    case 3: //sort by years of experience
                        f.findByYear();
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        break;
                    default:
                        dermaList.clear();
                        dermaList.addAll(alphabetical);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                initDermaList();
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derma);

        getGPS();

        spinner = findViewById(R.id.spinner_sort);
        swipeRefreshLayout = findViewById(R.id.derma_swipe);

        init();
        //initialize spinner
        initSpinner();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                getGPS();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 200);
            }
        });



    }

    private void initDermaList() {
        dermaList = f.getDermaList();

        Log.d("Derma.java", "Dermalist retrieved");
        initRecyclerView();

    }

    private void findCity() {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + ","
                + longitude + "&key" + R.string.google_maps_key;

        JsonObjectRequest objectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Geocoder geocoder = new Geocoder(Derma.this);
                try {
                    List<Address> matches = geocoder.getFromLocation(latitude, longitude, 1);
                    locality = matches.get(0).getLocality();
                    Toast.makeText(context, "You're in " + matches.get(0).getLocality(), Toast.LENGTH_LONG).show();

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
        queue.add(objectRequest);

    }

    private void getGPS() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(Derma.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            String bestProvider = String.valueOf(locationManager.getBestProvider(new Criteria(), true)).toString();
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if(location == null) {
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                onLocationChanged(location);
                findCity();
            }
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Location access required")
                    .setMessage("Allow SkinCheckr to access your location")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Derma.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(this)
                        .setTitle("Location was successfully granted")
                        .setMessage("Please refresh this page to display the UV Index of your location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            } else {
                displayMessage(getApplicationContext(), "Permission DENIED");
            }
        }
    }

    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.derma_recycler);
        adapter = new DermaAdapter(dermaList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        longitude = location.getLongitude();
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

