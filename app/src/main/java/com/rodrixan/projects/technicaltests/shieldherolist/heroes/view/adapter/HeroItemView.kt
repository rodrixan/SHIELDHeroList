package com.rodrixan.projects.technicaltests.shieldherolist.heroes.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.rodrixan.projects.technicaltests.shieldherolist.R
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero

class HeroItemView(parent: ViewGroup) : ConstraintLayout(parent.context) {

    private val rootView: ConstraintLayout
    val heroAlias: TextView
    val heroRealName: TextView
    val heroAvatar: ImageView

    var data: SuperHero? = null
    var onClickListener: ((SuperHero, View) -> Unit)? = null
        set(value) {
            field = value
            rootView.setOnClickListener { data?.let { onClickListener?.invoke(it, heroAvatar) } }
        }

    init {
        inflate(context, R.layout.rv_item_hero, this)
        rootView = findViewById(R.id.heroRootView)
        heroAlias = findViewById(R.id.heroAlias)
        heroRealName = findViewById(R.id.heroRealName)
        heroAvatar = findViewById(R.id.heroAvatar)
    }
}