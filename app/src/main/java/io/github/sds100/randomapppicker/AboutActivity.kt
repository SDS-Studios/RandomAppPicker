package io.github.sds100.randomapppicker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by sds2001 on 15/10/17.
 */

class AboutActivity : AppCompatActivity() {

    private fun androidx.fragment.app.FragmentManager.replaceFragment(fragment: androidx.fragment.app.Fragment) {
        beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.replaceFragment(AboutFragment())
    }

    //to stop the toolbar back button from quitting the activity
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}