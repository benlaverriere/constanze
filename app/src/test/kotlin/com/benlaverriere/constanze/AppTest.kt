package com.benlaverriere.constanze

import dev.kord.core.event.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AppTest {
  @Test
  fun `delegates event handling`() = runTest {
    val eventResponder = FakeEventResponder()
    val inputEvents = MutableSharedFlow<Event>()

    App.run(inputEvents, eventResponder, this)

    this.runCurrent()
    assertEquals(1, eventResponder.receivedEvents.size)
  }
}

class FakeEventResponder : EventResponder {
  var receivedEvents = mutableListOf<Event>()

  override suspend fun respond(event: Event) {
    receivedEvents.add(event)
  }
}
