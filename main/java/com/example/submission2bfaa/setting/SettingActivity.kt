package com.example.submission2bfaa.setting

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.submission2bfaa.R

class SettingActivity : AppCompatActivity() {
    private lateinit var mSharedPreference : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportFragmentManager.beginTransaction().add(R.id.setting_holder,MyPreferenceFragment()).commit()
    }
}