package com.rsp

import com.google.gson.Gson
import javax.inject.Singleton
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer

@Singleton
class PlayService() {

    val playData = PlayData("","", "")
    val gson = Gson()

    val choices = mapOf<String,List<String>>(
            "ROCK" to listOf("SCISSORS"),
            "SCISSORS" to listOf("PAPER"),
            "PAPER" to listOf("ROCK"),
    )

    private fun getRandomChoice(): String {
        return choices.keys.random()
    }

    fun validateChoice(choice: String) {
        if  (choice !in choices.keys) {
            throw RuntimeException("Not valid choice $choice. Use one of ${choices.keys}")
        }
    }

    fun setRandomComputerChoice() {
        playData.computerChoice = getRandomChoice()
    }

    fun performThePlay() {
        validateChoice(playData.customerChoice)
        validateChoice(playData.computerChoice)
        playData.result = if (playData.customerChoice == playData.computerChoice) "TIE" else (if (choices[playData.customerChoice]!!.contains(playData.computerChoice)) "WIN" else "LOSE")
    }

    fun getJsonPlayData(): String? {
        return gson.toJson(playData)
    }
}
