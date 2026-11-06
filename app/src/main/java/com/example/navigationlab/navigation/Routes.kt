package com.example.navigationlab.navigation

object Routes {
    const val HOME = "home"
    const val PROFILE = "profile"
    const val ABOUT = "about"
    const val STUDENT_ID = "studentId"
    const val DETAIL = "detail/{$STUDENT_ID}"

    fun detail(studentId: Int): String = "detail/$studentId"
}
