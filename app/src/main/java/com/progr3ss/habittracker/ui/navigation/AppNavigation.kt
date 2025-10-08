package com.progr3ss.habittracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.progr3ss.habittracker.ui.screens.ai.AIAssistantScreen
import com.progr3ss.habittracker.ui.screens.auth.LoginScreen
import com.progr3ss.habittracker.ui.screens.auth.RegisterScreen
import com.progr3ss.habittracker.ui.screens.habit.AddHabitScreen
import com.progr3ss.habittracker.ui.screens.home.HomeScreen
import com.progr3ss.habittracker.ui.screens.profile.EditProfileScreen
import com.progr3ss.habittracker.ui.screens.profile.ProfileScreen
import com.progr3ss.habittracker.ui.screens.schedule.CreateScheduleScreen
import com.progr3ss.habittracker.ui.screens.schedule.ScheduleDetailsScreen
import com.progr3ss.habittracker.ui.screens.splash.SplashScreen
import com.progr3ss.habittracker.ui.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                isLoggedIn = isLoggedIn,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCreateSchedule = {
                    navController.navigate(Screen.CreateSchedule.route)
                },
                onNavigateToScheduleDetails = { scheduleId ->
                    navController.navigate(Screen.ScheduleDetails.createRoute(scheduleId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToAI = {
                    navController.navigate(Screen.AIAssistant.route)
                }
            )
        }

        composable(Screen.CreateSchedule.route) {
            CreateScheduleScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAddHabit = {
                    navController.navigate(Screen.AddHabit.route)
                }
            )
        }

        composable(Screen.ScheduleDetails.route) {
            ScheduleDetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AddHabit.route) {
            AddHabitScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToEditProfile = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AIAssistant.route) {
            AIAssistantScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
