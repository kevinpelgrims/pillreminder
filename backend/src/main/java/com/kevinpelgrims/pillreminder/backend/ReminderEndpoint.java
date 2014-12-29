package com.kevinpelgrims.pillreminder.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "reminderApi",
        version = "v1",
        resource = "reminder",
        namespace = @ApiNamespace(
                ownerDomain = "backend.pillreminder.kevinpelgrims.com",
                ownerName = "backend.pillreminder.kevinpelgrims.com",
                packagePath = ""
        )
)
public class ReminderEndpoint {

    private static final Logger logger = Logger.getLogger(ReminderEndpoint.class.getName());

    /**
     * This method gets the <code>Reminder</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Reminder</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getReminder")
    public Reminder getReminder(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getReminder method");
        return null;
    }

    /**
     * This inserts a new <code>Reminder</code> object.
     *
     * @param reminder The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertReminder")
    public Reminder insertReminder(Reminder reminder) {
        // TODO: Implement this function
        logger.info("Calling insertReminder method");
        return reminder;
    }
}