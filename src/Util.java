import java.util.Random;

public class Util {
    public static int randomInt() {
        Random r = new Random();
        return r.nextInt(Constants.RANDOW_HIGH - Constants.RANDOM_LOW) + Constants.RANDOM_LOW;
    }
}
