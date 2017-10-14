package io.github.sdsstudios.randomapppicker

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import java.util.*


/**
 * Created by sds2001 on 14/10/17.
 */

class RandomAppWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val CLICK_ACTION: String = "CLICK_ACTION"
    }

    private val mRandom = Random()

    override fun onUpdate(ctx: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        for (appWidgetId in appWidgetIds) {

            val intent = Intent(ctx, RandomAppWidgetProvider::class.java)
            intent.action = CLICK_ACTION

            val pendingIntent = PendingIntent.getBroadcast(ctx,
                    appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val views = RemoteViews(ctx.packageName, R.layout.widget_layout)
            views.setOnClickPendingIntent(R.id.dice_image_view, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(ctx: Context, intent: Intent) {

        val chosenPackageNames = AppList.getAllAppLists(ctx)[0].packages

        if (intent.action == CLICK_ACTION) {

            if (chosenPackageNames.isNotEmpty()) {
                val randomIndex = mRandom.nextInt(chosenPackageNames.size)

                ctx.startActivity(ctx.packageManager.getLaunchIntentForPackage(chosenPackageNames[randomIndex]))
                Toast.makeText(ctx, R.string.opening_random_app, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(ctx, R.string.you_havent_chosen_any_apps, Toast.LENGTH_SHORT).show()
            }
        }
        super.onReceive(ctx, intent)
    }
}
