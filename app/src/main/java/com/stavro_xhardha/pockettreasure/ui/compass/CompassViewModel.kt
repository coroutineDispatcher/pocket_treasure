package com.stavro_xhardha.pockettreasure.ui.compass

import android.hardware.SensorEvent
import android.util.Log
import android.view.animation.RotateAnimation
import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CompassViewModel @AssistedInject constructor(
    private val rocket: Rocket,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): CompassViewModel
    }

    private val _rotateAnimation = MutableLiveData<RotateAnimation>()
    private val _qiblaFound = MutableLiveData<Boolean>()

    val rotateAnimation: LiveData<RotateAnimation> = _rotateAnimation
    val qiblaFound: LiveData<Boolean> = _qiblaFound

    private var degreesRotation = 0.0

    init {
        findQibla()
    }

    private fun findQibla() {
        viewModelScope.launch {
            val latitude = rocket.readFloat(LATITUDE_KEY)
            val longitude = rocket.readFloat(LONGITUDE_KEY)

            val currentLatRad = toRadians(latitude.toDouble())
            val currentLong = toRadians(longitude.toDouble())

            val maccahCurrentLatRad = toRadians(MAKKAH_LATITUDE)
            val maccahCurrentLongRad = toRadians(MAKKAH_LONGITUDE)

            val lonDelta = maccahCurrentLongRad - currentLong
            val y = (sin(lonDelta) * cos(maccahCurrentLatRad)).toFloat()
            val x =
                (cos(currentLatRad) * sin(maccahCurrentLatRad) - sin(currentLatRad) * cos(maccahCurrentLatRad) * cos(
                    lonDelta
                )).toFloat()

            val degreesToRotate = toDegrees(atan2(y.toDouble(), x.toDouble()))

            withContext(Dispatchers.Main) {
                rotateCompass(degreesToRotate)
            }

            if (isDebugMode)
                Log.d(APPLICATION_TAG, "$degreesToRotate")
        }
    }

    private fun rotateCompass(degreesToRotate: Double) {
        val rotateAnim = RotateAnimation(
            0.0f, degreesToRotate.toFloat(),
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )

        rotateAnim.duration = 0
        rotateAnim.fillAfter = true

        degreesRotation = degreesToRotate

        _rotateAnimation.value = rotateAnim
    }

    fun observeValues(sensorEvent: SensorEvent?) {
        if (((degreesRotation - 1) < sensorEvent!!.values[0]) && (sensorEvent.values[0] < (degreesRotation + 1))) {
            _qiblaFound.value = true
        }
    }
}