package com.example.navigationlab.data.repository
import com.example.navigationlab.data.model.Student
interface StudentRepository {
    suspend fun getStudents(): List<Student>
    suspend fun getStudent(id: Int): Student
    suspend fun searchStudents(query: String): List<Student>
}
