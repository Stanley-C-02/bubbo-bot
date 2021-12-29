package com.bubblebot.main;

/**Bubble Bot
 * A discord bot made using Discord4J
 */

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

public class MyBot {
  public static void main(String[] args) {
    // DiscordClient only provides operations while not logged in
    DiscordClient client = DiscordClient.create(Token.DISCORD_TOKEN);

    // GatewayDiscordClient provides connection
    // Invokes given function

    // returning Mono.empty() does "nothing"
    // Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> Mono.empty());

    Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> {
      EventHandler handler = new EventHandler();
      // ReadyEvent example
      // handled by printing the username and discriminator (tag) of the 'self user',
      // which is the user associated w/ the bot account
      Mono<Void> handleReadyEvent = gateway.on(ReadyEvent.class, handler::handleReady).then();

      // MessageCreateEvent example
      Mono<Void> handleMessageCreate = gateway.on(MessageCreateEvent.class, handler::handleMessageCreate).then();

      // Combine commands
      return handleReadyEvent.and(handleMessageCreate);
    });

    login.block();
  }
}
