-- ATTENTION : Vérifier le formatage des dates par rapport au JAVA + /!\ INSERTION FICTIF
-- ============================================================
-- ADDRESS
-- ============================================================
INSERT INTO Address (postalCode, postalBox, locality, hamlet) VALUES (75001, '1 Rue de Rivoli', 'Paris', 'Louvre');
INSERT INTO Address (postalCode, postalBox, locality, hamlet) VALUES (69002, '5 Place Bellecour', 'Lyon', NULL);
INSERT INTO Address (postalCode, postalBox, locality, hamlet) VALUES (13001, '12 Rue Paradis', 'Marseille', NULL);
INSERT INTO Address (postalCode, postalBox, locality, hamlet) VALUES (31000, '8 Rue du Taur', 'Toulouse', 'Capitole');
INSERT INTO Address (postalCode, postalBox, locality, hamlet) VALUES (67000, '3 Rue des Orfèvres', 'Strasbourg', NULL);

-- ============================================================
-- TIMESLOTBASE
-- startTime -> heure de début (on utilise DATE pour stocker une heure via TO_DATE)
-- duration  -> durée (ex: 1h = TO_DATE('01:00','HH24:MI'))
-- dayNumber -> 1=Lundi ... 7=Dimanche
-- ============================================================
INSERT INTO TimeSlotBase (startTime, duration, dayNumber) VALUES (TO_DATE('08:00', 'HH24:MI'), TO_DATE('01:00', 'HH24:MI'), 1);
INSERT INTO TimeSlotBase (startTime, duration, dayNumber) VALUES (TO_DATE('10:00', 'HH24:MI'), TO_DATE('01:30', 'HH24:MI'), 2);
INSERT INTO TimeSlotBase (startTime, duration, dayNumber) VALUES (TO_DATE('14:00', 'HH24:MI'), TO_DATE('02:00', 'HH24:MI'), 3);
INSERT INTO TimeSlotBase (startTime, duration, dayNumber) VALUES (TO_DATE('09:00', 'HH24:MI'), TO_DATE('00:45', 'HH24:MI'), 4);
INSERT INTO TimeSlotBase (startTime, duration, dayNumber) VALUES (TO_DATE('16:00', 'HH24:MI'), TO_DATE('01:00', 'HH24:MI'), 5);

-- ============================================================
-- TIMESLOTPUNCTUAL
-- ============================================================
INSERT INTO TimeSlotPunctual (startTime, duration, startDate, endDate) VALUES (TO_DATE('08:00', 'HH24:MI'), TO_DATE('01:00', 'HH24:MI'), TO_DATE('2025-01-10', 'YYYY-MM-DD'), TO_DATE('2025-01-10', 'YYYY-MM-DD'));
INSERT INTO TimeSlotPunctual (startTime, duration, startDate, endDate) VALUES (TO_DATE('10:30', 'HH24:MI'), TO_DATE('01:30', 'HH24:MI'), TO_DATE('2025-02-15', 'YYYY-MM-DD'), TO_DATE('2025-02-15', 'YYYY-MM-DD'));
INSERT INTO TimeSlotPunctual (startTime, duration, startDate, endDate) VALUES (TO_DATE('14:00', 'HH24:MI'), TO_DATE('02:00', 'HH24:MI'), TO_DATE('2025-03-01', 'YYYY-MM-DD'), TO_DATE('2025-03-01', 'YYYY-MM-DD'));
INSERT INTO TimeSlotPunctual (startTime, duration, startDate, endDate) VALUES (TO_DATE('09:00', 'HH24:MI'), TO_DATE('00:30', 'HH24:MI'), TO_DATE('2025-04-20', 'YYYY-MM-DD'), TO_DATE('2025-04-20', 'YYYY-MM-DD'));
INSERT INTO TimeSlotPunctual (startTime, duration, startDate, endDate) VALUES (TO_DATE('11:00', 'HH24:MI'), TO_DATE('01:00', 'HH24:MI'), TO_DATE('2025-05-05', 'YYYY-MM-DD'), TO_DATE('2025-05-05', 'YYYY-MM-DD'));

-- ============================================================
-- INTERPRETER
-- login généré automatiquement par trigger (I0001, I0002, ...)
-- password hashé automatiquement par trigger SHA256
-- weeklyWorkHours > 0
-- ============================================================
INSERT INTO Interpreter (password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES ('motdepasse1', 'Martin',  'Alice',   'alice.martin@example.com',    '0601010101', 35, 1);
INSERT INTO Interpreter (password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES ('motdepasse2', 'Dupont',  'Bob',     'bob.dupont@example.com',      '0602020202', 28, 2);
INSERT INTO Interpreter (password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES ('motdepasse3', 'Bernard', 'Claire',  'claire.bernard@example.com',  '0603030303', 40, 3);
INSERT INTO Interpreter (password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES ('motdepasse4', 'Leroy',   'David',   'david.leroy@example.com',     '0604040404', 20, 4);
INSERT INTO Interpreter (password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES ('motdepasse5', 'Petit',   'Emma',    'emma.petit@example.com',      '0605050505', 32, 5);
-- login générés par trigger : I0001, I0002, I0003, I0004, I0005
-- numInterpreter générés    : 1, 2, 3, 4, 5
INSERT INTO Interpreter (login, password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES (NULL, 'motdepasse1', 'Dubois',   'Pierre',   'pierre.dubois@example.com',   '0601234567', 35, 1);
INSERT INTO Interpreter (login, password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES (NULL, 'motdepasse2', 'Lambert',  'Sophie',   'sophie.lambert@example.com',  '0612345678', 28, 2);
INSERT INTO Interpreter (login, password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES (NULL, 'motdepasse3', 'Rousseau', 'Antoine',  'antoine.rousseau@example.com','0623456789', 40, 3);
INSERT INTO Interpreter (login, password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES (NULL, 'motdepasse4', 'Chevalier','Nathalie',  'nathalie.chevalier@example.com','0634567890', 20, 4);
INSERT INTO Interpreter (login, password, lastName, firstName, emailAddress, phoneNumber, weeklyWorkHours, FKAddress)
VALUES (NULL, 'motdepasse5', 'Bonnet',   'Julien',   'julien.bonnet@example.com',   '0645678901', 32, 5);


-- ============================================================
-- ABSENCE
-- CHECK : FKTimeSlotBase XOR FKTimeSlotPunctual (même logique que Appointment)
-- status IN ('en attente', 'accepte', 'refuse')
-- ============================================================
INSERT INTO Absence (status,reasons,FKTimeSlotBase, FKTimeSlotPunctual, FKnumInterpreter) VALUES ('en attente','Maladie', 1,    NULL, 1);
INSERT INTO Absence (status,reasons,privateReason, FKTimeSlotBase, FKTimeSlotPunctual, FKnumInterpreter) VALUES ('accepte','Maladie',1,    NULL, 2,    2);
INSERT INTO Absence (status,reasons, FKTimeSlotBase, FKTimeSlotPunctual, FKnumInterpreter) VALUES ('refuse','Maladie',     3,    NULL, 3);
INSERT INTO Absence (status,reasons, FKTimeSlotBase, FKTimeSlotPunctual, FKnumInterpreter) VALUES ('en attente','Maladie', NULL, 4,    4);
INSERT INTO Absence (status,reasons, FKTimeSlotBase, FKTimeSlotPunctual, FKnumInterpreter) VALUES ('accepte','Maladie',    5,    NULL, 5);

-- ============================================================
-- COORDINATOR
-- isAdmin DEFAULT 0, CHECK (0 ou 1)
-- FK -> Interpreter
-- ============================================================
INSERT INTO Coordinator (isAdmin, FKnumInterpreter) VALUES (1, 1);
INSERT INTO Coordinator (isAdmin, FKnumInterpreter) VALUES (0, 2);
INSERT INTO Coordinator (isAdmin, FKnumInterpreter) VALUES (0, 3);
INSERT INTO Coordinator (isAdmin, FKnumInterpreter) VALUES (0, 4);
INSERT INTO Coordinator (isAdmin, FKnumInterpreter) VALUES (0, 5);

-- ============================================================
-- ACADEMICSKILL
-- ============================================================
INSERT INTO AcademicSkill (designation) VALUES ('Mathématiques');
INSERT INTO AcademicSkill (designation) VALUES ('Français');
INSERT INTO AcademicSkill (designation) VALUES ('Sciences');
INSERT INTO AcademicSkill (designation) VALUES ('Histoire-Géographie');
INSERT INTO AcademicSkill (designation) VALUES ('Langues étrangères');
-- numAcademicSkill générés : 1, 2, 3, 4, 5

-- ============================================================
-- ACADEMICSKILLINTERPRETER
-- PK composite (numAcademicSkill, numInterpreter)
-- ============================================================
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (1, 1);
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (2, 2);
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (3, 3);
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (4, 4);
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (5, 5);

INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (1, 6);
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (2, 7);
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (3, 8);
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (4, 9);
INSERT INTO AcademicSkillInterpreter (numAcademicSkill, numInterpreter) VALUES (5, 10);

-- ============================================================
-- ESTABLISHMENT
-- FKAddress UNIQUE -> utiliser des adresses non encore utilisées
-- ============================================================

INSERT INTO Establishment (FKAddress, name, phoneNumber, educationLevel) VALUES (1,  'École Nationale du Nord',   '0320000001', '2');
INSERT INTO Establishment (FKAddress, name, phoneNumber, educationLevel) VALUES (2,  'Lycée Atlantique',          '0240000002', '3');
INSERT INTO Establishment (FKAddress, name, phoneNumber, educationLevel) VALUES (3,  'Collège Gironde',           '0556000003', '2');
INSERT INTO Establishment (FKAddress, name, phoneNumber, educationLevel) VALUES (4,  'Maternelle Soleil',         '0493000004', '1');
INSERT INTO Establishment (FKAddress, name, phoneNumber, educationLevel) VALUES (5, 'Université du Languedoc',   '0467000005', '4');
-- numEstablishment générés : 1, 2, 3, 4, 5

-- ============================================================
-- BENEFICIARY
-- login généré automatiquement par trigger (B0001, B0002, ...)
-- password hashé automatiquement par trigger SHA256
-- hourQuota > 0, educationLevel IN [0..4]
-- ============================================================
INSERT INTO Beneficiary (password, firstName, lastName, phoneNumber, emailAddress, hourQuota, educationLevel, communicationLanguage, FKnumInterpreter, FKAddress)
VALUES ('motdepasse6', 'Lucas',   'Fontaine', '0611111111', 'lucas.fontaine@example.com',   10, 2, 'Français',  1, 1);
INSERT INTO Beneficiary (password, firstName, lastName, phoneNumber, emailAddress, hourQuota, educationLevel, communicationLanguage, FKnumInterpreter, FKAddress)
VALUES ('motdepasse7', 'Chloé',   'Renard',   '0622222222', 'chloe.renard@example.com',     20, 3, 'Anglais',   2, 2);
INSERT INTO Beneficiary (password, firstName, lastName, phoneNumber, emailAddress, hourQuota, educationLevel, communicationLanguage, FKnumInterpreter, FKAddress)
VALUES ('motdepasse8', 'Théo',    'Garnier',  '0633333333', 'theo.garnier@example.com',     15, 1, 'Arabe',     3, 3);
INSERT INTO Beneficiary (password, firstName, lastName, phoneNumber, emailAddress, hourQuota, educationLevel, communicationLanguage, FKnumInterpreter, FKAddress)
VALUES ('motdepasse9', 'Inès',    'Perrin',   '0644444444', 'ines.perrin@example.com',      8,  4, 'Espagnol',  4, 4);
INSERT INTO Beneficiary (password, firstName, lastName, phoneNumber, emailAddress, hourQuota, educationLevel, communicationLanguage, FKnumInterpreter, FKAddress)
VALUES ('motdepasse10','Mathis',  'Marchand', '0655555555', 'mathis.marchand@example.com',  12, 0, 'Portugais', 5, 5);
-- login générés par trigger : B0001, B0002, B0003, B0004, B0005
-- numBeneficiary générés    : 1, 2, 3, 4, 5


-- ============================================================
-- APPOINTMENT
-- CHECK : FKTimeSlotBase XOR FKTimeSlotPunctual
-- status IN ('en attente', 'accepte', 'refuse')
-- FK : Interpreter, Establishment, Beneficiary
-- ============================================================
INSERT INTO Appointment (status, local, FKnumEstablishment, FKnumBeneficiary, FKTimeSlotBase, FKTimeSlotPunctual)
VALUES ('en attente', 'M1', 1, 1, 1,    NULL);
INSERT INTO Appointment (status, local, FKnumEstablishment, FKnumBeneficiary, FKTimeSlotBase, FKTimeSlotPunctual)
VALUES ('accepte',    'M2,M3', 2, 2, NULL, 1);
INSERT INTO Appointment (status, local, FKnumEstablishment, FKnumBeneficiary, FKTimeSlotBase, FKTimeSlotPunctual)
VALUES ('refuse',     'M4', 3, 3, 2,    NULL);
INSERT INTO Appointment (status, local, FKnumEstablishment, FKnumBeneficiary, FKTimeSlotBase, FKTimeSlotPunctual)
VALUES ('en attente', 'M5', 4, 4, NULL, 2);
INSERT INTO Appointment (status, local, FKnumEstablishment, FKnumBeneficiary, FKTimeSlotBase, FKTimeSlotPunctual)
VALUES ('accepte',    'M6,M7', 5, 5, 3,    NULL);
-- numAppointment générés : 1, 2, 3, 4, 5




-- ============================================================
-- REQUIREDACADEMICSKILL
-- PK composite (numAppointment, numAcademicSkill)
-- ============================================================
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (1, 1);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (2, 2);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (3, 3);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (4, 4);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (5, 5);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (6, 1);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (7, 2);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (8, 3);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (9, 4);
INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) VALUES (10, 5);

-- ============================================================
-- PROFESSIONALSKILL
-- ============================================================
INSERT INTO ProfessionalSkill (designation) VALUES ('Transcription');
INSERT INTO ProfessionalSkill (designation) VALUES ('Translittération');
INSERT INTO ProfessionalSkill (designation) VALUES ('Translation');
INSERT INTO ProfessionalSkill (designation) VALUES ('Lange Des signes');
INSERT INTO ProfessionalSkill (designation) VALUES ('Traduction signes');
-- numProfessionalSkill générés : 1, 2, 3, 4 ,5

-- ============================================================
-- REQUIREDPROFESSIONALSKILL
-- ============================================================
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (1, 1);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (2, 2);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (3, 3);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (4, 4);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (5, 5);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (6, 1);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (7, 2);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (8, 3);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (9, 4);
INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) VALUES (10, 5);

-- ============================================================
-- PROFESSIONALSKILLINTERPRETER
-- ============================================================
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (1, 1);
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (2, 2);
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (3, 3);
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (4, 4);
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (5, 5);

INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (1, 6);
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (2, 7);
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (3, 8);
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (4, 9);
INSERT INTO ProfessionalSkillInterpreter (numProfessionalSkill, numInterpreter) VALUES (5, 10);
-- ============================================================
-- REFERRER
-- emailAddress et phoneNumber : contraintes regex
-- FK -> Establishment
-- ============================================================
INSERT INTO Referrer (firstName, lastName, phoneNumber, emailAddress, FKEstablishment) VALUES ('Sophie',  'Moreau',   '0611111111', 'sophie.moreau@ecole.fr',    1);
INSERT INTO Referrer (firstName, lastName, phoneNumber, emailAddress, FKEstablishment) VALUES ('Nicolas', 'Laurent',  '0622222222', 'nicolas.laurent@lycee.fr',  2);
INSERT INTO Referrer (firstName, lastName, phoneNumber, emailAddress, FKEstablishment) VALUES ('Marie',   'Simon',    '0633333333', 'marie.simon@college.fr',    3);
INSERT INTO Referrer (firstName, lastName, phoneNumber, emailAddress, FKEstablishment) VALUES ('Julien',  'Michel',   '0644444444', 'julien.michel@maternelle.fr',4);
INSERT INTO Referrer (firstName, lastName, phoneNumber, emailAddress, FKEstablishment) VALUES ('Lucie',   'Lefebvre', '0655555555', 'lucie.lefebvre@univ.fr',    5);
-- numReferer générés : 1, 2, 3, 4, 5

-- ============================================================
-- RDVINTERPRETER
-- Table de liaison Appointment <-> Interpreter (supplémentaires)
-- ============================================================
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (1, 1);
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (2, 2);
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (3, 3);
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (4, 4);
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (5, 5);


INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (6, 6);
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (7, 7);
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (8, 8);
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (9, 9);
INSERT INTO RDVInterpreter (numAppointment, numInterpreter) VALUES (10, 10);


-- ============================================================
-- WORK
-- Liaison Referrer <-> Establishment
-- ============================================================
INSERT INTO Work (numReferer, numEstablishment) VALUES (1, 1);
INSERT INTO Work (numReferer, numEstablishment) VALUES (2, 2);
INSERT INTO Work (numReferer, numEstablishment) VALUES (3, 3);
INSERT INTO Work (numReferer, numEstablishment) VALUES (4, 4);
INSERT INTO Work (numReferer, numEstablishment) VALUES (5, 5);