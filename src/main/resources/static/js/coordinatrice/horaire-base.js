let currentTab = 'interprete';
let currentInterpreteId = null;
let currentBeneficiaireId = null;
let calendarInterprete = null;
let calendarBeneficiaire = null;

/* Generates time options in 5-minute increments between a start and end time (in minutes)
and populates the given select element. */
function generateHours(selectId, start, end) {
    const select = document.getElementById(selectId);
    select.innerHTML = '<option value="" disabled selected>-- Heure --</option>';
    for (let t = start; t <= end; t += 5) {
        const hh = String(Math.floor(t / 60)).padStart(2, '0');
        const mm = String(t % 60).padStart(2, '0');
        const option = document.createElement('option');
        option.value = `${hh}:${mm}`;
        option.textContent = `${hh}:${mm}`;
        select.appendChild(option);
    }
}

/* Displays an inline validation error on a form input. */
function displayError(input, message) {
    input.classList.add('is-invalid');
    const div = document.createElement('div');
    div.classList.add('invalid-feedback');
    div.style.display = 'block';
    div.style.color = '#dc3545';
    div.style.fontSize = '0.875rem';
    div.textContent = message;
    input.insertAdjacentElement('afterend', div);
}

/* Clears all validation errors from a list of elements. */
function clearErrors(elements) {
    elements.forEach(el => {
        el.classList.remove('is-invalid');
        el.style.borderColor = '';
        const fb = el.nextElementSibling;
        if (fb && fb.classList.contains('invalid-feedback')) fb.remove();
    });
}

/* Updates the selected interpreter and refreshes the calendar. */
function selectInterprete(element) {
    currentInterpreteId = parseInt(element.dataset.id);
    document.getElementById('interpreteSelectionne').textContent = element.dataset.nom;
    if (calendarInterprete) calendarInterprete.refetchEvents();
}

/* Updates the selected beneficiary and refreshes the calendar. */
function selectBeneficiaire(element) {
    currentBeneficiaireId = parseInt(element.dataset.id);
    document.getElementById('beneficiaireSelectionne').textContent = element.dataset.nom;
    if (calendarBeneficiaire) calendarBeneficiaire.refetchEvents();
}

/* Fills the interpreter select in the beneficiary add-creneau modal with the
suggested interpreters for the currently selected beneficiary (referent first). */
function loadSuggestedInterpreters() {
    const select = document.getElementById('creneauInterprete');
    select.innerHTML = '<option value="" disabled selected>Choisissez un interprète</option>';

    if (currentBeneficiaireId === null) return;

    fetch(`/coordinatrice/horaire-base/beneficiaire/${currentBeneficiaireId}/interpreters`)
        .then(r => r.json())
        .then(data => {
            data.forEach(i => {
                const option = document.createElement('option');
                option.value = i.numInterpreter;
                option.textContent = i.label;
                select.appendChild(option);
            });
        })
        .catch(err => console.error('Error loading suggested interpreters:', err));
}

document.addEventListener('DOMContentLoaded', function () {

    generateHours('creneauHeureDebutInterprete', 8 * 60, 18 * 60 + 55);
    generateHours('creneauHeureFinInterprete', 8 * 60 + 5, 19 * 60);
    generateHours('creneauHeureDebutBeneficiaire', 8 * 60, 18 * 60 + 55);
    generateHours('creneauHeureFinBeneficiaire', 8 * 60 + 5, 19 * 60);
    generateHours('editCreneauHeureDebutInterprete', 8 * 60, 18 * 60 + 55);
    generateHours('editCreneauHeureFinInterprete', 8 * 60 + 5, 19 * 60);
    generateHours('editCreneauHeureDebutBeneficiaire', 8 * 60, 18 * 60 + 55);
    generateHours('editCreneauHeureFinBeneficiaire', 8 * 60 + 5, 19 * 60);

    /* Selects the first interpreter and beneficiary from the lists rendered by Thymeleaf,
    so a calendar is shown by default on page load. */
    const firstInterprete = document.querySelector('#listeInterpretes .dropdown-interprete');
    if (firstInterprete) selectInterprete(firstInterprete);

    const firstBeneficiaire = document.querySelector('#listeBeneficiaires .dropdown-beneficiaire');
    if (firstBeneficiaire) selectBeneficiaire(firstBeneficiaire);

    /* Handles tab navigation between interpreters and beneficiaries. */
    document.querySelectorAll('#horaireTab .nav-link').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelectorAll('#horaireTab .nav-link').forEach(l => l.classList.remove('active'));
            this.classList.add('active');

            currentTab = this.dataset.tab;
            document.getElementById('section-interprete').style.display = currentTab === 'interprete' ? '' : 'none';
            document.getElementById('section-beneficiaire').style.display = currentTab === 'beneficiaire' ? '' : 'none';

            if (currentTab === 'beneficiaire') calendarBeneficiaire.updateSize();
            if (currentTab === 'interprete') calendarInterprete.updateSize();
        });
    });

    /* Binds click listeners on interpreter dropdown items. */
    document.getElementById('listeInterpretes').addEventListener('click', function (e) {
        const item = e.target.closest('.dropdown-interprete');
        if (!item) return;
        e.preventDefault();
        selectInterprete(item);
    });

    /* Binds click listeners on beneficiary dropdown items. */
    document.getElementById('listeBeneficiaires').addEventListener('click', function (e) {
        const item = e.target.closest('.dropdown-beneficiaire');
        if (!item) return;
        e.preventDefault();
        selectBeneficiaire(item);
    });

    /* Loads the suggested interpreters list for the currently selected beneficiary
    each time the add-creneau modal is opened. */
    document.getElementById('modalAjoutCreneauBeneficiaire').addEventListener('show.bs.modal', function () {
        loadSuggestedInterpreters();
    });

    /* Regenerates end hour options to always be after the selected start hour - interpreter add modal. */
    document.getElementById('creneauHeureDebutInterprete').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('creneauHeureFinInterprete', h * 60 + m + 5, 19 * 60);
    });

    /* Regenerates end hour options to always be after the selected start hour - beneficiary add modal. */
    document.getElementById('creneauHeureDebutBeneficiaire').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('creneauHeureFinBeneficiaire', h * 60 + m + 5, 19 * 60);
    });

    /* Regenerates end hour options to always be after the selected start hour - interpreter edit modal. */
    document.getElementById('editCreneauHeureDebutInterprete').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('editCreneauHeureFinInterprete', h * 60 + m + 5, 19 * 60);
    });

    /* Regenerates end hour options to always be after the selected start hour - beneficiary edit modal. */
    document.getElementById('editCreneauHeureDebutBeneficiaire').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('editCreneauHeureFinBeneficiaire', h * 60 + m + 5, 19 * 60);
    });

    /* Hides or shows appointment fields based on the unavailability checkbox - add modal. */
    document.getElementById('creneauIndispo').addEventListener('change', function () {
        document.getElementById('creneauInterpreteFields').style.display = this.checked ? 'none' : '';
    });

    /* Resets the interpreter add creneau modal on close. */
    document.getElementById('modalAjoutCreneauInterprete').addEventListener('hidden.bs.modal', function () {
        document.getElementById('creneauJourInterprete').selectedIndex = 0;
        document.getElementById('creneauIndispo').checked = false;
        document.getElementById('creneauInterpreteFields').style.display = '';
        document.getElementById('creneauBeneficiaire').selectedIndex = 0;
        document.getElementById('creneauEtablissementInterprete').selectedIndex = 0;
        document.getElementById('creneauLocalInterprete').value = '';
        document.getElementById('creneauDescriptionInterprete').value = '';
        generateHours('creneauHeureDebutInterprete', 8 * 60, 18 * 60 + 55);
        generateHours('creneauHeureFinInterprete', 8 * 60 + 5, 19 * 60);
        clearErrors([
            document.getElementById('creneauJourInterprete'),
            document.getElementById('creneauHeureDebutInterprete'),
            document.getElementById('creneauHeureFinInterprete')
        ]);
    });

    /* Resets the beneficiary add creneau modal on close. */
    document.getElementById('modalAjoutCreneauBeneficiaire').addEventListener('hidden.bs.modal', function () {
        document.getElementById('creneauJourBeneficiaire').selectedIndex = 0;
        document.getElementById('creneauAcademicSkill').selectedIndex = 0;
        document.getElementById('creneauProfessionalSkill').selectedIndex = 0;
        document.getElementById('creneauInterprete').selectedIndex = 0;
        document.getElementById('creneauEtablissementBeneficiaire').selectedIndex = 0;
        document.getElementById('creneauLocalBeneficiaire').value = '';
        document.getElementById('creneauDescriptionBeneficiaire').value = '';
        document.getElementById('creneauAcademicSkillError').style.display = 'none';
        document.getElementById('creneauProfessionalSkillError').style.display = 'none';
        generateHours('creneauHeureDebutBeneficiaire', 8 * 60, 18 * 60 + 55);
        generateHours('creneauHeureFinBeneficiaire', 8 * 60 + 5, 19 * 60);
        clearErrors([
            document.getElementById('creneauJourBeneficiaire'),
            document.getElementById('creneauHeureDebutBeneficiaire'),
            document.getElementById('creneauHeureFinBeneficiaire')
        ]);
    });

    /* Resets the interpreter edit modal on close. */
    document.getElementById('modalEditCreneauInterprete').addEventListener('hidden.bs.modal', function () {
        document.getElementById('editCreneauInterpreteId').value = '';
        document.getElementById('editCreneauIndispo').checked = false;
        document.getElementById('editCreneauInterpreteFields').style.display = '';
        document.getElementById('editCreneauBeneficiaire').selectedIndex = 0;
        document.getElementById('editCreneauEtablissementInterprete').selectedIndex = 0;
        document.getElementById('editCreneauLocalInterprete').value = '';
        document.getElementById('editCreneauDescriptionInterprete').value = '';
        generateHours('editCreneauHeureDebutInterprete', 8 * 60, 18 * 60 + 55);
        generateHours('editCreneauHeureFinInterprete', 8 * 60 + 5, 19 * 60);
        clearErrors([
            document.getElementById('editCreneauJourInterprete'),
            document.getElementById('editCreneauHeureDebutInterprete'),
            document.getElementById('editCreneauHeureFinInterprete')
        ]);
    });

    /* Resets the beneficiary edit modal on close. */
    document.getElementById('modalEditCreneauBeneficiaire').addEventListener('hidden.bs.modal', function () {
        document.getElementById('editCreneauBeneficiaireId').value = '';
        document.getElementById('editCreneauId').value = '';
        document.getElementById('editCreneauAcademicSkill').selectedIndex = 0;
        document.getElementById('editCreneauProfessionalSkill').selectedIndex = 0;
        document.getElementById('editCreneauInterprete').selectedIndex = 0;
        document.getElementById('editCreneauEtablissementBeneficiaire').selectedIndex = 0;
        document.getElementById('editCreneauLocalBeneficiaire').value = '';
        document.getElementById('editCreneauDescriptionBeneficiaire').value = '';
        generateHours('editCreneauHeureDebutBeneficiaire', 8 * 60, 18 * 60 + 55);
        generateHours('editCreneauHeureFinBeneficiaire', 8 * 60 + 5, 19 * 60);
        clearErrors([
            document.getElementById('editCreneauJourBeneficiaire'),
            document.getElementById('editCreneauHeureDebutBeneficiaire'),
            document.getElementById('editCreneauHeureFinBeneficiaire')
        ]);
    });

    /* Validates and submits the add creneau form for interpreter.
    Sends an x-www-form-urlencoded POST to /coordinatrice/horaire-base/interprete/{id},
    matching the @RequestParam signature of createInterpreterSlot. */
    document.getElementById('btnSauvegarderCreneauInterprete').addEventListener('click', function () {
        const jour = document.getElementById('creneauJourInterprete');
        const heureDebut = document.getElementById('creneauHeureDebutInterprete');
        const heureFin = document.getElementById('creneauHeureFinInterprete');
        const indispo = document.getElementById('creneauIndispo').checked;
        let valid = true;

        clearErrors([jour, heureDebut, heureFin]);

        if (!jour.value) {
            displayError(jour, 'Le jour est obligatoire.');
            valid = false;
        }
        if (!heureDebut.value) {
            displayError(heureDebut, "L'heure de début est obligatoire.");
            valid = false;
        }
        if (!heureFin.value) {
            displayError(heureFin, "L'heure de fin est obligatoire.");
            valid = false;
        }

        if (!valid) return;

        const params = new URLSearchParams();
        params.append('dayNumber', jour.value);
        params.append('startTime', heureDebut.value);
        params.append('endTime', heureFin.value);
        params.append('isAbsence', indispo);

        if (!indispo) {
            const beneficiaire = document.getElementById('creneauBeneficiaire').value;
            const etablissement = document.getElementById('creneauEtablissementInterprete').value;
            const local = document.getElementById('creneauLocalInterprete').value.trim();
            const description = document.getElementById('creneauDescriptionInterprete').value.trim();

            if (beneficiaire) params.append('numBeneficiary', beneficiaire);
            if (etablissement) params.append('numEstablishment', etablissement);
            if (local) params.append('local', local);
            if (description) params.append('description', description);
        }

        fetch(`/coordinatrice/horaire-base/interprete/${currentInterpreteId}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    bootstrap.Modal.getInstance(document.getElementById('modalAjoutCreneauInterprete')).hide();
                    calendarInterprete.refetchEvents();
                } else {
                    console.error('Error creating interpreter creneau');
                }
            })
            .catch(err => console.error('Error saving interpreter creneau:', err));
    });

    /* Validates and submits the add creneau form for beneficiary. */
    document.getElementById('btnSauvegarderCreneauBeneficiaire').addEventListener('click', function () {
        const jour = document.getElementById('creneauJourBeneficiaire');
        const heureDebut = document.getElementById('creneauHeureDebutBeneficiaire');
        const heureFin = document.getElementById('creneauHeureFinBeneficiaire');
        const academicSkill = document.getElementById('creneauAcademicSkill');
        const professionalSkill = document.getElementById('creneauProfessionalSkill');
        const interprete = document.getElementById('creneauInterprete');
        const etablissement = document.getElementById('creneauEtablissementBeneficiaire');
        const local = document.getElementById('creneauLocalBeneficiaire');
        const academicError = document.getElementById('creneauAcademicSkillError');
        const professionalError = document.getElementById('creneauProfessionalSkillError');
        let valid = true;

        clearErrors([jour, heureDebut, heureFin, interprete, etablissement, local]);
        academicError.style.display = 'none';
        professionalError.style.display = 'none';

        if (!jour.value) {
            displayError(jour, 'Le jour est obligatoire.');
            valid = false;
        }
        if (!heureDebut.value) {
            displayError(heureDebut, "L'heure de début est obligatoire.");
            valid = false;
        }
        if (!heureFin.value) {
            displayError(heureFin, "L'heure de fin est obligatoire.");
            valid = false;
        }
        if (!academicSkill.value) {
            academicError.style.display = 'block';
            valid = false;
        }
        if (!professionalSkill.value) {
            professionalError.style.display = 'block';
            valid = false;
        }
        if (!interprete.value) {
            displayError(interprete, "L'interprète est obligatoire.");
            valid = false;
        }
        if (!etablissement.value) {
            displayError(etablissement, "L'établissement est obligatoire.");
            valid = false;
        }
        if (!local.value.trim()) {
            displayError(local, 'Le local est obligatoire.');
            valid = false;
        }

        if (!valid) return;

        const params = new URLSearchParams();
        params.append('dayNumber', jour.value);
        params.append('startTime', heureDebut.value);
        params.append('endTime', heureFin.value);
        params.append('numAcademicSkill', academicSkill.value);
        params.append('numProfessionalSkill', professionalSkill.value);
        params.append('numInterpreter', interprete.value);
        params.append('numEstablishment', etablissement.value);
        params.append('local', local.value.trim());

        const description = document.getElementById('creneauDescriptionBeneficiaire').value.trim();
        if (description) params.append('description', description);

        fetch(`/coordinatrice/horaire-base/beneficiaire/${currentBeneficiaireId}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    bootstrap.Modal.getInstance(document.getElementById('modalAjoutCreneauBeneficiaire')).hide();
                    calendarBeneficiaire.refetchEvents();
                } else {
                    console.error('Error creating beneficiary creneau');
                }
            })
            .catch(err => console.error('Error saving beneficiary creneau:', err));
    });

    /* Validates and submits the interpreter edit form. */
    document.getElementById('btnModifierCreneauInterprete').addEventListener('click', function () {
        const id = document.getElementById('editCreneauInterpreteId').value;
        const jour = document.getElementById('editCreneauJourInterprete');
        const heureDebut = document.getElementById('editCreneauHeureDebutInterprete');
        const heureFin = document.getElementById('editCreneauHeureFinInterprete');
        let valid = true;

        clearErrors([jour, heureDebut, heureFin]);

        if (!heureDebut.value) {
            displayError(heureDebut, "L'heure de début est obligatoire.");
            valid = false;
        }
        if (!heureFin.value) {
            displayError(heureFin, "L'heure de fin est obligatoire.");
            valid = false;
        }

        if (!valid) return;

        const params = new URLSearchParams();
        params.append('numTimeSlot', id);
        params.append('dayNumber', jour.value);
        params.append('startTime', heureDebut.value);
        params.append('endTime', heureFin.value);

        fetch('/coordinatrice/horaire-base/update', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    bootstrap.Modal.getInstance(document.getElementById('modalEditCreneauInterprete')).hide();
                    calendarInterprete.refetchEvents();
                } else {
                    console.error('Error updating interpreter creneau');
                }
            })
            .catch(err => console.error('Error updating interpreter creneau:', err));
    });

    /* Validates and submits the beneficiary edit form. */
    document.getElementById('btnModifierCreneauBeneficiaire').addEventListener('click', function () {
        const id = document.getElementById('editCreneauBeneficiaireId').value;
        const jour = document.getElementById('editCreneauJourBeneficiaire');
        const heureDebut = document.getElementById('editCreneauHeureDebutBeneficiaire');
        const heureFin = document.getElementById('editCreneauHeureFinBeneficiaire');
        let valid = true;

        clearErrors([jour, heureDebut, heureFin]);

        if (!heureDebut.value) {
            displayError(heureDebut, "L'heure de début est obligatoire.");
            valid = false;
        }
        if (!heureFin.value) {
            displayError(heureFin, "L'heure de fin est obligatoire.");
            valid = false;
        }

        if (!valid) return;

        const params = new URLSearchParams();
        params.append('numTimeSlot', id);
        params.append('dayNumber', jour.value);
        params.append('startTime', heureDebut.value);
        params.append('endTime', heureFin.value);

        fetch('/coordinatrice/horaire-base/update', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    bootstrap.Modal.getInstance(document.getElementById('modalEditCreneauBeneficiaire')).hide();
                    calendarBeneficiaire.refetchEvents();
                } else {
                    console.error('Error updating beneficiary creneau');
                }
            })
            .catch(err => console.error('Error updating beneficiary creneau:', err));
    });

    /* Opens the delete confirmation modal from the interpreter edit modal. */
    document.getElementById('btnSupprimerCreneauInterprete').addEventListener('click', function () {
        document.getElementById('editCreneauId').value = document.getElementById('editCreneauInterpreteId').value;
        bootstrap.Modal.getInstance(document.getElementById('modalEditCreneauInterprete')).hide();
        new bootstrap.Modal(document.getElementById('modalSuppression')).show();
    });

    /* Opens the delete confirmation modal from the beneficiary edit modal. */
    document.getElementById('btnSupprimerCreneauBeneficiaire').addEventListener('click', function () {
        document.getElementById('editCreneauId').value = document.getElementById('editCreneauBeneficiaireId').value;
        bootstrap.Modal.getInstance(document.getElementById('modalEditCreneauBeneficiaire')).hide();
        new bootstrap.Modal(document.getElementById('modalSuppression')).show();
    });

    /* Confirms and submits the delete request.
    Sends an x-www-form-urlencoded POST to /coordinatrice/horaire-base/delete,
    matching the @RequestParam signature of deleteSlot (numTimeSlot). */
    document.getElementById('btnConfirmerSuppression').addEventListener('click', function () {
        const id = document.getElementById('editCreneauId').value;

        const params = new URLSearchParams();
        params.append('numTimeSlot', id);

        fetch('/coordinatrice/horaire-base/delete', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    bootstrap.Modal.getInstance(document.getElementById('modalSuppression')).hide();
                    if (currentTab === 'interprete') calendarInterprete.refetchEvents();
                    else calendarBeneficiaire.refetchEvents();
                } else {
                    console.error('Error deleting creneau');
                }
            })
            .catch(err => console.error('Error deleting creneau:', err));
    });

    /* Initializes the interpreter FullCalendar instance. */
    calendarInterprete = new FullCalendar.Calendar(document.getElementById('calendarInterprete'), {
        locale: 'fr',
        initialView: 'timeGridWeek',
        headerToolbar: {left: '', center: '', right: ''},
        dayHeaderFormat: {weekday: 'long'},
        slotMinTime: '08:00:00',
        slotMaxTime: '19:00:00',
        allDaySlot: false,
        height: 'auto',

        /* Fetches base schedule events for the selected interpreter. */
        events: function (fetchInfo, successCallback, failureCallback) {
            if (currentInterpreteId === null) {
                successCallback([]);
                return;
            }
            fetch(`/coordinatrice/horaire-base/interprete/${currentInterpreteId}/events`)
                .then(r => r.json())
                .then(data => successCallback(data))
                .catch(err => {
                    console.error('Error loading interpreter events:', err);
                    failureCallback(err);
                });
        },

        /* Opens the interpreter add creneau modal when clicking on an empty slot,
        pre-filling the day and start time. */
        dateClick: function (info) {
            document.getElementById('creneauJourInterprete').value = info.date.getDay() === 0 ? 7 : info.date.getDay();

            if (info.dateStr.includes('T')) {
                const [h, m] = info.dateStr.split('T')[1].substring(0, 5).split(':').map(Number);
                const totalMinutes = h * 60 + (m < 30 ? 0 : 30);
                generateHours('creneauHeureDebutInterprete', totalMinutes, 18 * 60 + 55);
                generateHours('creneauHeureFinInterprete', totalMinutes + 30, 19 * 60);
                document.getElementById('creneauHeureDebutInterprete').value =
                    `${String(h).padStart(2, '0')}:${m < 30 ? '00' : '30'}`;
            }

            new bootstrap.Modal(document.getElementById('modalAjoutCreneauInterprete')).show();
        },

        /* Opens the interpreter edit modal when clicking on an existing event,
        pre-filling all fields with the event data. */
        eventClick: function (info) {
            const props = info.event.extendedProps;

            document.getElementById('editCreneauInterpreteId').value = props.numTimeSlot || '';
            document.getElementById('editCreneauJourInterprete').value = props.dayNumber;

            generateHours('editCreneauHeureDebutInterprete', 8 * 60, 18 * 60 + 55);
            generateHours('editCreneauHeureFinInterprete', 8 * 60 + 5, 19 * 60);
            document.getElementById('editCreneauHeureDebutInterprete').value = props.startTime;
            document.getElementById('editCreneauHeureFinInterprete').value = props.endTime;

            const indispo = props.isAbsence || false;
            document.getElementById('editCreneauIndispo').checked = indispo;
            document.getElementById('editCreneauInterpreteFields').style.display = indispo ? 'none' : '';

            if (!indispo) {
                if (props.locals && props.locals.length > 0) {
                    document.getElementById('editCreneauLocalInterprete').value = props.locals[0];
                } else {
                    document.getElementById('editCreneauLocalInterprete').value = '';
                }
                document.getElementById('editCreneauDescriptionInterprete').value = props.description || '';
            }

            new bootstrap.Modal(document.getElementById('modalEditCreneauInterprete')).show();
        },

        /* Renders the HTML content of each interpreter calendar event. */
        eventContent: function (arg) {
            const props = arg.event.extendedProps;
            const start = arg.event.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const end = arg.event.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';

            let html = `<div class="fw-bold">${arg.event.title}</div>`;
            html += `<div class="small">${start} – ${end}</div>`;
            if (props.isAbsence) html += `<div class="small fst-italic">Indisponibilité récurrente</div>`;
            if (props.beneficiary) html += `<div class="small">Bénéficiaire : ${props.beneficiary}</div>`;
            if (props.establishment) html += `<div class="small">${props.establishment}</div>`;
            if (props.locals && props.locals.length > 0) html += `<div class="small">${props.locals.join(', ')}</div>`;
            if (props.description) html += `<div class="small">Description : ${props.description}</div>`;

            return {html: `<div class="p-1">${html}</div>`};
        }
    });

    /* Initializes the beneficiary FullCalendar instance. */
    calendarBeneficiaire = new FullCalendar.Calendar(document.getElementById('calendarBeneficiaire'), {
        locale: 'fr',
        initialView: 'timeGridWeek',
        headerToolbar: {left: '', center: '', right: ''},
        dayHeaderFormat: {weekday: 'long'},
        slotMinTime: '08:00:00',
        slotMaxTime: '19:00:00',
        allDaySlot: false,
        height: 'auto',

        events: function (fetchInfo, successCallback, failureCallback) {
            if (currentBeneficiaireId === null) {
                successCallback([]);
                return;
            }
            fetch(`/coordinatrice/horaire-base/beneficiaire/${currentBeneficiaireId}/events`)
                .then(r => r.json())
                .then(data => successCallback(data))
                .catch(err => {
                    console.error('Error loading beneficiary events:', err);
                    failureCallback(err);
                });
        },

        /* Opens the beneficiary add creneau modal when clicking on an empty slot,
        pre-filling the day and start time. */
        dateClick: function (info) {
            document.getElementById('creneauJourBeneficiaire').value = info.date.getDay() === 0 ? 7 : info.date.getDay();

            if (info.dateStr.includes('T')) {
                const [h, m] = info.dateStr.split('T')[1].substring(0, 5).split(':').map(Number);
                const totalMinutes = h * 60 + (m < 30 ? 0 : 30);
                generateHours('creneauHeureDebutBeneficiaire', totalMinutes, 18 * 60 + 55);
                generateHours('creneauHeureFinBeneficiaire', totalMinutes + 30, 19 * 60);
                document.getElementById('creneauHeureDebutBeneficiaire').value =
                    `${String(h).padStart(2, '0')}:${m < 30 ? '00' : '30'}`;
            }

            new bootstrap.Modal(document.getElementById('modalAjoutCreneauBeneficiaire')).show();
        },

        /* Opens the beneficiary edit modal when clicking on an existing event,
        pre-filling all fields with the event data. */
        eventClick: function (info) {
            const props = info.event.extendedProps;

            document.getElementById('editCreneauBeneficiaireId').value = props.numTimeSlot || '';
            document.getElementById('editCreneauId').value = props.numTimeSlot || '';
            document.getElementById('editCreneauJourBeneficiaire').value = props.dayNumber;

            generateHours('editCreneauHeureDebutBeneficiaire', 8 * 60, 18 * 60 + 55);
            generateHours('editCreneauHeureFinBeneficiaire', 8 * 60 + 5, 19 * 60);
            document.getElementById('editCreneauHeureDebutBeneficiaire').value = props.startTime;
            document.getElementById('editCreneauHeureFinBeneficiaire').value = props.endTime;

            if (props.locals && props.locals.length > 0) {
                document.getElementById('editCreneauLocalBeneficiaire').value = props.locals[0];
            } else {
                document.getElementById('editCreneauLocalBeneficiaire').value = '';
            }
            document.getElementById('editCreneauDescriptionBeneficiaire').value = props.description || '';

            new bootstrap.Modal(document.getElementById('modalEditCreneauBeneficiaire')).show();
        },

        /* Renders the HTML content of each beneficiary calendar event. */
        eventContent: function (arg) {
            const props = arg.event.extendedProps;
            const start = arg.event.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const end = arg.event.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';

            let html = `<div class="fw-bold">${arg.event.title}</div>`;
            html += `<div class="small">${start} – ${end}</div>`;
            if (props.academicSkills) html += `<div class="small">Matière : ${props.academicSkills}</div>`;
            if (props.professionalSkills) html += `<div class="small">Compétence : ${props.professionalSkills}</div>`;
            if (props.establishment) html += `<div class="small">${props.establishment}</div>`;
            if (props.locals && props.locals.length > 0) html += `<div class="small">${props.locals.join(', ')}</div>`;
            if (props.description) html += `<div class="small">Description : ${props.description}</div>`;

            return {html: `<div class="p-1">${html}</div>`};
        }
    });

    calendarInterprete.render();
    calendarBeneficiaire.render();
});