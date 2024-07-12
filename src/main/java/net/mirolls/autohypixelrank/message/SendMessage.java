package net.mirolls.autohypixelrank.message;
import java.util.Locale;
import java.util.Random;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.mirolls.autohypixelrank.store.RankStore;

public class SendMessage {
    static final String[] FIRST_MESSAGES = {"I really need ${nrank} rank!",
            "I really really need ${nrank} rank!",
            "I really really really need ${nrank} rank!",
            "Can you give me ${nrank}?",
            "Please GIVE ME ${nrank}!!!!",
            "Oh my god, please give me ${nrank}",
            "${nrank}${nrank}${nrank}${nrank}${nrank}",
            "I really want ${nrank}",
            "OK? I really want ${nrank}"
    };

    static final String[] SECOND_MESSAGES = {
            "I really don't have enough money",
            "I'm too pool",
            "I really need help",
    };

    static final String[] THIRD_MESSAGES = {
            "Please",
            "Plz",
            "Plzzzz",
            "Plzzzzzz",
            "Plzzzzzzzz",
            "Plzzzzzzzzzzz",
            "Plzzzzzzzzzzzzz",
    };

    static final String[] SPECIAL_S_MESSAGES = { //搭配pls/plz并且s和z循环的
            "${nrank}, ${nrank}, ${nrank}",
            "${nrank}${nrank}${nrank}${nrank}${nrank}",
            "${nrank}",
            "${nrank}${nrank}${nrank}"
    };

    static final String[] SPECIAL_SINGLE_MESSAGES = {
            "😩😩😩${nrank}😩😩😩",
            "->->->->->${nrank}<-<-<-<-<-<-",
    };

    static final String[] CHINESE = {
            "各位金主爸爸们求求了给个${nrank}吧孩子八百天没打Hypixel了",
            "很难绷啊求求来个${nrank}",
            "真的只需要一个rank就好什么都行给升级就可以",
            "求${nrank}",
            "在这里求了几千年了真的求得到rank吗",
            "啊啊啊啊啊啊啊啊啊啊啊啊啊啊来个${nrank}",
            "真的键盘要报废了，来个${nrank}不过分吧",
            "求求了真的需要一个${nrank}",
            "在没${nrank}就真的变成五行山下的猴子了真的",
            "感谢义父，给个${nrank}吧呜呜呜"
    };

    public static void sendRankMessage(ClientPlayNetworkHandler networkHandler){
        //先处理nrank
        String nrank = "MVP+";
        String rank = RankStore.getPlayerRank();
        if (rank.equalsIgnoreCase("DEFAULT")){
            nrank = "VIP+";
        }else if (rank.equalsIgnoreCase("VIP")){
            nrank = "VIP+";
        } else if (rank.equalsIgnoreCase("VIP+")) {
            nrank = "MVP";
        } else if (rank.equalsIgnoreCase("MVP")){
            nrank = "MVP+";
        } else if (rank.equalsIgnoreCase("MVP+")) {
            nrank = "MVP++";
        } else if (rank.equalsIgnoreCase("MVP++")) {
            //你都mvp++了你求个蛋啊
            return;
        }

        Random rand = new Random();
        int randomInt = rand.nextInt(100);
        if(randomInt <= 78){ // 78% 的几率打出英文字，用78是因为方便使用3的背书
            String rawEnglishWord = "";
            if (randomInt <= 26){//使用special
                if (randomInt <= 13){//使用single
                    int randomIndex = rand.nextInt(SPECIAL_SINGLE_MESSAGES.length);
                    rawEnglishWord = SPECIAL_SINGLE_MESSAGES[randomIndex];
                }else{//添加plz
                    int randomIndex = rand.nextInt(SPECIAL_S_MESSAGES.length);
                    rawEnglishWord = SPECIAL_S_MESSAGES[randomIndex];
                    int randomPlsz = rand.nextInt(2);
                    if(randomPlsz == 1){
                        rawEnglishWord = rawEnglishWord + " pls";
                    }else{
                        int plzZNumber = rand.nextInt(10);
                        rawEnglishWord = rawEnglishWord + " plz";
                        rawEnglishWord = rawEnglishWord + "z".repeat(plzZNumber);
                    }
                }
            } else if (randomInt <= 52) {//random大于26始终为true 使用word1+word2
                int randomIndex1 = rand.nextInt(FIRST_MESSAGES.length);
                int randomIndex2 = rand.nextInt(SECOND_MESSAGES.length);
                rawEnglishWord = FIRST_MESSAGES[randomIndex1] + " " + SECOND_MESSAGES[randomIndex2];
            }else{//保底，word2+word3
                int randomIndex2 = rand.nextInt(SECOND_MESSAGES.length);
                int randomIndex3 = rand.nextInt(THIRD_MESSAGES.length);
                rawEnglishWord = SECOND_MESSAGES[randomIndex2] + " " + THIRD_MESSAGES[randomIndex3];
            }
            // 大小写处理
            //预处理，防止nrank无法匹配
            rawEnglishWord = rawEnglishWord.replace("${nrank}","${[()]}");
            int randomLetter = rand.nextInt(100);
            String cookedEnglish;
            if (randomLetter > 80) {
                if (randomLetter > 90) { //其他各10%，这个是全部大写
                    rawEnglishWord = rawEnglishWord.toLowerCase(Locale.ROOT);
                }else {
                    rawEnglishWord = rawEnglishWord.toUpperCase();
                }
            }  // 80%的概率是不动
            cookedEnglish = rawEnglishWord.replace("${[()]}", nrank);

            //发射！
            networkHandler.sendChatCommand("ac " + cookedEnglish);
        }else{//中文字
            int randomIndex = rand.nextInt(CHINESE.length);
            networkHandler.sendChatCommand("ac "+ CHINESE[randomIndex]);
        }
    }
}
