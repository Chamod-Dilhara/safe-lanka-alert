package lk.safe.alert;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    // TextViews
    private TextView tvStatus, tvAlertTitle, tvAlertTime, tvAlertMessage, tvAlertSource;

    // Views
    private View viewAlertIndicator;

    // Spinner
    private Spinner districtSpinner;

    // CardViews
    private CardView cardAlert, cardSafe, cardEmergency, cardChecklist, cardGuide, cardSOS;

    // Buttons
    private Button btnDmc, btnPolice, btnSubhasadaka, btnRailway;

    // Sri Lankan districts in Sinhala
    private String[] districts = {
            "කොළඹ", "ගම්පහ", "කළුතර", "මහනුවර",
            "මාතලේ", "නුවරඑළිය", "ගාල්ල", "මාතර",
            "හම්බන්තොට", "ජාffන", "කිලිනොච්චිය", "මන්නාරම",
            "වවුනියාව", "මුලතිව්", "අනුරාධපුර", "පොළොන්නරුව",
            "බදුල්ල", "මොණරාගල", "රත්නපුර", "කෑගල්ල",
            "ත්‍රිකුණාමලය", "මඩකලපුව", "අම්පාර", "පුත්තලම"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Try-catch block to handle R.java issues
        try {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Safe Lanka Alert Ready", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading layout: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize all views
        initializeViews();

        // Setup district spinner
        setupDistrictSpinner();

        // Setup click listeners
        setupClickListeners();

        // Set initial alert
        setInitialAlert();
    }

    private void initializeViews() {
        try {
            // TextViews
            tvStatus = findViewById(R.id.tvStatus);
            tvAlertTitle = findViewById(R.id.tvAlertTitle);
            tvAlertTime = findViewById(R.id.tvAlertTime);
            tvAlertMessage = findViewById(R.id.tvAlertMessage);
            tvAlertSource = findViewById(R.id.tvAlertSource);

            // Views
            viewAlertIndicator = findViewById(R.id.viewAlertIndicator);

            // Spinner
            districtSpinner = findViewById(R.id.districtSpinner);

            // CardViews
            cardAlert = findViewById(R.id.cardAlert);
            cardSafe = findViewById(R.id.cardSafe);
            cardEmergency = findViewById(R.id.cardEmergency);
            cardChecklist = findViewById(R.id.cardChecklist);
            cardGuide = findViewById(R.id.cardGuide);
            cardSOS = findViewById(R.id.cardSOS);

            // Buttons
            btnDmc = findViewById(R.id.btnDmc);
            btnPolice = findViewById(R.id.btnPolice);
            btnSubhasadaka = findViewById(R.id.btnSubhasadaka);
            btnRailway = findViewById(R.id.btnRailway);

        } catch (Exception e) {
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupDistrictSpinner() {
        try {
            // Create adapter for spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    districts
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set adapter to spinner
            districtSpinner.setAdapter(adapter);

            // Set item selected listener
            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedDistrict = parent.getItemAtPosition(position).toString();

                    // Update alert based on selected district
                    updateAlertForDistrict(selectedDistrict);

                    // Show toast
                    String englishName = getEnglishDistrictName(selectedDistrict);
                    Toast.makeText(MainActivity.this,
                            "Selected: " + englishName + " District",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up district spinner", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        // Alert Card - Show details
        cardAlert.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Showing full alert details...", Toast.LENGTH_SHORT).show();
            // You can implement detailed alert view here
        });

        // ✅ I'm Safe Button - Mark as safe
        cardSafe.setOnClickListener(v -> {
            String currentDistrict = districtSpinner.getSelectedItem().toString();
            String englishDistrict = getEnglishDistrictName(currentDistrict);

            tvStatus.setText("Status: Safe in " + englishDistrict);
            tvStatus.setBackgroundColor(Color.parseColor("#388E3C"));

            Toast.makeText(MainActivity.this,
                    "Safety status reported for " + englishDistrict + " district",
                    Toast.LENGTH_LONG).show();
        });

        // 🚨 Call Police Button
        cardEmergency.setOnClickListener(v -> {
            makePhoneCall("119");
        });

        // 🆘 SOS Emergency Button
        cardSOS.setOnClickListener(v -> {
            openSOSActivity();
        });

        // 📋 Checklist Button
        cardChecklist.setOnClickListener(v -> {
            openChecklistActivity();
        });

        // 📖 Guide Button
        cardGuide.setOnClickListener(v -> {
            openGuideActivity();
        });

        // Emergency Contact Buttons
        btnDmc.setOnClickListener(v -> makePhoneCall("117"));
        btnPolice.setOnClickListener(v -> makePhoneCall("119"));
        btnSubhasadaka.setOnClickListener(v -> makePhoneCall("1990"));
        btnRailway.setOnClickListener(v -> makePhoneCall("112"));
    }

    // Open SOS Activity
    private void openSOSActivity() {
        try {
            Intent intent = new Intent(MainActivity.this, SOSActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            // Fallback to emergency call
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("🚨 EMERGENCY SOS");
            builder.setMessage("Call emergency services immediately?");
            builder.setPositiveButton("Call 119", (dialog, which) -> makePhoneCall("119"));
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
    }

    // Open Checklist Activity
    private void openChecklistActivity() {
        try {
            Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Checklist feature coming soon", Toast.LENGTH_SHORT).show();
        }
    }

    // Open Guide Activity
    private void openGuideActivity() {
        try {
            // Try SimpleGuideActivity first
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            startActivity(intent);
        } catch (Exception e1) {
            try {
                // Fallback to GuideActivity
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            } catch (Exception e2) {
                // Show basic guide in dialog
                showBasicGuideDialog();
            }
        }
    }

    // Show basic guide as fallback
    private void showBasicGuideDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("📖 Emergency Guide");
        builder.setMessage(
                "හදිසි උපදෙස් (Emergency Guide)\n\n" +
                        "🌊 ගංවතුර (Floods):\n" +
                        "• ඉහළ ප්‍රදේශවලට ගමන් කරන්න\n" +
                        "• වතුර හරහා ගමන් නොකරන්න\n" +
                        "• විදුලිය අක්‍රිය කරන්න\n\n" +
                        "⛰️ පස්වැල්ල (Landslides):\n" +
                        "• බෑවුම් වලින් ඈත් වන්න\n" +
                        "• ආරක්ෂිත ප්‍රදේශයකට යන්න\n" +
                        "• රේඩියෝවට සවන් දෙන්න\n\n" +
                        "📞 හදිසි අංක (Emergency):\n" +
                        "• පොලිස්: 119\n" +
                        "• DMC: 117\n" +
                        "• ගිනි නිවන: 111\n" +
                        "• හදිසි ගුවන් ගමන්: 110"
        );

        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Call Police (119)", (dialog, which) -> {
            makePhoneCall("119");
        });

        builder.setNeutralButton("Share", (dialog, which) -> {
            shareBasicGuide();
        });

        builder.show();
    }

    private void shareBasicGuide() {
        String shareText = "Emergency Guide - Safe Lanka Alert\n\n" +
                "Floods: Move to higher ground\n" +
                "Landslides: Move away from slopes\n" +
                "Emergency Contacts: Police-119, DMC-117, Fire-111\n\n" +
                "Download Safe Lanka Alert App";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share Guide"));
    }

    private void setInitialAlert() {
        updateAlert(
                "⚠️ Heavy Rain Warning",
                "Colombo District\nHeavy rainfall expected. Be prepared for possible flooding.",
                "14:30",
                "DMC & Meteorology Department",
                "medium"
        );
    }

    private void updateAlertForDistrict(String district) {
        String englishName = getEnglishDistrictName(district);

        // Simulate different alerts based on district
        if (district.equals("කොළඹ") || district.equals("ගම්පහ") || district.equals("කළුතර")) {
            updateAlert(
                    "⚠️ Heavy Rain Warning",
                    englishName + " District\nHeavy rainfall expected. Be prepared for possible flooding.",
                    getCurrentTime(),
                    "DMC & Meteorology Department",
                    "medium"
            );
        } else if (district.equals("මහනුවර") || district.equals("නුවරඑළිය") || district.equals("බදුල්ල")) {
            updateAlert(
                    "🌧️ Landslide Alert",
                    englishName + " District\nLandslide risk in hilly areas. Be cautious.",
                    getCurrentTime(),
                    "National Building Research Organization",
                    "high"
            );
        } else if (district.equals("ගාල්ල") || district.equals("මාතර") || district.equals("හම්බන්තොට")) {
            updateAlert(
                    "🌊 Coastal Advisory",
                    englishName + " District\nStrong winds and rough seas expected.",
                    getCurrentTime(),
                    "Meteorology Department",
                    "low"
            );
        } else if (district.equals("ජාffන") || district.equals("කිලිනොච්චිය") || district.equals("මන්නාරම")) {
            updateAlert(
                    "🌡️ Heat Advisory",
                    englishName + " District\nHigh temperatures expected. Stay hydrated.",
                    getCurrentTime(),
                    "Meteorology Department",
                    "low"
            );
        } else {
            updateAlert(
                    "✅ All Clear",
                    englishName + " District\nNo active alerts at this time.",
                    getCurrentTime(),
                    "Safe Lanka System",
                    "safe"
            );
        }
    }

    public void updateAlert(String title, String message, String time, String source, String alertLevel) {
        tvAlertTitle.setText(title);
        tvAlertMessage.setText(message);
        tvAlertTime.setText("Time: " + time);
        tvAlertSource.setText("Source: " + source);

        // Update colors based on alert level
        switch (alertLevel.toLowerCase()) {
            case "high":
                viewAlertIndicator.setBackgroundColor(Color.parseColor("#D32F2F"));
                tvStatus.setText("Status: HIGH ALERT");
                tvStatus.setBackgroundColor(Color.parseColor("#D32F2F"));
                break;
            case "medium":
                viewAlertIndicator.setBackgroundColor(Color.parseColor("#FF8C00"));
                tvStatus.setText("Status: ALERT");
                tvStatus.setBackgroundColor(Color.parseColor("#FF8C00"));
                break;
            case "low":
                viewAlertIndicator.setBackgroundColor(Color.parseColor("#FFC107"));
                tvStatus.setText("Status: ADVISORY");
                tvStatus.setBackgroundColor(Color.parseColor("#FFC107"));
                break;
            case "safe":
            default:
                viewAlertIndicator.setBackgroundColor(Color.parseColor("#388E3C"));
                tvStatus.setText("Status: Safe");
                tvStatus.setBackgroundColor(Color.parseColor("#388E3C"));
                break;
        }
    }

    private String getCurrentTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }

    private String getEnglishDistrictName(String sinhalaName) {
        // Map Sinhala district names to English
        switch(sinhalaName) {
            case "කොළඹ": return "Colombo";
            case "ගම්පහ": return "Gampaha";
            case "කළුතර": return "Kalutara";
            case "මහනුවර": return "Kandy";
            case "මාතලේ": return "Matale";
            case "නුවරඑළිය": return "Nuwara Eliya";
            case "ගාල්ල": return "Galle";
            case "මාතර": return "Matara";
            case "හම්බන්තොට": return "Hambantota";
            case "ජාffන": return "Jaffna";
            case "කිලිනොච්චිය": return "Kilinochchi";
            case "මන්නාරම": return "Mannar";
            case "වවුනියාව": return "Vavuniya";
            case "මුලතිව්": return "Mullaitivu";
            case "අනුරාධපුර": return "Anuradhapura";
            case "පොළොන්නරුව": return "Polonnaruwa";
            case "බදුල්ල": return "Badulla";
            case "මොණරාගල": return "Moneragala";
            case "රත්නපුර": return "Ratnapura";
            case "කෑගල්ල": return "Kegalle";
            case "ත්‍රිකුණාමලය": return "Trincomalee";
            case "මඩකලපුව": return "Batticaloa";
            case "අම්පාර": return "Ampara";
            case "පුත්තලම": return "Puttalam";
            default: return sinhalaName;
        }
    }

    private void makePhoneCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot make call: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Test method to simulate new alert
    public void simulateAlert(View view) {
        String[] testAlerts = {
                "🚨 Tsunami Warning - Coastal evacuation advised",
                "🌪️ Cyclone Alert - Strong winds expected",
                "💧 Flood Warning - River levels rising",
                "🔥 Fire Hazard - High temperature warning"
        };

        String randomAlert = testAlerts[new java.util.Random().nextInt(testAlerts.length)];
        String currentDistrict = districtSpinner.getSelectedItem().toString();
        String englishName = getEnglishDistrictName(currentDistrict);

        updateAlert(
                randomAlert.split(" - ")[0],
                englishName + " District\n" + randomAlert.split(" - ")[1],
                getCurrentTime(),
                "Test Alert System",
                "high"
        );

        Toast.makeText(this, "Test alert activated for " + englishName, Toast.LENGTH_LONG).show();
    }

    // Test SOS button
    public void testSOS(View view) {
        openSOSActivity();
    }
}