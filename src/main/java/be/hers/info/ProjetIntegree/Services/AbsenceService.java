package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAbsence;
import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;



/**
 * @author Quentin Vanderheyden
 * @reviewer Nicolas Jean-François, Halet Louis
 */

/**
 * Service link to the Absence.
 */
@Service
public class AbsenceService {

    /**
     * It create an Absence in the database using the data in the absenceDTO given in param.
     * @param absenceDTO is the DTOAbsence used by spring to copy the data in the form.

     */
    public void createAbsence(DTOAbsence absenceDTO, int numInterpreter) throws BadStatusException, SQLException {
        Absence absence = new Absence();
        absence.setReason(absenceDTO.getReason());
        absence.setStatus("en attente");
        absence.setPrivateReason(absenceDTO.isPrivateReason());
        TimeSlotPunctual timeSlotPunctual =  new TimeSlotPunctual();
        timeSlotPunctual.setStartDate(absenceDTO.getStartDate());
        timeSlotPunctual.setEndDate(absenceDTO.getEndDate());
        LocalTime duration;
        if(absenceDTO.isFullDay()){
            timeSlotPunctual.setStartTime(LocalTime.MIDNIGHT);
            duration = LocalTime.MIDNIGHT.plus(Duration.ofHours(24));
        }
        else{
            timeSlotPunctual.setStartTime(absenceDTO.getStartTime());
            Duration d = Duration.between(absenceDTO.getStartTime(), absenceDTO.getEndTime());
            duration = LocalTime.MIDNIGHT.plus(d);
        }
        timeSlotPunctual.setDuration(duration);

        absence.setTimeSlot(timeSlotPunctual);
        DAOAbsence daoAbsence = new DAOAbsence();
        daoAbsence.create(absence, numInterpreter);
    }

}
