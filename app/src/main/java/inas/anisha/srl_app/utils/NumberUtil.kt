package inas.anisha.srl_app.utils

class NumberUtil {
    companion object {
        fun Long.roundUp(num: Long, divisor: Long): Long {
            return (num + divisor - 1) / divisor
        }
    }
}