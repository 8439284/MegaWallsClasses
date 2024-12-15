package org.ajls.megawallsclasses;

public class GetClassEnum {
    public static ClassEnum getClassEnum(int classIndex) {
        switch (classIndex) {
            case 1:
                return ClassEnum.ZOMBIE;
            case 2:
                return ClassEnum.HEROBRINE;
            case 3:
                return ClassEnum.SKELETON;
            case 4:
                return ClassEnum.ENDERMAN;
            case 5:
                return ClassEnum.NULL;
            case 6:
                return ClassEnum.DREAD_LORD;
            case 7:
                return ClassEnum.ENTITY_303;
            case 8:
                return ClassEnum.CREEPER;
            case 9:
                return ClassEnum.UNDEAD_KNIGHT;
            case 10:
                return ClassEnum.DROWNKING;
            case 11:
                return ClassEnum.SPIDER;
            case 12:
                return ClassEnum.SHAMAN;
            case 13:
                return ClassEnum.SNOWMAN;
            case 14:
                return ClassEnum.MOLE;
            case 15:
                return ClassEnum.ELAINA;
            case 18:
                return ClassEnum.SQUID;
            case 28:
                return ClassEnum.SKELETON_LORD;
                                                            

        }
        return ClassEnum.NAC;
    }
}
