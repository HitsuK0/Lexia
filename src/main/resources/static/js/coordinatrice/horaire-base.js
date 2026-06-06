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

/* Fetches interpreters from the server and fills the dropdown. */
function loadInterpretes() {
    fetch('/coordinatrice/horaire-base/interpretes')
        .then(r => r.json())
        .then(data => {
            const liste = document.getElementById('listeInterpretes');
            liste.innerHTML = '';
            if (data.length === 0) {
                liste.innerHTML = '<li><span class="dropdown-item text-muted">Aucun interprète</span></li>';
                return;
            }
            data.forEach(i => {
                const li = document.createElement('li');
                li.innerHTML = `<a class="dropdown-item dropdown-interprete" href="#"
                    data-id="${i.numInterpreter}"
                    data-nom="${i.lastName + ' ' + i.firstName}">
                    ${i.lastName} ${i.firstName}
                </a>`;
                liste.appendChild(li);
            });
            const first = liste.querySelector('.dropdown-interprete');
            if (first) selectInterprete(first);
        })
        .catch(err => console.error('Error loading interpreters:', err));
}

/* Fetches beneficiaries from the server and fills the dropdown. */
function loadBeneficiaires() {
    fetch('/coordinatrice/horaire-base/beneficiaires')
        .then(r => r.json())
        .then(data => {
            const liste = document.getElementById('listeBeneficiaires');
            liste.innerHTML = '';
            if (data.length === 0) {
                liste.innerHTML = '<li><span class="dropdown-item text-muted">Aucun bénéficiaire</span></li>';
                return;
            }
            data.forEach(b => {
                const li = document.createElement('li');
                li.innerHTML = `<a class="dropdown-item dropdown-beneficiaire" href="#"
                    data-id="${b.numBeneficiary}"
                    data-nom="${b.lastName + ' ' + b.firstName}">
                    ${b.lastName} ${b.firstName}
                </a>`;
                liste.appendChild(li);
            });
            const first = liste.querySelector('.dropdown-beneficiaire');
            if (first) selectBeneficiaire(first);
        })
        .catch(err => console.error('Error loading beneficiaries:', err));
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

document.addEventListener('DOMContentLoaded', function () {

    generateHours('creneauHeureDebutInterprete', 8 * 60, 18 * 60 + 55);
    generateHours('creneauHeureFinInterprete', 8 * 60 + 5, 19 * 60);
    generateHours('creneauHeureDebutBeneficiaire', 8 * 60, 18 * 60 + 55);
    generateHours('creneauHeureFinBeneficiaire', 8 * 60 + 5, 19 * 60);
    generateHours('editCreneauHeureDebut', 8 * 60, 18 * 60 + 55);
    generateHours('editCreneauHeureFin', 8 * 60 + 5, 19 * 60);

    // TEMPORAIRE
    currentInterpreteId = window.CURRENT_INTERPRETE_ID;
    currentBeneficiaireId = window.CURRENT_BENEFICIAIRE_ID;

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

    /* Regenerates end hour options to always be after the selected start hour - interpreter modal. */
    document.getElementById('creneauHeureDebutInterprete').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('creneauHeureFinInterprete', h * 60 + m + 5, 19 * 60);
    });

    /* Regenerates end hour options to always be after the selected start hour - beneficiary modal. */
    document.getElementById('creneauHeureDebutBeneficiaire').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('creneauHeureFinBeneficiaire', h * 60 + m + 5, 19 * 60);
    });

    /* Regenerates end hour options to always be after the selected start hour - edit modal. */
    document.getElementById('editCreneauHeureDebut').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('editCreneauHeureFin', h * 60 + m + 5, 19 * 60);
    });

    /* Hides or shows appointment fields based on the unavailability checkbox. */
    document.getElementById('creneauIndispo').addEventListener('change', function () {
        document.getElementById('creneauInterpreteFields').style.display = this.checked ? 'none' : '';
    });

    /* Resets the interpreter add creneau modal on close. */
    document.getElementById('modalAjoutCreneauInterprete').addEventListener('hidden.bs.modal', function () {
        document.getElementById('creneauJourInterprete').selectedIndex = 0;
        document.getElementById('creneauIndispo').checked = false;
        document.getElementById('creneauInterpreteFields').style.display = '';
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

    /* Resets the edit creneau modal on close. */
    document.getElementById('modalEditCreneau').addEventListener('hidden.bs.modal', function () {
        document.getElementById('editCreneauId').value = '';
        generateHours('editCreneauHeureDebut', 8 * 60, 18 * 60 + 55);
        generateHours('editCreneauHeureFin', 8 * 60 + 5, 19 * 60);
        clearErrors([
            document.getElementById('editCreneauJour'),
            document.getElementById('editCreneauHeureDebut'),
            document.getElementById('editCreneauHeureFin')
        ]);
    });

    /* Validates and submits the add creneau form for interpreter. */
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

        const payload = {
            dayNumber: parseInt(jour.value),
            startTime: heureDebut.value,
            endTime: heureFin.value,
            isAbsence: indispo,
            numBeneficiary: indispo ? null : parseInt(document.getElementById('creneauBeneficiaire').value) || null,
            numEstablishment: indispo ? null : parseInt(document.getElementById('creneauEtablissementInterprete').value) || null,
            local: indispo ? null : document.getElementById('creneauLocalInterprete').value.trim() || null,
            description: indispo ? null : document.getElementById('creneauDescriptionInterprete').value.trim() || null
        };

        fetch(`/coordinatrice/horaire-base/interprete/${currentInterpreteId}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
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

        const payload = {
            dayNumber: parseInt(jour.value),
            startTime: heureDebut.value,
            endTime: heureFin.value,
            numAcademicSkill: parseInt(academicSkill.value),
            numProfessionalSkill: parseInt(professionalSkill.value),
            numInterpreter: parseInt(interprete.value),
            numEstablishment: parseInt(etablissement.value),
            local: local.value.trim(),
            description: document.getElementById('creneauDescriptionBeneficiaire').value.trim() || null
        };

        fetch(`/coordinatrice/horaire-base/beneficiaire/${currentBeneficiaireId}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
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

    /* Validates and submits the edit creneau form. */
    document.getElementById('btnModifierCreneau').addEventListener('click', function () {
        const id = document.getElementById('editCreneauId').value;
        const jour = document.getElementById('editCreneauJour');
        const heureDebut = document.getElementById('editCreneauHeureDebut');
        const heureFin = document.getElementById('editCreneauHeureFin');
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

        fetch(`/coordinatrice/horaire-base/${id}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                dayNumber: parseInt(jour.value),
                startTime: heureDebut.value,
                endTime: heureFin.value
            })
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    bootstrap.Modal.getInstance(document.getElementById('modalEditCreneau')).hide();
                    if (currentTab === 'interprete') calendarInterprete.refetchEvents();
                    else calendarBeneficiaire.refetchEvents();
                } else {
                    console.error('Error updating creneau');
                }
            })
            .catch(err => console.error('Error updating creneau:', err));
    });

    /* Opens the delete confirmation modal from the edit modal. */
    document.getElementById('btnSupprimerCreneau').addEventListener('click', function () {
        bootstrap.Modal.getInstance(document.getElementById('modalEditCreneau')).hide();
        new bootstrap.Modal(document.getElementById('modalSuppression')).show();
    });

    /* Confirms and submits the delete request. */
    document.getElementById('btnConfirmerSuppression').addEventListener('click', function () {
        const id = document.getElementById('editCreneauId').value;

        fetch(`/coordinatrice/horaire-base/${id}`, {method: 'DELETE'})
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
        headerToolbar: {
            left: '',
            center: '',
            right: ''
        },
        dayHeaderFormat: {weekday: 'long'},
        slotMinTime: '08:00:00',
        slotMaxTime: '19:00:00',
        allDaySlot: false,
        height: 'auto',

        /* Fetches base schedule events for the selected interpreter. */
        events: function (fetchInfo, successCallback, failureCallback) {
            successCallback(window.HARDCODED_EVENTS_INTERPRETE || []);
        },

        /* Opens the add creneau modal when clicking on an empty slot,
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

        /* Opens the edit modal when clicking on an existing event. */
        eventClick: function (info) {
            const props = info.event.extendedProps;
            document.getElementById('editCreneauId').value = props.id;
            document.getElementById('editCreneauJour').value = props.dayNumber;

            generateHours('editCreneauHeureDebut', 8 * 60, 18 * 60 + 55);
            generateHours('editCreneauHeureFin', 8 * 60 + 5, 19 * 60);
            document.getElementById('editCreneauHeureDebut').value = props.startTime;
            document.getElementById('editCreneauHeureFin').value = props.endTime;

            new bootstrap.Modal(document.getElementById('modalEditCreneau')).show();
        },

        /* Renders the HTML content of each calendar event. */
        eventContent: function (arg) {
            const props = arg.event.extendedProps;
            const start = arg.event.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const end   = arg.event.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';

            let html = `<div class="fw-bold">${arg.event.title}</div>`;
            html += `<div class="small">${start} – ${end}</div>`;

            if (props.beneficiary)        html += `<div class="small">Bénéficiaire : ${props.beneficiary}</div>`;
            if (props.establishment)      html += `<div class="small">${props.establishment}</div>`;
            if (props.local)              html += `<div class="small">${props.local}</div>`;
            if (props.description)        html += `<div class="small">Description : ${props.description}</div>`;
            if (props.academicSkill)      html += `<div class="small">Matière : ${props.academicSkill}</div>`;
            if (props.professionalSkill)  html += `<div class="small">Compétence : ${props.professionalSkill}</div>`;
            if (props.interpreter)        html += `<div class="small">Interprète : ${props.interpreter}</div>`;
            if (props.isAbsence)          html += `<div class="small fst-italic">Indisponibilité récurrente</div>`;

            return { html: `<div class="p-1">${html}</div>` };
        }
    });

    /* Initializes the beneficiary FullCalendar instance. */
    calendarBeneficiaire = new FullCalendar.Calendar(document.getElementById('calendarBeneficiaire'), {
        locale: 'fr',
        initialView: 'timeGridWeek',
        headerToolbar: {
            left: '',
            center: '',
            right: ''
        },
        dayHeaderFormat: {weekday: 'long'},
        slotMinTime: '08:00:00',
        slotMaxTime: '19:00:00',
        allDaySlot: false,
        height: 'auto',

        /* Fetches base schedule events for the selected beneficiary. */
        events: function (fetchInfo, successCallback, failureCallback) {
            successCallback(window.HARDCODED_EVENTS_BENEFICIAIRE || []);
        },

        /* Opens the add creneau modal when clicking on an empty slot,
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

        /* Opens the edit modal when clicking on an existing event. */
        eventClick: function (info) {
            const props = info.event.extendedProps;
            document.getElementById('editCreneauId').value = props.id;
            document.getElementById('editCreneauJour').value = props.dayNumber;

            generateHours('editCreneauHeureDebut', 8 * 60, 18 * 60 + 55);
            generateHours('editCreneauHeureFin', 8 * 60 + 5, 19 * 60);
            document.getElementById('editCreneauHeureDebut').value = props.startTime;
            document.getElementById('editCreneauHeureFin').value = props.endTime;

            new bootstrap.Modal(document.getElementById('modalEditCreneau')).show();
        },

        /* Renders the HTML content of each calendar event. */
        eventContent: function (arg) {
            const props = arg.event.extendedProps;
            const start = arg.event.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const end   = arg.event.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';

            let html = `<div class="fw-bold">${arg.event.title}</div>`;
            html += `<div class="small">${start} – ${end}</div>`;

            if (props.beneficiary)        html += `<div class="small">Bénéficiaire : ${props.beneficiary}</div>`;
            if (props.establishment)      html += `<div class="small">${props.establishment}</div>`;
            if (props.local)              html += `<div class="small">${props.local}</div>`;
            if (props.description)        html += `<div class="small">Description : ${props.description}</div>`;
            if (props.academicSkill)      html += `<div class="small">Matière : ${props.academicSkill}</div>`;
            if (props.professionalSkill)  html += `<div class="small">Compétence : ${props.professionalSkill}</div>`;
            if (props.interpreter)        html += `<div class="small">Interprète : ${props.interpreter}</div>`;
            if (props.isAbsence)          html += `<div class="small fst-italic">Indisponibilité récurrente</div>`;

            return { html: `<div class="p-1">${html}</div>` };
        }
    });

    calendarInterprete.render();
    calendarBeneficiaire.render();
});