package com.rodrixan.projects.technicaltests.shieldherolist.heroes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rodrixan.projects.technicaltests.shieldherolist.R
import com.rodrixan.projects.technicaltests.shieldherolist.common.util.goToFragment
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.view.HeroesListFragment
import kotlinx.android.synthetic.main.activity_heroes.*

class HeroesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heroes)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            goToFragment(HeroesListFragment.newInstance(), addToBackStack = false)
        }
    }
}

