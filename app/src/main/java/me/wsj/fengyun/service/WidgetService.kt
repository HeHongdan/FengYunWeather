package me.wsj.fengyun.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import me.wsj.fengyun.ui.activity.AutoBootActivity
import me.wsj.fengyun.ui.activity.HomeActivity
import per.wsj.commonlib.utils.LogUtil

class WidgetService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.e("onStartCommand: ---------------------")

        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
        return super.onStartCommand(intent, flags, startId)
    }
}