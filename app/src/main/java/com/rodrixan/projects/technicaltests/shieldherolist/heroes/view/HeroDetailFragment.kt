package com.rodrixan.projects.technicaltests.shieldherolist.heroes.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.rodri.projectlist.common.fragment.BaseFragment
import com.example.rodri.projectlist.common.util.loadFromUrl
import com.rodrixan.projects.technicaltests.shieldherolist.R
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero
import com.rodrixan.projects.technicaltests.shieldherolist.common.util.showLoading
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel.HeroDetailViewModel
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel.ShieldHeroDetailViewModel
import kotlinx.android.synthetic.main.fragment_heroes_detail.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class HeroDetailFragment : BaseFragment() {

    private val viewModel: HeroDetailViewModel by viewModel<ShieldHeroDetailViewModel>()
    private val heroDetailsObserver = Observer<AppInternalData<SuperHero>> {
        when (it) {
            is AppInternalData.Loading -> {
                act.showLoading()
            }
            is AppInternalData.Error -> {
                act.showLoading(false)
                toast(it.message)
                act.onBackPressed()
            }
            is AppInternalData.Success -> {
                act.showLoading(false)
                loadData(it.data)
            }
        }
    }

    private var heroName: String = ""
    private var transitionName: String = ""

    private fun loadData(details: SuperHero) {
        with(details) {
            Timber.d("[loadData] Details for: ${details.name}")
            ivHeroAvatar.loadFromUrl(photo,
                    R.drawable.shield_logo_white,
                    { startPostponedEnterTransition() },
                    true)
            tvRealName.text = realName
            tvHeight.text = height
            tvPowers.text = power
            tvAbilities.text = abilities
            tvGroups.text = groupsFormatted
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.apply {
            getString(ARG_HERO_NAME_KEY)?.run {
                heroName = this

            }
            getString(ARG_TRANSITION_NAME)?.run {
                transitionName = this
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.heroDetails.observe(this, heroDetailsObserver)
        if (savedInstanceState == null) {
            //only load first time
            viewModel.queryHeroDetails(heroName)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                act.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_heroes_detail,
            container,
            false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar(heroName, true)

        ivHeroAvatar.transitionName = transitionName
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.heroDetails.removeObserver(heroDetailsObserver)
    }

    override val fragmentTag = HeroDetailFragment.javaClass.simpleName

    companion object {
        fun newInstance(heroName: String,
                        avatarTransitionName: String) = HeroDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_HERO_NAME_KEY, heroName)
                putString(ARG_TRANSITION_NAME, avatarTransitionName)
            }
        }

        const val ARG_HERO_NAME_KEY = "HeroName"
        const val ARG_TRANSITION_NAME = "TransitionName"
    }
}