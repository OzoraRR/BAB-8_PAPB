package com.example.navigationlab.ui.home

import com.example.navigationlab.data.model.Student

data class HomeUiState(
    val students: List<Student> = emptyList(),
    val searchQuery: String = "",
    val resultQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
