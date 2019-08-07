package com.stavro_xhardha.pockettreasure.dependency_injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureViewModelFactory
import com.stavro_xhardha.pockettreasure.dependency_injection.ViewModelKey
import com.stavro_xhardha.pockettreasure.ui.compass.CompassViewModel
import com.stavro_xhardha.pockettreasure.ui.gallery.GalleryViewModel
import com.stavro_xhardha.pockettreasure.ui.home.HomeViewModel
import com.stavro_xhardha.pockettreasure.ui.map.MapViewModel
import com.stavro_xhardha.pockettreasure.ui.names.NamesViewModel
import com.stavro_xhardha.pockettreasure.ui.quran.QuranViewModel
import com.stavro_xhardha.pockettreasure.ui.quran.aya.AyaViewModel
import com.stavro_xhardha.pockettreasure.ui.settings.SettingsViewModel
import com.stavro_xhardha.pockettreasure.ui.setup.SetupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindsViewModelFactory(factory: PocketTreasureViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    abstract fun bindsGalleryViewModel(viewModel: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindsMapViewModel(viewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NamesViewModel::class)
    abstract fun bindsNamesViewModel(viewModel: NamesViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(NewsViewModel::class)
//    abstract fun bindsNewsViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuranViewModel::class)
    abstract fun bindsQuranViewModel(viewModel: QuranViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AyaViewModel::class)
    abstract fun bindsAyaViewModel(viewModel: AyaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindsSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompassViewModel::class)
    abstract fun bindsCountrySettingsViewModel(viewModel: CompassViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewModel::class)
    abstract fun bindsSetupViewModel(viewModel: SetupViewModel): ViewModel
}