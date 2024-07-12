package net.mirolls.autohypixelrank.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Mixin(ClientPlayNetworkHandler.class)

public abstract class ClientChatMixin implements ClientChatAccessor{

//    @Shadow public abstract void sendChatCommand(String command);

    @Unique
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Unique
    private boolean isTimerStart = false;


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
                player.sendMessage(Text.literal("不能发送以*开头的消息"));
                executeCustomCode(player, message);
            }
        }
    }

    @Unique
    private void executeCustomCode(ClientPlayerEntity player, String message) {
        String[] splitMessage = message.trim().split("\\s+");
        if (splitMessage.length != 2){
            player.sendMessage(Text.literal("[操作失败] 命令缺少了参数"));
        }else{
            if(Objects.equals(splitMessage[1], "start")){
                if (isTimerStart){
                    player.sendMessage(Text.literal("[操作成功] 开启了 自动获取Ranks"));
                    isTimerStart = true;
                    ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
                    if (networkHandler != null) {
                        networkHandler.sendChatCommand("/ac Good Evening everyone");
                    }else{
                        return;
                    }
                    networkHandler.sendChatCommand("/ac Good Evening everyone");
                    scheduler.scheduleAtFixedRate(()->{
                        networkHandler.sendChatCommand("/ac Good Evening everyone");
                    },0, 4, TimeUnit.SECONDS);
                }else{
                    player.sendMessage(Text.literal("[操作失败] 自动获取Ranks 已经被开启了"));
                }
            } else if (Objects.equals(splitMessage[1], "stop")) {
                if (isTimerStart) {
                    player.sendMessage(Text.literal("[操作成功] 关闭了 自动获取Ranks"));
                    scheduler.shutdownNow();
                    isTimerStart = false;
                }else{
                    player.sendMessage(Text.literal("[操作失败] 目前 自动获取Ranks 还未开启"));
                }
            }else{
                player.sendMessage(Text.literal("[操作失败] 未知的参数"));
            }
        }
    }
}
