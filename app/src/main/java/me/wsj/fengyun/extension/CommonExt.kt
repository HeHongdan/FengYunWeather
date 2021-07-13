package me.wsj.fengyun.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.wsj.fengyun.R
import me.wsj.fengyun.databinding.CustomToastBinding
import kotlin.contracts.contract

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

private fun showToast(context: Context, content: String) {
    Toast.makeText(context, content, Toast.LENGTH_LONG).show()
}

inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun CharSequence?.notEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}