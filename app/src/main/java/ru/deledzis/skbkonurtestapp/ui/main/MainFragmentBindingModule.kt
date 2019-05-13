package ru.deledzis.skbkonurtestapp.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.deledzis.skbkonurtestapp.ui.detail.DetailsFragment
import ru.deledzis.skbkonurtestapp.ui.list.ListFragment

@Module
abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun provideListFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun provideDetailsFragment(): DetailsFragment
}
