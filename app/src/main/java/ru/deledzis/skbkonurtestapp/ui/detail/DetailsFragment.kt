package ru.deledzis.skbkonurtestapp.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.contact_details_fragment.view.*
import ru.deledzis.skbkonurtestapp.R
import ru.deledzis.skbkonurtestapp.base.BaseFragment
import ru.deledzis.skbkonurtestapp.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    lateinit var mView: View
    lateinit var contactNameTextView: TextView
    lateinit var contactPhoneTextView: TextView
    lateinit var contactTemperamentTextView: TextView
    lateinit var contactEducationPeriodTextView: TextView
    lateinit var contactBiographyTextView: TextView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.contact_details_fragment, container, false)

        bindViews()

        return mView
    }

    private fun bindViews() {
        with(mView) {
            contactNameTextView = contact_details_fragment_name_tv
            contactPhoneTextView = contact_details_fragment_phone_tv
            contactTemperamentTextView = contact_details_fragment_temperament_tv
            contactEducationPeriodTextView = contact_details_fragment_edu_period_tv
            contactBiographyTextView = contact_details_fragment_bio_tv
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsViewModel = ViewModelProviders.of(baseActivity!!, viewModelFactory).get(DetailsViewModel::class.java)
        detailsViewModel.restoreFromBundle(savedInstanceState)
        displayRepo()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        detailsViewModel.saveToBundle(outState)
    }

    private fun displayRepo() {
        detailsViewModel.selectedContact.observe(this, Observer { contact ->
            if (contact != null) {
                val start = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(contact.educationPeriod.start)
                val end = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(contact.educationPeriod.end)
                val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                contactNameTextView.text = contact.name
                contactPhoneTextView.text = contact.phone
                contactTemperamentTextView.text = contact.temperament
                contactEducationPeriodTextView.text = "${df.format(start)} - ${df.format(end)}"
                contactBiographyTextView.text = contact.biography

                contactPhoneTextView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${contact.phone}")
                    startActivity(intent)
                }
            }
        })
    }
}
