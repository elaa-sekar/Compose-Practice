package com.practice.composetest.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.composetest.data.models.SuperHero
import com.practice.composetest.data.repositories.SuperHeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuperHeroViewModel @Inject constructor(private val repository: SuperHeroRepository) :
    ViewModel() {

    private val _superHeroesFlow = MutableStateFlow(emptyList<SuperHero>())
    val superHeroesFlow: StateFlow<List<SuperHero>>
        get() = _superHeroesFlow

    init {
        viewModelScope.launch {
            repository.getSuperHeroes()?.let {
                _superHeroesFlow.emit(it)
            }
        }
    }

    fun updateSuperHero(isSelected: Boolean, index: Int) {
        viewModelScope.launch {
            _superHeroesFlow.value.toMutableList().let {
                val updateItem = it[index].copy(isSelected = isSelected)
                it[index] = updateItem
                _superHeroesFlow.value = it.toList()
            }
        }
    }
}