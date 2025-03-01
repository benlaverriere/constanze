package com.benlaverriere.constanze

import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.getSpotifyAuthorizationUrl
import io.github.cdimascio.dotenv.dotenv
import dev.kord.core.Kord
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

suspend fun main() {
    val dotenv = dotenv()
    val kord = Kord(dotenv["DISCORD_BOT_TOKEN"])
    kord.on<MessageCreateEvent> {
        if (message.author?.isBot != false) return@on

        if (message.content != "!link") return@on

        val url: String = getSpotifyAuthorizationUrl(
            SpotifyScope.PlaylistReadPrivate,
            SpotifyScope.PlaylistModifyPrivate,
            clientId = dotenv["SPOTIFY_CLIENT_ID"],
            redirectUri = "https://benlaverriere.com", // redirect to Discord itself?
        )
        message.channel.createMessage(url)
    }
    kord.on<ReactionAddEvent> {
        if (this.emoji != ReactionEmoji.Unicode("\uD83C\uDFA7")) return@on

        this.channel.createMessage("I heard that!")
    }

    kord.login {
        // we need to specify this to receive the content of messages
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}
