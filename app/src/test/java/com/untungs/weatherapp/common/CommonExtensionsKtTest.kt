package com.untungs.weatherapp.common

import org.junit.Assert
import org.junit.Test
import java.text.DateFormat

class CommonExtensionsKtTest {
    @Test
    fun testLongToDateStringFullFormat() {
        Assert.assertEquals("Saturday, June 11, 2022", 1654926438000L.toDateString(DateFormat.FULL))
    }

    @Test
    fun testLongToDateString() {
        Assert.assertEquals("Jun 11, 2022", 1654926438000L.toDateString())
    }
}