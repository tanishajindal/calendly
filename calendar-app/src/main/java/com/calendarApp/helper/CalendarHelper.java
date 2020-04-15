package com.calendarApp.helper;

import com.calendarApp.model.*;
import com.calendarApp.model.ImmutableBooking;
import com.calendarApp.model.ImmutableSlotAvailability;
import com.calendarApp.model.ImmutableUserDateInfo;
import com.calendarApp.model.ImmutableValidationResult;
import com.calendarApp.model.ImmutableUser;
import com.calendarApp.state.DatabaseMock;
import com.google.common.collect.ImmutableList;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

import static com.calendarApp.helper.Constants.*;
import static com.calendarApp.helper.ValidationHelper.*;

public class CalendarHelper {
    public static ValidationResult addUser(User user) {
        DatabaseMock databaseMock = DatabaseMock.getInstance();
        //check if user with same username already exists
        if(ValidationHelper.checkForUserExistence(user.getName(), databaseMock.getUsers())){
            return ImmutableValidationResult.builder()
                    .errors(Collections.singletonList(Constants.DUPLICATE_USER))
                    .entity(user).build();
        }
        UUID userId = UUID.randomUUID();
        User userToAdd = ImmutableUser.builder().from(user).id(userId).build();
        databaseMock.getUsers().put(userId, userToAdd);
        databaseMock.getUserSlotInfo().put(userId, new HashMap<>());
        return ImmutableValidationResult.builder()
                .errors(ImmutableList.of())
                .entity(userToAdd).build();
    }

    public static ValidationResult addAvailableSlots(SlotAvailability request, UUID id) {
        DatabaseMock databaseMock = DatabaseMock.getInstance();
        HashMap<LocalDate, UserDateInfo> dateTimeMap = databaseMock.getUserSlotInfo().get(id);
        UserDateInfo userDateInfo = dateTimeMap.getOrDefault(request.getDate(),
                ImmutableUserDateInfo.builder()
                        .availability(ImmutableList.of())
                        .bookings(new HashMap<>()).build());
        dateTimeMap.put(request.getDate(), ImmutableUserDateInfo.builder().from(userDateInfo)
                .addAllAvailability(request.getSlots()).build());
        return ImmutableValidationResult.builder().errors(ImmutableList.of()).entity(request).build();
    }

    public static ValidationResult getAvailableSlots(UUID id) {
        DatabaseMock databaseMock = DatabaseMock.getInstance();
        //check if user id is valid and corresponding user exists
        if(!isValidUUID(id) || !isValidUser(id, databaseMock.getUsers())){
            return ImmutableValidationResult.builder()
                    .errors(Collections.singletonList(Constants.INVALID_USER))
                    .build();
        }
        HashMap<LocalDate, UserDateInfo> dateTimeMap = databaseMock.getUserSlotInfo().get(id);
        ArrayList<SlotAvailability> availabilities = new ArrayList<>();
        dateTimeMap.entrySet().stream().forEach(entry ->
                availabilities.add(ImmutableSlotAvailability
                        .builder().date(entry.getKey())
                        .slots(entry.getValue().getAvailability()).build())
        );
        return ImmutableValidationResult.builder().errors(ImmutableList.of()).entity(availabilities).build();
    }

    public static ValidationResult processReserveRequest(BookingRequest request, UUID hostId) {
        UUID attendeeId = request.getAttendee();
        DatabaseMock databaseMock = DatabaseMock.getInstance();

        //check if attendee user id is valid and corresponding attendee user exists
        if(!isValidUUID(attendeeId) || !isValidUser(attendeeId, databaseMock.getUsers())){
            return ImmutableValidationResult.builder()
                    .errors(Collections.singletonList(INVALID_ATTENDEE))
                    .entity(request).build();
        }

        ArrayList<String> errors = new ArrayList<>();
        // check for slot availability for host
        if(!isSlotAvailable(hostId, request.getDate(), request.getTime(),
                databaseMock.getUserSlotInfo())) {
            errors.add(String.format(UNAVAILABLE_SLOT, request.getTime(), hostId));
        }
        // check for slot availability for attendee
        if(!isSlotAvailable(attendeeId, request.getDate(), request.getTime(),
                databaseMock.getUserSlotInfo())) {
            errors.add(String.format(UNAVAILABLE_SLOT, request.getTime(), attendeeId));
        }
        if(!errors.isEmpty()) {
            return ImmutableValidationResult.builder()
                .errors(errors)
                .entity(request).build();
        }

        // remove from available slot of host and attendee
        UserDateInfo hostUserDateInfo = databaseMock.getUserSlotInfo().get(hostId).get(request.getDate());
        UserDateInfo attendeeUserDateInfo = databaseMock.getUserSlotInfo().get(attendeeId).get(request.getDate());
        Set<Time> availability = hostUserDateInfo.getAvailability();
        Set<Time> updatedHostAvailability = new HashSet<>();
        updatedHostAvailability.addAll(availability);
        updatedHostAvailability.remove(request.getTime());
        availability = attendeeUserDateInfo.getAvailability();
        Set<Time> updatedAttendeeAvailability = new HashSet<>();
        updatedAttendeeAvailability.addAll(availability);
        updatedAttendeeAvailability.remove(request.getTime());

        // add booking details to host and attendee
        Booking booking = ImmutableBooking.builder()
                .attendee(attendeeId)
                .host(hostId)
                .date(request.getDate())
                .time(request.getTime())
                .build();
        UUID bookingId = UUID.randomUUID();
        databaseMock.getBookingsInfo().put(bookingId, booking);
        hostUserDateInfo = ImmutableUserDateInfo.builder().from(hostUserDateInfo).availability(updatedHostAvailability).build();
        attendeeUserDateInfo = ImmutableUserDateInfo.builder().from(hostUserDateInfo).availability(updatedAttendeeAvailability).build();
        hostUserDateInfo.getBookings().put(request.getTime(), bookingId);
        attendeeUserDateInfo.getBookings().put(request.getTime(), bookingId);
        databaseMock.getUserSlotInfo().get(hostId).put(request.getDate(), hostUserDateInfo);
        databaseMock.getUserSlotInfo().get(attendeeId).put(request.getDate(), attendeeUserDateInfo);
        return ImmutableValidationResult.builder()
                .errors(ImmutableList.of())
                .entity(booking).build();
    }
}
