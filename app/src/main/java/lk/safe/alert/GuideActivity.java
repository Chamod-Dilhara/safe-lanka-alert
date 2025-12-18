package lk.safe.alert;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_guide);

        TextView tvTitle = findViewById(R.id.tvSimpleGuideTitle);
        Button btnBack = findViewById(R.id.btnBackToMain);
        Button btnFloodGuide = findViewById(R.id.btnFloodGuide);
        Button btnLandslideGuide = findViewById(R.id.btnLandslideGuide);
        Button btnFirstAid = findViewById(R.id.btnFirstAid);
        Button btnEmergencyContacts = findViewById(R.id.btnEmergencyContacts);

        tvTitle.setText("Emergency Guide 🇱🇰");

        btnBack.setOnClickListener(v -> finish());

        btnFloodGuide.setOnClickListener(v -> showGuideDialog(
                "🌊 Flood Safety Guide",
                "ගංවතුර ආරක්ෂාව\n\n" +
                        "පෙර සුදානම්:\n" +
                        "• දැනගන්න ඉවත්වීමේ මාර්ග\n" +
                        "• හදිසි බෑගයක් සකස් කරන්න\n" +
                        "• වතුර අවහිරයන් පිරිසිදු කරන්න\n\n" +
                        "ගංවතුර ඇති විට:\n" +
                        "• ඉහළ ප්‍රදේශවලට යන්න\n" +
                        "• වතුර හරහා ගමන් නොකරන්න\n" +
                        "• විදුලිය අක්‍රිය කරන්න\n" +
                        "• රේඩියෝවට සවන් දෙන්න\n\n" +
                        "ගංවතුරින් පසු:\n" +
                        "• ජලය උණුසුම් කර බොන්න\n" +
                        "• විදුලි උපකරණ පරීක්ෂා කරන්න\n" +
                        "• සර්පයන් ගැන සැලකිලිමත් වන්න"
        ));

        btnLandslideGuide.setOnClickListener(v -> showGuideDialog(
                "⛰️ Landslide Safety Guide",
                "පස්වැල්ල ආරක්ෂාව\n\n" +
                        "ලකුණු හඳුනාගැනීම:\n" +
                        "• බෑවුම්වල ඉරිතැලීම්\n" +
                        "• ගස්, පවුරු නැමී යාම\n" +
                        "• ගිගුරුම් හෝ ගස් කැඩී යන ශබ්ද\n\n" +
                        "පස්වැල්ල ඇති විට:\n" +
                        "• ක්ෂණිකව බෑවුම් වලින් ඈත් වන්න\n" +
                        "• ආරක්ෂිත ප්‍රදේශයකට යන්න\n" +
                        "• ගස්, විදුලි කම්බි වලින් ඈත් වන්න\n\n" +
                        "පසුකාලීන:\n" +
                        "• අනතුරු ඇඟවීම් ඉවත් වන තුරු නොගෙන්වන්න\n" +
                        "• ගොඩනැගිලි පරීක්ෂා කරන්න\n" +
                        "• DMCට වාර්තා කරන්න"
        ));

        btnFirstAid.setOnClickListener(v -> showGuideDialog(
                "🩺 First Aid Basics",
                "පළමු ආධාර මූලික කරුණු\n\n" +
                        "ලේ ගැලීම:\n" +
                        "1. පිරිසිදු පටියකින් පීඩනය කරන්න\n" +
                        "2. තුවාලය ඉහළට ඔසවන්න\n" +
                        "3. වෛද්‍ය සහය ලබා ගන්න\n\n" +
                        "දාහ:\n" +
                        "• සීතල ජලය යොදන්න (මිනිත්තු 10)\n" +
                        "• ඇඳුම් කපන්න, ඉරන්න එපා\n" +
                        "• විශාල දාහ සඳහා වහාම රෝහලට යන්න\n\n" +
                        "CPR (පුහුණුව ඇත්නම් පමණක්):\n" +
                        "1. 119 අමතන්න\n" +
                        "2. වායුමය මාර්ග පරීක්ෂා කරන්න\n" +
                        "3. 30 උගුරු තල්ලු කරන්න\n" +
                        "4. 2 හුස්ම පිඹින්න\n" +
                        "5. නැවත ආරම්භ වන තුරු දිගටම කරන්න"
        ));

        btnEmergencyContacts.setOnClickListener(v -> {
            // Create dialog with emergency contacts
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("📞 Emergency Contacts");
            builder.setMessage(
                    "හදිසි සම්බන්ධතා:\n\n" +
                            "🚨 Emergency: 119\n" +
                            "👮 Police: 118 / 119\n" +
                            "🚑 Ambulance: 110\n" +
                            "🔥 Fire: 111\n" +
                            "📢 Disaster Management: 117\n" +
                            "🏥 Hospital Emergency: 1990\n" +
                            "💬 Mental Health: 1926\n" +
                            "🚂 Railway Police: 112\n" +
                            "🌊 Coast Guard: 011-2446411\n\n" +
                            "කොළඹ රෝහල: 011-2691111\n" +
                            "මහනුවර රෝහල: 081-2222261\n" +
                            "ගාල්ල රෝහල: 091-2232261"
            );

            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("Call Police (119)", (dialog, which) -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:119"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Cannot make call", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNeutralButton("Share", (dialog, which) -> {
                String shareText = "Emergency Contacts for Sri Lanka:\n\n" +
                        "Emergency: 119\nPolice: 118\nAmbulance: 110\nFire: 111\nDMC: 117\n" +
                        "Hospital: 1990\nMental Health: 1926\n\nFrom Safe Lanka Alert App";

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Share Contacts"));
            });

            builder.show();
        });
    }

    private void showGuideDialog(String title, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Share", (dialog, which) -> {
            String shareText = title + "\n\n" + message + "\n\nFrom Safe Lanka Alert App";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Guide"));
        });
        builder.show();
    }
}