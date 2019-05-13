package ru.deledzis.skbkonurtestapp.data.rest

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.deledzis.skbkonurtestapp.data.model.Contact

interface ApiService {
    @GET("{file}.json")
    fun getContacts(
            @Path("file") file: String
    ): Single<List<Contact>>
}