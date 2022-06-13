package com.artybyte.test.whacamole.observe

interface IObservable {
    val observers: ArrayList<Observer>

    fun add(observer: Observer) {
        observers.add(observer)
    }

    fun remove(observer: Observer) {
        observers.remove(observer)
    }

    fun sendUpdateEvent(container: Observer) {
        observers.forEach { it.update() }
    }
}