package lk.safe.alert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChecklistActivity extends AppCompatActivity {

    // UI Elements
    LinearLayout checklistContainer;
    Button btnSave, btnReset, btnShare, btnBack;
    TextView tvProgress, tvLastSaved, tvTitle;

    // Checklist data
    ArrayList<CheckBox> checkboxes = new ArrayList<>();
    ArrayList<ChecklistItem> checklistItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        initializeViews();
        createChecklistItems();
        createChecklistUI();
        loadSavedChecklist();
        updateProgress();
    }

    private void initializeViews() {
        checklistContainer = findViewById(R.id.checklistContainer);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);
        btnShare = findViewById(R.id.btnShare);
        btnBack = findViewById(R.id.btnBack);
        tvProgress = findViewById(R.id.tvProgress);
        tvLastSaved = findViewById(R.id.tvLastSaved);
        tvTitle = findViewById(R.id.tvTitle);

        // Set button listeners
        btnSave.setOnClickListener(v -> saveChecklist());
        btnReset.setOnClickListener(v -> resetChecklist());
        btnShare.setOnClickListener(v -> shareChecklist());
        btnBack.setOnClickListener(v -> finish());
    }

    private void createChecklistItems() {
        // Essential items - Sinhala with English translation
        checklistItems.add(new ChecklistItem("වැදගත් ලියකියවිලි", "Important documents",
                "ජාතික හැඳුනුම්පත්, බලපත්‍ර, දේපත්, රක්ෂණ ලියකියවිලි", true));

        checklistItems.add(new ChecklistItem("පානීය ජලය", "Drinking water",
                "පුද්ගලයෙකුට ලීටර් 4 ක් (දින 3 සඳහා)", true));

        checklistItems.add(new ChecklistItem("නොනරක් වන ආහාර", "Non-perishable food",
                "බිස්කට්, කැනරි ආහාර, නියෝග, තේ, සීනි (දින 3 සඳහා)", true));

        checklistItems.add(new ChecklistItem("ඖෂධ", "Medicines",
                "නිත්‍ය ඖෂධ, පළමු ආධාර කට්ටලය, පෙනහළු ක්‍රියාකාරක යන්ත්‍රය", true));

        checklistItems.add(new ChecklistItem("අතින් ගෙනයා හැකි බල්බ", "Torch/flashlight",
                "අතින් ගෙනයා හැකි බල්බ සහ අමතර බැටරි", true));

        checklistItems.add(new ChecklistItem("රේඩියෝව", "Radio",
                "බැටරි පරිශීලනය කරන රේඩියෝවක් + අමතර බැටරි", true));

        checklistItems.add(new ChecklistItem("ජංගම දුරකථනය", "Mobile phone",
                "පූර්ණ බැටරියක් සහිත දුරකථනය + බල බැංකුව", true));

        checklistItems.add(new ChecklistItem("මුදල්", "Cash",
                "කුඩා මුදල් වර්ග (රු. 50, 100, 500 නෝට්ටු)", true));

        checklistItems.add(new ChecklistItem("පළමු ආධාර කට්ටලය", "First aid kit",
                "ප්ලාස්ටර්, ඇතුළු තුවාල මලම, අත් පටි, කපු පටි", false));

        checklistItems.add(new ChecklistItem("ඇඳුම්", "Clothing",
                "තද ඇඳුම්, ජාකට්, වැස්මක්, රෙදි පෙට්ටිය", false));

        checklistItems.add(new ChecklistItem("කම්බි", "Blankets",
                "රෙදි පෙට්ටි හෝ නිදන බෑග්", false));

        checklistItems.add(new ChecklistItem("සනීපාරක්ෂක උපකරණ", "Sanitary supplies",
                "තෙත් තීන්ත, සනීප පටි, දත් මලම", false));

        checklistItems.add(new ChecklistItem("මෙවලම්", "Tools",
                "බහු කාර්ය තල්ල, රිබර් පටි, දුරකථනය", false));

        checklistItems.add(new ChecklistItem("සත්ව ආහාර", "Pet food",
                "ඔබට සතුන් ඇත්නම්, ඔවුන් සඳහා ආහාර සහ ජලය", false));

        checklistItems.add(new ChecklistItem("ළමා අවශ්‍යතා", "Baby needs",
                "ළදරු ආහාර, ඩයපර්, කිරි බෝතල් (අවශ්‍ය නම්)", false));

        checklistItems.add(new ChecklistItem("පුද්ගලික අවශ්‍යතා", "Personal items",
                "කණ්නාඩි, ගුවන් විදුලි උපකරණ, බොත්තම්", false));

        checklistItems.add(new ChecklistItem("මූලික උපකරණ", "Basic utilities",
                "තල්ල, කැනෝපි, තුඩුව, දැල්ල", false));

        checklistItems.add(new ChecklistItem("හදිසි සම්බන්ධතා", "Emergency contacts",
                "ලියා ඇති හදිසි සම්බන්ධතා අංක", true));

        checklistItems.add(new ChecklistItem("ප්‍රාදේශීය සිතියම", "Local map",
                "ප්‍රදේශයේ සිතියමක් සහ ආරක්ෂිත මාර්ග", false));

        checklistItems.add(new ChecklistItem("ගොඩනැගිලි ලේඛන", "Building documents",
                "ගෘහ නිර්මාණ ලේඛන, රේඛා සිතියම්", false));
    }

    private void createChecklistUI() {
        checklistContainer.removeAllViews();
        checkboxes.clear();

        for (int i = 0; i < checklistItems.size(); i++) {
            ChecklistItem item = checklistItems.get(i);

            // Create card-like container
            LinearLayout itemContainer = new LinearLayout(this);
            itemContainer.setOrientation(LinearLayout.VERTICAL);
            itemContainer.setPadding(20, 16, 20, 16);

            // If you don't have R.drawable.bg_checklist_item, use this instead:
            itemContainer.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 8);
            itemContainer.setLayoutParams(params);

            // Create checkbox
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item.sinhalaName + " (" + item.englishName + ")");
            checkBox.setTextSize(16);
            checkBox.setTag(i);
            checkBox.setPadding(0, 0, 0, 8);

            // Set importance color
            if (item.isEssential) {
                checkBox.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                checkBox.setText(checkBox.getText() + " ★");
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                checklistItems.get((int) buttonView.getTag()).isChecked = isChecked;
                updateProgress();
            });

            // Create description text
            TextView tvDescription = new TextView(this);
            tvDescription.setText(item.description);
            tvDescription.setTextSize(12);
            tvDescription.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tvDescription.setPadding(40, 0, 0, 0);

            // Add to container
            itemContainer.addView(checkBox);
            itemContainer.addView(tvDescription);

            // Add to main container
            checklistContainer.addView(itemContainer);
            checkboxes.add(checkBox);
        }
    }

    private void updateProgress() {
        int total = checklistItems.size();
        int checked = 0;
        int essentialChecked = 0;
        int essentialTotal = 0;

        for (ChecklistItem item : checklistItems) {
            if (item.isChecked) checked++;
            if (item.isEssential) {
                essentialTotal++;
                if (item.isChecked) essentialChecked++;
            }
        }

        int percentage = (int) (((float) checked / total) * 100);
        int essentialPercentage = essentialTotal > 0 ?
                (int) (((float) essentialChecked / essentialTotal) * 100) : 0;

        String progressText = "ප්‍රගතිය: " + percentage + "% (" + checked + "/" + total + ")\n" +
                "අත්‍යවශ්‍ය අයිතම: " + essentialPercentage + "% (" + essentialChecked + "/" + essentialTotal + ")";

        tvProgress.setText(progressText);

        // Update color based on progress
        if (percentage == 100) {
            tvProgress.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            tvProgress.append("\n✅ සුදානම්!");
        } else if (percentage >= 80) {
            tvProgress.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else if (percentage >= 50) {
            tvProgress.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        } else {
            tvProgress.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Update title with progress
        tvTitle.setText("හදිසි සුදානම් ලැයිස්තුව (" + percentage + "%)");
    }

    private void saveChecklist() {
        SharedPreferences prefs = getSharedPreferences("ChecklistData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save each item's state
        for (int i = 0; i < checklistItems.size(); i++) {
            editor.putBoolean("item_" + i, checklistItems.get(i).isChecked);
        }

        // Save timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        editor.putString("last_saved", timestamp);

        editor.apply();

        // Update last saved time display
        tvLastSaved.setText("අවසන් වරට සුරක්ෂිත කලේ: " + timestamp);

        Toast.makeText(this, "ලැයිස්තුව සුරක්ෂිත කරන ලදී!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedChecklist() {
        SharedPreferences prefs = getSharedPreferences("ChecklistData", MODE_PRIVATE);

        // Load each item's state
        for (int i = 0; i < checklistItems.size(); i++) {
            checklistItems.get(i).isChecked = prefs.getBoolean("item_" + i, checklistItems.get(i).isEssential);
        }

        // Load last saved time
        String lastSaved = prefs.getString("last_saved", "මීට පෙර සුරක්ෂිත කර නැත");
        tvLastSaved.setText("අවසන් වරට සුරක්ෂිත කලේ: " + lastSaved);

        // Update checkboxes
        for (int i = 0; i < checkboxes.size(); i++) {
            checkboxes.get(i).setChecked(checklistItems.get(i).isChecked);
        }

        updateProgress();
    }

    private void resetChecklist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ලැයිස්තුව යලි සකසන්න");
        builder.setMessage("ඔබට සියලු අයිතම නැවත සකසා ගැනීමට අවශ්‍යද?");

        builder.setPositiveButton("ඔව්", (dialog, which) -> {
            for (ChecklistItem item : checklistItems) {
                item.isChecked = item.isEssential; // Keep essential items checked
            }

            for (int i = 0; i < checkboxes.size(); i++) {
                checkboxes.get(i).setChecked(checklistItems.get(i).isChecked);
            }

            updateProgress();
            Toast.makeText(ChecklistActivity.this, "ලැයිස්තුව යලි සකසන ලදී", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("නැත", null);
        builder.show();
    }

    private void shareChecklist() {
        StringBuilder shareText = new StringBuilder();
        shareText.append("මගේ හදිසි සුදානම් ලැයිස්තුව\n");
        shareText.append("ප්‍රගතිය: ").append(getProgressPercentage()).append("%\n\n");

        int section = 1;
        shareText.append(section++).append(". අත්‍යවශ්‍ය අයිතම:\n");
        for (ChecklistItem item : checklistItems) {
            if (item.isEssential) {
                shareText.append(item.isChecked ? "✅ " : "⬜ ").append(item.englishName).append("\n");
            }
        }

        shareText.append("\n").append(section++).append(". අනෙකුත් අයිතම:\n");
        for (ChecklistItem item : checklistItems) {
            if (!item.isEssential) {
                shareText.append(item.isChecked ? "✅ " : "⬜ ").append(item.englishName).append("\n");
            }
        }

        shareText.append("\nසම්පාදනය: Safe Lanka Alert App\n");
        shareText.append("දිනය: ").append(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Emergency Preparedness Checklist");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        startActivity(Intent.createChooser(shareIntent, "ලැයිස්තුව බෙදාගන්න"));
    }

    private int getProgressPercentage() {
        int total = checklistItems.size();
        int checked = 0;

        for (ChecklistItem item : checklistItems) {
            if (item.isChecked) checked++;
        }

        return (int) (((float) checked / total) * 100);
    }

    public void markAllEssential(View view) {
        for (int i = 0; i < checklistItems.size(); i++) {
            if (checklistItems.get(i).isEssential) {
                checklistItems.get(i).isChecked = true;
                checkboxes.get(i).setChecked(true);
            }
        }
        updateProgress();
        Toast.makeText(this, "සියලු අත්‍යවශ්‍ය අයිතම සලකුණු කරන ලදී", Toast.LENGTH_SHORT).show();
    }

    public void markAllComplete(View view) {
        for (int i = 0; i < checklistItems.size(); i++) {
            checklistItems.get(i).isChecked = true;
            checkboxes.get(i).setChecked(true);
        }
        updateProgress();
        Toast.makeText(this, "සියලු අයිතම සලකුණු කරන ලදී", Toast.LENGTH_SHORT).show();
    }

    public void clearAll(View view) {
        for (int i = 0; i < checklistItems.size(); i++) {
            checklistItems.get(i).isChecked = false;
            checkboxes.get(i).setChecked(false);
        }
        updateProgress();
        Toast.makeText(this, "සියලු අයිතම ඉවත් කරන ලදී", Toast.LENGTH_SHORT).show();
    }

    // Checklist item class
    private static class ChecklistItem {
        String sinhalaName;
        String englishName;
        String description;
        boolean isEssential;
        boolean isChecked;

        ChecklistItem(String sinhalaName, String englishName, String description, boolean isEssential) {
            this.sinhalaName = sinhalaName;
            this.englishName = englishName;
            this.description = description;
            this.isEssential = isEssential;
            this.isChecked = isEssential; // Essential items start as checked
        }
    }
}