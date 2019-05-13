package ru.deledzis.skbkonurtestapp.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.deledzis.skbkonurtestapp.ui.main.MainActivity
import ru.deledzis.skbkonurtestapp.ui.main.MainFragmentBindingModule

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [MainFragmentBindingModule::class])
    abstract fun bindMainActivity(): MainActivity
}