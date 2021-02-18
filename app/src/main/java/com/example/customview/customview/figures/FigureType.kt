package com.example.customview.customview.figures

/**
 * Тип фигуры
 */
enum class FigureType(val key: Int) {
    CIRCLE(0),
    SQUARE(1),
    ROUNDED_SQUARE(2),
    UNKNOWN(-1);

    companion object {

        /**
         * Получить тип фигуры по значению [Int]
         */
        fun getByValue(value: Int): FigureType {
            return values().firstOrNull { it.key == value } ?: UNKNOWN
        }
    }
}