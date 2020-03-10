package com.luys.jframe

import androidx.lifecycle.Observer
import com.luys.jframe.databinding.ActivityMainBinding
import com.luys.library.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initVariableId(): Int = BR.viewModel

    override fun initParam() {
    }

    override fun initData() {
    }

    override fun initViewObservable() {
    }

    override fun setOrientation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
