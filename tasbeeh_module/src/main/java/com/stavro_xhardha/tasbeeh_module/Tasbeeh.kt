package com.stavro_xhardha.tasbeeh_module

data class Tasbeeh(
    val arabicPhrase: String,
    val transliteration: String,
    val translation: String,
    val numberOfTimesPressed: Int = 0
)