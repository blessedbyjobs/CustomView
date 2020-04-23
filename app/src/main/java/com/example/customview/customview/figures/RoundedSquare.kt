package com.example.customview.customview.figures

import android.graphics.Paint
import android.graphics.RectF

/**
 * Класс квадрата с закруглениями
 *
 * @param square - рисуемый квадрат
 * @param color - цвет отрисовки
 * @param cornerRadius - радиус закругления
 */
data class RoundedSquare(
    val square: RectF,
    val color: Paint,
    val cornerRadius: Float
) : Figure