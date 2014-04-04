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

    @Override
    public int hashCode() {
        return abonentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return abonentId == address.abonentId;

    }
}


