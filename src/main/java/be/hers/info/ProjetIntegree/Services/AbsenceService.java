package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAbsence;
import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;


/**
 * Service link to the Absence.
 */
@Service
public class AbsenceService {
    
    /**
     * It create an Absence in the database using the data in the absenceDTO given in param.
     * @param absenceDTO is the DTOAbsence used by spring to copy the data in the form.
     * @param absence is the Absence used to convert the DTOAbsence to Absence and create an Absence in the database.
     */
    public void createAbsence(DTOAbsence absenceDTO, Absence absence) {
        absence.setReason(absenceDTO.getReason());
        try{
            absence.setStatus("en attente");
        }
        catch(BadStatusException e){

        }
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
        DAOabsence.create(absence);
    }
}
