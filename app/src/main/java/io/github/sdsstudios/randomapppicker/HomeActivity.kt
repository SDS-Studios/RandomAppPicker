package io.github.sdsstudios.randomapppicker

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.coroutines.experimental.buildSequence

class HomeActivity : AppCompatActivity(), OnAppClickListener {

    private val mRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerViewApps) }
    private val mSharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    private val mAllAppLists: List<AppList> by lazy { AppList.getAllAppLists(this) }

    private lateinit var mAppListAdapter: AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        populateRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.action_select_all -> {
                mAppListAdapter.selectAll()
                return true
            }

            R.id.action_un_select_all -> {
                mAppListAdapter.unSelectAll()
                return true
            }

            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun populateRecyclerView() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        val progress = ProgressDialog.show(this, "Loading Apps...",
                "loading...", true)

        Thread(Runnable {
            val packages = getApps()
            mAppListAdapter = AppListAdapter(this, packages, mAllAppLists[0].packages, this)

            runOnUiThread {
                progress.dismiss()
                mRecyclerView.adapter = mAppListAdapter
                updateToolbarText()
            }
        }).start()
    }

    private fun getApps(): List<ApplicationInfo> {
        val userApps: List<ApplicationInfo> = buildSequence {

            //get all the installed apps
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA).forEach { app ->
                //only allow apps which can be launched by the user
                if (packageManager.getLaunchIntentForPackage(app.packageName) != null) yield(app)
            }

        }.toList()

        return userApps.sortedWith(Comparator { text1, text2 ->
            text1.loadLabel(packageManager).toString().compareTo(text2.loadLabel(packageManager).toString(), ignoreCase = true)
        })
    }

    private fun saveAppList() {
        mAllAppLists[0].packages = mAppListAdapter.selectedPackageNames
        mSharedPreferences.edit().putString(AppList.KEY_APP_LISTS, AppList.serialize(mAllAppLists)).apply()
    }

    override fun updateToolbarText() {
        supportActionBar!!.title = "Selected ${mAppListAdapter.selectedItemPositions.size} Apps"
    }

    override fun onAppClick(position: Int) {
        mAppListAdapter.toggleSelection(position)
        updateToolbarText()
    }

    override fun onPause() {
        super.onPause()

        saveAppList()
    }
}
