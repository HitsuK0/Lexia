-- Delete all create app Lexia
DROP table Work;
DROP table RDVInterpreter;
DROP table Referrer;
DROP table ProfessionalSkillInterpreter;
DROP table RequiredProfessionalSkill;
DROP table ProfessionalSkill;
DROP table requiredAcademicSkill;
DROP table Appointment;
DROP table Beneficiary;
DROP table Establishment;
DROP table AcademicSkillInterpreter;
DROP table AcademicSkill;
DROP table Coordinator;
DROP table Absence;
DROP table Interpreter;
DROP table TimeSlotPunctual;
DROP table TimeSlotBase;
DROP table Address;

DROP SEQUENCE seq_userlexia_login;

DROP TRIGGER trg_hash_password_interprete;
DROP TRIGGER trg_hash_password_beneficiary;
DROP TRIGGER trg_generate_login_interpreter;
DROP TRIGGER trg_generate_login_coordinator;
DROP TRIGGER trg_generate_login_beneficiairy;
DROP TRIGGER trg_delete_appointment;
DROP TRIGGER trg_delete_beneficiary;
DROP TRIGGER trg_delete_interpreter;
DROP TRIGGER trg_delete_academic_skill;
DROP TRIGGER trg_delete_professional_skill;
DROP TRIGGER trg_delete_timeslot_base;
DROP TRIGGER trg_delete_timeslot_punctual;
DROP TRIGGER trg_delete_referrer;
DROP TRIGGER trg_delete_establishment;
DROP TRIGGER trg_delete_address;
