package com.progr3ss.habittracker.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object CreateSchedule : Screen("create_schedule")
    object ScheduleDetails : Screen("schedule_details/{scheduleId}") {
        fun createRoute(scheduleId: String) = "schedule_details/$scheduleId"
    }
    object AddHabit : Screen("add_habit")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object AIAssistant : Screen("ai_assistant")
}
