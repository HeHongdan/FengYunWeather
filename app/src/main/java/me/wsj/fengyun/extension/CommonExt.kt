package me.wsj.fengyun.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlin.contracts.contract

fun Context.toast(content: String) {
    Toast.makeText(this, content, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(content: String) {
    Toast.makeText(requireContext(), content, Toast.LENGTH_LONG).show()
}

inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

public inline fun CharSequence?.notEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}