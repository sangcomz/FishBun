package com.sangcomz.fishbundemo

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class WithFragmentActivity : AppCompatActivity() {

    private lateinit var areaContainer: RelativeLayout
    private lateinit var subFragment: SubFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withfragment)

        areaContainer = findViewById(R.id.area_container)
        subFragment = SubFragment()

        supportFragmentManager.beginTransaction().add(areaContainer.id, subFragment).commit()

    }
}
