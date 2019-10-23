package com.rodrixan.projects.technicaltests.shieldherolist.heroes.view.adapter

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.example.rodri.projectlist.common.util.loadFromUrl
import com.rodrixan.projects.technicaltests.shieldherolist.common.DetailTransitionConstants
import com.rodrixan.projects.technicaltests.shieldherolist.common.adapter.BaseAdapterDiffCallback
import com.rodrixan.projects.technicaltests.shieldherolist.common.adapter.RecyclerViewBaseAdapter
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero

class HeroAdapter : RecyclerViewBaseAdapter<SuperHero, HeroItemView>(HeroesDiffCallback()) {

    override fun bind(data: SuperHero, itemView: HeroItemView, position: Int) {

        with(itemView) {
            this.data = data
            heroAlias.text = data.name
            heroRealName.text = data.realName
            heroAvatar.loadFromUrl(data.photo)

            ViewCompat.setTransitionName(heroAvatar,
                    DetailTransitionConstants.DETAIL_TRANSITION_NAME + "_$heroAlias")

            onClickListener = this@HeroAdapter.onClickListener
        }

    }

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): HeroItemView = HeroItemView(
            parent)
}

private class HeroesDiffCallback : BaseAdapterDiffCallback<SuperHero>() {
    override fun areItemsTheSame(old: SuperHero, new: SuperHero): Boolean {
        val equalName = old.name == new.name
        val equalRealName = old.realName == new.realName
        return equalName && equalRealName
    }
}