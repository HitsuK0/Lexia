-- Delete all create app Lexia
DROP TABLE IF EXISTS Work;
DROP TABLE IF EXISTS RDVInterpreter;
DROP TABLE IF EXISTS Referrer;
DROP TABLE IF EXISTS ProfessionalSkillInterpreter;
DROP TABLE IF EXISTS RequiredProfessionalSkill;
DROP TABLE IF EXISTS ProfessionalSkill;
DROP TABLE IF EXISTS requiredAcademicSkill;
DROP TABLE IF EXISTS Appointment;
DROP TABLE IF EXISTS Beneficiary;
DROP TABLE IF EXISTS Establishment;
DROP TABLE IF EXISTS AcademicSkillInterpreter;
DROP TABLE IF EXISTS AcademicSkill;
DROP TABLE IF EXISTS Coordinator;
DROP TABLE IF EXISTS Absence;
DROP TABLE IF EXISTS Interpreter;
DROP TABLE IF EXISTS TimeSlotPunctual;
DROP TABLE IF EXISTS TimeSlotBase;
DROP TABLE IF EXISTS Address;

DROP SEQUENCE IF EXISTS seq_userlexia_login;

DROP FUNCTION IF EXISTS trg_generate_login_interpreter_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_generate_login_coordinator_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_generate_login_beneficiairy_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_appointment_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_beneficiary_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_interpreter_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_academic_skill_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_professional_skill_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_timeslot_base_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_timeslot_punctual_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_referrer_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_establishment_fn() CASCADE;
DROP FUNCTION IF EXISTS trg_delete_address_fn() CASCADE;
