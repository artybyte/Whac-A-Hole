package com.artybyte.test.whacamole.observe

interface IPreferencesLoadedEventObservable {
    val preferencesLoadedEventObservers: ArrayList<PreferencesLoadedEventObserver>

    fun add(observer: PreferencesLoadedEventObserver) {
        preferencesLoadedEventObservers.add(observer)
    }

    fun remove(observer: PreferencesLoadedEventObserver) {
        preferencesLoadedEventObservers.remove(observer)
    }

    fun sendUpdateEvent() {
        preferencesLoadedEventObservers.forEach { it.preferencesLoadedEventUpdate() }
    }
}