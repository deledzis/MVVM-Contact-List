package ru.deledzis.skbkonurtestapp.ui.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ru.deledzis.skbkonurtestapp.data.model.Contact
import ru.deledzis.skbkonurtestapp.data.model.LoadError
import ru.deledzis.skbkonurtestapp.data.rest.ContactsRepository
import javax.inject.Inject

class ListViewModel @Inject
constructor(private val mContactsRepository: ContactsRepository) : ViewModel() {
    private var mCompositeDisposable: CompositeDisposable? = null

    private val mContactsLiveData = MutableLiveData<List<Contact>>()
    private val mContactLoadError = MutableLiveData<LoadError>()
    private val mIsLoading = MutableLiveData<Boolean>()

    private val mReady1 = MutableLiveData<Boolean>()
    private val mReady2 = MutableLiveData<Boolean>()
    private val mReady3 = MutableLiveData<Boolean>()

    internal val contactsLiveData: LiveData<List<Contact>>
        get() = mContactsLiveData
    internal val error: LiveData<LoadError>
        get() = mContactLoadError
    internal val isLoading: LiveData<Boolean>
        get() = mIsLoading

    init {
        mCompositeDisposable = CompositeDisposable()
        fetchContacts()
    }

    fun refresh() {
        fetchContacts()
    }

    fun setContacts(contacts: List<Contact>) {
        mContactsLiveData.value = contacts
    }

    private fun fetchContacts() {
        mIsLoading.value = true
        mReady1.value = false
        mReady2.value = false
        mReady3.value = false
        mCompositeDisposable?.add(mContactsRepository.getContacts("generated-01")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Contact>>() {
                    override fun onSuccess(value: List<Contact>) {
                        mContactLoadError.value = LoadError(null, false)
                        mContactsLiveData.value = value
                        mReady1.value = true
                        isReady()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ListViewModel", "Error: " + e.message)
                        mContactLoadError.value = LoadError(e, true)
                        mReady1.value = true
                        isReady()
                    }
                })
        )
        mCompositeDisposable?.add(mContactsRepository.getContacts("generated-02")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Contact>>() {
                    override fun onSuccess(value: List<Contact>) {
                        mContactLoadError.value = LoadError(null, false)
                        mContactsLiveData.value = value
                        mReady2.value = true
                        isReady()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ListViewModel", "Error: " + e.message)
                        mContactLoadError.value = LoadError(e, true)
                        mReady2.value = true
                        isReady()
                    }
                })
        )
        mCompositeDisposable?.add(mContactsRepository.getContacts("generated-03")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Contact>>() {
                    override fun onSuccess(value: List<Contact>) {
                        mContactLoadError.value = LoadError(null, false)
                        mContactsLiveData.value = value
                        mReady3.value = true
                        isReady()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ListViewModel", "Error: " + e.message)
                        mContactLoadError.value = LoadError(e, true)
                        mReady3.value = true
                        isReady()
                    }
                })
        )
    }

    private fun isReady() {
        mIsLoading.value = !(mReady1.value == true && mReady2.value == true && mReady3.value == true)
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }
}
