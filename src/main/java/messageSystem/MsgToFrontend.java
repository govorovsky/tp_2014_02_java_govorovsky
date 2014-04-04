package messageSystem;

import frontend.Frontend;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */
public abstract class MsgToFrontend extends Msg {

    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof Frontend) {
            exec((Frontend) abonent);
        }
    }

    abstract void exec(Frontend frontend);
}
