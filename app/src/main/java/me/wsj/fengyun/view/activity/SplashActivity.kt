package me.wsj.fengyun.view.activity

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.wsj.fengyun.R
import me.wsj.fengyun.db.AppRepo
import me.wsj.lib.extension.startActivity
import per.wsj.commonlib.permission.PermissionUtil
import per.wsj.commonlib.utils.LogUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        PermissionUtil.with(this).permission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )
            .onGranted {
                startIntent()
            }
            .onDenied {
                startIntent()
            }.start()
    }

    private fun startIntent() {
        lifecycleScope.launch {
            var citySize = 0
            withContext(Dispatchers.IO) {
                val start = System.currentTimeMillis()
                val cities = AppRepo.getInstance().getCities()
//                LogUtil.e("time use: " + (System.currentTimeMillis() - start))
                citySize = cities.size
                delay(1000L)
            }
            if (citySize == 0) {
                AddCityActivity.startActivity(this@SplashActivity, true)
            } else {
                startActivity<HomeActivity>()
            }
            finish()
        }

    }
}