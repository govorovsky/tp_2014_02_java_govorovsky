package messageSystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */

public class Address {
    private static final AtomicInteger abonentIdCreator = new AtomicInteger();
    private final int abonentId;

    public Address() {
        this.abonentId = abonentIdCreator.incrementAndGet();
    }

    public int hashCode() {
        return abonentId;
    }
}


