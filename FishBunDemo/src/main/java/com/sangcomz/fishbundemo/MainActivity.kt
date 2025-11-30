package com.sangcomz.fishbundemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sangcomz.fishbundemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBarInsets(binding.toolbar)
        setSupportActionBar(binding.toolbar)

        binding.btnWithActivityBasic.setOnClickListener {
            val i = Intent(this@MainActivity, WithActivityActivity::class.java)
            i.putExtra("mode", 0)
            startActivity(i)
        }

        binding.btnWithActivityDark.setOnClickListener {
            val i = Intent(this@MainActivity, WithActivityActivity::class.java)
            i.putExtra("mode", 1)
            startActivity(i)
        }

        binding.btnWithActivityLight.setOnClickListener {
            val i = Intent(this@MainActivity, WithActivityActivity::class.java)
            i.putExtra("mode", 2)
            startActivity(i)
        }

        binding.btnWithFragment.setOnClickListener {
            val i = Intent(this@MainActivity, WithFragmentActivity::class.java)
            startActivity(i)
        }
    }
}
