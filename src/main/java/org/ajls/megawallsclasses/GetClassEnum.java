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
            case 29:
                return ClassEnum.TRANSFIGURATION_MASTER;
                                                            

        }
        return null;
    }

    public static int getClassIndex(ClassEnum classEnum) {//idea smart class enum register with ints marking empty slot
        for (int i = 1; i <= 35; i++) {
            ClassEnum c = getClassEnum(i);
            if (c == classEnum) {
                return i;
            }
        }
        return -1;
    }
    /*
    I Understand that what you want is to have a print (or any other) function where you can pass any possible enum, to print each of the enum's possible values (i.e. constants). I've found the following two approaches to do this:

    Lets say we have the following enum:

    // The test enum, any other will work too
    public static enum ETest
    {
        PRINT,MY,VALUES
    }
    Variante 1: Pass the array of constants from your enum to your function; As the enum's constants are static values, they can easily be accessed and passed to your 'print' function as follows:

    public static void main(String[] args)
    {
        // retreive all constants of your enum by YourEnum.values()
        // then pass them to your function
        printEnum(ETest.values());
    }

    // print function: type safe for Enum values
    public static <T extends Enum<T>> void printEnum(T[] aValues)
    {
        System.out.println(java.util.Arrays.asList(aValues));
    }
    Variante 2: Pass the enum's class as function parameter. This might look more beautiful, but be aware that reflection is involved (performance):

    public static void main(String[] args)
    {
        printEnum2(ETest.class);
    }

    // print function: accepts enum class and prints all constants
    public static <T extends Enum<T>> void printEnum2(Class<T> aEnum)
    {
        // retreive all constants of your enum (reflection!!)
        System.out.println(java.util.Arrays.asList(aEnum.getEnumConstants()));
    }
    In my opinion, it's better to use variante 1 because of the overuse of reflection in variante 2. The only advantage that variante 2 gives you, is that you have the Class object of the Enum itself (the static enum, not only it's constants) inside your function, so I've mentioned it for completeness.
        */
}
