package me.wsj.lib.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.wsj.lib.databinding.CustomToastBinding

/**
 * 自定义吐司。
 *
 * @receiver Context 上下文。
 * @param content 显示的内容。
 */
fun Context.toast(content: String) {
    showToast(this, content)
}

fun Fragment.toast(content: String) {
    showToast(requireContext(), content)
}

fun Fragment.toastCenter(content: String) {
    val context = requireContext()
    val toast = Toast(context)
    toast.duration = Toast.LENGTH_LONG
    val inflate = CustomToastBinding.inflate(layoutInflater)
    inflate.tvContent.text = content
    toast.view = inflate.root
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

/**
 * 显示吐司。
 *
 * @param context 上下文。
 * @param content 显示的内容。
 */
private fun showToast(context: Context, content: String) {
    Toast.makeText(context, content, Toast.LENGTH_LONG).show()
}

/**
 * 跳转 Activity (目标Activity为泛型)。
 *
 * @receiver 目标 Activity。
 */
inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T : Activity> Activity.startActivity(pair: Pair<String, Int>) {
    val intent = Intent(this, T::class.java)
    intent.putExtra(pair.first, pair.second)
    startActivity(intent)
}

inline fun CharSequence?.notEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}