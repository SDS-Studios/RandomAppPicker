package io.github.sds100.randomapppicker

import android.content.Context
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by sds2001 on 14/10/17.
 */

class AppListAdapter(ctx: Context, modelList: List<ApplicationInfo>, selectedPackageNames: List<String>,
                     private val mOnAppClickListener: OnAppClickListener)

    : SelectableAdapter<ApplicationInfo, AppListAdapter.ViewHolder>(ctx, modelList) {

    private val mPackageManager = ctx.packageManager

    init {
        selectedPackageNames.forEach { selectedPackageName ->
            selectedItemPositions.add(modelList.indexOfFirst { model -> model.packageName == selectedPackageName })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                    LayoutInflater.from(ctx).inflate(R.layout.app_item_adapter, null),
                    mOnAppClickListener
            )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = modelList[position]

        if (selectedItemPositions.contains(position)) {
            holder.view.setBackgroundColor(selectedItemBackground)
        } else {
            holder.view.setBackgroundColor(normalBackground)
        }

        holder.textViewAppName.text = appInfo.loadLabel(mPackageManager)
        holder.textViewPackageName.text = appInfo.packageName
        holder.imageViewAppIcon.setImageDrawable(appInfo.loadIcon(mPackageManager))
    }

    fun selectAll() {
        selectedItemPositions.clear()
        selectedItemPositions.addAll(IntRange(0, modelList.size - 1))

        notifyDataSetChanged()
        mOnAppClickListener.updateToolbarText()
    }

    fun unSelectAll() {
        selectedItemPositions.clear()
        notifyDataSetChanged()
        mOnAppClickListener.updateToolbarText()
    }

    class ViewHolder(itemView: View, onAppClickListener: OnAppClickListener) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val textViewAppName: TextView = itemView.findViewById(R.id.textViewAppName)
        val textViewPackageName: TextView = itemView.findViewById(R.id.textViewPackageName)
        val imageViewAppIcon: ImageView = itemView.findViewById(R.id.imageViewAppIcon)

        init {
            itemView.setOnClickListener { onAppClickListener.onAppClick(adapterPosition) }
        }
    }

}