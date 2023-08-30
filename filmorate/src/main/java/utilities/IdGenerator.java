package utilities;

public class IdGenerator {

    private static long idCounter = 1;

    public static synchronized long generateId() {
        return idCounter++;
    }
}


