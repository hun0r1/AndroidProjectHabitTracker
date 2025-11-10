package com.digiventure.ventnote.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.digiventure.ventnote.data.persistence.NoteModel
import com.digiventure.ventnote.feature.backup.BackupPage
import com.digiventure.ventnote.feature.note_creation.NoteCreationPage
import com.digiventure.ventnote.feature.note_detail.NoteDetailPage
import com.digiventure.ventnote.feature.notes.NotesPage
import com.digiventure.ventnote.feature.share_preview.SharePreviewPage
import com.digiventure.ventnote.ui.AuthPage

@Composable
fun NavGraph(navHostController: NavHostController, openDrawer: () -> Unit) {
    val emptyString = ""
    val noteIdNavArgument = "noteId"
    val noteDataNavArgument = "noteData"
    val stringZero = "0"

    NavHost(
        navController = navHostController,
        startDestination = Route.AuthPage.routeName,
    ) {
        composable(Route.AuthPage.routeName) {
            AuthPage(
                onSuccess = {
                    navHostController.navigate(Route.NotesPage.routeName) {
                        popUpTo(Route.AuthPage.routeName) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        // keep plain route for compatibility - pass empty filter
        composable(Route.NotesPage.routeName) {
            NotesPage(navHostController = navHostController, openDrawer = openDrawer, filter = emptyString)
        }

        // new optional query param route for filter, lehessen filtralni a jegyzeteket
        composable(
            route = "${Route.NotesPage.routeName}?filter={filter}",
            arguments = listOf(navArgument("filter") {
                type = NavType.StringType
                defaultValue = emptyString
            })
        ) {
            val filter = it.arguments?.getString("filter") ?: emptyString
            NotesPage(navHostController = navHostController, openDrawer = openDrawer, filter = filter)
        }

        composable(
            route = "${Route.NoteDetailPage.routeName}/{${noteIdNavArgument}}",
            arguments = listOf(navArgument(noteIdNavArgument) {
                type = NavType.StringType
                defaultValue = emptyString
            })
        ) {

            NoteDetailPage(navHostController = navHostController,
                id = it.arguments?.getString(noteIdNavArgument) ?: stringZero)
        }
        composable(Route.NoteCreationPage.routeName) {
            NoteCreationPage(navHostController = navHostController)
        }
        composable(
            route = "${Route.SharePreviewPage.routeName}/{${noteDataNavArgument}}",
            arguments = listOf(navArgument(noteDataNavArgument) {
                type = NoteModelParamType()
            })
        ) {
            val note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.arguments?.getParcelable(noteDataNavArgument, NoteModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.arguments?.getParcelable(noteDataNavArgument)
            }
            SharePreviewPage(navHostController = navHostController, note = note)
        }
        composable(Route.BackupPage.routeName) {
            BackupPage(navHostController = navHostController)
        }
    }
}