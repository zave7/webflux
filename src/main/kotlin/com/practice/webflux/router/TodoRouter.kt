package com.practice.webflux.router

import com.practice.webflux.handler.TodoHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunctions.*
import org.springframework.web.reactive.function.server.router

@Configuration
class TodoRouter(private val handler : TodoHandler) {

    @Bean
    fun routerFunction() = nest(
        path("/todos"),
        router {
            listOf(
                GET("/", handler::getAll),
                GET("/{id}", handler::getById),
                POST("/", handler::save),
                PUT("/{id}/done", handler::done),
                DELETE("/{id}", handler::delete))
        }
    )
}