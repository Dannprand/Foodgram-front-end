package com.example.foodgram.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodgram.enums.PagesEnum
import com.example.foodgram.ui.theme.Orange80
import com.example.foodgram.viewmodels.AuthenticationViewModel
import com.example.foodgram.viewmodels.HomeViewModel
import com.example.foodgram.viewmodels.LibraryViewModel
import com.example.foodgram.viewmodels.PostViewModel
import com.example.foodgram.viewmodels.UserViewModel
import com.example.foodgram.views.templates.CustomBottomNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FoodGramApp(
    navController: NavHostController = rememberNavController(),
    authenticationViewModel: AuthenticationViewModel = viewModel(factory = AuthenticationViewModel.Factory),
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory),
    postViewModel: PostViewModel = viewModel(factory = PostViewModel.Factory),
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    libraryViewModel: LibraryViewModel = viewModel(factory = LibraryViewModel.Factory)
) {
    val localContext = LocalContext.current
    val token = userViewModel.token.collectAsState()

    val excludedRoutes = listOf(PagesEnum.Login.name, PagesEnum.Start.name, PagesEnum.Register.name) // Excluded pages
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != null && currentRoute !in excludedRoutes) {
                CustomBottomNavigation(
                    currentScreenRoute = currentRoute,
                    onItemSelected = { item ->
                        if (item.route == PagesEnum.Profile.name && currentRoute != PagesEnum.PostDetail.name) {
                            userViewModel.setSelectedUserIDToOwner(navController)
                        }

                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (token.value != "Unknown" && token.value != "") {
                PagesEnum.Home.name
            } else {
                PagesEnum.Start.name
            },
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, if (currentRoute !in excludedRoutes) 64.dp else 0.dp)
        ) {
            composable(route = PagesEnum.Start.name) {
                StartView(
                    modifier = Modifier
                        .background(color = Orange80)
                        .fillMaxSize(),
                    navController = navController,
                    context = localContext
                )
            }

            composable(route = PagesEnum.Login.name) {
                LoginView(
                    modifier = Modifier
                        .background(color = Orange80)
                        .fillMaxSize(),
                    authenticationViewModel = authenticationViewModel,
                    navController = navController,
                    context = localContext
                )
            }

            composable(route = PagesEnum.Register.name) {
                RegisterView(
                    modifier = Modifier
                        .background(color = Orange80)
                        .fillMaxSize(),
                    authenticationViewModel = authenticationViewModel,
                    navController = navController,
                    context = localContext
                )
            }

            composable(route = PagesEnum.CreatePost.name) {
                CreatePostView(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize()
                        .padding(20.dp, 40.dp),
                    postViewModel = postViewModel,
                    navController = navController,
                    token = token.value,
                    context = localContext
                )
            }

            composable(route = PagesEnum.Profile.name) {
                ProfileView(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize()
                        .padding(20.dp, 40.dp),
                    userViewModel = userViewModel,
                    navController = navController,
                    postViewModel = postViewModel,
                    context = localContext
                )
            }

            composable(route = PagesEnum.Settings.name) {
                SettingsView(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize()
                        .padding(20.dp, 40.dp),
                    userViewModel = userViewModel,
                    navController = navController,
                    token = token.value,
                    context = localContext
                )
            }

            composable(route = PagesEnum.Home.name) {
                HomeView(homeViewModel = homeViewModel, postViewModel = postViewModel, userViewModel = userViewModel, navController = navController)
            }

            composable(route = PagesEnum.PostDetail.name) {
                PostDetailView(
                    postViewModel = postViewModel,
                    homeViewModel = homeViewModel,
                    userViewModel = userViewModel,
                    navController = navController,
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize()
                        .padding(20.dp, 40.dp)
                )
            }

            composable(route = PagesEnum.Library.name) {
                LibraryView(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize()
                        .padding(20.dp, 40.dp),
                    libraryViewModel = libraryViewModel,
                    navController = navController
                )
            }

            composable(route = PagesEnum.LibraryDetail.name) {
                LibraryDetailView(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize()
                        .padding(20.dp, 40.dp),
                    libraryViewModel = libraryViewModel,
                    token = token.value,
                    navController = navController
                )
            }
        }
    }
}