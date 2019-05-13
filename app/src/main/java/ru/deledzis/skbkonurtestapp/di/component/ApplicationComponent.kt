package ru.deledzis.skbkonurtestapp.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import ru.deledzis.skbkonurtestapp.base.BaseApplication
import ru.deledzis.skbkonurtestapp.di.module.ActivityBindingModule
import ru.deledzis.skbkonurtestapp.di.module.ApplicationModule
import ru.deledzis.skbkonurtestapp.di.module.ContextModule
import ru.deledzis.skbkonurtestapp.di.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ContextModule::class,
    ApplicationModule::class,
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    ViewModelModule::class
])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: BaseApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}