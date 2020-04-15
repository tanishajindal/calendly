package com.calendarApp.helper;

import com.calendarApp.model.User;
import com.calendarApp.model.UserDateInfo;

import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

public class ValidationHelper {
    /**
     * check if user exists
     * @param id
     * @return
     */
    public static boolean isValidUser(UUID id, HashMap<UUID, User> users) {
        return users.containsKey(id);
    }

    public static boolean isValidUUID(UUID id) {
        return ((id != null) && !id.equals(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }

    public static boolean checkForUserExistence(String userName, HashMap<UUID, User> users) {
        return users.values().stream()
                .anyMatch(user -> user.getName().trim().equals(userName.trim()));
    }

    public static boolean isSlotAvailable(UUID userId, LocalDate date, Time time,
                                          HashMap<UUID, HashMap<LocalDate, UserDateInfo>> userAvailabilitySlotInfo) {
        HashMap<LocalDate, UserDateInfo> dateAvailabilityMap = userAvailabilitySlotInfo.get(userId);
        if(!dateAvailabilityMap.containsKey(date)) {
            return false;
        }
        UserDateInfo userDateInfo = dateAvailabilityMap.get(date);
        return userDateInfo.getAvailability().contains(time);
    };
}
