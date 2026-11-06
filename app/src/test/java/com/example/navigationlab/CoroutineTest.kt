package com.example.navigationlab
import com.example.navigationlab.data.model.Student
import com.example.navigationlab.data.repository.StudentRepository
import com.example.navigationlab.ui.home.HomeViewModel
import com.example.navigationlab.ui.detail.DetailViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTest {
    class Repo: StudentRepository {
        val calls = mutableListOf<String>()
        var fail = false
        var slow = false
        var cancelled = false
        val student = Student(102,"John Doe","john@example.com","Brown University")
        override suspend fun getStudents(): List<Student> { calls += "list"; if(fail) throw IOException(); return listOf(student) }
        override suspend fun searchStudents(query:String): List<Student> {
            calls += query
            if(slow && query == "old") try { delay(10000) } finally { cancelled=true }
            if(fail) throw IOException()
            return if(query == "none") emptyList() else listOf(student)
        }
        override suspend fun getStudent(id:Int): Student { calls += "detail:$id"; if(fail) throw IOException(); return student.copy(id=id) }
    }
    private fun check(block:suspend TestScope.(Repo)->Unit) = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try { block(Repo()) } finally { Dispatchers.resetMain() }
    }
    @Test fun initialRequestOnlyOnce() = check { r ->
        val vm=HomeViewModel(r); runCurrent(); assertTrue(r.calls.isEmpty()); advanceTimeBy(500); runCurrent()
        assertEquals(listOf("list"),r.calls); assertEquals(1,vm.uiState.value.students.size)
    }
    @Test fun debounceAndDuplicateQuery() = check { r ->
        val vm=HomeViewModel(r); runCurrent(); vm.updateSearchQuery("j"); advanceTimeBy(200); vm.updateSearchQuery("john")
        advanceTimeBy(499); runCurrent(); assertTrue(r.calls.isEmpty()); advanceTimeBy(1); runCurrent()
        assertEquals(listOf("john"),r.calls); vm.updateSearchQuery("john"); advanceTimeBy(1000); runCurrent(); assertEquals(1,r.calls.size)
    }
    @Test fun refreshEmptyAndActiveQuery() = check { r ->
        val vm=HomeViewModel(r); advanceTimeBy(501); runCurrent(); vm.refresh(); runCurrent(); assertEquals(listOf("list","list"),r.calls)
        vm.updateSearchQuery("John"); advanceTimeBy(501); runCurrent(); vm.refresh(); runCurrent(); assertEquals(listOf("list","list","John","John"),r.calls)
    }
    @Test fun errorRefreshRecoversWithSameQuery() = check { r ->
        val vm=HomeViewModel(r); r.fail=true; vm.updateSearchQuery("John"); advanceTimeBy(501); runCurrent()
        assertNotNull(vm.uiState.value.errorMessage); r.fail=false; vm.refresh(); runCurrent()
        assertNull(vm.uiState.value.errorMessage); assertFalse(vm.uiState.value.isLoading); assertEquals("John",r.calls.last())
    }
    @Test fun latestCancelsOldWork() = check { r ->
        r.slow=true; val vm=HomeViewModel(r); vm.updateSearchQuery("old"); advanceTimeBy(501); runCurrent()
        assertTrue(vm.uiState.value.isLoading); vm.updateSearchQuery("new"); advanceTimeBy(501); runCurrent()
        assertTrue(r.cancelled); assertEquals("new",vm.uiState.value.resultQuery); assertNull(vm.uiState.value.errorMessage)
    }
    @Test fun whitespaceRestoresInitialList() = check { r ->
        val vm=HomeViewModel(r); vm.updateSearchQuery("   "); advanceTimeBy(501); runCurrent(); assertEquals(listOf("list"),r.calls)
    }
    @Test fun emptyResultIsSuccess() = check { r ->
        val vm=HomeViewModel(r); vm.updateSearchQuery("none"); advanceTimeBy(501); runCurrent(); assertTrue(vm.uiState.value.students.isEmpty()); assertNull(vm.uiState.value.errorMessage)
    }
    @Test fun detailRetryAndSameIdNoRefetch() = check { r ->
        val vm=DetailViewModel(r); r.fail=true; vm.loadStudent(102); runCurrent(); assertNotNull(vm.uiState.value.errorMessage)
        r.fail=false; vm.retry(); runCurrent(); assertEquals(102,vm.uiState.value.student?.id)
        vm.loadStudent(102); runCurrent(); assertEquals(2,r.calls.size)
    }
}
