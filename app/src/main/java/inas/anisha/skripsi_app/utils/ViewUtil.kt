package inas.anisha.skripsi_app.utils

import android.content.Context
import android.graphics.Paint
import android.widget.TextView

class ViewUtil {

    companion object {

        fun TextView.strikeThrough(enable: Boolean) {
            paintFlags = if (enable) {
                (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        fun dpToPx(context: Context, dp: Double): Int {
            val displayMetrics = context.resources.displayMetrics
            return ((dp * displayMetrics.density) + 0.5).toInt()
        }

        fun pxToDp(context: Context, px: Double): Int {
            val displayMetrics = context.resources.displayMetrics
            return ((px / displayMetrics.density) + 0.5).toInt()
        }

    }
}