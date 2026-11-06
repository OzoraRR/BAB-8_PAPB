package com.example.navigationlab.data.remote.dto

import com.example.navigationlab.data.model.Student

data class UserDto(
    val id: Int,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val university: String? = null
)

fun UserDto.toStudent() = Student(
    id = id,
    name = listOfNotNull(firstName, lastName).joinToString(" ").trim().ifBlank { "Tanpa nama" },
    email = email?.takeIf { it.isNotBlank() } ?: "-",
    university = university?.takeIf { it.isNotBlank() } ?: "-"
)
