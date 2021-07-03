package me.wsj.fengyun.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import me.wsj.fengyun.R
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.view.activity.AboutActivity
import me.wsj.fengyun.view.activity.CityControlActivity

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cityManager = findPreference<Preference>("key_city_manager")
        cityManager?.setOnPreferenceClickListener {
            startActivity(Intent(context, CityControlActivity::class.java))
            true
        }

        cityManager?.widgetLayoutResource = R.layout.layout_arrow_right

        val about = findPreference<Preference>("key_about")
        about?.setOnPreferenceClickListener {
            startActivity(Intent(context, AboutActivity::class.java))
            true
        }

        about?.widgetLayoutResource = R.layout.layout_arrow_right

        val unitHua = findPreference<CheckBoxPreference>("key_unit_hua")
        val unitShe = findPreference<CheckBoxPreference>("key_unit_she")

        unitHua?.isSelectable = !unitHua!!.isChecked
        unitHua.setOnPreferenceClickListener {
            unitShe?.isChecked = !unitHua.isChecked
            unitHua.isSelectable = !unitHua.isChecked
            unitShe?.isSelectable = unitHua.isChecked

            changeUnit("hua")
            true
        }

        unitShe?.isSelectable = !unitShe!!.isChecked
        unitShe.setOnPreferenceClickListener {
            unitHua.isChecked = !unitShe.isChecked
            unitShe.isSelectable = !unitShe.isChecked
            unitHua.isSelectable = unitShe.isChecked

            changeUnit("she")
            true
        }

        val lanSys = findPreference<CheckBoxPreference>("key_lan_system")
        val lanCn = findPreference<CheckBoxPreference>("key_lan_cn")
        val lanEn = findPreference<CheckBoxPreference>("key_lan_en")

        lanSys?.isSelectable = !lanSys!!.isChecked
        lanSys?.setOnPreferenceClickListener {
            lanCn?.isChecked = !lanSys.isChecked
            lanEn?.isChecked = !lanSys.isChecked

            lanSys.isSelectable = !lanSys.isChecked
            lanCn?.isSelectable = lanSys.isChecked
            lanEn?.isSelectable = lanSys.isChecked

            changeLang("sys")
            true
        }

        lanCn?.isSelectable = !lanCn!!.isChecked
        lanCn?.setOnPreferenceClickListener {
            lanSys?.isChecked = !lanCn.isChecked
            lanEn?.isChecked = !lanCn.isChecked

            lanCn.isSelectable = !lanCn.isChecked
            lanSys?.isSelectable = lanCn.isChecked
            lanEn?.isSelectable = lanCn.isChecked

            changeLang("zh")
            true
        }

        lanEn?.isSelectable = !lanEn!!.isChecked
        lanEn?.setOnPreferenceClickListener {
            lanCn?.isChecked = !lanEn.isChecked
            lanSys?.isChecked = !lanEn.isChecked

            lanEn.isSelectable = !lanEn.isChecked
            lanCn?.isSelectable = lanEn.isChecked
            lanSys?.isSelectable = lanEn.isChecked

            changeLang("en")
            true
        }
    }

    /**
     * 修改单位
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
     * 修改语言
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