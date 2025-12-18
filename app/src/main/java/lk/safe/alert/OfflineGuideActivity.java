package lk.safe.alert;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OfflineGuideActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    TextView tvGuideTitle, tvEmergencyTip;
    List<String> guideCategories;
    HashMap<String, List<GuideItem>> guideItems;
    GuideExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_guide);

        initializeViews();
        prepareGuideData();
        setupAdapter();
        setupListeners();
    }

    private void initializeViews() {
        expandableListView = findViewById(R.id.expandableListView);
        tvGuideTitle = findViewById(R.id.tvGuideTitle);
        tvEmergencyTip = findViewById(R.id.tvEmergencyTip);

        tvGuideTitle.setText("🇱🇰 හදිසි උපදෙස් - Offline Emergency Guide");
    }

    private void prepareGuideData() {
        guideCategories = new ArrayList<>();
        guideItems = new HashMap<>();

        // 1. FLOOD GUIDES
        guideCategories.add("🌊 ගංවතුර (Floods)");
        List<GuideItem> floodItems = new ArrayList<>();
        floodItems.add(new GuideItem("පෙර සුදානම්වීම",
                "• වතුර අවහිරයන් නිතර පිරිසිදු කරන්න\n" +
                        "• ඉහළ මහල්වලට ගෙනයා හැකි අයිතම සකස් කරන්න\n" +
                        "• විදුලි උපකරණ ඉහළ ස්ථානවලට ගෙනයන්න\n" +
                        "• ගංවතුර රඳවා ගැනීමේ බෑග් සකස් කරන්න",
                "flood_prepare"));

        floodItems.add(new GuideItem("ගංවතුර ඇති විට කළ යුතු දේ",
                "• ඉහළ ප්‍රදේශවලට ගමන් කරන්න\n" +
                        "• වතුරේ ඇවිදීමෙන් වලකින්න (අඩි 15cm වතුර ප්‍රමාණවත්ය)\n" +
                        "• විදුලිය අක්‍රිය කරන්න\n" +
                        "• රේඩියෝවට සවන් දෙන්න (FM 89.6, 93.0)\n" +
                        "• ගෙදර හෝ රථයේ සිරවී ඇත්නම්, උඩු ගිලින්නන් ළඟා වන තුරු සිටින්න",
                "flood_during"));

        floodItems.add(new GuideItem("ගංවතුරින් පසු කළ යුතු දේ",
                "• ජලය උණුසුම් කර පානය කරන්න\n" +
                        "• විදුලි උපකරණ පරීක්ෂා කරන්න\n" +
                        "• ගොඩනැගිලි ව්‍යුහයන් පරීක්ෂා කරන්න\n" +
                        "• සර්පයන් සහ වෙනත් සතුන් ගැන සැලකිලිමත් වන්න\n" +
                        "• අනතුරු ඇඟවීම් ඉවත් වන තුරු නොගෙන්වන්න",
                "flood_after"));

        guideItems.put(guideCategories.get(0), floodItems);

        // 2. LANDSLIDE GUIDES
        guideCategories.add("⛰️ පස්වැල්ල (Landslides)");
        List<GuideItem> landslideItems = new ArrayList<>();
        landslideItems.add(new GuideItem("ලක්ෂණ හඳුනාගැනීම",
                "• බෑවුම්වල නව ඉරිතැලීම්\n" +
                        "• බිම විස්ථාපනය වීම\n" +
                        "• ගස්, තීරු, පවුරු නැමී යාම\n" +
                        "• බිමෙන් වතුර ගලා යාම\n" +
                        "• ගිගුරුම් හෝ ගස් කැඩී යන ශබ්ද",
                "landslide_signs"));

        landslideItems.add(new GuideItem("පස්වැල්ල ඇති විට",
                "• බෑවුම් සහිත ප්‍රදේශ වලින් ඉවතට යන්න\n" +
                        "• නිකුත් කරන ලද ඉක්මන් මාර්ග භාවිතා කරන්න\n" +
                        "• ගස්, විදුලි කම්බි, ගොඩනැගිලි වලින් ඈත්ව සිටින්න\n" +
                        "• ගොඩනැගිලි කැඩී යාමට සවන් දෙන්න\n" +
                        "• ගල් පතුරුවා හැලීමේ අවදානම ගැන සිතන්න",
                "landslide_during"));

        landslideItems.add(new GuideItem("පසුකාලීන කටයුතු",
                "• අනතුරු ඇඟවීම් ඉවත් වන තුරු නොගෙන්වන්න\n" +
                        "• පාලම්, පාර්සල් සහ ගොඩනැගිලි පරීක්ෂා කරන්න\n" +
                        "• පොලීසියට හෝ DMCට වාර්තා කරන්න\n" +
                        "• අසල්වැසියන්ගේ ආරක්ෂාව පරීක්ෂා කරන්න",
                "landslide_after"));

        guideItems.put(guideCategories.get(1), landslideItems);

        // 3. FIRST AID GUIDES
        guideCategories.add("🩺 පළමු ආධාර (First Aid)");
        List<GuideItem> firstAidItems = new ArrayList<>();
        firstAidItems.add(new GuideItem("ලේ ගැලීම නතර කිරීම",
                "1. සුදුසු ආරක්ෂක අත්වැසුම් ඇඳගන්න\n" +
                        "2. පිරිසිදු පටියකින් තුවාලය මත පීඩනය කරන්න\n" +
                        "3. තුවාලය හදිසියේ ඉහළට ඔසවන්න\n" +
                        "4. ලේ ගැලීම නතර නොවේ නම්, පීඩන ලක්ෂ්‍ය භාවිතා කරන්න\n" +
                        "5. වෛද්‍ය සහය ලබා ගන්න",
                "firstaid_bleeding"));

        firstAidItems.add(new GuideItem("CPR (හදවත නතර වුවහොත්)",
                "⚠️ පුහුණුව ඇත්නම් පමණක් කරන්න\n\n" +
                        "1. පුද්ගලයාගේ හදිසි තත්වය පරීක්ෂා කරන්න\n" +
                        "2. 110 අමතන්න (හදිසි ගුවන් ගමන්)\n" +
                        "3. සිනාවක් මත උඩු බද්ධ කරන්න\n" +
                        "4. වායුමය මාර්ග පරීක්ෂා කරන්න\n" +
                        "5. 30 උගුරු තල්ලු කරන්න (විනාඩියකට 100-120 වේගයෙන්)\n" +
                        "6. 2 හුස්ම පිඹින්න\n" +
                        "7. නැවත ආරම්භ වන තුරු හෝ වෛද්‍ය සේවා ලබා ගන්නා තුරු දිගටම කරගෙන යන්න",
                "firstaid_cpr"));

        firstAidItems.add(new GuideItem("දාහ ප්‍රතිකාර",
                "• සුළු දාහ: සීතල ජලය යොදන්න (අවම වශයෙන් මිනිත්තු 10)\n" +
                        "• ඇඳුම් ඉරන්න එපා, කපන්න\n" +
                        "• ප්ලාස්ටර් හෝ බන්ධන පටි භාවිතා නොකරන්න\n" +
                        "• වස්තු ඇලවී ඇත්නම්, ඉවත් නොකරන්න\n" +
                        "• බෙහෙත්, බටර් හෝ අයිස් යොදන්න එපා\n" +
                        "• විශාල දාහ සඳහා වහාම වෛද්‍ය උපකාර ලබා ගන්න",
                "firstaid_burns"));

        firstAidItems.add(new GuideItem("බිඳීම් හා පරිඝණ",
                "• අසුන නොකරන්න\n" +
                        "• රෝගියා චලනය නොකරන්න\n" +
                        "• බිඳුණු අග්‍රය ස්ථාවර කරන්න\n" +
                        "• සිසිල් කරන්න (අයිස් හෝ සීතල බහාලුමක්)\n" +
                        "• උසට ඔසවන්න\n" +
                        "• වෛද්‍ය සහය ලබා ගන්න",
                "firstaid_fracture"));

        guideItems.put(guideCategories.get(2), firstAidItems);

        // 4. SRI LANKAN EMERGENCY CONTACTS
        guideCategories.add("📞 හදිසි සම්බන්ධතා (Emergency Contacts)");
        List<GuideItem> contactItems = new ArrayList<>();
        contactItems.add(new GuideItem("පොදු හදිසි අංක",
                "• පොලිස්: 119 / 118\n" +
                        "• සුබසාධක (මානසික සෞඛ්‍ය): 1926 / 1990\n" +
                        "• දුම්රිය පොලිස්: 112\n" +
                        "• ගිනි නිවන: 111\n" +
                        "• ගිනි නිවන (කොළඹ): 011-2422222\n" +
                        "• හදිසි ගුවන් ගමන්: 110\n" +
                        "• දුම්රිය ගිල්වීම: 1333",
                "contacts_general"));

        contactItems.add(new GuideItem("දුරප්‍රදේශ අංක",
                "• කොළඹ රෝහල: 011-2691111\n" +
                        "• ගාල්ල රෝහල: 091-2232261\n" +
                        "• මහනුවර රෝහල: 081-2222261\n" +
                        "• රත්නපුර රෝහල: 045-2222261\n" +
                        "• යාපනය රෝහල: 021-2222261\n" +
                        "• කළුතර රෝහල: 034-2222261\n" +
                        "• මාතර රෝහල: 041-2222261",
                "contacts_hospitals"));

        contactItems.add(new GuideItem("ග්‍රාම නිලධාරී කාර්යාල",
                "• ඔබේ ප්‍රදේශයේ GN කාර්යාලය දන්නා අංකයක් ලියා තබා ගන්න\n" +
                        "• ප්‍රාදේශීය සභා කාර්යාලය\n" +
                        "• පොල් ඇල වගකීම (NBRO): 011-2674570\n" +
                        "• ජල සම්පත් කළමනාකරණ මණ්ඩලය",
                "contacts_local"));

        guideItems.put(guideCategories.get(3), contactItems);

        // 5. EVACUATION GUIDES
        guideCategories.add("🚨 ඉවත් කිරීමේ මාර්ග (Evacuation)");
        List<GuideItem> evacuationItems = new ArrayList<>();
        evacuationItems.add(new GuideItem("ආරක්ෂිත ස්ථාන",
                "• ප්‍රාදේශීය පාසල්\n" +
                        "• බෞද්ධ හෝ අනෙකුත් ආරාම\n" +
                        "• ප්‍රාදේශීය සභා කාර්යාල\n" +
                        "• ග්‍රාම නිලධාරී කාර්යාල\n" +
                        "• උස් ගොඩනැගිලි (3 මහල් හෝ ඊට වැඩි)\n" +
                        "• රජයේ ගොඩනැගිලි\n" +
                        "• පලාත් සභා ක්‍රීඩාංගන",
                "evac_locations"));

        evacuationItems.add(new GuideItem("ඉවත් කිරීමේ බෑගය",
                "ඔබගේ හදිසි බෑගයේ ඇතුළත් විය යුතු දේ:\n\n" +
                        "✅ වැදගත් ලියකියවිලි (පිටපත්)\n" +
                        "✅ ජලය (ලීටර් 4ක්/පුද්ගලයා)\n" +
                        "✅ නොනරක් වන ආහාර\n" +
                        "✅ ඖෂධ (3-දින සැපයුම)\n" +
                        "✅ පළමු ආධාර කට්ටලය\n" +
                        "✅ අතින් ගෙනයා හැකි බල්බ + බැටරි\n" +
                        "✅ රේඩියෝව + අමතර බැටරි\n" +
                        "✅ මුදල් (කුඩා නෝට්ටු)\n" +
                        "✅ ඇඳුම් + කම්බි\n" +
                        "✅ පුද්ගලික අවශ්‍යතා",
                "evac_bag"));

        evacuationItems.add(new GuideItem("පවුල් සැලැස්ම",
                "1. සම්මුඛ ස්ථානයක් තෝරාගන්න\n" +
                        "2. ආරක්ෂිත මාර්ග සලකුණු කරන්න\n" +
                        "3. වයස්ගත හෝ ආබාධිත සාමාජිකයන් සඳහා සැලැස්මක් කරන්න\n" +
                        "4. ළමයින්ගේ පාසල් සැලැස්ම දන්නා බවට වග බලා ගන්න\n" +
                        "5. සත්වයන් සඳහා සැලැස්මක් කරන්න\n" +
                        "6. නිවසේ ආරක්ෂිත කාමරයක් හඳුනාගන්න\n" +
                        "7. පවුල් සාමාජිකයන්ගේ ජංගම දුරකථන අංක ලැයිස්තුගත කරන්න",
                "evac_family"));

        guideItems.put(guideCategories.get(4), evacuationItems);

        // 6. WEATHER WARNINGS
        guideCategories.add("🌧️ කාලගුණ අනතුරු ඇඟවීම් (Weather Warnings)");
        List<GuideItem> weatherItems = new ArrayList<>();
        weatherItems.add(new GuideItem("වර්ෂාපතන මට්ටම්",
                "• සැහැල්ලු වර්ෂාව: පැයකට mm 2.5 ට වඩා අඩු\n" +
                        "• මධ්‍යස්ථ වර්ෂාව: පැයකට mm 2.6 සිට 7.6 දක්වා\n" +
                        "• බරපතල වර්ෂාව: පැයකට mm 7.6 සිට 50 දක්වා\n" +
                        "• ඉතා බරපතල වර්ෂාව: පැයකට mm 50 ට වැඩි\n" +
                        "• අතිශයින් බරපතල වර්ෂාව: පැයකට mm 100 ට වැඩි",
                "weather_levels"));

        weatherItems.add(new GuideItem("රතු/කහ/කොළ ඇඟවීම්",
                "🟢 කොළ: නිරීක්ෂණය කිරීම - සාමාන්‍ය කාලගුණ තත්වයන්\n" +
                        "🟡 කහ: සුදානම් වීම - අවදානම් කාලගුණ තත්වයන්\n" +
                        "🔴 රතු: ක්‍රියාමාර්ග - අන්තරායකාරී කාලගුණ තත්වයන්\n\n" +
                        "⚠️ NBRO බෑවුම් ස්ථාන: මෙට් දෙපාර්තමේන්තුව විසින් නිකුත් කරනු ලැබේ",
                "weather_alerts"));

        weatherItems.add(new GuideItem("කාලගුණ තොරතුරු මූලාශ්‍ර",
                "• මෙට් දෙපාර්තමේන්තුව: www.meteo.gov.lk\n" +
                        "• දුරකථන: 011-2694847\n" +
                        "• DMC: www.dmc.gov.lk / 117\n" +
                        "• NBRO (පොල් ඇල): 011-2674570\n" +
                        "• FM රේඩියෝ: 89.6, 93.0, 99.6\n" +
                        "• SLBC ගුවන් විදුලිය",
                "weather_sources"));

        guideItems.put(guideCategories.get(5), weatherItems);

        // Set emergency tip
        String[] tips = {
                "💡 උපදෙස්: ඔබගේ ජංගම දුරකථනය සැමවිටම පූර්ණ බැටරියක් සහිතව තබාගන්න",
                "💡 උපදෙස්: පවුල් සාමාජිකයන් සමග හදිසි සැලැස්මක් සාකච්ඡා කරන්න",
                "💡 උපදෙස්: ඔබගේ ප්‍රදේශයේ ආරක්ෂිත ස්ථාන සහ ඉවත් කිරීමේ මාර්ග දැන ගන්න",
                "💡 උපදෙස්: FM රේඩියෝවක් සහ අමතර බැටරි තබාගන්න",
                "💡 උපදෙස්: වයස්ගත සහ ආබාධිත සාමාජිකයන් සඳහා විශේෂ සැලැස්මක් කරන්න"
        };
        tvEmergencyTip.setText(tips[(int) (Math.random() * tips.length)]);
    }

    private void setupAdapter() {
        adapter = new GuideExpandableListAdapter(this, guideCategories, guideItems);
        expandableListView.setAdapter(adapter);

        // Expand first group by default
        expandableListView.expandGroup(0);
    }

    private void setupListeners() {
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            return false;
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            GuideItem item = guideItems.get(guideCategories.get(groupPosition)).get(childPosition);
            showDetailedGuide(item);
            return true;
        });
    }

    private void showDetailedGuide(GuideItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.getTitle());
        builder.setMessage(item.getDescription());

        builder.setPositiveButton("තේරුණා", null);

        if (item.getIcon().equals("contacts_general")) {
            builder.setNeutralButton("කොටස් කරන්න", (dialog, which) -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        item.getTitle() + ":\n" + item.getDescription() + "\n\nFrom Safe Lanka Alert App");
                startActivity(Intent.createChooser(shareIntent, "උපදෙස් බෙදාගන්න"));
            });
        }

        if (item.getIcon().contains("weather")) {
            builder.setNeutralButton("මෙට් දෙපාර්තමේන්තුව", (dialog, which) -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.meteo.gov.lk"));
                startActivity(intent);
            });
        }

        builder.show();
    }

    // Navigation methods
    public void goBack(View view) {
        finish();
    }

    public void openMeteoWebsite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.meteo.gov.lk"));
        startActivity(intent);
    }

    public void openDmcWebsite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.dmc.gov.lk"));
        startActivity(intent);
    }

    public void shareGuide(View view) {
        StringBuilder shareText = new StringBuilder();
        shareText.append("හදිසි උපදෙස් - Safe Lanka Alert App\n\n");

        for (int i = 0; i < Math.min(3, guideCategories.size()); i++) {
            String category = guideCategories.get(i);
            shareText.append(category).append(":\n");

            List<GuideItem> items = guideItems.get(category);
            for (int j = 0; j < Math.min(2, items.size()); j++) {
                GuideItem item = items.get(j);
                shareText.append("• ").append(item.getTitle()).append("\n");
            }
            shareText.append("\n");
        }

        shareText.append("Download app: Safe Lanka Alert");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "හදිසි උපදෙස් - Safe Lanka Alert");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        startActivity(Intent.createChooser(shareIntent, "උපදෙස් බෙදාගන්න"));
    }

    public void showFirstAidVideo(View view) {
        Toast.makeText(this, "First aid video feature coming soon", Toast.LENGTH_SHORT).show();
    }

    public void printGuide(View view) {
        Toast.makeText(this, "Print feature would open print dialog", Toast.LENGTH_SHORT).show();
    }

    // GuideItem class
    public static class GuideItem {
        private String title;
        private String description;
        private String icon;

        public GuideItem(String title, String description, String icon) {
            this.title = title;
            this.description = description;
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}