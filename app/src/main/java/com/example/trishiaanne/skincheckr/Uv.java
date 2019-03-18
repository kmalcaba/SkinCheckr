package com.example.trishiaanne.skincheckr;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Uv extends AppCompatActivity implements LocationListener {
    private TextView uvIndex, uvDate, uvLoc, noUV;
    private ImageView uvLabel;
    private LocationManager locationManager;
    double latitude;
    double longitude;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int LOCATION_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uv);

        //Get the location and allow location
        getGPS();

        //Swipe refresh function
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                uvIndex = findViewById(R.id.uvIndex);
                uvDate = findViewById(R.id.uvDate);
                uvLoc = findViewById(R.id.uvLoc);
                uvLabel = findViewById(R.id.uv_description);
                noUV = findViewById(R.id.noUV);

                displayToolbar();
                getGPS();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },200);
            }
        });

        uvIndex = findViewById(R.id.uvIndex);
        uvDate = findViewById(R.id.uvDate);
        uvLoc = findViewById(R.id.uvLoc);
        uvLabel = findViewById(R.id.uv_description);
        noUV = findViewById(R.id.noUV);
        displayToolbar();
    }

    private void findUvIndex() {
        //displayMessage(getApplicationContext(), "Lat and Long: " + latitude + " " + longitude);
        String url = "http://api.openweathermap.org/data/2.5/uvi?appid=9acd5cafe1888d79a02ca97c69497737" +
                "&lat=" + latitude + "&lon=" + longitude;

        //displayMessage(getApplicationContext(), "User Lat and Long: " + longitude + "\n" + latitude);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Get the UV Index
                    Integer uvI = response.getInt("value");

                    //displayMessage(getApplicationContext(),"UV INDEX: " + uvI);
                    uvIndex.setText(uvI.toString());
                    if (uvI == 0) {
                        String mess = "UV INDEX NOT APPLICABLE!" + "\nUV Index range values are greater than 0";
                        noUV.setText(mess);
                        uvLabel.setImageBitmap(null);
                    } else if (uvI >0 && uvI <=2) {
                        Bitmap low = BitmapFactory.decodeResource(getResources(), R.drawable.uv_low);
                        noUV.setText(null);
                        uvLabel.setImageBitmap(low);
                    } else if (uvI >2 && uvI <= 5) {
                        Bitmap moderate = BitmapFactory.decodeResource(getResources(), R.drawable.uv_moderate);
                        noUV.setText(null);
                        uvLabel.setImageBitmap(moderate);
                    } else if (uvI >5 && uvI <= 7) {
                        Bitmap high = BitmapFactory.decodeResource(getResources(), R.drawable.uv_high);
                        noUV.setText(null);
                        uvLabel.setImageBitmap(high);
                    } else if (uvI >7 && uvI <=10) {
                        Bitmap veryhigh = BitmapFactory.decodeResource(getResources(), R.drawable.uv_veryhigh);
                        noUV.setText(null);
                        uvLabel.setImageBitmap(veryhigh);
                    } else {

//                        getNotification(getApplicationContext());
                        Bitmap extreme = BitmapFactory.decodeResource(getResources(), R.drawable.uv_extreme);
                        noUV.setText(null);
                        uvLabel.setImageBitmap(extreme);
                    }

                    //Get the City and Country Name
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String cityName = addresses.get(0).getLocality();
                    String countryName = addresses.get(0).getCountryName();

                    if (cityName != null) {
                        String address = cityName + ", " + countryName;
                        uvLoc.setText(address);
                    } else {
                        String address = countryName;
                        uvLoc.setText(address);
                    }

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
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(Uv.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            findUvIndex();
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
                            ActivityCompat.requestPermissions(Uv.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
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

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
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

    private void displayToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
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
                                startActivity(new Intent(Uv.this, Profile.class));
                                break;
                            case R.id.uv:
                                startActivity(new Intent(Uv.this, Uv.class));
                                break;
                            case R.id.derma:
//                                Toast.makeText(Uv.this, "Find Nearby Dermatologist", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Uv.this, Derma.class));
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
                                Toast.makeText(Uv.this, "Sign Out", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected (MenuItem item){
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getNotification(View v) {
        Intent intent = new Intent(Uv.this, NotifUV.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Context context = v.getContext();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.outline_notification_important_24)
                .setContentTitle("Check your Skin!")
                .setContentText("Regularly check your skin, click for UV index")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(001, mBuilder.build());
    }
}
