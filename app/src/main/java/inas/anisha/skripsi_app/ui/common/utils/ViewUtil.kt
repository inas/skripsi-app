package inas.anisha.skripsi_app.ui.common.utils

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

    }
}