package net.mirolls.autohypixelrank.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientPlayNetworkHandler.class)
public interface ClientChatAccessor {

//    @Accessor(ClientWorld)
//    @Invoker("sendChatCommand");
//    String sendChatCommand(String command);
}
