package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAbsence;
import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;

@Service
public class AbsenceService {


    public void createAbsence(DTOAbsence absenceDTO, Absence absence) {
        absence.setReason(absenceDTO.getReason());
        try{
            absence.setStatus("en attente");
        }
        catch(BadStatusException e){

        }
        absence.setPrivateReason(absenceDTO.isPrivateReason());
        TimeSlotPunctual timeSlotPunctual =  new TimeSlotPunctual();
        timeSlotPunctual.setStartTime(absenceDTO.getStartTime());
        timeSlotPunctual.setStartDate(absenceDTO.getStartDate());
        timeSlotPunctual.setEndDate(absenceDTO.getEndDate());
        LocalTime duration;
        if(absenceDTO.isFullDay()){
            duration = LocalTime.MIDNIGHT.plus(Duration.ofHours(24));
        }
        else{
            Duration d = Duration.between(absenceDTO.getStartTime(), absenceDTO.getEndTime());
            duration = LocalTime.MIDNIGHT.plus(d);
        }
        timeSlotPunctual.setDuration(duration);

        absence.setTimeSlot(timeSlotPunctual);
        DAOAbsence DAOabsence = new DAOAbsence();
        DAOabsence.create(absence);
    }
}
