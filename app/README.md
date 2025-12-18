# 🚨 Safe Lanka Alert - Emergency Preparedness App
### 🇱🇰 Comprehensive Disaster Management Solution for Sri Lanka

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://www.android.com/)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![GitHub Stars](https://img.shields.io/github/stars/yourusername/safe-lanka-alert?style=social)](https://github.com/yourusername/safe-lanka-alert)
[![Open Source](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://opensource.org/)

<div align="center">
  <img src="screenshots/app_icon.png" width="100" alt="App Icon">
  <h3>Your Emergency Companion During Sri Lankan Disasters</h3>
</div>

## 📱 Screenshots

| Main Dashboard | SOS Emergency | Checklist | Emergency Guide |
|:---:|:---:|:---:|:---:|
| <img src="screenshots/main_screen.jpg" width="200"> | <img src="screenshots/sos_screen.jpg" width="200"> | <img src="screenshots/checklist.jpg" width="200"> | <img src="screenshots/guide.jpg" width="200"> |

## ✨ Key Features

### 🆘 **One-Tap SOS Emergency**
- 5-second countdown with vibration and visual alerts
- Automatically calls emergency services (119, 117, 110)
- Sends location-based SMS to emergency contacts
- Built-in flashlight and loud alarm for attention

### 📍 **District-Based Alert System**
- All 25 Sri Lankan districts in **Sinhala & English**
- Real-time weather and disaster warnings
- Color-coded alert levels (Red/Orange/Green)
- Automatic location detection

### 📞 **Instant Emergency Access**
- Direct dial to Police (119), Ambulance (110), Fire (111)
- DMC (117) and specialized emergency services
- Subhasadaka mental health support (1990)
- Railway emergency (112)

### ✅ **Comprehensive Preparedness**
- **Emergency Checklist** with progress tracking
- **"I'm Safe" reporting** with location sharing
- **Disaster Guides** for floods, landslides, first aid
- **Offline functionality** - works without internet

### 📚 **Educational Resources**
- Step-by-step disaster response procedures
- First aid instructions with visual guides
- Emergency contact directory
- Evacuation route information

## 🏗️ Technical Architecture

```mermaid
graph TD
    A[MainActivity] --> B[SOSActivity]
    A --> C[ChecklistActivity]
    A --> D[GuideActivity]
    A --> E[LocationService]

    B --> F[EmergencyCallHandler]
    B --> G[SMSDispatcher]

    C --> H[SharedPreferences]
    C --> I[SQLite Database]

    D --> J[ExpandableListView]
    D --> K[AlertDialog]

    E --> L[GPS Provider]
    E --> M[Network Provider]

    style A fill:#1B5E20,color:#fff
    style B fill:#D32F2F,color:#fff
    