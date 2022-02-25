package com.simplifyd.zerodata.android.utils

import android.graphics.Color
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.utils.ColorTemplate
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveData<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(it: T?) {
        pending.set(true)
        super.setValue(it)
    }

    @MainThread
    operator fun invoke() {
        value = null
    }
}

val DESIGN_COLORS = intArrayOf(
    Color.rgb(37, 209, 162),
    Color.rgb(0, 81, 153),
    Color.rgb(210, 139, 235),
    Color.rgb(255, 205, 76),
    Color.rgb(148, 148, 148),
    Color.rgb(210, 139, 235),
    Color.rgb(255, 69, 96),
    Color.rgb(76, 175, 80),
    Color.rgb(236, 57, 68)
)

val allColors = mutableListOf<Int>().apply {
    val intArray = DESIGN_COLORS + ColorTemplate.PASTEL_COLORS + ColorTemplate.COLORFUL_COLORS +
            ColorTemplate.JOYFUL_COLORS + ColorTemplate.LIBERTY_COLORS +
            ColorTemplate.VORDIPLOM_COLORS + ColorTemplate.MATERIAL_COLORS

    addAll(intArray.toList())
    add(ColorTemplate.getHoloBlue())
}

sealed class Status<out T : Any> {

    data class Success<out T : Any>(val data: T) : Status<T>()

    data class Error(val error: Throwable) : Status<Nothing>()

    object Loading : Status<Nothing>()
}

interface AutoUpdateRecyclerView {

    fun <T> RecyclerView.Adapter<*>.autoNotify(
        old: List<T>,
        new: List<T>,
        compare: (T, T) -> Boolean
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(old[oldItemPosition], new[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == new[newItemPosition]
            }

            override fun getOldListSize() = old.size

            override fun getNewListSize() = new.size
        })

        diff.dispatchUpdatesTo(this)
    }
}

fun handleError(exception: Throwable): Throwable {
    return if (exception is SocketTimeoutException || exception is ConnectException) {
        Throwable("Please check your internet connection and retry.")
    } else {
        Throwable("There was an error handling your request, please retry.")
    }
}
