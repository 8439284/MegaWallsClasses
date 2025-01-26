package org.ajls.megawallsclasses;

public class Documentation {
    public static String getClassDocumentation(int classIndex) {
        ClassEnum classEnum = GetClassEnum.getClassEnum(classIndex);//ClassEnum.values()[classIndex];
        if (classEnum == null) {return "";}
        switch (classEnum) {
            case ZOMBIE:
                return "僵尸\n" +
                        "主动 回血\n" +
                        "被动2 射箭 速度 力量";
            case HEROBRINE:
                return "him\n" +
                        "主动 打雷\n" +
                        "被动1 打人 速度 生命恢复";
            case SKELETON:
                return "骷髅\n" +
                        "主动 爆炸箭";
            case ENDERMAN:
                return "末影人\n" +
                        "主动 传送\n" +
                        "被动 能量满时 生命恢复";
            case NULL:
                return "黑暗君主\n" +
                        "主动 失明\n" +
                        "被动 打人 隐身";
            case DREAD_LORD:
                return "恐惧魔王\n" +
                        "主动 凋灵炸弹";
            case ENTITY_303:
                return "实体303\n" +
                        "装备 火弓\n" +
                        "主动 3发射线";
            case CREEPER:
                return "苦力怕\n" +
                        "主动 tnt刮痧";
            case UNDEAD_KNIGHT:
                return "死灵骑士\n" +
                        "主动 骷髅马拉扯\n" +
                        "被动 凋灵弓";
            case DROWNKING:
                return "溺尸王\n" +
                        "主动 血越少伤害越大（丢叉子自动触发）";
            case SPIDER:
                return "蜘蛛\n" +
                        "主动 向看着的地方跳起来\n" +
                        "被动 落地产生爆炸";
            case SHAMAN:
                return "萨满\n" +
                        "主动 龙卷风\n" +
                        "被动 打人 虚弱\n" +
                        "被动 被打召唤狼";
            case SNOWMAN:
                return "雪人\n" +
                        "主动 没做\n" +
                        "被动 铲子右键 敌人缓慢 自身回血";
            case MOLE:
                return "鼹鼠\n" +
                        "装备 生牛肉生命恢复 南瓜派增加能量（食用无视饱食度）\n" +
                        "主动 向看着的方向破坏方块 伤害沿途敌人\n" +
                        "被动 挖掘泥土获得速度和急迫";
            case ELAINA:
                return "魔女\n" +
                        "装备 火焰附加棍子\n" +
                        "主动 没做\n" +
                        "被动 手持速度药水2s可以飞\n" +
                        "被动 冰棱锥攻击（木棍右键切换模式）";
            case SQUID:
                return "鱿鱼\n" +
                        "装备 多3瓶 8hp伤害吸收药水（鱿鱼药）\n" +
                        "主动 吸人过来扣血 自己回血\n" +
                        "被动 喝药 附近敌人失明\n" +
                        "被动 少于18血 回血";
            case SKELETON_LORD:
                return "骷髅王\n" +
                        "主动 召唤骷髅小兵 攻击 木棍右键标记的敌人\n" +
                        "被动 骷髅小兵打人 自己概率获得金苹果";


        }
        return "";
    }
}
