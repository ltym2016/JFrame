package com.luys.jframe

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luys.jframe.databinding.FragmentNetworkBinding
import com.luys.library.base.BaseFragment


class NetworkFragment : BaseFragment<FragmentNetworkBinding, NetworkViewModel>() {

    override fun getLayoutId(): Int = R.layout.fragment_network

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
    }

    override fun initViewObservable() {
    }


}
