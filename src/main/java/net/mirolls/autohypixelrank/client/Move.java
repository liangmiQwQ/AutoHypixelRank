package net.mirolls.autohypixelrank.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.lang.reflect.Method;

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
//            pressKey(keyBinding, true);
            KeyBinding.setKeyPressed(keyBinding.getDefaultKey(), true);

            // 启动一个新线程在1秒后松开按键
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 模拟松开按键
                    KeyBinding.setKeyPressed(keyBinding.getDefaultKey(), false);
//                    pressKey(keyBinding, false);
                }
            }).start();
        }
    }

    private static void pressKey(KeyBinding keyBinding, boolean pressed) {
        try {
            Method method = KeyBinding.class.getDeclaredMethod("setPressed", boolean.class);
            method.setAccessible(true);
            method.invoke(keyBinding, pressed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
