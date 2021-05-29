package com.practice.webflux.domain.repository

import com.practice.webflux.domain.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long> {
}