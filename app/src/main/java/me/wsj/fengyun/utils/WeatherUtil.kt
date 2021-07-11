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
        fun bitmapResize(src: Bitmap, pxX: Float, pxY: Float): Bitmap {
            //压缩图片
            val matrix = Matrix()
            matrix.postScale(pxX / src.width, pxY / src.height)
            return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        }

        @JvmStatic
        fun getAirBackground(context: Context, aqi: String): Drawable? {
            val num = aqi.toInt()
            return when {
                num <= 50 -> {
                    ResourcesCompat.getDrawable(context.resources, R.drawable.shape_aqi_excellent, null)
                }
                num <= 100 -> {
                    ResourcesCompat.getDrawable(context.resources, R.drawable.shape_aqi_good, null)
                }
                num <= 150 -> {
                    ResourcesCompat.getDrawable(context.resources, R.drawable.shape_aqi_low, null)
                }
                num <= 200 -> {
                    ResourcesCompat.getDrawable(context.resources, R.drawable.shape_aqi_mid, null)
                }
                num <= 300 -> {
                    ResourcesCompat.getDrawable(context.resources, R.drawable.shape_aqi_bad, null)
                }
                else -> {
                    ResourcesCompat.getDrawable(context.resources, R.drawable.shape_aqi_serious, null)
                }
            }
        }
    }
}