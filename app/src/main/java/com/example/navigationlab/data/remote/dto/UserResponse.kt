package com.example.navigationlab.data.remote.dto

data class UserResponse(
    val users: List<UserDto>? = null,
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0
)
