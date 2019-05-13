package ru.deledzis.skbkonurtestapp.ui.list

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.contacts_fragment.view.*
import ru.deledzis.skbkonurtestapp.R
import ru.deledzis.skbkonurtestapp.base.BaseFragment
import ru.deledzis.skbkonurtestapp.data.model.Contact
import ru.deledzis.skbkonurtestapp.ui.detail.DetailsFragment
import ru.deledzis.skbkonurtestapp.ui.detail.DetailsViewModel
import ru.deledzis.skbkonurtestapp.ui.main.MainActivity
import ru.deledzis.skbkonurtestapp.util.SharedPreferencesUtils.getContactsFromSharedPrefs
import ru.deledzis.skbkonurtestapp.util.SharedPreferencesUtils.saveContactsToSharedPrefs
import ru.deledzis.skbkonurtestapp.util.ViewModelFactory
import javax.inject.Inject

class ListFragment : BaseFragment(), ContactSelectionListener {

    private lateinit var mView: View
    private lateinit var mContactsRecyclerView: RecyclerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mSearchView: SearchView
    private lateinit var mQueryTextListener: SearchView.OnQueryTextListener

    private lateinit var mContactsListAdapter: ContactsListAdapter

    private var mIsSync = false

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    private lateinit var mViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.contacts_fragment, container, false)

        bindViews()

        return mView
    }

    private fun bindViews() {
        with(mView) {
            mContactsRecyclerView = contacts_fragment_list
            mSwipeRefreshLayout = contacts_fragment_swipe_refresh_layout
            mProgressBar = contacts_fragment_progress_bar
        }
    }

    override fun onResume() {
        super.onResume()
        (baseActivity as MainActivity).toggleToolbarState(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ListViewModel::class.java)

        val contactsFromStorage = getContactsFromSharedPrefs(baseActivity!!)
        if (contactsFromStorage.isNotEmpty()) {
            mViewModel.setContacts(contactsFromStorage)

            Handler().postDelayed({
                baseActivity!!.runOnUiThread {
                    mIsSync = true
                    mViewModel.refresh()
                }
            }, 5 * 1000)
        }

        mContactsListAdapter = ContactsListAdapter(mViewModel, this, this)
        mContactsRecyclerView.adapter = mContactsListAdapter
        mContactsRecyclerView.layoutManager = LinearLayoutManager(context)

        observableViewModel()

        mSwipeRefreshLayout.setOnRefreshListener {
            mViewModel.refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val searchManager = baseActivity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView = baseActivity!!.findViewById(R.id.activity_main_search_view)
        mSearchView.clearFocus()
        val searchIcon = mSearchView.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_button)
        searchIcon.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_search))

        mSearchView.setOnClickListener {
            mSearchView.isIconified = false
        }
        mSearchView.setSearchableInfo(searchManager
                .getSearchableInfo(baseActivity!!.componentName))
        mQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mContactsListAdapter.filter.filter(query)
                return false
            }
            override fun onQueryTextChange(query: String): Boolean {
                mContactsListAdapter.filter.filter(query)
                return false
            }
        }
        mSearchView.setOnQueryTextListener(mQueryTextListener)
    }

    override fun onContactSelected(contact: Contact) {
        val detailsViewModel = ViewModelProviders.of(baseActivity!!, mViewModelFactory).get(DetailsViewModel::class.java)
        detailsViewModel.setSelectedContact(contact)

        (baseActivity as MainActivity).toggleToolbarState(true)

        baseActivity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_fragment_holder, DetailsFragment())
                .addToBackStack(null)
                .commit()
    }

    private fun observableViewModel() {
        mViewModel.contactsLiveData.observe(this, Observer { contacts ->
            if (contacts != null) {
                mContactsRecyclerView.visibility = View.VISIBLE
            }
        })

        mViewModel.error.observe(this, Observer { loadError ->
            if (loadError?.isError == true) {
                mContactsRecyclerView.visibility = View.GONE
                Snackbar.make(mView, loadError.throwable?.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        })

        mViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading != null) {
                mProgressBar.visibility = if (isLoading && !mIsSync) View.VISIBLE else View.GONE
                if (isLoading && !mIsSync) {
                    mProgressBar.visibility = View.VISIBLE
                    mContactsRecyclerView.visibility = View.GONE
                    mSwipeRefreshLayout.isRefreshing = false
                    mViewModel.contactsLiveData.value?.let {
                        saveContactsToSharedPrefs(baseActivity!!, it)
                    }
                }
                if (!isLoading && mIsSync) mIsSync = false
            }
        })
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}
