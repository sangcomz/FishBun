package com.sangcomz.fishbundemo

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity


class WithFragmentActivity : AppCompatActivity() {

    private lateinit var areaContainer: RelativeLayout
    private lateinit var subFragment: SubFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withfragment)

        areaContainer = findViewById(R.id.area_container)
        subFragment = SubFragment.newInstance()

        supportFragmentManager.beginTransaction().add(areaContainer.id, subFragment).commit()

    }

    /**
     * Send onActivityResult method to SubFragment
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        subFragment.onActivityResult(requestCode, resultCode, data)
    }

}
