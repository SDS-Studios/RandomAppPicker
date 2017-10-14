package io.github.sdsstudios.randomapppicker

import android.app.ProgressDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*



class HomeActivity : AppCompatActivity(),  OnAppClickListener {

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

    private fun populateRecyclerView() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        val progress = ProgressDialog.show(this, "Loading Apps...",
                "loading...", true)

        Thread(Runnable {
            val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

            Collections.sort(packages) { text1, text2 ->
                text1.loadLabel(packageManager).toString()
                        .compareTo(text2.loadLabel(packageManager).toString(), ignoreCase = true)
            }

            mAppListAdapter = AppListAdapter(this, packages, mAllAppLists[0].packages, this)

            runOnUiThread {
                progress.dismiss()
                mRecyclerView.adapter = mAppListAdapter
                updateToolbarText()
            }
        }).start()
    }

    private fun saveAppList(){
        mAllAppLists[0].packages = mAppListAdapter.selectedPackageNames
        mSharedPreferences.edit().putString(AppList.KEY_APP_LISTS, AppList.serialize(mAllAppLists)).apply()
    }

    private fun updateToolbarText(){
        supportActionBar!!.title = "Selected ${mAppListAdapter.selectedItemPositions.size} Apps"
    }

    override fun onAppClick(position: Int) {
        mAppListAdapter.toggleSelection(position)
        updateToolbarText()
    }

    override fun onPause() {
        super.onPause()

        saveAppList()

        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(ComponentName(application, RandomAppWidgetProvider::class.java))
        val intent = Intent(this, RandomAppWidgetProvider::class.java)

        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}
