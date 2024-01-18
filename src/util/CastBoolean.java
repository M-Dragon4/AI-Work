package util;

public class CastBoolean {

    /**
     * 2 mod 2 is 0 and 0 is false
     * @param b the Byte to cast
     * @return the boolean equivalent of the Byte
     */
    public static boolean byteToBoolean(byte b) {
        if (b % 2 == 0) {
            return false;
        } else return true;
    }

    /**
     * 2 mod 2 is 0 and 0 is false
     * @param s the Short to cast
     * @return the boolean equivalent of the Short
     */
    public static boolean shortToBoolean(short s) {
        if (s % 2 == 0) {
            return false;
        } else return true;
    }

    /**
     * 2 mod 2 is 0 and 0 is false
     * @param i the Integer to cast
     * @return the boolean equivalent of the Integer
     */
    public static boolean intToBoolean(int i) {
        if (i % 2 == 0) {
            return false;
        } else return true;
    }

    /**
     * 2 mod 2 is 0 and 0 is false
     * @param l the Long to cast
     * @return the boolean equivalent of the Long
     */
    public static boolean longToBoolean(long l) {
        if (l % 2 == 0) {
            return false;
        } else return true;
    }

    /**
     * The location of a Float between any two values is derived from its floating portion
     * @param f the Float to cast
     * @return the boolean equivalent of the Float
     */
    public static boolean floatToBoolean(float f) {
        f = Math.abs(f - Math.round(f));
        if (f < 0.5) return false;
        else return true;
    }

    /**
     * The location of a Double between any two values is derived from its floating portion
     * @param d the Double to cast
     * @return the boolean equivalent of the Double
     */
    public static boolean doubleToBoolean(double d) {
        d = Math.abs(d) - Math.floor(Math.abs(d));
        if (d < 0.5) return false;
        else return true;
    }
}
