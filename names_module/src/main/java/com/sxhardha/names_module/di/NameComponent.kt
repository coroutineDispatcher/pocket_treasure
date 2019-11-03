package com.sxhardha.names_module.di

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.names_module.db.NamesDao
import com.sxhardha.names_module.network.NamesApi
import com.sxhardha.names_module.ui.NamesViewModel
import dagger.Component

@FragmentScoped
@Component(
    modules = [NamesViewModelModule::class, NamesDatabaseModule::class, NamesNetworkModule::class],
    dependencies = [CoreComponent::class]
)
interface NameComponent {
    val namesViewModelFactory: NamesViewModel.Factory

    val namesDao: NamesDao

    val namesApi: NamesApi
}