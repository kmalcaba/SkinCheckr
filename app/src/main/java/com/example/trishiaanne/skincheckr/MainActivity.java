package com.example.trishiaanne.skincheckr;

import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button guestButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guestButton = (Button) findViewById(R.id.guestBtn);
        loginButton = (Button) findViewById(R.id.loginBtn);

        guestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent guestIntent = new Intent(MainActivity.this, Camera.class);
                Intent guestIntent = new Intent(MainActivity.this, Camera.class);
                startActivity(guestIntent);
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(MainActivity.this, Profile2.class);
                startActivity(userIntent);

                getNotification(v);
            }
        });
    }
    private void getNotification(View v) {
        Intent intent = new Intent(MainActivity.this, NotifUV.class);
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
