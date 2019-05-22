package com.revoluttest.view.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.revoluttest.view.MoneyEditText
import com.revoluttest.viewmodel.CurrencyItemViewModel

@BindingAdapter("items")
internal fun setAdapter(recyclerView: RecyclerView, items: List<CurrencyItemViewModel>) {
    var adapter = recyclerView.adapter as? CurrenciesAdapter
    if (adapter == null) {
        adapter = CurrenciesAdapter().apply { setHasStableIds(true) }
        recyclerView.adapter = adapter
    }

    adapter.updateData(items)
}

@BindingAdapter("onSelected")
internal fun onSelected(editText: MoneyEditText, onSelected: () -> Unit) {
    editText.setOnSelectedListener(onSelected)
}

@BindingAdapter("onAmountChanged")
internal fun setOnAmountChangedListener(
    editText: MoneyEditText,
    onAmountChanged: (amount: CharSequence) -> Unit
) {
    editText.amountChangedListener = onAmountChanged
}
