package com.rodrixan.projects.technicaltests.shieldherolist.common.di

import com.rodrixan.projects.technicaltests.shieldherolist.heroes.repository.HeroesRepository
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.repository.ShieldHeroRepository
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel.ShieldHeroDetailViewModel
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel.ShieldHeroesListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val heroModule = module {
    single<HeroesRepository> { ShieldHeroRepository() }
    viewModel { ShieldHeroesListViewModel(get()) }
    viewModel { ShieldHeroDetailViewModel(get()) }
}