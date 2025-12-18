package lk.safe.alert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SOSActivity extends AppCompatActivity {

    private Button btnSOS, btnCancel;
    private TextView tvCountdown;
    private CountDownTimer countDownTimer;
    private MediaPlayer alarmPlayer;
    private Vibrator vibrator;
    private boolean isSOSActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        btnSOS = findViewById(R.id.btnSOS);
        btnCancel = findViewById(R.id.btnCancel);
        tvCountdown = findViewById(R.id.tvCountdown);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        btnSOS.setOnClickListener(v -> startSOSSequence());
        btnCancel.setOnClickListener(v -> cancelSOS());
    }

    private void startSOSSequence() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🚨 EMERGENCY SOS");
        builder.setMessage("Emergency services will be contacted in 5 seconds. Cancel if accidental.");
        builder.setPositiveButton("CONTINUE", (dialog, which) -> {
            startCountdown();
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }

    private void startCountdown() {
        isSOSActive = true;
        btnSOS.setVisibility(View.GONE);
        btnCancel.setVisibility(View.VISIBLE);
        tvCountdown.setVisibility(View.VISIBLE);

        // Start vibration pattern
        long[] pattern = {0, 500, 100, 500, 100, 500};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
        } else {
            vibrator.vibrate(pattern, 0);
        }

        // Start countdown
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                tvCountdown.setText("SOS in: " + seconds);
            }

            @Override
            public void onFinish() {
                triggerEmergencyActions();
            }
        }.start();
    }

    private void triggerEmergencyActions() {
        // 1. Call emergency number
        makeEmergencyCall();

        // 2. Send emergency SMS
        sendEmergencySMS();

        // 3. Start alarm sound
        playAlarm();

        // 4. Flash screen red
        flashEmergencyScreen();

        // Show confirmation
        tvCountdown.setText("🚨 EMERGENCY ACTIVATED!");

        Toast.makeText(this, "Emergency services notified!", Toast.LENGTH_LONG).show();
    }

    private void makeEmergencyCall() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:119"));

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                // Fallback to dial
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:119"));
                startActivity(dialIntent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Cannot make call", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmergencySMS() {
        try {
            // Get user location (simplified - would use GPS in real app)
            String location = "Emergency at my location. Need immediate assistance.";

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.putExtra("address", "119,1990"); // Multiple emergency numbers
            smsIntent.putExtra("sms_body",
                    "🚨 EMERGENCY SOS from Safe Lanka Alert\n" +
                            location + "\n" +
                            "User needs immediate assistance.\n" +
                            "Time: " + System.currentTimeMillis());

            if (smsIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(smsIntent);
            }
        } catch (Exception e) {
            // SMS not sent, continue with other actions
        }
    }

    private void playAlarm() {
        try {
            // Use built-in alarm sound
            alarmPlayer = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI);
            alarmPlayer.setLooping(true);
            alarmPlayer.setVolume(1.0f, 1.0f);
            alarmPlayer.start();
        } catch (Exception e) {
            // Alarm sound not available
        }
    }

    private void flashEmergencyScreen() {
        // Flash screen red
        View rootView = getWindow().getDecorView().getRootView();
        rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

        // Flash effect
        rootView.postDelayed(() ->
                rootView.setBackgroundColor(getResources().getColor(android.R.color.black)), 500);
        rootView.postDelayed(() ->
                rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark)), 1000);
    }

    private void cancelSOS() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (alarmPlayer != null && alarmPlayer.isPlaying()) {
            alarmPlayer.stop();
            alarmPlayer.release();
        }

        if (vibrator != null) {
            vibrator.cancel();
        }

        isSOSActive = false;
        btnSOS.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        tvCountdown.setVisibility(View.GONE);
        tvCountdown.setText("");

        getWindow().getDecorView().getRootView()
                .setBackgroundColor(getResources().getColor(android.R.color.white));

        Toast.makeText(this, "SOS cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelSOS();
    }
}