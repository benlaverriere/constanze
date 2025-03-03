package com.benlaverriere.constanze

import dev.kord.core.Kord
import dev.kord.core.event.Event
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

object App {
  fun run(eventSource: SharedFlow<Event>, eventResponder: EventResponder, scope: CoroutineScope) {
    eventSource
        .buffer(Channel.UNLIMITED)
        .onEach { event -> scope.launch { scope.runCatching { eventResponder.respond(event) } } }
        .launchIn(scope)
  }
}

suspend fun main() {
  val dotenv = dotenv()
  val kord = Kord(dotenv["DISCORD_BOT_TOKEN"])
  App.run(kord.events, DiscordEventResponder, kord)
  kord.login {
    // we need to specify this to receive the content of messages
    @OptIn(PrivilegedIntent::class)
    intents += Intent.MessageContent
  }
}
