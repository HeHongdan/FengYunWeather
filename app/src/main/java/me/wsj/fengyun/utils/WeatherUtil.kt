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
			val res = context.resources
            return when {
                num <= 50 -> {
                    ResourcesCompat.getDrawable(
                        res,
                        R.drawable.shape_aqi_excellent,
                        null
                    )
                }
                num <= 100 -> {
                    ResourcesCompat.getDrawable(res, R.drawable.shape_aqi_good, null)
                }
                num <= 150 -> {
                    ResourcesCompat.getDrawable(res, R.drawable.shape_aqi_low, null)
                }
                num <= 200 -> {
                    ResourcesCompat.getDrawable(res, R.drawable.shape_aqi_mid, null)
                }
                num <= 300 -> {
                    ResourcesCompat.getDrawable(res, R.drawable.shape_aqi_bad, null)
                }
                else -> {
                    ResourcesCompat.getDrawable(
                        res,
                        R.drawable.shape_aqi_serious,
                        null
                    )
                }
            }
        }
    }
}