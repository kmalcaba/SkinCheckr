package com.example.trishiaanne.skincheckr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trishiaanne.skincheckr.dermaSearch.Dermatologist;
import com.example.trishiaanne.skincheckr.dermaSearch.Finder;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Derma extends AppCompatActivity implements LocationListener {
    Spinner spinner;
    private List<Dermatologist> dermaList;
    private List<Dermatologist> alphabetical = new ArrayList<>();
    Finder f;
    RecyclerView recyclerView;
    DermaAdapter adapter;

    DrawerLayout mDrawerLayout;
    LocationManager locationManager;
    Location location;
    double longitude = 0.0;
    double latitude = 0.0;
    private int LOCATION_PERMISSION_CODE = 1;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;

    String locality = "";
    int city = 5;

    private void init() {

        getGPS();
        context = getApplicationContext();
        findCity();
        Log.d("Locality", locality + "Here");
        f = new Finder(city, this);
//        f = new Finder(locality, this);
        initDermaList();
        alphabetical.addAll(dermaList);
//        displayToolbar();
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
                    case 0: //sort alphabetically
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

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private void initDermaList() {
        dermaList = f.getDermaList();
//        findByDistance();

        Log.d("Derma.java", "Dermalist retrieved");
        initRecyclerView();

    }

    private void findCity() {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + ","
                + longitude + "&key" + R.string.google_maps_key;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Geocoder geocoder = new Geocoder(Derma.this);
                try {
                    List<Address> matches = geocoder.getFromLocation(latitude, longitude, 1);
                    locality = matches.get(0).getLocality();
                    chooseLocality(matches.get(0).getLocality());
                    Toast.makeText(Derma.this, "You're in " + matches.get(0).getLocality(), Toast.LENGTH_LONG).show();
                    if (!matches.get(0).getAdminArea().equals("Metro Manila")) {
                        Toast.makeText(Derma.this, "You're not in Metro Manila", Toast.LENGTH_LONG).show();
                        locality = "Manila";
                        chooseLocality(locality);
                    }

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

    private double getDistanceMatrix(String destination) {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + ","
                + longitude + "&destinations=" + destination + "&key=AIzaSyAswjvzMmUmqp9EShBRdPoJPcXw18xdR4Q";

        final double[] dist = {0};
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray rows = response.getJSONArray("rows");
                    JSONObject row = rows.getJSONObject(0);
                    JSONArray elements = row.getJSONArray("elements");
                    JSONObject element = elements.getJSONObject(0);
                    JSONObject distance = element.getJSONObject("distance");
                    dist[0] = distance.getDouble("value");
//                    dist[0] = Double.parseDouble(distance.getString("value"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(objectRequest);

        return dist[0];
    }



    private void findByDistance() {
        Collections.sort(dermaList, new SortByDistance());
    }

    class SortByDistance implements Comparator<Dermatologist> {

        @Override
        public int compare(Dermatologist d1, Dermatologist d2) {
            String loc1 = d1.getLocation().replace(", ", "+");
            loc1 = loc1.replace(" ", "");
            String loc2 = d2.getLocation().replace(", ", "+");
            loc2 = loc2.replace(" ", "");

            double dist1 = getDistanceMatrix(loc1);
            double dist2 = getDistanceMatrix(loc2);

            if(dist1 > dist2) {
                return 1;
            } else if (dist1 < dist2) {
                return -1;
            } else
                return 0;
        }
    }

    private void getGPS() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(Derma.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            findCity();
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
                            ActivityCompat.requestPermissions(Derma.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
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

    private void displayToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.profile:
                                startActivity(new Intent(Derma.this, Profile.class));
                                break;
                            case R.id.uv:
                                startActivity(new Intent(Derma.this, Uv.class));
                                break;
                            case R.id.derma:
//                                Toast.makeText(Derma.this, "Find Nearby Dermatologist", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Derma.this, Derma.class));
                                break;
//                            case R.id.editProfile:
//                                Toast.makeText(Result.this, "Profile", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(Result.this, EditProfile.class);
//                                startActivity(i);
//                                break;
//                            case R.id.records:
//                                Toast.makeText(Result.this, "Records", Toast.LENGTH_SHORT).show();
//                                break;
                            case R.id.signout:
                                Toast.makeText(Derma.this, "Sign Out", Toast.LENGTH_SHORT).show();
                                if (id == R.id.signout) {
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            default:
                                return true;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public void onLocationChanged(Location location) {
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

    private void chooseLocality(String locality) {
        String s = locality.toLowerCase();
        switch(s) {
            case "manila":
                city = 5;
                break;
            case "quezon city":
                city = 11;
                break;
            case "caloocan":
                city = 0;
                break;
            case "las pinas":
                city = 1;
                break;
            case "makati":
                city = 2;
                break;
            case "malabon":
                city = 3;
                break;
            case "mandaluyong":
                city = 4;
                break;
            case "muntinlupa":
                city = 6;
                break;
            case "parañaque":
                city = 7;
                break;
            case "pasay":
                city = 8;
                break;
            case "pasig":
                city = 9;
                break;
            case "pateros":
                city = 10;
                break;
            case "san juan":
                city = 12;
                break;
            case "taguig":
                city = 13;
                break;
            case "valenzuela":
                city = 14;
                break;
            default:
                city = 0;
                break;

        }
    }

}

