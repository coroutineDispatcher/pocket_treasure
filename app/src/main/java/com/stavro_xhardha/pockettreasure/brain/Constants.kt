package com.stavro_xhardha.pockettreasure.brain

import com.stavro_xhardha.pockettreasure.R

//App constants
const val APPLICATION_TAG = "PocketTreasure"
const val SHARED_PREFERENCES_TAG = "pocket_treasure_app"
const val TREASURE_DATABASE_NAME = "pocket_treasure_db"

const val PRAYER_API_BASE_URL: String = "https://api.aladhan.com/v1/"
const val COUNTRIES_API_URL = "https://restcountries.eu/rest/v2/all"
const val NEWS_BASE_URL = "https://newsapi.org/v2/everything/"
const val SEARCH_KEY_WORD = "islam"
const val SEARCH_NEWS_API_KEY = "08fef133f02f4fe898007ea05c1dcf16"
const val INITIAL_PAGE_SIZE = 20
const val UNSPLASH_BASE_URL = "https://api.unsplash.com/search/photos"
const val UNPLASH_QUERY_VALUE = "islam|mosque"
const val CLIENT_ID = "9c80ea2aa631bda7ec75798cd6f0cfc2008ea6152721d80913464c4fc2f142bc"
const val CLIENT_SECRET = "a69a283d85e85b8a72309e50c219211ab13284c4e0190ae30199e8e1d0ac3aa3"
const val GOOGLE_MAP_PLACE_SEARCH =
    "https://maps.googleapis.com/maps/api/place/findplacefromtext/json"
const val REQUEST_STORAGE_PERMISSION = 355
const val REQUEST_LOCATION_PERMISSION = 366
const val REQUEST_CHECK_LOCATION_SETTINGS = 367
const val QURAN_API_CALL_BASE_URL = "https://api.alquran.cloud/v1/quran/ar.alafasy"
const val FIRST_SURAH_SENTENCE = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
const val PLAY_STORE_URL =
    "https://play.google.com/store/apps/details?id=com.stavro_xhardha.pockettreasure"

//Colors
const val WHITE_BACKGROUND = R.color.md_white_1000
const val LIGHT_SELECTOR = R.color.colorAccent
const val DARK_BACKGROUND = R.color.md_grey_800
const val DARK_SELECTOR = R.color.colorAccentDark

//SharedPreferences
const val COUNTRY_SHARED_PREFERENCE_KEY = "app_country"
const val CAPITAL_SHARED_PREFERENCES_KEY = "app_capital"
const val FAJR_KEY = "app_fejr"
const val SUNRISE_KEY = "app_sunrise"
const val DHUHR_KEY = "app_dhuhr"
const val ASR_KEY = "app_asr"
const val SUNSET_KEY = "app_sunset"
const val MAGHRIB_KEY = "app_maghrib"
const val ISHA_KEY = "app_isha"
const val IMSAK_KEY = "app_imsak"
const val MIDNIGHT_KEY = "app_midnight"
const val HIRJI_DAY_OF_MONTH_KEY = "app_hijri_day_of_month"
const val HIJRI_MONTH_NAME_KEY = "app_hijri_month_name"
const val HIJRI_YEAR_KEY = "app_hijri_year"
const val GREGORIAN_DAY_KEY = "app_gregorian_day"
const val GREGORIAN_MONTH_KEY = "app_gregorian_month"
const val GREGORIAN_MONTH_NAME_KEY = "app_gregorian_month_name"
const val GREGORIAN_YEAR_KEY = "app_gregorian_year"
const val NOTIFY_USER_FOR_FAJR = "app_notify_fajr"
const val NOTIFY_USER_FOR_DHUHR = "app_notify_dhuhr"
const val NOTIFY_USER_FOR_ASR = "app_notify_asr"
const val NOTIFY_USER_FOR_MAGHRIB = "app_notify_maghrib"
const val NOTIFY_USER_FOR_ISHA = "app_notify_isha"
const val COUNTRY_UPDATED = "app_country_updated"
const val HAS_ONCE_ENTERED_NEWS = "app_has_once_entered_news"
const val WORKER_FIRED_KEY = "app_worker_has_been_fired"
const val DATA_ARE_READY = "app_worker_started"
const val LATITUDE_KEY = "app_latitude_key"
const val LONGITUDE_KEY = "app_longitude_key"

const val PENDING_INTENT_SYNC = 1001
const val PENDING_INTENT_FIRE_NOTIFICATION_FAJR = 2002
const val PENDING_INTENT_FIRE_NOTIFICATION_DHUHR = 2003
const val PENDING_INTENT_FIRE_NOTIFICATION_ASR = 2004
const val PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB = 2005
const val PENDING_INTENT_FIRE_NOTIFICATION_ISHA = 2006
const val PENDING_INTENT_FIRE_MAIN_ACTIVITY = 2007

const val MAKKAH_LATITUDE = 21.4225
const val MAKKAH_LONGITUDE = 39.8262

const val PRAYER_TITLE = "prayer_title"
const val PRAYER_DESCRIPTION = "prayer_description"
const val FAJR = "Fajr Time"
const val DHUHR = "Dhuhr Time"
const val ASR = "Asr Time"
const val MAGHRIB = "Maghrib Time"
const val ISHA = "Isha Time"

const val FULL_IMAGE_SAVED_STATE = "bundle_image"
const val NAMES_LIST_STATE = "bundle_names"