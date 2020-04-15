package com.calendarApp.state;

import com.calendarApp.model.Booking;
import com.calendarApp.model.User;
import com.calendarApp.model.UserDateInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

/**
 * Singleton class to mock database
 */
public final class DatabaseMock {
    // main db entities
    private HashMap<UUID, HashMap<LocalDate, UserDateInfo>> userAvailabilitySlotInfo;
    private HashMap<UUID, User> users;
    private HashMap<UUID, Booking> bookingsInfo;

    // session management
    private HashMap<UUID, UUID> userTokens;
    private HashMap<UUID, LocalDateTime> activeTokenSessions;

    static DatabaseMock database = null;

    private DatabaseMock() {
        userAvailabilitySlotInfo = new HashMap<>();
        users = new HashMap<>();
        bookingsInfo = new HashMap<>();
        userTokens = new HashMap<>();
        activeTokenSessions = new HashMap<>();
    }

    public static DatabaseMock getInstance() {
        if(database == null){
            database = new DatabaseMock();
        }
        return database;
    }

    public HashMap<UUID, HashMap<LocalDate, UserDateInfo>> getUserSlotInfo() {
        return userAvailabilitySlotInfo;
    }

    public HashMap<UUID, User> getUsers() {
        return users;
    }

    public HashMap<UUID, Booking> getBookingsInfo() {
        return bookingsInfo;
    }

    public HashMap<UUID, UUID> getUserTokens() {
        return userTokens;
    }

    public HashMap<UUID, LocalDateTime> getActiveTokenSessions() {
        return activeTokenSessions;
    }
}
