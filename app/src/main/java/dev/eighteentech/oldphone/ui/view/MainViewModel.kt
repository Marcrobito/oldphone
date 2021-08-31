package dev.eighteentech.oldphone.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _words = MutableLiveData<List<String>>(listOf())
    val words : LiveData<List<String>> get() = _words


    fun updateWordsList(number: Int){
        val letters = getCurrentLetters(number)
        if (letters.isNotEmpty()){
            val currentWords = _words.value
            currentWords?.apply {
                if (this.isEmpty()) _words.value = letters
                else {
                    val current = mutableListOf<String>()
                    for (word in currentWords){
                        for (letter in letters){
                            current.add(word + letter)
                        }
                    }
                    _words.value = current
                }
            }

        }
    }

    fun clearWordsList(){
        _words.value = emptyList()
    }


    private fun getCurrentLetters(number: Int) = when (number) {
        2 -> listOf("A", "B", "C")
        3 -> listOf("D", "E", "F")
        4 -> listOf("G", "H", "I")
        5 -> listOf("J", "K", "L")
        6 -> listOf("M", "N", "O")
        7 -> listOf("P", "R", "S")
        8 -> listOf("T", "U", "V")
        9 -> listOf("W", "X", "Y")
        else -> listOf()
    }
}