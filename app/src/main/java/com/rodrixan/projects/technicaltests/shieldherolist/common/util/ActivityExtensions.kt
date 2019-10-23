package com.rodrixan.projects.technicaltests.shieldherolist.common.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.transition.TransitionInflater
import com.example.rodri.projectlist.common.fragment.BaseFragment
import com.example.rodri.projectlist.common.util.loadFromDrawable
import com.rodrixan.projects.technicaltests.shieldherolist.R
import com.rodrixan.projects.technicaltests.shieldherolist.common.DetailTransitionConstants
import kotlinx.android.synthetic.main.activity_heroes.*

fun FragmentActivity.goToFragment(fragment: BaseFragment,
                                  containerId: Int = R.id.main_container,
                                  addToBackStack: Boolean = true) {

    val transaction = supportFragmentManager.beginTransaction()
    if (addToBackStack) {
        transaction.addToBackStack(fragment.fragmentTag)
    }

    transaction.replace(containerId, fragment, fragment.fragmentTag).commit()
}

fun FragmentActivity.goToDetailFragment(currentFragment: BaseFragment,
                                        newFragment: BaseFragment,
                                        transitionTag: String,
                                        sharedView: View,
                                        containerId: Int = R.id.main_container) {
    currentFragment.apply {
        sharedElementReturnTransition = TransitionInflater.from(this@goToDetailFragment).inflateTransition(
                DetailTransitionConstants.DETAIL_TRANSITION_RES_ID)
        exitTransition = TransitionInflater.from(this@goToDetailFragment).inflateTransition(
                DetailTransitionConstants.FADE_TRANSITION_RES_ID)
    }

    newFragment.apply {
        sharedElementEnterTransition = TransitionInflater.from(this@goToDetailFragment).inflateTransition(
                DetailTransitionConstants.DETAIL_TRANSITION_RES_ID)
        enterTransition = TransitionInflater.from(this@goToDetailFragment).inflateTransition(
                DetailTransitionConstants.FADE_TRANSITION_RES_ID)
        postponeEnterTransition() //wait for animation to load
    }

    supportFragmentManager.beginTransaction().apply {
        replace(containerId, newFragment, newFragment.fragmentTag)
        addToBackStack(transitionTag)
        addSharedElement(sharedView, ViewCompat.getTransitionName(sharedView) ?: "")
        commit()
    }
}

fun FragmentActivity.showLoading(show: Boolean = true) {
    ivLoading.loadFromDrawable(R.raw.shield_loading)
    ivLoading.visibility = if (show) View.VISIBLE else View.GONE
}

fun AppCompatActivity.showLogoNavigation() {
    toolbar.setNavigationIcon(R.drawable.shield_logo_white)
}

fun AppCompatActivity.showBackArrowNavigation() {
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
}