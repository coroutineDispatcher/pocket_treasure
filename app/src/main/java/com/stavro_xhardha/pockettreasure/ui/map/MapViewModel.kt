//package com.stavro_xhardha.pockettreasure.ui.map
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import javax.inject.Inject
//
//class MapViewModel @Inject constructor(private val mapRepository: MapRepository) : ViewModel() {
//
//    val city: MutableLiveData<String> = MutableLiveData()
//
//    suspend fun initMapData() {
//        city.value = mapRepository.readCurrentSavedCity()
//    }
//}
