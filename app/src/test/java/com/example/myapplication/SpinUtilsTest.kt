package com.example.myapplication

import com.example.myapplication.util.SpinUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class SpinUtilsTest {
    @Test
    fun nextTargetRotationAddsAtLeastTwoTurns() {
        val current = 90f
        val target = SpinUtils.nextTargetRotation(current)
        val delta = target - current
        assertTrue(delta >= 720f)
    }
}

