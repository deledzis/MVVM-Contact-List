package ru.deledzis.skbkonurtestapp.util

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.deledzis.skbkonurtestapp.data.model.Contact

object SharedPreferencesUtils {
    fun saveContactsToSharedPrefs(context: Context, contacts: List<Contact>) {
        val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val prefsEditor = appSharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(contacts)
        prefsEditor.putString("Contacts", json)
        prefsEditor.apply()
    }

    fun getContactsFromSharedPrefs(context: Context): List<Contact> {
        val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val gson = Gson()
        val json = appSharedPrefs.getString("Contacts", "")
        val contacts = gson.fromJson<Any>(json, object : TypeToken<ArrayList<Contact>>() {}.type)
        return contacts as List<Contact>? ?: arrayListOf()
    }
}