package me.wsj.fengyun.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.ViewPropertyAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.wsj.fengyun.R
import me.wsj.fengyun.db.AppRepo
import me.wsj.fengyun.service.WidgetService
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.lib.extension.startActivity
import per.wsj.commonlib.permission.PermissionUtil
import per.wsj.commonlib.utils.DisplayUtil

class SplashActivity : AppCompatActivity() {

    lateinit var animate: ViewPropertyAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Looper.myQueue().addIdleHandler {//主线程空闲时执行，onResume 方法之前执行
            PermissionUtil.with(this).permission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                .onGranted {
                    startIntent()
                }
                .onDenied {
                    startIntent()
                }.start()
            false
        }

//        animate = findViewById<ImageView>(R.id.ivLogo).animate()

//        animate.apply {
//            duration = 1000L
//            interpolator = BounceInterpolator()
//            translationYBy(-80F)
//            scaleXBy(1.1F)
//            scaleYBy(1.1F)
//        }
    }

    private fun startIntent() {
        lifecycleScope.launch {//lifecycle 被销毁时，操作也会被取消
            var citySize: Int

//            DensityUtil.setDensity(application, 418f)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this@SplashActivity, WidgetService::class.java))
            } else {
                startService(Intent(this@SplashActivity, WidgetService::class.java))
            }

            withContext(Dispatchers.IO) {//协程：不创建新的协程，指定(IO)协程上运行代码块
//                val start = System.currentTimeMillis()
                val cities = AppRepo.getInstance().getCities()//数据库中取出城市
//                LogUtil.e("time use: " + (System.currentTimeMillis() - start))
                citySize = cities.size

                getScreenInfo()

                delay(800L)//协程：挂起当前协程
            }
            if (citySize == 0) {
                AddCityActivity.startActivity(this@SplashActivity, true)//跳转：选择城市
            } else {
                startActivity<HomeActivity>()//跳转：首页
            }
            finish()
        }
    }

    private fun getScreenInfo() {
        //屏幕的Y(高度)
        val screenRealSize = DisplayUtil.getScreenRealSize(this@SplashActivity).y

        //导航栏高度
        val navHeight =
            if (DisplayUtil.isNavigationBarShowing(this@SplashActivity))
                DisplayUtil.getNavigationBarHeight(this@SplashActivity) else 0

        //状态栏高度
        val statusBarHeight = DisplayUtil.getStatusBarHeight2(this@SplashActivity)
        val dp45 = DisplayUtil.dp2px(45f)//可能底部作者
        //可见(布局)高度
        ContentUtil.visibleHeight = screenRealSize - navHeight - statusBarHeight - dp45
    }

    override fun onDestroy() {
        super.onDestroy()
//        animate.cancel()
    }
}