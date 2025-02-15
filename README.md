# <img src="https://github.com/savvasdalkitsis/uhuruphotos-android/raw/main/icons/src/main/ic_launcher-playstore.png" alt="logo" width = 60px> UhuruPhotos. A LibrePhotos client

UhuruPhotos is an Android client for [LibrePhotos](https://github.com/LibrePhotos/librephotos) 
written using the latest Android technologies, like Jetpack Compose, SQLDelight, Coroutines etc 
using an MVI architecture.

It borrows a lot of ideas from Google Photos and aims to become a full featured photo album 
replacement, including features like offline support, backup and sync etc.

|<img src="https://github.com/savvasdalkitsis/uhuruphotos-android/raw/main/assets/screen-1.png" alt="screen 1" width = 180px>|<img src="https://github.com/savvasdalkitsis/uhuruphotos-android/raw/main/assets/screen-2.png" alt="screen 2" width = 180px>|<img src="https://github.com/savvasdalkitsis/uhuruphotos-android/raw/main/assets/screen-3.png" alt="screen 3" width = 180px>|<img src="https://github.com/savvasdalkitsis/uhuruphotos-android/raw/main/assets/screen-4.png" alt="screen 4" width = 180px>|

While still early days, it already has a lot of features:

* Photo feed with multiple views which can be changed by pinch to zoom gestures
* Multiple select in feed to share/delete multiple items at once
* Periodic background synchronization with LibrePhotos server
* Photo details view with information like date, location, gps map, people view, sharing, 
  adding/removing from favourites (synced with LibrePhotos server)
* Video details view with all the above features and video playback
* Search your photos using LibrePhotos' search engine. Get search suggestions based on your photos.
* People view and suggestions for people with most photos.
* Photo map. See a heatmap of your photos. Navigate around the globe with the interactive map and 
  see photos taken in the location currently viewed.
* Dark/Light mode (manual and auto)
* Tablet support
* A lot of settings to help you control the app storage and memory requirements along with how 
  frequently to perform synchronization with the LibrePhotos server.
  
As mentioned above, a lot of features will soon be implemented before the full public release, 
mainly:

* Local photo support. This will make UhuruPhotos a viable Google Photos alternative which can be 
  used as your primary camera roll viewer.
* Backup/Sync local images with LibrePhotos server. Take control of your data by never having to 
  worry about photo backups.
* Album support. Currently the app only shows the full photo feed. Soon, user created albums will be
  supported as well.
* Basic photo editing capabilities.
* Foldables support.
* ...and more

# Play Store

The app is currently in closed beta on the Google Play store. Contact me on 
[twitter](https://twitter.com/geeky_android) for access.

# Contributions

* Translation: UhuruPhotos uses Weblate for its translations. Feel free to contribute or view
existing translations at [weblate](https://hosted.weblate.org/engage/uhuruphotos/)