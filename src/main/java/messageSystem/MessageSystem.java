package messageSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */
public class MessageSystem {
    private final AddressService addressService = new AddressService();
    private final Map<Address, ConcurrentLinkedQueue<Msg>> messages = new HashMap<>();

    public void addService(Abonent abonent) {
        messages.put(abonent.getAddress(), new ConcurrentLinkedQueue<Msg>());
    }

    public void sendMessage(Msg message) {
        Queue<Msg> messageQueue = messages.get(message.getTo());
        messageQueue.add(message);
    }

    public void execForAbonent(Abonent abonent) {
        Queue<Msg> messageQueue = messages.get(abonent.getAddress());
        if (messageQueue == null) {
            return;
        }
        while (!messageQueue.isEmpty()) {
            Msg message = messageQueue.poll();
            message.exec(abonent);
        }
    }

    public AddressService getAddressService() {
        return addressService;
    }
}
