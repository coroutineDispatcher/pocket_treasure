package com.stavro_xhardha.core_module

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.core_module.brain.*
import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    val rocket: Rocket,
    private val appCoroutineDispatchers: AppCoroutineDispatchers
) : ViewModel() {

    private val _launchSetupVisibility = MutableLiveData<Boolean>()
    val launchSetupVisibility: LiveData<Boolean> = _launchSetupVisibility

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }

    fun checkSavedTheme() {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher + coroutineExceptionHandler) {
            val currentTheme =
                rocket.readInt(
                    NIGHT_MODE_KEY
                )
            if (currentTheme != 0) {
                withContext(appCoroutineDispatchers.mainDispatcher) {
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

        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher + coroutineExceptionHandler) {
            val themeToSave = AppCompatDelegate.getDefaultNightMode()
            rocket.writeInt(
                NIGHT_MODE_KEY, themeToSave
            )
        }
    }

    fun checkConfigurationState() {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher + coroutineExceptionHandler) {
            val isSetupDone =
                rocket.readFloat(LATITUDE_KEY) != 0.toFloat() && rocket.readFloat(
                    LONGITUDE_KEY
                ) != 0.toFloat()
                        && rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)!!.isNotEmpty() && rocket.readString(
                    COUNTRY_SHARED_PREFERENCE_KEY
                ).isNotEmpty()

            if (!isSetupDone) {
                withContext(appCoroutineDispatchers.mainDispatcher) {
                    _launchSetupVisibility.value = true
                    _launchSetupVisibility.value = false
                }
            }
        }
    }
}