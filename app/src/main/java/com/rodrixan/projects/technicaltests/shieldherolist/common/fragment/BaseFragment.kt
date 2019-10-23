package com.example.rodri.projectlist.common.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rodrixan.projects.technicaltests.shieldherolist.common.util.showBackArrowNavigation
import com.rodrixan.projects.technicaltests.shieldherolist.common.util.showLogoNavigation
import org.jetbrains.anko.support.v4.act
import timber.log.Timber

abstract class BaseFragment : Fragment() {
    abstract val fragmentTag: String

    //Must be called IN onViewCreated
    fun setToolbar(title: String, hasBackButton: Boolean) {
        Timber.d("[setToolbar] tag: $fragmentTag} title: $title hasBackButton: $hasBackButton")

        act.setTitle(title)

        if (act is AppCompatActivity) {
            if (hasBackButton) {
                (act as AppCompatActivity).showBackArrowNavigation()
            } else {
                (act as AppCompatActivity).showLogoNavigation()
            }
        }
    }
}