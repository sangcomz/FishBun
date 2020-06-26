package com.sangcomz.fishbun

import com.sangcomz.fishbun.util.future.CallableFutureTask
import com.sangcomz.fishbun.util.future.FutureCallback
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.Callable

/**
 * Created by Seok-Won on 31/10/2016.
 */
class CallableFutureTaskTest {

    @Test
    @Throws(Exception::class)
    fun check_callback() {
        val a = CallableFutureTask<String>(Callable { "test" })
        a.execute(object : FutureCallback<String> {
            override fun onSuccess(result: String) {
                assertEquals("test", result)
            }
        })
    }
}