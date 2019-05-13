package ru.deledzis.skbkonurtestapp.ui.main

import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
import ru.deledzis.skbkonurtestapp.R
import ru.deledzis.skbkonurtestapp.ui.list.ListFragment

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var mToolbar: Toolbar
    private lateinit var mSearchBoxLayout: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = findViewById(R.id.activity_main_toolbar)

        mSearchBoxLayout = findViewById(R.id.activity_main_search_box)

        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.activity_main_fragment_holder, ListFragment())
                    .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun toggleToolbarState(inDetails: Boolean) {
        mSearchBoxLayout.visibility = if (inDetails) View.GONE else View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(inDetails)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back))
    }
}
