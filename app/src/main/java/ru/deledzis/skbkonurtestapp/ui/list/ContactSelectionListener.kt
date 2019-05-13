package ru.deledzis.skbkonurtestapp.ui.list

import ru.deledzis.skbkonurtestapp.data.model.Contact

interface ContactSelectionListener {
    fun onContactSelected(contact: Contact)
}
