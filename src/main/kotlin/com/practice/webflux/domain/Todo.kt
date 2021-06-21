package com.practice.webflux.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "todos")
data class Todo(
    @Lob
    @Column(name = "content")
    var content : String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long = 0

    @Column(name = "done")
    var done : Boolean = false

    @Column(name = "created_at")
    var createdAt : LocalDateTime = LocalDateTime.now()

    @Column
    var modifiedAt : LocalDateTime = createdAt
    override fun toString(): String {
        return "Todo(content=$content, id=$id, done=$done, createdAt=$createdAt, modifiedAt=$modifiedAt)"
    }

}