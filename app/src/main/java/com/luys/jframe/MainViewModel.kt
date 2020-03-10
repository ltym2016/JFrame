package com.luys.jframe

import android.app.Application
import android.widget.Toast
import com.luys.library.base.BaseViewModel
import com.luys.library.callback.BindingAction
import com.luys.library.callback.BindingCommand

/**
 * @author luys
 * @describe
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    val clickBindingCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            Toast.makeText(application,"ddd", Toast.LENGTH_LONG).show()
        }
    })
}