package com.shabelnikd.bilimtrack

import com.shabelnikd.bilimtrack.model.core.RetrofitClient
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository
import com.shabelnikd.bilimtrack.ui.fragments.auth.AuthViewModel
import com.shabelnikd.bilimtrack.ui.fragments.profile.ProfileViewModel
import com.shabelnikd.bilimtrack.ui.fragments.rating.RatingTabPageViewModel
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PreferenceHelper(androidContext()) }
    single { RetrofitClient(androidContext()) }
    single { get<RetrofitClient>().retrofitService }
    single { BilimTrackRepository(get()) }
}

val viewModelModule = module() {
    viewModel { AuthViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { RatingTabPageViewModel() }
}