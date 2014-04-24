package util;

import resource.ResourceSystem;
import resource.UserStateResource;

/**
 * Created by Andrew Govorovsky on 15.03.14
 */
public enum UserState {

    USER_ADDED(true) {
        @Override
        public String getMessage() {
            return USR.getUSER_ADDED();
        }
    },
    WAIT_USER_REG(true) {
        @Override
        public String getMessage() {
            return USR.getWAIT_USER_REG();
        }

    },
    WAIT_AUTH(true) {
        @Override
        public String getMessage() {
            return USR.getWAIT_AUTH();
        }

    },
    AUTHORIZED(true) {
        @Override
        public String getMessage() {
            return USR.getAUTHORIZED();
        }
    },
    USER_ALREADY_EXISTS(false) {
        @Override
        public String getMessage() {
            return USR.getUSER_ALREADY_EXISTS();
        }
    },
    FAILED_AUTH(false) {
        @Override
        public String getMessage() {
            return USR.getFAILED_AUTH();
        }

    },
    NO_SUCH_USER_FOUND(false) {
        @Override
        public String getMessage() {
            return USR.getNO_SUCH_USER_FOUND();
        }
    },
    SQL_ERROR(false) {
        @Override
        public String getMessage() {
            return USR.getSQL_ERROR();
        }
    },
    EMPTY_DATA(false) {
        @Override
        public String getMessage() {
            return USR.getEMPTY_DATA();
        }
    },
    OK(true) {
        @Override
        public String getMessage() {
            return USR.getAUTHORIZED();
        }
    };

    UserState(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    private boolean isSuccess;

    private static final String RES = "userstate.xml";
    private static final UserStateResource USR = (UserStateResource) ResourceSystem.getInstance().getResource(RES);


    abstract public String getMessage();


    public boolean isSuccess() {
        return isSuccess;
    }

}
