package org.jiang.chat.common.exception;

import org.jiang.chat.common.enums.StatusEnum;

public class ChatException extends GenericException {
    public ChatException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ChatException(Exception e, String errorCode, String errorMessage) {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ChatException(String message) {
        super(message);
        this.errorMessage = message;
    }


    public ChatException(StatusEnum statusEnum) {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public ChatException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public ChatException(Exception oriEx) {
        super(oriEx);
    }

    public ChatException(Throwable oriEx) {
        super(oriEx);
    }

    public ChatException(String message, Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public ChatException(String message, Throwable oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }


    public static boolean isResetByPeer(String msg) {
        if ("Connection reset by peer".equals(msg)) {
            return true;
        }
        return false;
    }
}
