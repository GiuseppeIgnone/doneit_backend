package com.sini.doneit.model;

import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;

public class MessageCode {
    public static final int INVALID_CREDENTIAL = 1;
    public static final int INVALID_DATA = 2;
    public static final int SUCCESSFUL_LOGIN = 3;
    public static final int SUCCESSFUL_REGISTER = 4;
    public static final int TOKEN_EXCEPTION = 5;
    public static final int TODO_CREATED = 6;
    public static final int TODO_DELETED = 7;
    public static final int ERROR_TODO_REMOVAL = 8;
    public static final int SUCCESS_TODO_MODIFIED = 9;
    public static final int FAILED_TODO_MODIFY = 10;
    public static final int USER_ALREADY_CREATED = 11;
    public static final int SUCCESSFUL_REQUEST = 12;
    public static final int FIRST_LOGIN = 13;
    public static final int FOLLOWING_REQUEST_FAILED = 13;
    public static final int PROPOSAL_CREATED = 14;
    public static final int CFU_INSUFFICIENT = 15;
    public static final int INCOMPLETE_REGISTER = 16;
    public static final int EVENT_CREATED = 17;
    public static final int CHAT_MESSAGE_SENT = 19;
    public static final int CHAT_MESSAGE_NOT_SENT = 20;
}
