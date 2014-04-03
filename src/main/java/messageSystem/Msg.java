package messageSystem;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */
public abstract class Msg {
    private Address from, to;

    public Msg(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    protected Address getFrom() {
        return from;
    }

    protected Address getTo() {
        return to;
    }

    abstract void exec(Abonent abonent);
}
