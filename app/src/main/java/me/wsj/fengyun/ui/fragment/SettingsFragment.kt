package me.wsj.fengyun.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.preference.*
import me.wsj.fengyun.R
import me.wsj.fengyun.ui.activity.AboutActivity
import me.wsj.fengyun.ui.activity.CityManagerActivity
import me.wsj.fengyun.utils.ContentUtil

/**
 * 类描述：设置页面。
 *
 * @author HeHongdan
 * @date 2021/9/25
 * @since v2021/9/25
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)//加载配置 xml 文件
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cityManager = findPreference<Preference>("key_city_manager")//城市管理
        cityManager?.setOnPreferenceClickListener {
            startActivity(Intent(context, CityManagerActivity::class.java))
            true
        }
        cityManager?.widgetLayoutResource = R.layout.layout_arrow_right//覆写城市管理的布局

        val about = findPreference<Preference>("key_about")//关于
        about?.setOnPreferenceClickListener {
            startActivity(Intent(context, AboutActivity::class.java))
            true
        }
        about?.widgetLayoutResource = R.layout.layout_arrow_right//覆写关于的布局

        val lanCategory = findPreference<PreferenceCategory>("key_lan_group")!!//语言选择(PreferenceCategory：选项组)
        val unitCategory = findPreference<PreferenceCategory>("key_unit_group")!!//单位选择

        initState(lanCategory)
        initState(unitCategory)

        val unitHua = findPreference<CheckBoxPreference>("key_unit_hua")!!//华氏度
        val unitShe = findPreference<CheckBoxPreference>("key_unit_she")!!//摄氏度

        unitHua.setOnPreferenceClickListener {
            changeState(unitCategory, it as CheckBoxPreference)

            changeUnit("hua")
            true
        }

        unitShe.setOnPreferenceClickListener {
            changeState(unitCategory, it as CheckBoxPreference)

            changeUnit("she")
            true
        }

        val lanSys = findPreference<CheckBoxPreference>("key_lan_system")!!//系统语言
        val lanCn = findPreference<CheckBoxPreference>("key_lan_cn")!!//中文
        val lanEn = findPreference<CheckBoxPreference>("key_lan_en")!!//英文

        lanSys.setOnPreferenceClickListener {
            changeState(lanCategory, it as CheckBoxPreference)

            changeLang("sys")
            true
        }

        lanCn.setOnPreferenceClickListener {
            changeState(lanCategory, it as CheckBoxPreference)

            changeLang("zh")
            true
        }

        lanEn.setOnPreferenceClickListener {
            changeState(lanCategory, it as CheckBoxPreference)

            changeLang("en")
            true
        }
    }

    /**
     * 初始化可选状态。
     *
     * @param category 组。
     */
    fun initState(category: PreferenceCategory) {
        val childCount = category.preferenceCount
        for (i in 0 until childCount) {
            val item = category.getPreference(i) as CheckBoxPreference
            item.isSelectable = !item.isChecked
        }
    }

    /**
     * 改变可选及选中状态。
     *
     * @param category 组。
     * @param target 目标选项框。
     */
    fun changeState(category: PreferenceCategory, target: CheckBoxPreference) {
        val childCount = category.preferenceCount
        target.isSelectable = false
        for (i in 0 until childCount) {
            val other = category.getPreference(i) as CheckBoxPreference
            if (other.key != target.key) {
                other.isChecked = false
                other.isSelectable = true
            }
        }
    }

    /**
     * 修改单位。
     *
     * @param unit 单位。
     */
    private fun changeUnit(unit: String) {
        ContentUtil.UNIT_CHANGE = true
        ContentUtil.APP_SETTING_UNIT = unit

        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putString("unit", unit)
            apply()
        }
    }

    /**
     * 修改语言。
     *
     * @param lan 语言。
     */
    private fun changeLang(lan: String) {
//        ContentUtil.APP_SETTING_LANG = lan
//        ContentUtil.CHANGE_LANG = true

        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putString("lan", lan)
            apply()
        }
    }


}