package com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.repository.HeroesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShieldHeroDetailViewModel(val repository: HeroesRepository) : ViewModel(), HeroDetailViewModel {
    override val heroDetails: LiveData<AppInternalData<SuperHero>>
        get() = repository.heroDetail

    override fun queryHeroDetails(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHeroDetail(name)
        }
    }
}