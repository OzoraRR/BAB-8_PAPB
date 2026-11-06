package com.example.navigationlab.ui.home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.repository.StudentRepository
import com.example.navigationlab.ui.toUserMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val refreshRequests = MutableSharedFlow<String>()
    init { observeSearch() }
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            merge(searchQuery.debounce(500).distinctUntilChanged(), refreshRequests)
                .collectLatest { query -> fetch(query) }
        }
    }
    fun refresh() {
        viewModelScope.launch { refreshRequests.emit(searchQuery.value) }
    }
    fun retry() = refresh()
    private suspend fun fetch(raw: String) {
        val query = raw.trim()
        _uiState.value = _uiState.value.copy(students = emptyList(), resultQuery = query, isLoading = true, errorMessage = null)
        try {
            val students = if (query.isBlank()) repository.getStudents() else repository.searchStudents(query)
            _uiState.value = _uiState.value.copy(students = students, isLoading = false)
        } catch (e: CancellationException) { throw e
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.toUserMessage())
        }
    }
    companion object {
        fun factory(repository: StudentRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(HomeViewModel::class.java))
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(repository) as T
                }
            }
    }
}
