package com.digiventure.ventnote.feature.note_detail

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import com.digiventure.ventnote.R
import com.digiventure.ventnote.commons.Constants.EMPTY_STRING
import com.digiventure.ventnote.commons.TestTags
import com.digiventure.ventnote.components.LockScreenOrientation
import com.digiventure.ventnote.components.dialog.LoadingDialog
import com.digiventure.ventnote.components.dialog.TextDialog
import com.digiventure.ventnote.data.persistence.NoteModel
import com.digiventure.ventnote.feature.note_detail.components.navbar.EnhancedBottomAppBar
import com.digiventure.ventnote.feature.note_detail.components.navbar.NoteDetailAppBar
import com.digiventure.ventnote.feature.note_detail.components.section.NoteSection
import com.digiventure.ventnote.feature.note_detail.components.section.TitleSection
import com.digiventure.ventnote.feature.note_detail.viewmodel.NoteDetailPageBaseVM
import com.digiventure.ventnote.feature.note_detail.viewmodel.NoteDetailPageVM
import com.digiventure.ventnote.navigation.PageNavigation
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailPage(
    navHostController: NavHostController,
    viewModel: NoteDetailPageBaseVM = hiltViewModel<NoteDetailPageVM>(),
    id: String
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val navigationActions = remember(navHostController) {
        PageNavigation(navHostController)
    }

    // Precompute strings in composable context
    val addImageText = stringResource(id = R.string.add) + " image"
    val successUpdatedText = stringResource(R.string.successfully_updated)
    val titleTextFieldLabel = stringResource(R.string.title_textField)
    val bodyTextFieldLabel = stringResource(R.string.body_textField)
    val titleInputPlaceholder = stringResource(R.string.title_textField_input)
    val bodyInputPlaceholder = stringResource(R.string.body_textField_input)

    // State observers
    val noteDetailState by viewModel.noteDetail.observeAsState()
    val data = noteDetailState?.getOrNull()
    val isEditingState by viewModel.isEditing
    val loadingState by viewModel.loader.observeAsState()

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    // Dialog states
    val requiredDialogState = remember { mutableStateOf(false) }
    val deleteDialogState = remember { mutableStateOf(false) }
    val cancelDialogState = remember { mutableStateOf(false) }
    val openLoadingDialog = remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    // Image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (isEditingState && uris.isNotEmpty()) {
            val strings = uris.map { it.toString() }
            viewModel.imageUris.value = (viewModel.imageUris.value + strings).distinct()
        }
    }

    val initData = {
        data?.let {
            viewModel.titleText.value = it.title
            viewModel.descriptionText.value = it.note
            viewModel.imageUris.value = it.images
        }
    }

    val updateNote = remember(data, scope, focusManager, successUpdatedText) {
        {
            val titleText = viewModel.titleText.value
            val descriptionText = viewModel.descriptionText.value

            if (titleText.isEmpty() || descriptionText.isEmpty()) {
                requiredDialogState.value = true
            } else {
                data?.let { noteData ->
                    focusManager.clearFocus()
                    scope.launch {
                        val updatedNote = noteData.copy(
                            title = titleText,
                            note = descriptionText,
                            images = viewModel.imageUris.value
                        )
                        viewModel.updateNote(updatedNote)
                            .onSuccess {
                                viewModel.isEditing.value = false
                                snackBarHostState.showSnackbar(
                                    message = successUpdatedText,
                                    withDismissAction = true
                                )
                            }
                            .onFailure { error ->
                                snackBarHostState.showSnackbar(
                                    message = error.message ?: "",
                                    withDismissAction = true
                                )
                            }
                    }
                }
            }
        }
    }

    // Effects
    LaunchedEffect(id) { viewModel.getNoteDetail(id.toInt()) }
    LaunchedEffect(noteDetailState) { initData() }
    LaunchedEffect(isEditingState) { if (!isEditingState) focusManager.clearFocus() }
    LaunchedEffect(loadingState) { openLoadingDialog.value = loadingState == true }

    // Scroll behavior
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )

    val haptics = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            NoteDetailAppBar(
                isEditing = isEditingState,
                onBackPressed = {
                    navigationActions.navigateToNotesPage()
                },
                scrollBehavior = scrollBehavior,
                onSharePressed = {
                    data?.let {
                        val json = Uri.encode(Gson().toJson(it))
                        navigationActions.navigateToSharePage(json)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusManager.clearFocus()
                    },
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    TitleSection(
                        viewModel = viewModel,
                        isEditingState = isEditingState,
                        titleTextField = titleTextFieldLabel,
                        titleInput = titleInputPlaceholder
                    )
                }

                item {
                    NoteSection(
                        viewModel = viewModel,
                        isEditingState = isEditingState,
                        bodyTextField = bodyTextFieldLabel,
                        bodyInput = bodyInputPlaceholder
                    )
                }

                // Add image while editing
                if (isEditingState) {
                    item {
                        IconButton(onClick = { launcher.launch("image/*") }) {
                            Icon(
                                imageVector = Icons.Filled.AddAPhoto,
                                contentDescription = addImageText
                            )
                        }
                    }
                }

                // Thumbnails preview of current images
                items(viewModel.imageUris.value) { uriString ->
                    AsyncImage(
                        model = uriString,
                        contentDescription = null
                    )
                }
            }
        },
        bottomBar = {
            EnhancedBottomAppBar(
                isEditing = isEditingState,
                onEditClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    viewModel.isEditing.value = true
                },
                onSaveClick = { updateNote() },
                onDeleteClick = { deleteDialogState.value = true },
                onCancelClick = { cancelDialogState.value = true }
            )
        },
        modifier = Modifier
            .semantics { testTag = TestTags.NOTE_DETAIL_PAGE }
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
    )

    // Dialogs
    val titlePlaceholderText = titleInputPlaceholder
    val notePlaceholderText = bodyInputPlaceholder

    val missingFieldName = remember(
        viewModel.titleText.value,
        viewModel.descriptionText.value
    ) {
        when {
            viewModel.titleText.value.isEmpty() -> titlePlaceholderText
            viewModel.descriptionText.value.isEmpty() -> notePlaceholderText
            else -> EMPTY_STRING
        }
    }

    if (requiredDialogState.value) {
        TextDialog(
            title = stringResource(R.string.required_title),
            description = stringResource(R.string.required_confirmation_text, missingFieldName),
            isOpened = requiredDialogState.value,
            onDismissCallback = { requiredDialogState.value = false },
            onConfirmCallback = { requiredDialogState.value = false }
        )
    }

    if (cancelDialogState.value) {
        TextDialog(
            title = stringResource(R.string.cancel_title),
            description = stringResource(R.string.cancel_confirmation_text),
            isOpened = cancelDialogState.value,
            onDismissCallback = { cancelDialogState.value = false },
            onConfirmCallback = {
                viewModel.isEditing.value = false
                cancelDialogState.value = false
                initData()
            }
        )
    }

    if (deleteDialogState.value) {
        TextDialog(
            isOpened = deleteDialogState.value,
            onDismissCallback = { deleteDialogState.value = false },
            onConfirmCallback = {
                data?.let { noteData ->
                    scope.launch {
                        viewModel.deleteNoteList(noteData)
                            .onSuccess {
                                deleteDialogState.value = false
                                navHostController.navigateUp()
                            }
                            .onFailure { error ->
                                deleteDialogState.value = false
                                snackBarHostState.showSnackbar(
                                    message = error.message ?: "",
                                    withDismissAction = true
                                )
                            }
                    }
                }
            }
        )
    }

    if (openLoadingDialog.value) {
        LoadingDialog(
            isOpened = openLoadingDialog.value,
            onDismissCallback = { openLoadingDialog.value = false }
        )
    }

    BackHandler {
        if (viewModel.isEditing.value) {
            cancelDialogState.value = true
        } else {
            navHostController.navigateUp()
        }
    }
}

@Preview
@Composable
fun NoteDetailPagePreview() {
    // Provide a lightweight mock that is not a ViewModel to satisfy lint
    val loaderLD = remember { MutableLiveData<Boolean>(false) }
    val noteDetailLD = remember {
        MutableLiveData<Result<NoteModel>>(Result.success(NoteModel(0, "Sample", "Body", images = emptyList())))
    }
    val titleState = remember { mutableStateOf("Sample") }
    val descState = remember { mutableStateOf("Body") }
    val editingState = remember { mutableStateOf(false) }
    val imagesState = remember { mutableStateOf<List<String>>(emptyList()) }

    val mockVM = object : NoteDetailPageBaseVM {
        override val loader = loaderLD
        override var noteDetail = noteDetailLD
        override var titleText = titleState
        override var descriptionText = descState
        override var imageUris = imagesState
        override var isEditing = editingState
        override suspend fun getNoteDetail(id: Int) {}
        override suspend fun updateNote(note: NoteModel) = Result.success(true)
        override suspend fun deleteNoteList(vararg notes: NoteModel) = Result.success(true)
    }

    NoteDetailPage(
        navHostController = rememberNavController(),
        viewModel = mockVM,
        id = "0"
    )
}