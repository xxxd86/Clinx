package com.nine.clinx.dynamic

import com.nine.clinx.mvi.*

sealed interface ViewIntent : MviIntent {
    object Initial : ViewIntent
}

data class ViewState(
    val isLoading: Boolean,
    val keyword: String,
    val resultData: List<String>
) : MviViewState {
    companion object {
        fun initial() = ViewState(
            isLoading = true,
            keyword = "",
            resultData = emptyList()
        )
    }
}

sealed interface SingleEvent : MviSingleEvent {
    sealed interface UI : SingleEvent {
        object Success : UI

    }
}

internal sealed interface PartialChange {
    fun reduce(vs: ViewState): ViewState
    sealed class UI : PartialChange {
        override fun reduce(vs: ViewState): ViewState {
            return when (this) {
                is Success -> vs.copy()

            }
        }

        object Success : UI()
    }
}