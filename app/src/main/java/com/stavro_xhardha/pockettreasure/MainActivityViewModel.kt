package com.stavro_xhardha.pockettreasure

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(val rocket: Rocket) : ViewModel() {

    private val _launchSetupVisibility = MutableLiveData<Boolean>()
    val launchSetupVisibility: LiveData<Boolean> = _launchSetupVisibility

    fun checkSavedTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTheme =
                rocket.readInt(
                    NIGHT_MODE_KEY
                )
            if (currentTheme != 0) {
                withContext(Dispatchers.Main) {
                    AppCompatDelegate.setDefaultNightMode(currentTheme)
                }
            }
        }
    }

    fun changeCurrentTheme() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val themeToSave = AppCompatDelegate.getDefaultNightMode()
            rocket.writeInt(
                NIGHT_MODE_KEY, themeToSave
            )
        }
    }

    fun checkConfigurationState() {
        viewModelScope.launch(Dispatchers.IO) {
            val isSetupDone =
                rocket.readFloat(LATITUDE_KEY) != 0.toFloat() && rocket.readFloat(
                    LONGITUDE_KEY
                ) != 0.toFloat()
                        && rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)!!.isNotEmpty() && rocket.readString(
                    COUNTRY_SHARED_PREFERENCE_KEY
                )!!.isNotEmpty()

            if (!isSetupDone) {
                withContext(Dispatchers.Main) {
                    _launchSetupVisibility.value = true
                    _launchSetupVisibility.value = false
                }
            }
        }
    }
}