package app.denhan.module


import app.denhan.view.home.HomeViewModel
import app.denhan.view.login.LoginViewModel
import app.denhan.view.owner.AddLogViewModel
import app.denhan.view.owner.OwnerViewModel
import app.denhan.view.sign.SignViewModel
import app.denhan.view.splash.SplashViewModel
import app.denhan.view.subtask.SubTaskViewModel
import app.denhan.view.taskdetail.TaskDetailViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel {
        LoginViewModel(get(),get())
    }
    viewModel {
        HomeViewModel(get(), get())
    }

    viewModel {
        TaskDetailViewModel(get(), get())
    }
    viewModel {
        OwnerViewModel(get(), get())
    }
    viewModel {

        AddLogViewModel(get(), get())
    }
    viewModel {
        SubTaskViewModel(get(), get())
    }

    viewModel {
        SignViewModel(get(), get())
    }

    viewModel {
        SplashViewModel(get(), get())
    }
}
