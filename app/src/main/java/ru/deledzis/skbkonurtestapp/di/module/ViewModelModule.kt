package ru.deledzis.skbkonurtestapp.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.deledzis.skbkonurtestapp.di.util.ViewModelKey
import ru.deledzis.skbkonurtestapp.ui.detail.DetailsViewModel
import ru.deledzis.skbkonurtestapp.ui.list.ListViewModel
import ru.deledzis.skbkonurtestapp.util.ViewModelFactory
import javax.inject.Singleton

@Singleton
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListViewModel(listViewModel: ListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(detailsViewModel: DetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
