package com.sxhardha.names_module.di

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.names_module.ui.NamesFragment
import dagger.Component

@FragmentScoped
@Component(
    modules = [NamesViewModelModule::class, NamesDatabaseModule::class, NamesNetworkModule::class],
    dependencies = [CoreComponent::class]
)
interface NameComponent {
    fun inject(namesFragment: NamesFragment)
}