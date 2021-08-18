package com.rsp
import com.rsp.PlayData
import com.rsp.PlayService
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class PlayServiceTest {

    @Inject
    lateinit var playService: PlayService

    @BeforeEach
    fun clearData() {
        playService.playData.computerChoice = ""
        playService.playData.customerChoice = ""
        playService.playData.result = ""
    }

    @Test
    fun randomComputerChoiceIsSetCorrectlly() {
        givenTheRandomComputerChoiceIsSet()
        thenComputerChoiceContainsValidValue()
    }

    @Test
    fun gameResultIsCalculatedCorrectly() {
        val choicesAndExpectedResults = listOf(
                listOf("ROCK", "PAPER", "LOSE"),
                listOf("ROCK", "SCISSORS", "WIN"),
                listOf("PAPER", "PAPER", "TIE")
        )
        choicesAndExpectedResults.forEach {
            givenCustomerChoiceIsSetTo(it[0])
            givenComputerChoiceIsSetTo(it[1])
            whenPlayPerformed()
            thenGameResultIs(it[2])
        }
    }

    @Test
    fun gettingExceptionOnTryingToPerformGameWithoutComputerData() {
        givenCustomerChoiceIsSetTo("ROCK")
        thenGettingRuntimeExceptionOnTryingToPerformGame("Not valid choice")
    }

    @Test
    fun performingPlayCanNotHappenWithoutCustomerData() {
        givenTheRandomComputerChoiceIsSet()
        thenGettingRuntimeExceptionOnTryingToPerformGame("Not valid choice")
    }

    @Test
    fun gettingExceptionOnTryingToPerformGameWithInvalidComputerData() {
        givenCustomerChoiceIsSetTo("ROCK")
        givenComputerChoiceIsSetTo("INVALID")
        thenGettingRuntimeExceptionOnTryingToPerformGame("Not valid choice")
    }

    @Test
    fun gettingExceptionOnTryingToPerformGameWithInvalidCustomerData() {
        givenCustomerChoiceIsSetTo("INVALID")
        givenComputerChoiceIsSetTo("PAPER")
        thenGettingRuntimeExceptionOnTryingToPerformGame("Not valid choice")
    }

    @Test
    fun playDataIsConvertedCorrectlyToJson() {
        givenCustomerChoiceIsSetTo("PAPER")
        givenComputerChoiceIsSetTo("ROCK")
        whenPlayPerformed()
        thenJsonPlayDataIsEqualTo("{\"result\":\"WIN\",\"customerChoice\":\"PAPER\",\"computerChoice\":\"ROCK\"}")
    }

    private fun givenTheRandomComputerChoiceIsSet() {
        playService.setRandomComputerChoice()
    }

    private fun thenComputerChoiceContainsValidValue() {
        Assertions.assertNotNull(playService.playData.computerChoice)
        Assertions.assertTrue(playService.playData.computerChoice in listOf("ROCK", "SCISSORS", "PAPER"))
    }

    private fun givenCustomerChoiceIsSetTo(value: String) {
        playService.playData.customerChoice = value
    }

    private fun givenComputerChoiceIsSetTo(value: String) {
        playService.playData.computerChoice = value
    }

    private fun whenPlayPerformed() {
        playService.performThePlay()
    }

    private fun thenGameResultIs(expectedResult: String) {
        Assertions.assertEquals(playService.playData.result, expectedResult)
    }

    private fun thenGettingRuntimeExceptionOnTryingToPerformGame(textContainedInErrorMessage: String) {
        val message = assertThrows(
                RuntimeException::class.java, { playService.performThePlay() }
        )
        Assertions.assertNotNull(message.message)
        Assertions.assertTrue(message.message!!.contains(textContainedInErrorMessage))
    }

    private fun thenJsonPlayDataIsEqualTo(jsonPlayerDataString: String) {
        Assertions.assertEquals(playService.getJsonPlayData(),jsonPlayerDataString)
    }
}
