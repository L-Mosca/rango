package com.example.rango.host

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rango.R
import com.example.rango.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.bind(findViewById(R.id.navHostContainer))

        initView()
        initObserver()
    }

    private fun initObserver() {}
    private fun initView() {}
}