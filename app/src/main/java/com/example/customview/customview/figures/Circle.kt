package com.example.customview.customview.figures

import android.graphics.Paint

/**
 * Класс круга
 *
 * @param x - координата х
 * @param y - координата у
 * @param color - цвет отрисовки
 * @param radius - радиус круга
 */
data class Circle(
    val x: Float,
    val y: Float,
    val color: Paint,
    val radius: Float
) : Figure