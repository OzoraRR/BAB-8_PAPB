package com.example.navigationlab.data.repository
import com.example.navigationlab.data.remote.StudentRemoteDataSource
import com.example.navigationlab.data.remote.dto.toStudent
class DefaultStudentRepository(private val remoteDataSource: StudentRemoteDataSource) : StudentRepository {
    override suspend fun getStudents() = remoteDataSource.getStudents().map { it.toStudent() }
    override suspend fun getStudent(id: Int) = remoteDataSource.getStudent(id).toStudent()
    override suspend fun searchStudents(query: String) = remoteDataSource.searchStudents(query).map { it.toStudent() }
}
