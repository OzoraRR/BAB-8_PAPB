package com.example.navigationlab.ui.detail
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.repository.StudentRepository
import com.example.navigationlab.ui.toUserMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class DetailViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()
    private var request: Job? = null
    private var studentId: Int? = null
    fun loadStudent(id: Int) { if (studentId == id) return; studentId = id; fetch(id) }
    fun retry() { studentId?.let(::fetch) }
    private fun fetch(id: Int) {
        request?.cancel()
        request = viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            try { _uiState.value = DetailUiState(student = repository.getStudent(id), isLoading = false) }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.value = DetailUiState(isLoading = false, errorMessage = e.toUserMessage()) }
        }
    }
    companion object {
        fun factory(repository: StudentRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(DetailViewModel::class.java))
                    @Suppress("UNCHECKED_CAST")
                    return DetailViewModel(repository) as T
                }
            }
    }
}
