package io.github.sds100.randomapppicker

import android.content.Context
import android.preference.PreferenceManager
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson

/**
 * Created by sds2001 on 14/10/17.
 */

data class AppList(var name: String = "Chosen Apps", var packages: List<String> = ArrayList()) {

    companion object {
        const val KEY_APP_LISTS = "app_lists"

        fun getAllAppLists(ctx: Context): List<AppList> {
            val jsonString = PreferenceManager
                    .getDefaultSharedPreferences(ctx)
                    .getString(AppList.KEY_APP_LISTS, null)

            return if (jsonString == null){
                val newList = java.util.ArrayList<AppList>()
                newList.add(AppList())

                newList
            }else{
                Gson().fromJson(jsonString)
            }
        }
        fun serialize(allAppLists: List<AppList>): String =
                Gson().toJson(allAppLists)
    }
}