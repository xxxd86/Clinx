package com.nine.clinx.ui.page.home

import android.os.Parcelable
import com.nine.clinx.app.AppSystemSetManage
import com.nine.clinx.mvi.MviIntent
import com.nine.clinx.mvi.MviSingleEvent
import com.nine.clinx.mvi.MviViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize

sealed interface HomeViewIntent : MviIntent {
    object Initial : HomeViewIntent
    object GetNoteFolder : HomeViewIntent
    object GetNoteInfo : HomeViewIntent
    object Clean : HomeViewIntent
    data class CurrentNoteFolder(val folderEntity: String) : HomeViewIntent
    data class OpenNoteEntity(val noteEntity: String) : HomeViewIntent
    object ConsentAgreement : HomeViewIntent
}


@Parcelize
data class HomeViewState(
    val searchContent: String,
    val postInfo: List<String>,
    val postEntity: String?,
    val noteFolder: String?,
    val consentAgreement:Boolean,
    val routeUrl: String
    ) : MviViewState, Parcelable {
    companion object {
        fun initial() = HomeViewState(
            searchContent = "",
            postInfo = emptyList(),
            postEntity = null,
            noteFolder = null,
            consentAgreement = AppSystemSetManage.consentAgreement,
            routeUrl = "Home"
        )
    }
}


sealed interface HomeSingleEvent : MviSingleEvent {
    sealed interface Refresh : HomeSingleEvent {
        object Success : Refresh
    }
}

internal sealed interface HomePartialChange {
    fun reduce(vs: HomeViewState): HomeViewState


    sealed class NoteData : HomePartialChange {
        override fun reduce(vs: HomeViewState): HomeViewState {
            return when (this) {
                is PostFolders -> vs
                is PostInfoData -> vs.copy(postInfo = noteInfo)
                is PostEntityData -> vs.copy(postEntity = noteEntity)
            }
        }

        data class PostFolders(val noteFolders: Flow<List<String>>) : NoteData()
        data class PostInfoData(val noteInfo: List<String>) : NoteData()
        data class PostEntityData(val noteEntity: String) : NoteData()
    }

    sealed class UI : HomePartialChange {
        override fun reduce(vs: HomeViewState): HomeViewState {
            return when (this) {

                is Close -> {
                    vs.copy(searchContent = "")
                }
                is Init-> vs
                is Route -> {
                    vs.copy(routeUrl = routeUrl)
                }
            }
        }

        object Init : UI()
        object Close : UI()
        data class Route(val routeUrl:String) :UI()
    }
}
