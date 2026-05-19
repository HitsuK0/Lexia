package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.DAO.DAOTimeSlotPunctual;
import be.hers.info.ProjetIntegree.DTO.DTOAppointment;
import be.hers.info.ProjetIntegree.POJO.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service link to the form used to ask an appointment
 */
public class AppointmentFormService {
    /**
     * It creates an appointment in the database using the data in the absenceDTO given in param.
     * @param appointmentDTO the appointmentDTO used to retrieve the data in the form.
     */
    public void createAppointment(DTOAppointment appointmentDTO) throws BadStatusException, SQLException {
        Appointment newAppointment = new Appointment();

        newAppointment.setStatus(appointmentDTO.getStatus());
        newAppointment.setAppointmentLocals(appointmentDTO.getAppointmentLocals());

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        Beneficiary beneficiary = daoBeneficiary.find(appointmentDTO.getNumBeneficiary());
        newAppointment.setBeneficiary(beneficiary);

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        List<Interpreter> listInterpreters = new ArrayList<>();
        for(int num : appointmentDTO.getNumInterpreters())
            listInterpreters.add(daoInterpreter.find(num));

        TimeSlotPunctual newTimeSlotPunctual = new TimeSlotPunctual(
                appointmentDTO.getStartTime(),
                appointmentDTO.getEndTime(),
                appointmentDTO.getStartDate(),
                appointmentDTO.getEndDate()
        );

        DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
        TimeSlotPunctual tempTimeSlot = daoTimeSlotPunctual.findSameTimeSlot(newTimeSlotPunctual);
        if(tempTimeSlot == null)
            daoTimeSlotPunctual.create(newTimeSlotPunctual);
        else
            newTimeSlotPunctual = tempTimeSlot;


        newAppointment.setBeneficiary(beneficiary);
    }
}
