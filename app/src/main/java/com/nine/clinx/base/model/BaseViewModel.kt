package com.nine.clinx.base.model

import com.nine.clinx.mvi.*

abstract class BaseViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> : AbstractMviViewModel<I, S, E>() {

}