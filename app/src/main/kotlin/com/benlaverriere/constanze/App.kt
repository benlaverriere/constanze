package com.benlaverriere.constanze

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
    val kord = Kord(dotenv()["DISCORD_BOT_TOKEN"])
    kord.on<MessageCreateEvent> { // runs every time a message is created that our bot can read

        // ignore other bots, even ourselves. We only serve humans here!
        if (message.author?.isBot != false) return@on

        // check if our command is being invoked
        if (message.content != "!ping") return@on

        // all clear, give them the pong!
        message.channel.createMessage("pong!")
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
