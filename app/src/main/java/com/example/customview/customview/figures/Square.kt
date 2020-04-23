package com.example.customview.customview.figures

import android.graphics.Paint

/**
 * Класс квадрата
 *
 * @param x - координата х
 * @param y - координата у
 * @param color - цвет отрисовки
 * @param size - размер квадрата
 */
data class Square(
    val x: Float,
    val y: Float,
    val color: Paint,
    val size: Float
) : Figure