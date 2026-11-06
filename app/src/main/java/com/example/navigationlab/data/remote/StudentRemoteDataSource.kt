package com.example.navigationlab.data.remote
class StudentRemoteDataSource(private val api: StudentApiService) {
    suspend fun getStudents() = requireNotNull(api.getUsers().users) { "Respons daftar profil tidak valid." }
    suspend fun getStudent(id: Int) = api.getUser(id)
    suspend fun searchStudents(query: String) = requireNotNull(api.searchUsers(query).users) { "Respons daftar profil tidak valid." }
}
