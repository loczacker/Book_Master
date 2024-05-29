package com.zacker.bookmaster

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.zacker.bookmaster.databinding.ActivityMainBinding
import com.zacker.bookmaster.util.Const

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var  prefs: SharedPreferences
    private var backPressedCount = 0
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        prefs = getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
//        val isLoggin = prefs.getBoolean(Const.KEY_LOGIN, false)
//        val editor = prefs.edit()
//        editor.putBoolean(Const.KEY_LOGIN, true)
//        editor.apply()
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.main_nav_fragment).navigateUp()
}