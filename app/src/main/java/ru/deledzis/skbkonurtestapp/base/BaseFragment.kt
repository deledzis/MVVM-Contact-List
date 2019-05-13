package ru.deledzis.skbkonurtestapp.base

import android.content.Context
import android.support.v7.app.AppCompatActivity

import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {
    var baseActivity: AppCompatActivity? = null
        private set

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        context?.let {
            baseActivity = it as AppCompatActivity
        }
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }
}