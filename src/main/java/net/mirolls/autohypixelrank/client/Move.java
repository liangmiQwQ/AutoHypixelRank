package net.mirolls.autohypixelrank.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class Move {
    public static void moveForwardForOneSecond() {
        MinecraftClient client = MinecraftClient.getInstance();
        moveForOneSecond(client.options.forwardKey);
    }

    public static void moveBackwardForOneSecond() {
        MinecraftClient client = MinecraftClient.getInstance();
        moveForOneSecond(client.options.backKey);
    }

    public static void moveLeftForOneSecond() {
        MinecraftClient client = MinecraftClient.getInstance();
        moveForOneSecond(client.options.leftKey);
    }

    public static void moveRightForOneSecond() {
        MinecraftClient client = MinecraftClient.getInstance();
        moveForOneSecond(client.options.rightKey);
    }

    public static void jumpForOneSecond() {
        MinecraftClient client = MinecraftClient.getInstance();
        moveForOneSecond(client.options.jumpKey);
    }

    private static void moveForOneSecond(KeyBinding keyBinding) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            // 模拟按下按键
            KeyBinding.onKeyPressed(InputUtil.fromTranslationKey(keyBinding.getTranslationKey()));

            // 启动一个新线程在1秒后松开按键
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 模拟松开按键
                    KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(keyBinding.getTranslationKey()), false);
                }
            }).start();
        }
    }
}
