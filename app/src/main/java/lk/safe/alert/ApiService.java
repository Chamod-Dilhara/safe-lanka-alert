package lk.safe.alert;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

    private Context context;

    public ApiService(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public interface AlertCallback {
        void onSuccess(List<Alert> alerts);
        void onFailure(String error);
    }

    public void fetchAlerts(AlertCallback callback) {
        if (!isNetworkAvailable()) {
            callback.onFailure("No internet connection");
            return;
        }

        new FetchAlertsTask(callback).execute();
    }

    private class FetchAlertsTask extends AsyncTask<Void, Void, List<Alert>> {
        private AlertCallback callback;
        private String error = null;

        FetchAlertsTask(AlertCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<Alert> doInBackground(Void... voids) {
            List<Alert> alerts = new ArrayList<>();

            try {
                // Try DMC API first
                String dmcUrl = "http://www.dmc.gov.lk/index.php?option=com_alert&format=json";
                String data = fetchDataFromUrl(dmcUrl);

                if (data != null && data.contains("alert")) {
                    // Parse DMC JSON
                    JSONObject json = new JSONObject(data);
                    JSONArray alertArray = json.getJSONArray("alerts");

                    for (int i = 0; i < alertArray.length(); i++) {
                        JSONObject alertJson = alertArray.getJSONObject(i);
                        Alert alert = new Alert();
                        alert.title = alertJson.getString("title");
                        alert.message = alertJson.getString("message");
                        alert.district = alertJson.getString("district");
                        alert.timestamp = alertJson.getString("timestamp");
                        alert.type = "dmc";
                        alerts.add(alert);
                    }
                } else {
                    // Fallback: Create sample alerts
                    alerts.add(createSampleAlert("flood", "high"));
                    alerts.add(createSampleAlert("landslide", "medium"));
                }

            } catch (Exception e) {
                error = e.getMessage();
                Log.e("ApiService", "Error: " + e.getMessage());

                // Return sample data on error
                alerts.add(createSampleAlert("flood", "high"));
                alerts.add(createSampleAlert("landslide", "medium"));
            }

            return alerts;
        }

        @Override
        protected void onPostExecute(List<Alert> alerts) {
            if (error != null && alerts.isEmpty()) {
                callback.onFailure(error);
            } else {
                callback.onSuccess(alerts);
            }
        }

        private String fetchDataFromUrl(String urlString) throws Exception {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000); // 10 seconds
            conn.setReadTimeout(10000);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        }

        private Alert createSampleAlert(String type, String level) {
            Alert alert = new Alert();
            alert.type = type;
            alert.level = level;
            alert.timestamp = "Just now";

            if (type.equals("flood")) {
                alert.title = "Flood Warning";
                alert.message = "Heavy rainfall causing flooding in low-lying areas";
                alert.district = "Colombo";
            } else {
                alert.title = "Landslide Warning";
                alert.message = "Risk of landslides in hilly areas";
                alert.district = "Ratnapura";
            }

            return alert;
        }
    }

    public static class Alert {
        public String title;
        public String message;
        public String district;
        public String timestamp;
        public String type;
        public String level;

        public String getSinhalaMessage() {
            if (type.equals("flood")) {
                return "ගංවතුර අනතුරු ඇඟවීම - " + district + " දිස්ත්‍රික්කය";
            } else {
                return "පස්වැල්ල අනතුරු ඇඟවීම - " + district + " දිස්ත්‍රික්කය";
            }
        }

        public int getColor() {
            if (level.equals("high")) return 0xFFFF4444; // Red
            if (level.equals("medium")) return 0xFFFFBB33; // Orange
            return 0xFF99CC00; // Green
        }
    }
}