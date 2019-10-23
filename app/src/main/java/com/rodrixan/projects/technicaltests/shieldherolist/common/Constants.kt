package com.rodrixan.projects.technicaltests.shieldherolist.common

import com.rodrixan.projects.technicaltests.shieldherolist.BuildConfig
import com.rodrixan.projects.technicaltests.shieldherolist.R
import okhttp3.logging.HttpLoggingInterceptor

object RestConstants {
    const val BASE_URL = "https://api.myjson.com/"
    val HTTP_LOGGING_LEVEL = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}

object DetailTransitionConstants {
    const val FADE_TRANSITION_RES_ID = android.R.transition.fade
    const val DETAIL_TRANSITION_RES_ID = R.transition.detail_transition

    const val DETAIL_TRANSITION_NAME = "detail_transition"
}