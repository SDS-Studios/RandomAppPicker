package io.github.sdsstudios.randomapppicker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView

/**
 * Created by sds2001 on 15/10/17.
 */

class AboutFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    companion object {
        private val KEY_CHANGELOG = "prefChangelog"
        private val KEY_DEVELOPERS = "prefDevelopers"
        private val KEY_RATE_AND_REVIEW = "prefRateAndReview"
        private val KEY_SOURCE_CODE = "prefSource"
        private val KEY_LICENSE = "prefLicense"

        private val PREFERENCE_KEYS = arrayOf(KEY_CHANGELOG, KEY_DEVELOPERS, KEY_RATE_AND_REVIEW, KEY_SOURCE_CODE, KEY_LICENSE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.content_about_settings)

        PREFERENCE_KEYS.forEach { findPreference(it).onPreferenceClickListener = this }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            KEY_CHANGELOG -> textViewAlertDialog(FileReader.CHANGELOG)

            KEY_LICENSE -> textViewAlertDialog(FileReader.LICENSE)

            KEY_DEVELOPERS -> developersDialog()

            KEY_RATE_AND_REVIEW -> rateAndReview()

            KEY_SOURCE_CODE -> {
                val sourceCodeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_link)))
                startActivity(sourceCodeIntent)
            }
        }
        return true
    }

    private fun rateAndReview() {
        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_link)))
        startActivity(playStoreIntent)
    }

    private fun textViewAlertDialog(type: String) {

        var text = ""
        var title = ""

        when (type) {
            FileReader.CHANGELOG -> {
                text = FileReader.textFromFileToString(FileReader.CHANGELOG, activity)
                title = getString(R.string.changelog)
            }

            FileReader.LICENSE -> {
                text = FileReader.textFromFileToString(FileReader.LICENSE, activity)
                title = getString(R.string.open_source_licenses)
            }
        }

        val dialogView = LayoutInflater.from(activity).inflate(R.layout.text_view_scroll_layout, null)
        val textView: TextView = dialogView.findViewById(R.id.textViewChangelog)

        showCustomDialog(title, dialogView = dialogView)

        textView.text = text
    }

    private fun sendEmail() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode(getString(R.string.email)) +
                "?subject=" + Uri.encode("Feedback for Random App Picker") +
                "&body=" + Uri.encode("")
        val uri = Uri.parse(uriText)

        send.data = uri
        startActivity(Intent.createChooser(send, "Send mail..."))
        startActivity(send)
    }


    private fun developersDialog() {
        val dialogView = LayoutInflater.from(context)
                .inflate(R.layout.developers_layout, null)

        val buttonGithub = dialogView.findViewById<Button>(R.id.githubButton)
        val buttonSendEmail = dialogView.findViewById<Button>(R.id.emailButton)

        showCustomDialog(getString(R.string.developers), dialogView = dialogView)

        buttonGithub.setOnClickListener {
            val intentGithub = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/orgs/SDS-Studios"))
            startActivity(intentGithub)
        }

        buttonSendEmail.setOnClickListener { sendEmail() }
    }

    private fun showCustomDialog(title: String,
                                 dialogView: View): AlertDialog {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setView(dialogView)
        builder.setPositiveButton(R.string.okay, null)

        return builder.show()
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {}
}