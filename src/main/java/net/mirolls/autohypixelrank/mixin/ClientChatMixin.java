package net.mirolls.autohypixelrank.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.mirolls.autohypixelrank.client.Move;
import net.mirolls.autohypixelrank.message.SendMessage;
import net.mirolls.autohypixelrank.store.RankStore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


@Mixin(ClientPlayNetworkHandler.class)

public abstract class ClientChatMixin implements ClientChatAccessor{

//    @Shadow public abstract void sendChatCommand(String command);

    @Unique
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
                executeCustomCode(player, message);
            }
        }
    }

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {
            String msgText = packet.content().getString();

            msgText = removeColorCodes(msgText);

            // 检查消息格式是否包含Rank信息
            if (msgText.contains("[VIP]") && msgText.contains(client.player.getGameProfile().getName() + ":")){
                // 从消息中提取玩家的Rank信息
                RankStore.setPlayerRank("VIP"); // 存储Rank信息
                client.player.sendMessage(Text.of("发现了rank为" + "VIP" + "并且已经存储"));
            } else if (msgText.contains("[VIP+]") && msgText.contains(client.player.getGameProfile().getName() + ":")) {
                RankStore.setPlayerRank("VIP+"); // 存储Rank信息
                client.player.sendMessage(Text.of("发现了rank为" + "VIP+" + "并且已经存储"));
            } else if (msgText.contains("[MVP]") && msgText.contains(client.player.getGameProfile().getName() + ":")) {
                RankStore.setPlayerRank("MVP"); // 存储Rank信息
                client.player.sendMessage(Text.of("发现了rank为" + "MVP" + "并且已经存储"));
            } else if (msgText.contains("[MVP+]") && msgText.contains(client.player.getGameProfile().getName() + ":")) {
                RankStore.setPlayerRank("MVP+"); // 存储Rank信息
                client.player.sendMessage(Text.of("发现了rank为" + "MVP+" + "并且已经存储"));
            } else if (msgText.contains("[MVP++]") && msgText.contains(client.player.getGameProfile().getName() + ":")) {
                RankStore.setPlayerRank("MVP+"); // 存储Rank信息
                client.player.sendMessage(Text.of("发现了rank为" + "MVP++" + "并且已经存储"));
            } else{
                RankStore.setPlayerRank("DEFAULT");
            }
        }
    }

    @Unique
    private String removeColorCodes(String input) {
        return Pattern.compile("§.").matcher(input).replaceAll("");
    }

    @Unique
    private void executeCustomCode(ClientPlayerEntity player, String message) {
        String[] splitMessage = message.trim().split("\\s+");
        if (splitMessage.length != 2){
            Move.moveForwardForOneSecond();
            player.sendMessage(Text.literal("[操作失败] 命令缺少了参数"));
        }else{
            if(Objects.equals(splitMessage[1], "start")){
                if (!isTimerStart){
                    // 如果isTimerStart是假的，意味着没有开启计时器，需要开启计时器，所以!isTimerStart是真，下面代码运行
                    player.sendMessage(Text.literal("[操作成功] 开启了 自动获取Ranks"));
                    isTimerStart = true;
                    ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
                    if (networkHandler == null) {
                        return;
                    }
                    scheduler.scheduleAtFixedRate(()->{
                        SendMessage.sendRankMessage(networkHandler);

                        Random rand = new Random();
                        int randomMove = rand.nextInt(100);
                        if(randomMove > 50 && randomMove <= 60){ // 向前移动
                            Move.moveForwardForOneSecond();
                        } else if (randomMove > 60 && randomMove <= 70) {//向后移动
                            Move.moveBackwardForOneSecond();
                        } else if (randomMove > 70 && randomMove <= 80) {//向左移动
                            Move.moveLeftForOneSecond();
                        } else if (randomMove > 80 && randomMove <= 90) {//向右移动
                            Move.moveRightForOneSecond();
                        }else{//跳跃
                            Move.jumpForOneSecond();
                        }

                    },0, 12, TimeUnit.SECONDS);
                }else{
                    player.sendMessage(Text.literal("[操作失败] 自动获取Ranks 已经被开启了"));
                }
            } else if (Objects.equals(splitMessage[1], "stop")) {
                if (isTimerStart) {
                    player.sendMessage(Text.literal("[操作成功] 关闭了 自动获取Ranks"));
                    scheduler.shutdownNow();
                    // re-init
                    scheduler = Executors.newScheduledThreadPool(1);
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
