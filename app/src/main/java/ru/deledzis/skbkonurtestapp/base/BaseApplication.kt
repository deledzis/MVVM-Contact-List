package ru.deledzis.skbkonurtestapp.base

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import ru.deledzis.skbkonurtestapp.di.component.DaggerApplicationComponent

class BaseApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)

        return component
    }
}