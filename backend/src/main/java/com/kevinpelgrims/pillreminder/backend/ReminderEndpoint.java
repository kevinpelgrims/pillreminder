package com.kevinpelgrims.pillreminder.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.kevinpelgrims.pillreminder.backend.OfyService.ofy;

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
     * This method returns a list of <code>Reminder</code> objects
     *
     * @return A list of <code>Reminder</code> objects
     */
    @ApiMethod(name = "listReminder")
    public List<Reminder> listReminder() {
        logger.info("Calling listReminder method");
        return ofy().load().type(Reminder.class).list();
    }

    /**
     * This method gets the <code>Reminder</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Reminder</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getReminder")
    public Reminder getReminder(@Named("id") Long id) {
        logger.info("Calling getReminder method");
        return ofy().load().type(Reminder.class).id(id).now();
        // Also an option: type(..).filter("id",id).first.now();
    }

    /**
     * This inserts a new <code>Reminder</code> object.
     *
     * @param reminder The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertReminder")
    public Reminder insertReminder(Reminder reminder) throws ConflictException {
        logger.info("Calling insertReminder method");
        if (reminder.getId() != null && getReminder(reminder.getId()) != null) {
            throw new ConflictException("Reminder already exists");
        }
        ofy().save().entity(reminder).now();
        return reminder;
    }

    /**
     * This updates an existing <code>Reminder</code> object.
     *
     * @param reminder The object to be updated.
     * @return The object to be updated.
     * @throws NotFoundException
     */
    @ApiMethod(name = "updateReminder")
    public Reminder updateReminder(Reminder reminder) throws NotFoundException {
        logger.info("Calling updateReminder method");
        if (reminder.getId() == null || getReminder(reminder.getId()) == null) {
            throw new NotFoundException("Reminder does not exist");
        }
        ofy().save().entity(reminder).now();
        return reminder;
    }

    /**
     * This deletes an existing <code>Reminder</code> object.
     *
     * @param id The id of the object to be deleted.
     * @throws NotFoundException
     */
    @ApiMethod(name = "deleteReminder")
    public void deleteReminder(@Named("id") Long id) throws NotFoundException {
        logger.info("Calling deleteReminder method");
        Reminder reminder = getReminder(id);
        if (reminder == null) {
            throw new NotFoundException("Reminder does not exist");
        }
        ofy().delete().entity(reminder).now();
    }
}