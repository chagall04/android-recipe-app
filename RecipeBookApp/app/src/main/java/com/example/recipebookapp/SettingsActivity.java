package com.example.recipebookapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

/**
 * SettingsActivity
 * Demonstrates extra features: Camera, Calendar, Email, SMS, and Sound.
 * This version uses MaterialButtons for a modern look.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private MediaPlayer mediaPlayer; // For playing a click sound

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize MediaPlayer with a click sound from res/raw/click_sound.mp3
        mediaPlayer = MediaPlayer.create(this, R.raw.click_sound);

        // Set up MaterialButtons with click listeners
        MaterialButton cameraBtn = findViewById(R.id.cameraBtn);
        MaterialButton calendarBtn = findViewById(R.id.calendarBtn);
        MaterialButton emailBtn = findViewById(R.id.emailBtn);
        MaterialButton smsBtn = findViewById(R.id.smsBtn);

        cameraBtn.setOnClickListener(v -> {
            playSound();
            openCamera();
        });

        calendarBtn.setOnClickListener(v -> {
            playSound();
            addCalendarEvent();
        });

        emailBtn.setOnClickListener(v -> {
            playSound();
            sendEmail();
        });

        smsBtn.setOnClickListener(v -> {
            playSound();
            sendSms();
        });
    }

    /**
     * Plays a short click sound.
     */
    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    }

    /**
     * Launches the device camera to capture a photo.
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            Toast.makeText(this, "No camera app found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Photo captured successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens the calendar to add a new event.
     */
    private void addCalendarEvent() {
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
        calendarIntent.putExtra(CalendarContract.Events.TITLE, "Try New Recipe");
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Home Kitchen");
        calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Cook something delicious!");
        long startTime = System.currentTimeMillis() + 3600000;
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime + 3600000);
        startActivity(calendarIntent);
    }

    /**
     * Launches an email client to send an email.
     */
    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"a00321687@student.tus.ie"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Recipe Browser App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "What's cookin', good lookin'?");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launches the SMS app to send a text message.
     */
    private void sendSms() {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + Uri.encode("+353876023980")));
        smsIntent.putExtra("sms_body", "What's cookin', good lookin'?");
        startActivity(smsIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
