package ru.deledzis.skbkonurtestapp.ui.list

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import kotlinx.android.synthetic.main.contacts_fragment_item.view.*
import ru.deledzis.skbkonurtestapp.R
import ru.deledzis.skbkonurtestapp.data.model.Contact

class ContactsListAdapter (
    viewModel: ListViewModel,
    lifecycleOwner: LifecycleOwner,
    private val contactSelectionListener: ContactSelectionListener
) : RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>(), Filterable {

    private var contactsList = ArrayList<Contact>()
    private var contactsListFiltered = ArrayList<Contact>()

    init {
        viewModel.contactsLiveData.observe(lifecycleOwner, Observer { contacts ->
            contactsList.clear()
            if (contacts != null) {
                contactsList.addAll(contacts)
                notifyDataSetChanged()
            }
        })
        setHasStableIds(true)
        contactsListFiltered = contactsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts_fragment_item, parent, false)
        return ContactViewHolder(view, contactSelectionListener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactsListFiltered[position])
    }

    override fun getItemCount(): Int {
        return contactsListFiltered.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    contactsListFiltered = contactsList
                } else {
                    val filteredList = ArrayList<Contact>()
                    for (row in contactsList) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase()) || row.phone.contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }
                    contactsListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = contactsListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                Log.d("Adapter", "Publishing results!")
                contactsListFiltered = filterResults.values as ArrayList<Contact>
                notifyDataSetChanged()
            }
        }
    }

    class ContactViewHolder(
            itemView: View,
            contactSelectionListener: ContactSelectionListener
    ) : RecyclerView.ViewHolder(itemView) {

        private var contactNameTextView: TextView
        private var contactPhoneTextView: TextView
        private var contactHeightTextView: TextView

        private var mContact: Contact? = null

        init {
            with(itemView) {
                contactNameTextView = contacts_fragment_item_name_tv
                contactPhoneTextView = contacts_fragment_item_phone_tv
                contactHeightTextView = contacts_fragment_item_height_tv

                setOnClickListener {
                    if (mContact != null) {
                        contactSelectionListener.onContactSelected(mContact!!)
                    }
                }
            }
        }

        fun bind(contact: Contact) {
            this.mContact = contact
            contactNameTextView.text = contact.name
            contactPhoneTextView.text = contact.phone
            contactHeightTextView.text = contact.height.toString()
        }
    }
}
