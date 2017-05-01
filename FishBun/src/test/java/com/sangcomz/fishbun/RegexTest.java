package com.sangcomz.fishbun;

import com.sangcomz.fishbun.util.RegexUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Seok-Won on 31/10/2016.
 */

public class RegexTest {
    @Test
    public void checkGif_isCorrect() throws Exception {
        RegexUtil regexUtil = new RegexUtil();
        assertEquals(regexUtil.checkGif("/storage/emulated/0/Download/giphy.gif"), true);
        assertEquals(regexUtil.checkGif("/storage/emulated/0/Download/gif.gif.png"), false);
        assertEquals(regexUtil.checkGif("/storage/emulated/0/Download/gif.png"), false);
        assertEquals(regexUtil.checkGif("/storage/emulated/0/Download/giphy (1).gif"), true);

    }
}
