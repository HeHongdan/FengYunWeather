package me.wsj.bg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val code = intent.getIntExtra("code",0)
        val dayBack = IconUtils.getDayBg(this, code)
        findViewById<ImageView>(R.id.ivBg).setImageResource(dayBack)
    }
}