package com.example.myapplication.util

import kotlin.random.Random

object SpinUtils {
    fun nextTargetRotation(currentRotation: Float): Float {
        val extraTurns = Random.nextInt(2, 5) * 360f
        val randomOffset = Random.nextInt(0, 360)
        return currentRotation + extraTurns + randomOffset
    }
}

