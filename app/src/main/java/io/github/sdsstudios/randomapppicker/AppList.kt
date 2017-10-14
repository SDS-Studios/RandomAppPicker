package io.github.sdsstudios.randomapppicker

import com.google.gson.Gson

/**
 * Created by sds2001 on 14/10/17.
 */

data class AppList(var name: String = "Chosen Apps", var packageNames: List<String> = ArrayList()) {

    companion object {
        const val KEY_APP_LISTS = "app_lists"

        fun serialize(allAppLists: List<AppList>): String =
                Gson().toJson(allAppLists)
    }
}