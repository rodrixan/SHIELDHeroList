package com.rodrixan.projects.technicaltests.shieldherolist.heroes.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rodri.projectlist.common.fragment.BaseFragment
import com.rodrixan.projects.technicaltests.shieldherolist.R
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero
import com.rodrixan.projects.technicaltests.shieldherolist.common.util.goToDetailFragment
import com.rodrixan.projects.technicaltests.shieldherolist.common.util.showLoading
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.view.adapter.HeroAdapter
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel.HeroesListViewModel
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel.ShieldHeroesListViewModel
import kotlinx.android.synthetic.main.fragment_heroes_list.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class HeroesListFragment : BaseFragment() {

    private val viewModel: HeroesListViewModel by viewModel<ShieldHeroesListViewModel>()

    private val heroesListObserver = Observer<AppInternalData<List<SuperHero>>> {
        when (it) {
            is AppInternalData.Loading -> {
                act.showLoading()
            }
            is AppInternalData.Error -> {
                act.showLoading(false)
                Timber.e("[heroesListObserver] Error! ${it.message}")
                toast(it.message)
            }
            is AppInternalData.Success -> {
                act.showLoading(false)
                Timber.d("[heroesListObserver] Received ${it.data.size} projects")
                heroAdapter.updateItems(it.data)
            }
        }
    }

    private val heroAdapter = HeroAdapter().apply {
        onClickListener = { hero, view ->
            act.goToDetailFragment(this@HeroesListFragment,
                    HeroDetailFragment.newInstance(hero.name ?: "", view.transitionName),
                    view.transitionName,
                    view)
        }
    }

    //simple state management
    private var isSearching = false
    private var currentSearchQuery: String? = null

    private val searchQueryListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText == null || newText.length < MIN_QUERY_TEXT_SIZE) {
                return true
            }
            currentSearchQuery = newText
            viewModel.queryHeroes(newText)
            return true
        }
    }

    private val searchActionListener = object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            isSearching = true
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            viewModel.refreshSuperHeroesList()
            isSearching = false
            currentSearchQuery = ""
            return true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true //simple state management
        setHasOptionsMenu(true)
        viewModel.heroesList.observe(this, heroesListObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list, menu)

        val itemMenuSearch = menu.findItem(R.id.action_search)

        itemMenuSearch?.setOnActionExpandListener(searchActionListener)

        val searchActionView = itemMenuSearch?.actionView as SearchView?
        searchActionView?.apply {
            queryHint = getString(R.string.action_search_query)
            setOnQueryTextListener(searchQueryListener)
        }

        if (isSearching && currentSearchQuery?.isNotEmpty() == true) {
            itemMenuSearch?.expandActionView()
            searchActionView?.setQuery(currentSearchQuery, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_heroes_list,
            container,
            false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHeroes.layoutManager = LinearLayoutManager(act, RecyclerView.VERTICAL, false)
        rvHeroes.adapter = heroAdapter

        setToolbar(getString(R.string.app_name), false)
    }

    override fun onResume() {
        super.onResume()
        if (isSearching && currentSearchQuery?.isNotEmpty() == true) {
            Timber.d("[onResume] is searching")
            return
        }
        Timber.d("[onResume] query heroes")
        viewModel.refreshSuperHeroesList()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.heroesList.removeObserver(heroesListObserver)
    }

    override val fragmentTag = HeroesListFragment.javaClass.simpleName

    companion object {
        private const val MIN_QUERY_TEXT_SIZE = 2

        fun newInstance() = HeroesListFragment()
    }
}