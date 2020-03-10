package com.luys.library.base

import com.luys.library.R

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class NoMoreDataEntity : BaseBindingItem {

    override fun getViewType(): Int = R.layout.item_bottom

    override fun isFooter(): Boolean {
        return true
    }
}