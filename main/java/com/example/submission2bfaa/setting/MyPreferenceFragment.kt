package com.example.submission2bfaa.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.submission2bfaa.R
import com.example.submission2bfaa.alarm.MyReceiver
import java.util.*

class MyPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var ALARM : String
    private lateinit var alarmPreference : SwitchPreference
    private lateinit var alarmReceiver: MyReceiver


    companion object{
        private const val DEFAULT_VALUE = "Tidak ada"
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun init(){
        ALARM = resources.getString(R.string.alarm_preferences)
        alarmPreference = findPreference<SwitchPreference>(ALARM) as SwitchPreference
        alarmReceiver = MyReceiver()
    }

    private fun setSummaries(){
        val sh =preferenceManager.sharedPreferences
        alarmPreference.isChecked = sh.getBoolean(ALARM,false)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == ALARM){
            init()
            alarmPreference.isChecked = sharedPreferences.getBoolean(ALARM,false)
            Log.d("alarm", "onSharedPreferenceChanged: ${alarmPreference.isChecked}")
            when(alarmPreference.isChecked){
                true -> alarmReceiver.setRepeatingAlarm(activity?.applicationContext,MyReceiver.TYPE_REPEATING,"12:01","Check for latest news for Kotlin!")
                false -> alarmReceiver.batalkanAlarm(activity?.applicationContext)
            }
        }
    }


}