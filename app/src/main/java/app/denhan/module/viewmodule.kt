package app.denhan.module


import app.denhan.view.login.LoginViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel {
        LoginViewModel(get(),get())
    }
}
