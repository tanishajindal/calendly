package com.calendarApp.resource;

import com.calendarApp.helper.CalendarHelper;
import com.calendarApp.model.BookingRequest;
import com.calendarApp.model.SlotAvailability;
import com.calendarApp.model.User;
import com.calendarApp.model.ValidationResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.calendarApp.helper.LoginHelper.processUserToken;
import static com.calendarApp.helper.LoginHelper.validateAndSlideSession;

@Path("/v1/calendar")
@Produces(MediaType.APPLICATION_JSON)
public class CalendarResource {

    static Logger LOGGER = Logger.getLogger("Calendar Resource");

    @POST
    @Path("/user")
    public Response registerUser(User user) {
        try {
            ValidationResult result = CalendarHelper.addUser(user);
            if(!result.getErrors().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            // if user is created create valid session and return token Id
            result = processUserToken((User)result.getEntity());
            LOGGER.log(Level.INFO, "Successfully created User");
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/user/{id}/slots")
    public Response addSlotsAvailability(SlotAvailability request, @PathParam("id") UUID id, @HeaderParam("token") UUID token) {
        try {
            ValidationResult result = validateAndSlideSession(token, id);
            if(!result.getErrors().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            result = CalendarHelper.addAvailableSlots(request, id);
            if(!result.getErrors().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            LOGGER.log(Level.INFO, "Successfully added slots");
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{id}/slots")
    public Response getAvailableSlots(BookingRequest request, @PathParam("id") UUID id, @HeaderParam("token") UUID token) {
        try {
            ValidationResult result = validateAndSlideSession(token, id);
            if(!result.getErrors().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            result = CalendarHelper.getAvailableSlots(id);
            if(!result.getErrors().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            return Response.ok().entity(result).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/user/{id}/reserve")
    public Response bookSlotsAvailability(BookingRequest request, @PathParam("id") UUID hostId, @HeaderParam("token") UUID token) {
        try {
            ValidationResult result = validateAndSlideSession(token, hostId);
            if(!result.getErrors().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            result = CalendarHelper.processReserveRequest(request, hostId);
            if(!result.getErrors().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}