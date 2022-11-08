package com.waigel.testresultapi.controllers

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping()
@Hidden
class SwaggerController {

    @RequestMapping()
    fun swaggerOnRootLevel(httpServletResponse: HttpServletResponse) {
        httpServletResponse.setHeader("Location", "/swagger-ui.html")
        httpServletResponse.status = 302
    }
}

