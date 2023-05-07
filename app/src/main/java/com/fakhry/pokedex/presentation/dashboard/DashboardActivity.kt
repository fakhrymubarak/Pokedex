package com.fakhry.pokedex.presentation.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pokedex.core.utils.components.viewBinding
import com.fakhry.pokedex.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityDashboardBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}