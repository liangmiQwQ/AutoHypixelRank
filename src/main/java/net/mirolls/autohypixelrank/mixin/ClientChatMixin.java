package net.mirolls.autohypixelrank.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)

public class ClientChatMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        // 检查消息是否以*开头
        if (message.startsWith("*")) {
            // 阻止消息发送
            ci.cancel();

            // 获取当前客户端玩家实体
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                // 执行自定义代码，例如发送反馈消息
//                player.sendMessage(Text.literal("不能发送以*开头的消息"), new Object(), player.getUuid());
                player.sendMessage(Text.literal("不能发送以*开头的消息"));
                executeCustomCode(player, message);
            }
        }
    }

    @Unique
    private void executeCustomCode(ClientPlayerEntity player, String message) {
        // 在这里执行你的自定义代码
        // 例如：给玩家发送一条消息
        player.sendMessage(Text.literal("不能发送以*开头的消息"));
//        player.sendMessage(Text.literal("你触发了自定义代码：" + message), MessageType.SYSTEM, player.getUuid());
    }
}
