package com.simplifyd.zerodata.android.ui.main.notification.mapper

abstract class UIModelMapper<D, U> {

    abstract fun mapToUI(entity: D): U


    fun mapToUIList(entities: List<D>): List<U> {
        val models = mutableListOf<U>()
        entities.forEach {
            models.add(mapToUI(it))
        }

        return models
    }
}