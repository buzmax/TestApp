package com.revoluttest.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.revoluttest.R
import com.revoluttest.BR
import com.revoluttest.viewmodel.CurrencyItemViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

internal class CurrenciesAdapter : RecyclerView.Adapter<BindingViewHolder>() {

    private val compositeDisposable = CompositeDisposable()
    private val items = mutableListOf<CurrencyItemViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_currency,
            parent,
            false)
        return BindingViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemId(position: Int): Long = items[position].currency.name.hashCode().toLong()

    fun updateData(newItems: List<CurrencyItemViewModel>) {
        compositeDisposable.add(Single.fromCallable {
            DiffUtil.calculateDiff(DiffUtilCallback(items, newItems))
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { applyDiffResult(newItems, it) })
        )
    }

    private fun applyDiffResult(newItems: List<CurrencyItemViewModel>, it: DiffUtil.DiffResult) {
        this.items.clear()
        this.items.addAll(newItems)
        it.dispatchUpdatesTo(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }
}

internal class DiffUtilCallback(
    private val oldList: List<CurrencyItemViewModel>,
    private val newList: List<CurrencyItemViewModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].currency.name == newList[newItemPosition].currency.name

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].currency.name == newList[newItemPosition].currency.name
            && oldList[oldItemPosition].exchangedAmount.get() == newList[newItemPosition].exchangedAmount.get()

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return newList[newItemPosition].exchangedAmount
    }
}

internal class BindingViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: CurrencyItemViewModel) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }
}
