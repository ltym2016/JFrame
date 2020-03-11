package com.luys.library.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author luys
 * @describe
 * @date 2020/3/11
 * @email samluys@foxmail.com
 */
class BaseBindingAdapter(var onItemClickListener: OnItemClickListener? = null) :
    RecyclerView.Adapter<BaseBindingAdapter.BindViewHolder>() {

    private lateinit var mList:List<BaseBindingItem>

    class BindViewHolder(var binding:ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item:BaseBindingItem, onItemClickListener: OnItemClickListener?, position: Int) {
            binding.apply {
                setVariable(com.luys.library.BR.item, item)
                setVariable(com.luys.library.BR.position, position)
                if (onItemClickListener != null) {
                    setVariable(com.luys.library.BR.itemClick, onItemClickListener)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val binding :ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),viewType,parent,false)
        return BindViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bindData(mList[position], onItemClickListener, position)
    }

    override fun getItemViewType(position: Int): Int {
        return mList[position].getViewType()
    }

    fun getList():List<BaseBindingItem> = mList

    fun setList(list:List<BaseBindingItem>) {
        mList = list
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val gridLayoutManager = manager as GridLayoutManager
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if (mList.size > 0 && mList.size > position) {
                        val item = mList[position]
                        if (item.isFooter()) gridLayoutManager.spanCount else 1
                    } else if (position == 0) {
                        val item = mList[0]
                        if (item.isHeader()) gridLayoutManager.spanCount else 1
                    } else {
                        1
                    }
                }
            }
        }
    }
}