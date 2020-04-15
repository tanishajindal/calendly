package com.calendarApp.helper;

public class Constants {
    public static final String INVALID_USER = "INVALID_USER: Unauthorized User";
    public static final String INVALID_HOST = "INVALID_HOST: Invalid or Unauthorized Host User";
    public static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED: Unauthenticated User, check token or login to create valid token";
    public static final String SESSION_EXPIRED = "SESSION_EXPIRED: User session expired with passed token, login to create fresh valid token";
    public static final String UNAVAILABLE_SLOT = "UNAVAILABLE_SLOT: Unavailable slot @Time: {%s} for @Host/Attendee: {%s}";
    public static final String INVALID_ATTENDEE = "INVALID_ATTENDEE: Invalid or Unauthorized Attendee User";
    public static final String DUPLICATE_USER = "DUPLICATE_USER: User Name Already Exists";
    public static final long EXPIRATION_TIME_IN_MINUTES = 30L;
}
