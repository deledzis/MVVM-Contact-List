package ru.deledzis.skbkonurtestapp.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import ru.deledzis.skbkonurtestapp.data.model.Contact
import ru.deledzis.skbkonurtestapp.data.model.EducationPeriod
import javax.inject.Inject

class DetailsViewModel @Inject
constructor() : ViewModel() {

    private var disposable: CompositeDisposable? = null

    val selectedContact = MutableLiveData<Contact>()

    init {
        disposable = CompositeDisposable()
    }

    fun setSelectedContact(contact: Contact) {
        selectedContact.value = contact
    }

    fun saveToBundle(outState: Bundle) {
        selectedContact.value?.let {
            outState.putStringArray("client_details", arrayOf(
                    it.id,
                    it.name,
                    it.phone,
                    it.biography,
                    it.height.toString(),
                    it.temperament,
                    it.educationPeriod.start,
                    it.educationPeriod.end
            ))
        }
    }

    fun restoreFromBundle(savedInstanceState: Bundle?) {
        if (selectedContact.value == null) {
            if (savedInstanceState != null && savedInstanceState.containsKey("client_details")) {
                loadRepo(savedInstanceState.getStringArray("client_details")!!)
            }
        }
    }

    private fun loadRepo(repo_details: Array<String>) {
        selectedContact.value = Contact(
                repo_details[0],
                repo_details[1],
                repo_details[2],
                repo_details[3],
                repo_details[4].toDouble(),
                repo_details[5],
                EducationPeriod(repo_details[6], repo_details[7])
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
        disposable = null
    }
}
