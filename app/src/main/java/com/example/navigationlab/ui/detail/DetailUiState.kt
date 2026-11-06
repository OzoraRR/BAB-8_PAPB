package com.example.navigationlab.ui.detail

import com.example.navigationlab.data.model.Student

data class DetailUiState(val student: Student? = null, val isLoading: Boolean = true, val errorMessage: String? = null)
