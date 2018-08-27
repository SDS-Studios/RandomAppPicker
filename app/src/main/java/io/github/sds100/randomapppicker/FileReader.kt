package io.github.sds100.randomapppicker

import android.content.Context
import android.widget.Toast

/**
 * Created by sds2001 on 16/10/17.
 */

object FileReader {
    val CHANGELOG = "CHANGELOG.txt"
    val LICENSE = "LICENSE.txt"

    fun textFromFileToString(fileName: String, ctx: Context): String {
        try {
            return ctx.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {

            Toast.makeText(ctx, "Error reading file!", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

        return ""
    }
}
