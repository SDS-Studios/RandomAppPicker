package io.github.sdsstudios.randomapppicker

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : AppCompatActivity() {

    private val mRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerViewApps) }
    private val mSharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    private val mAllAppLists: List<AppList> by lazy { getAllAppLists() }

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

            mAppListAdapter = AppListAdapter(this, packages, mAllAppLists[0].packageNames)

            runOnUiThread {
                progress.dismiss()
                mRecyclerView.adapter = mAppListAdapter
            }
        }).start()
    }

    private fun getAllAppLists(): List<AppList> {
        val jsonString = mSharedPreferences.getString(AppList.KEY_APP_LISTS, null)

        return if (jsonString == null){
            val newList = ArrayList<AppList>()
            newList.add(AppList())

            newList
        }else{
            Gson().fromJson(jsonString)
        }
    }

    private fun saveAppList(){
        mAllAppLists[0].packageNames = mAppListAdapter.selectedPackageNames
        mSharedPreferences.edit().putString(AppList.KEY_APP_LISTS, AppList.serialize(mAllAppLists)).apply()
    }

    override fun onPause() {
        super.onPause()

        saveAppList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
