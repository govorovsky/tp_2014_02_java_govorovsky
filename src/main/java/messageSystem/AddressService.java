package messageSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */
public class AddressService {
    private final Map<Class<?>, ArrayList<Address>> addresses = new HashMap<>();
    private static final Random random = new Random();

    public void addAddress(Abonent abonent) {
        ArrayList<Address> adrList = addresses.get(abonent.getClass());
        if (adrList != null) {
            adrList.add(abonent.getAddress());
        } else {
            ArrayList<Address> newList = new ArrayList<>();
            newList.add(abonent.getAddress());
            addresses.put(abonent.getClass(), newList);
        }
    }

    public Address getAddress(Class<?> clazz) {
        ArrayList<Address> adrList = addresses.get(clazz);
        if (adrList != null) {
            return adrList.get(random.nextInt(adrList.size())); /* TODO smart balancing */
        } else {
            return null;
        }
    }
}
