package com.sangcomz.fishbundemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_withfragment.*

class WithFragmentActivity : AppCompatActivity() {

    private lateinit var subFragment: SubFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withfragment)

        subFragment = SubFragment.newInstance()
        supportFragmentManager.beginTransaction().add(area_container.id, subFragment).commit()
    }
}
