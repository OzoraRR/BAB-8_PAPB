package com.example.navigationlab.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigationlab.data.repository.DefaultStudentRepository
import com.example.navigationlab.data.remote.StudentRemoteDataSource
import com.example.navigationlab.data.remote.RetrofitClient
import com.example.navigationlab.data.repository.StudentRepository
import com.example.navigationlab.ui.detail.DetailViewModel
import com.example.navigationlab.ui.home.HomeViewModel
import com.example.navigationlab.ui.screen.AboutScreen
import com.example.navigationlab.ui.screen.DetailScreen
import com.example.navigationlab.ui.screen.HomeScreen
import com.example.navigationlab.ui.screen.ProfileScreen

@Composable
fun NavigationLabApp(
    navController: NavHostController = rememberNavController(),
    repositoryOverride: StudentRepository? = null
) {
    // Dependency manual dibuat sekali per komposisi level aplikasi.
    val dataSource = remember { StudentRemoteDataSource(RetrofitClient.apiService) }
    val repository = repositoryOverride ?: remember(dataSource) { DefaultStudentRepository(dataSource) }
    val homeFactory = remember(repository) { HomeViewModel.factory(repository) }
    val detailFactory = remember(repository) { DetailViewModel.factory(repository) }

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = Modifier.fillMaxSize().safeDrawingPadding()
    ) {
        composable(Routes.HOME) { backStackEntry ->
            val homeViewModel: HomeViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = homeFactory
            )
            val homeState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = homeState,
                searchQuery = searchQuery,
                onOpenDetail = { id -> navController.navigate(Routes.detail(id)) },
                onQueryChange = homeViewModel::updateSearchQuery,
                onRefresh = homeViewModel::refresh,
                onRetry = homeViewModel::retry,
                onOpenProfile = { navController.navigate(Routes.PROFILE) },
                onOpenAbout = { navController.navigate(Routes.ABOUT) }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument(Routes.STUDENT_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt(Routes.STUDENT_ID) ?: -1
            val detailViewModel: DetailViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = detailFactory
            )
            LaunchedEffect(studentId) {
                detailViewModel.loadStudent(studentId)
            }
            val detailState by detailViewModel.uiState.collectAsStateWithLifecycle()
            DetailScreen(
                uiState = detailState,
                onRetry = detailViewModel::retry,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
