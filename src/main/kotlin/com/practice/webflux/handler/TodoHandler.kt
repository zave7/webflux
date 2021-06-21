package com.practice.webflux.handler

import com.practice.webflux.domain.Todo
import com.practice.webflux.domain.repository.TodoRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

@Component
class TodoHandler(private val repo : TodoRepository) {

    fun getAll(req : ServerRequest) : Mono<ServerResponse> = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body<List<Todo>>(Mono.just(repo.findAll()))
        .switchIfEmpty(ServerResponse.notFound().build())

    fun getAllLimitFive(req : ServerRequest) : Mono<ServerResponse> = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body<TodoList>(Mono.just(
            TodoList(
                repo.findAll()
                .sortedByDescending { it.id }
                .subList(0, req.pathVariable("limit").toInt()))))
        .switchIfEmpty(ServerResponse.notFound().build())

    fun getById(req : ServerRequest) : Mono<ServerResponse> = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body<Todo>(Mono.justOrEmpty(repo.findById(req.pathVariable("id").toLong())))
        .switchIfEmpty(ServerResponse.notFound().build())

    fun save(req : ServerRequest) : Mono<ServerResponse> = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(req.bodyToMono(Todo::class.java)
            .filter(Objects::nonNull)
            .flatMap {
                todo ->
                Mono.fromCallable {
                    println(todo.toString())
                    repo.save(todo)
                }.then(Mono.just(todo))
            }
        ).switchIfEmpty(ServerResponse.notFound().build())

    fun done(req : ServerRequest) : Mono<ServerResponse> = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.justOrEmpty(repo.findById(req.pathVariable("id").toLong()))
            .switchIfEmpty(Mono.empty())
            .filter(Objects::nonNull)
            .flatMap { todo ->
                Mono.fromCallable {
                    todo.done = true
                    todo.modifiedAt = LocalDateTime.now()
                    repo.save(todo)
                }.then(Mono.just(todo))
            }
        ).switchIfEmpty(ServerResponse.notFound().build())

    fun delete(req : ServerRequest) : Mono<ServerResponse> = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.justOrEmpty(repo.findById(req.pathVariable("id").toLong()))
            .switchIfEmpty(Mono.empty())
            .filter(Objects::nonNull)
            .flatMap { todo ->
                Mono.fromCallable {
                    repo.delete(todo)
                }.then(Mono.just(todo))
            }
        ).switchIfEmpty(ServerResponse.notFound().build())
}
data class TodoList(val list : List<Todo>)