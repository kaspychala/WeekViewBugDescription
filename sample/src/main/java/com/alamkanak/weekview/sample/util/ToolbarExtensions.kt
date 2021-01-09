@file:JvmName("ToolbarUtils")
package com.alamkanak.weekview.sample.util

import android.app.Activity
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.sample.R
import com.alamkanak.weekview.threetenabp.scrollToDateTime
import org.threeten.bp.LocalDateTime

enum class WeekViewType(val value: Int) {
    DayView(1),
    ThreeDayView(3),
    WeekView(7);

    companion object {
        fun create(
            numberOfVisibleDays: Int
        ): WeekViewType = values().first { it.value == numberOfVisibleDays }
    }
}

fun Toolbar.setupWithWeekView(weekView: WeekView) {
    val activity = context as Activity
    title = activity.label

    var currentViewType = WeekViewType.create(weekView.numberOfVisibleDays)

    inflateMenu(R.menu.menu_main)
    setOnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.action_today -> {
                weekView.scrollToDateTime(dateTime = LocalDateTime.now())
                true
            }
            else -> {
                val viewType = item.mapToWeekViewType()
                if (viewType != currentViewType) {
                    item.isChecked = !item.isChecked
                    currentViewType = viewType
                    weekView.numberOfVisibleDays = viewType.value
                }
                true
            }
        }
    }

    val isRootActivity = activity.isTaskRoot
    if (!isRootActivity) {
        setNavigationIcon(R.drawable.ic_arrow_back)
        setNavigationOnClickListener { activity.onBackPressed() }
    }
}

val Activity.label: String
    get() = getString(packageManager.getActivityInfo(componentName, 0).labelRes)

fun MenuItem.mapToWeekViewType(): WeekViewType {
    return when (itemId) {
        R.id.action_day_view -> WeekViewType.DayView
        R.id.action_three_day_view -> WeekViewType.ThreeDayView
        R.id.action_week_view -> WeekViewType.WeekView
        else -> throw IllegalArgumentException("Invalid menu item ID $itemId")
    }
}
