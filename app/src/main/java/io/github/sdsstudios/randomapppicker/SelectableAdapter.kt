package io.github.sdsstudios.randomapppicker

/**
 * Created by sds2001 on 14/10/17.
 */

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import java.util.*

abstract class SelectableAdapter<T : ApplicationInfo, VH : RecyclerView.ViewHolder>(
        val ctx: Context,
        var modelList: List<T>) : RecyclerView.Adapter<VH>(){

    val selectedItemPositions: ArrayList<Int> = ArrayList()

    val selectedItemBackground: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ctx.getColor(R.color.selectedItemBackground)
    } else {
        ctx.resources.getColor(R.color.selectedItemBackground)
    }

    var normalBackground: Int = 0

    init {
        val typedValue = TypedValue()
        ctx.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
        normalBackground = typedValue.data
    }

    val selectedPackageNames: List<String>
        get() {
            val list = ArrayList<String>()
            selectedItemPositions.forEach { list.add(modelList[it].packageName) }
            return list
        }

    fun isSelected(position: Int): Boolean = selectedItemPositions.contains(position)

    fun toggleSelection(position: Int) {
        if (selectedItemPositions.contains(position)) {
            selectedItemPositions.removeAt(selectedItemPositions.indexOf(position))
        } else {
            selectedItemPositions.add(position)
        }
        notifyItemChanged(position)
    }

    fun clearSelection() {
        selectedItemPositions.forEach({ notifyItemChanged(it) })
        selectedItemPositions.clear()
    }

    override fun getItemCount(): Int = modelList.size
}