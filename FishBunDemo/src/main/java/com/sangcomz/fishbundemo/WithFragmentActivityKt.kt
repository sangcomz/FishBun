package com.sangcomz.fishbundemo

import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_withfragment.*

class WithFragmentActivityKt : AppCompatActivity() {

    private lateinit var subFragment: SubFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withfragment)

        subFragment = SubFragment.newInstance()
        supportFragmentManager.beginTransaction().add(area_container.id, subFragment).commit()
    }

    /**
     * Send onActivityResult method to SubFragment
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        subFragment.onActivityResult(requestCode, resultCode, data)
    }

}