package com.google.android.material.button

import android.content.Context
import android.util.AttributeSet
import android.view.View

open class FullCornersMaterialToggleButtonGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButtonToggleGroup(context, attrs, defStyleAttr) {


    override fun updateChildShapes() {
        super.updateChildShapes()
        val childCount = childCount
        val firstVisibleChildIndex = getFirstVisibleChildIndex()
        val lastVisibleChildIndex = getLastVisibleChildIndex()
        for (i in 0 until childCount) {
            val button = getChildButton(i)
            if (button?.visibility == View.GONE) {
                continue
            }
            val builder = button?.shapeAppearanceModel?.toBuilder()
            if (i == firstVisibleChildIndex) {
                builder?.setAllCornerSizes(button.shapeAppearanceModel.topLeftCornerSize)
            } else if (i == lastVisibleChildIndex) {
                builder?.setAllCornerSizes(button.shapeAppearanceModel.topRightCornerSize)
            }
            button?.shapeAppearanceModel = builder?.build()!!
        }
    }

    fun getChildButton(index: Int): MaterialButton? {
        return getChildAt(index) as MaterialButton
    }

    fun getLastVisibleChildIndex(): Int {
        val childCount = childCount
        for (i in childCount - 1 downTo 0) {
            if (isChildVisible(i)) {
                return i
            }
        }
        return -1
    }

    fun isChildVisible(i: Int): Boolean {
        val child = getChildAt(i)
        return child.visibility != View.GONE
    }

    fun getFirstVisibleChildIndex(): Int {
        val childCount = childCount
        for (i in 0 until childCount) {
            if (isChildVisible(i)) {
                return i
            }
        }
        return -1
    }
}