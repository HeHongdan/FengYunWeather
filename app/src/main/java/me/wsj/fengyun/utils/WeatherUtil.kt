package me.wsj.fengyun.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import me.wsj.fengyun.R

class WeatherUtil {
    companion object {

        @JvmStatic
        fun getWarningRes(context: Context, level: String): Pair<Drawable, Int> {
            val result: Pair<Drawable, Int>
            val res = context.resources
            when (level) {
                "黄色", "Yellow" -> {
                    result =
                        res.getDrawable(R.drawable.shape_yellow_alarm) to res.getColor(
                            R.color.white
                        )
                }
                "橙色", "Orange" -> {
                    result =
                        res.getDrawable(R.drawable.shape_orange_alarm) to res.getColor(
                            R.color.white
                        )
                }
                "红色", "Red" -> {
                    result =
                        res.getDrawable(R.drawable.shape_red_alarm) to res.getColor(
                            R.color.white
                        )
                }
                "白色", "White" -> {
                    result =
                        res.getDrawable(R.drawable.shape_white_alarm) to res.getColor(
                            R.color.black
                        )
                }
                else -> {
                    result =
                        res.getDrawable(R.drawable.shape_blue_alarm) to res.getColor(
                            R.color.white
                        )
                }
            }
            return result
        }

        @JvmStatic
        fun getF(value: String): Long {
            return try {
                var i = value.toInt().toLong()
                i = Math.round(i * 1.8 + 32)
                i
            } catch (e: Exception) {
                0
            }
        }

        @JvmStatic
        fun bitmapResize(src: Bitmap, pxX: Float, pxY: Float): Bitmap {
            //压缩图片
            val matrix = Matrix()
            matrix.postScale(pxX / src.width, pxY / src.height)
            return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        }

        @JvmStatic
        fun getAirBackground(context: Context, aqi: String): Drawable? {
            val num = aqi.toInt()

            val drawable = when {
                num <= 50 -> {
                    R.drawable.shape_aqi_excellent
                }
                num <= 100 -> {
                    R.drawable.shape_aqi_good
                }
                num <= 150 -> {
                    R.drawable.shape_aqi_low
                }
                num <= 200 -> {
                    R.drawable.shape_aqi_mid
                }
                num <= 300 -> {
                    R.drawable.shape_aqi_bad
                }
                else -> {
                    R.drawable.shape_aqi_serious
                }
            }

            return ResourcesCompat.getDrawable(context.resources, drawable, null)
        }

        /**
         * 获取星期
         *
         * @param num 0-6
         * @return 星期
         */
        @JvmStatic
        public fun getWeek(num: Int): String {
            var week = " "
            when (num) {
                1 -> week = "周一"
                2 -> week = "周二"
                3 -> week = "周三"
                4 -> week = "周四"
                5 -> week = "周五"
                6 -> week = "周六"
                7 -> week = "周日"
            }
            return week
        }
    }
}