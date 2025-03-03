package com.benlaverriere.constanze

import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.getSpotifyAuthorizationUrl
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.ReactionAddEvent
import io.github.cdimascio.dotenv.dotenv

interface EventResponder {
  suspend fun respond(event: Event)
}

object DiscordEventResponder : EventResponder {
  override suspend fun respond(event: Event) {
    when (event) {
      is MessageCreateEvent -> handle(event)
      is ReactionAddEvent -> handle(event)
      else -> return
    }
  }

  private suspend fun handle(event: MessageCreateEvent) {
    if (event.message.author?.isBot != false) return

    if (event.message.content != "!link") return

    val url: String =
        getSpotifyAuthorizationUrl(
            SpotifyScope.PlaylistReadPrivate,
            SpotifyScope.PlaylistModifyPrivate,
            clientId = dotenv()["SPOTIFY_CLIENT_ID"],
            redirectUri = "https://benlaverriere.com", // redirect to Discord itself?
        )
    event.message.channel.createMessage(url)
  }

  private suspend fun handle(event: ReactionAddEvent) {
    if (event.emoji != ReactionEmoji.Unicode("\uD83C\uDFA7")) return

    event.channel.createMessage("I heard that!")
  }
}
