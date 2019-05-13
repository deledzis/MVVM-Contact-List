package ru.deledzis.skbkonurtestapp.data.rest

import io.reactivex.Single
import ru.deledzis.skbkonurtestapp.data.model.Contact
import javax.inject.Inject

class ContactsRepository @Inject
constructor(private val mApiService: ApiService) {
    fun getContacts(file: String): Single<List<Contact>> = mApiService.getContacts(file)
}
