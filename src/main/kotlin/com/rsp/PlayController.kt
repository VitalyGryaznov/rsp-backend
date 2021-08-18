package com.rsp

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post


@Controller("/")
class PlayController {

    data class Request (var customerChoice: String)

    @Post("/play", consumes = ["application/json"], processes = ["application/json"])
    fun performThePlay(@Body customerChoice: Request): MutableHttpResponse<String?>? {
        val playService = PlayService()
        playService.playData.customerChoice = customerChoice.customerChoice
        playService.setRandomComputerChoice()
        playService.performThePlay()
        return HttpResponse
                .status<String>(HttpStatus.OK)
                .body(playService.getJsonPlayData())
                .header("content-type", "application/json")
    }
}
