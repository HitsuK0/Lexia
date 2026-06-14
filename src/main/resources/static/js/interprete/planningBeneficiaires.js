document.addEventListener('DOMContentLoaded', function () {

    let currentBeneficiaryNum = null;

    if (beneficiariesList && beneficiariesList.length > 0) {
        currentBeneficiaryNum = beneficiariesList[0].numBeneficiary;
    }

    /**
     * Fills the #rdvBeneficiary select with beneficiariesList entries.
     * Resets the select before inserting options. Does nothing if the list is empty.
     */
    function fillBeneficiarySelect() {
        const select = document.getElementById('rdvBeneficiary');
        select.innerHTML = '<option value="" disabled selected>Choisissez un bénéficiaire</option>';
        if (beneficiariesList && beneficiariesList.length > 0) {
            beneficiariesList.forEach(b => {
                const option = document.createElement('option');
                option.value = b.numBeneficiary;
                option.textContent = b.lastName + ' ' + b.firstName;
                select.appendChild(option);
            });
        }
    }

    /**
     * Populates a <select> with time options in 5-minute increments.
     * Resets the select before inserting options.
     * @param {string} selectId - Target select element id
     * @param {number} start - Start time in minutes from midnight (e.g. 480 = 08:00)
     * @param {number} end - End time in minutes from midnight (e.g. 1140 = 19:00)
     */
    function generateTimeOptions(selectId, start, end) {
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

    /**
     * Updates the currently selected beneficiary and refreshes calendar events.
     * @param {HTMLElement} element - The clicked <a> element, must have data-num and data-nom
     */
    function changeBeneficiary(element) {
        currentBeneficiaryNum = parseInt(element.dataset.num);
        document.getElementById('beneficiaireSelectionne').textContent = element.dataset.nom;
        calendar.refetchEvents();
    }

    /**
     * Displays an inline validation error on a form input.
     * @param {HTMLElement} input - The invalid input element
     * @param {string} message - The error message to display
     */
    function displayError(input, message) {
        input.classList.add('is-invalid');
        const div = document.createElement('div');
        div.classList.add('invalid-feedback');
        div.textContent = message;
        input.insertAdjacentElement('afterend', div);
    }

    fillBeneficiarySelect();
    generateTimeOptions('rdvHourStart', 8 * 60, 18 * 60 + 55);
    generateTimeOptions('rdvHourEnd', 8 * 60 + 5, 19 * 60);

    const calendar = new FullCalendar.Calendar(document.getElementById('calendar'), {
        locale: 'fr',
        initialView: 'timeGridWeek',
        headerToolbar: {
            left: 'prev',
            center: 'title',
            right: 'next'
        },
        slotMinTime: '08:00:00',
        slotMaxTime: '19:00:00',
        allDaySlot: false,

        /**
         * Dynamically loads appointments for the current beneficiary.
         * Fetches GET /interprete/planning/beneficiaires/events with start, end and num params.
         * Returns an empty list if no beneficiary is selected.
         */
        events: function (fetchInfo, successCallback, failureCallback) {
            if (currentBeneficiaryNum === null) {
                successCallback([]);
                return;
            }

            const start = fetchInfo.startStr;
            const end = fetchInfo.endStr;

            fetch(`/interprete/planning/beneficiaires/events?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}&num=${currentBeneficiaryNum}`)
                .then(response => {
                    if (!response.ok) throw new Error('Network error');
                    return response.json();
                })
                .then(data => successCallback(data))
                .catch(err => {
                    console.error('Error loading beneficiary events:', err);
                    failureCallback(err);
                });
        },

        /**
         * Opens the RDV modal when clicking on a calendar slot.
         * Ignores past dates. Pre-fills date and start time based on the clicked slot.
         */
        dateClick: function (info) {
            const today = new Date().toISOString().split('T')[0];
            const clickedDate = info.dateStr.split('T')[0];

            if (clickedDate < today) return;

            document.getElementById('rdvDate').value = clickedDate;
            document.getElementById('rdvDate').min = today;

            if (info.dateStr.includes('T')) {
                const hourStr = info.dateStr.split('T')[1].substring(0, 5);
                const [h, m] = hourStr.split(':').map(Number);
                const roundedMinutes = m < 5 ? 0 : m;
                const totalMinutes = h * 60 + roundedMinutes;
                generateTimeOptions('rdvHourEnd', totalMinutes + 5, 19 * 60);
                const roundedHour = `${String(h).padStart(2, '0')}:${String(roundedMinutes).padStart(2, '0')}`;
                document.getElementById('rdvHourStart').value = roundedHour;
            }

            const modal = new bootstrap.Modal(document.getElementById('modalRDV'));
            modal.show();
        },

        height: 'calc(100vh - 170px)',

        /**
         * Renders the HTML content of each calendar event.
         * Displays academic skills, professional skills, beneficiary name, locals, and a pending icon.
         */
        eventContent: function (arg) {
            const ep = arg.event.extendedProps;
            const lines = [];

            lines.push(`<div class="fw-bold">${arg.event.title || ''}</div>`);

            if (ep.professionalSkills) {
                lines.push(`<div class="small">${ep.professionalSkills}</div>`);
            }
            if (ep.beneficiary) {
                lines.push(`<div class="small">${ep.beneficiary}</div>`);
            }
            if (ep.locals && ep.locals.length > 0) {
                const localsStr = Array.isArray(ep.locals) ? ep.locals.join(', ') : ep.locals;
                lines.push(`<div class="small">${localsStr}</div>`);
            }

            const icon = (ep.status === 'en attente')
                ? '<i class="bi bi-hourglass-split" style="float:right; font-size:0.85rem;"></i>'
                : '';

            return { html: `<div class="p-1">${icon}${lines.join('')}</div>` };
        },

        /* Opens the event detail modal on click, displaying the appointment's
        schedule, beneficiary, establishment, locals, description and professional skills. */
        eventClick: function (info) {
            const props = info.event.extendedProps;
            const start = info.event.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const end = info.event.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';

            document.getElementById('modalEventTitle').textContent = info.event.title;

            let body = `<p><strong>Horaire :</strong> ${start} – ${end}</p>`;

            if (props.beneficiary)       body += `<p><strong>Bénéficiaire :</strong> ${props.beneficiary}</p>`;
            if (props.establishment)     body += `<p><strong>Établissement :</strong> ${props.establishment}</p>`;
            if (props.locals?.length > 0) body += `<p><strong>Local :</strong> ${Array.isArray(props.locals) ? props.locals.join(', ') : props.locals}</p>`;
            if (props.description)       body += `<p><strong>Description :</strong> ${props.description}</p>`;
            if (props.professionalSkills) body += `<p><strong>Compétence métier :</strong> ${props.professionalSkills}</p>`;

            const statusLabels = {
                'en attente': '<span class="badge" style="background-color:#f0ad4e;">En attente</span>',
                'accepte':    '<span class="badge" style="background-color:#81c784;">Accepté</span>',
                'refuse':     '<span class="badge" style="background-color:#f28b82;">Refusé</span>'
            };
            if (props.status) body += `<p><strong>Statut :</strong> ${statusLabels[props.status] ?? props.status}</p>`;

            document.getElementById('modalEventBody').innerHTML = body;
            new bootstrap.Modal(document.getElementById('modalEvent')).show();
        }
    });

    calendar.render();

    /** Binds click listeners on each beneficiary dropdown item, delegates to changeBeneficiary(). */
    document.querySelectorAll('.dropdown-beneficiary').forEach(el => {
        el.addEventListener('click', function (e) {
            e.preventDefault();
            changeBeneficiary(this);
        });
    });

    /** On modal open, pre-selects the current beneficiary and sets the min date to today. */
    document.getElementById('modalRDV').addEventListener('show.bs.modal', function () {
        if (currentBeneficiaryNum !== null) {
            document.getElementById('rdvBeneficiary').value = currentBeneficiaryNum;
        }
        document.getElementById('rdvDate').min = new Date().toISOString().split('T')[0];
    });

    /** Regenerates end time options to always be at least 5 minutes after the selected start time. */
    document.getElementById('rdvHourStart').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateTimeOptions('rdvHourEnd', h * 60 + m + 5, 19 * 60);
    });

    /** Validates the RDV form and sends a POST to /interprete/planning/beneficiaires/rdv. Closes the modal on success. */
    document.getElementById('btnSendRDV').addEventListener('click', function () {
        let valid = true;

        const rdvBeneficiary      = document.getElementById('rdvBeneficiary');
        const rdvDate             = document.getElementById('rdvDate');
        const rdvHourStart        = document.getElementById('rdvHourStart');
        const rdvHourEnd          = document.getElementById('rdvHourEnd');
        const rdvEstablishment    = document.getElementById('rdvEstablishment');
        const rdvLocal            = document.getElementById('rdvLocal');
        const rdvAcademicSkill    = document.getElementById('rdvAcademicSkill');
        const rdvProfessionalSkill = document.getElementById('rdvProfessionalSkill');
        const skillsError         = document.getElementById('rdvAcademicSkillsError');
        const compsError          = document.getElementById('rdvCompsError');

        [rdvBeneficiary, rdvDate, rdvHourStart, rdvHourEnd, rdvEstablishment, rdvLocal, rdvAcademicSkill, rdvProfessionalSkill].forEach(el => {
            el.classList.remove('is-invalid');
            const feedback = el.nextElementSibling;
            if (feedback && feedback.classList.contains('invalid-feedback')) feedback.remove();
        });
        skillsError.style.display = 'none';
        compsError.style.display = 'none';

        if (!rdvBeneficiary.value)       { displayError(rdvBeneficiary,   'Veuillez sélectionner un bénéficiaire.'); valid = false; }
        if (!rdvDate.value)              { displayError(rdvDate,          'La date est obligatoire.'); valid = false; }
        if (!rdvHourStart.value)         { displayError(rdvHourStart,     "L'heure de début est obligatoire."); valid = false; }
        if (!rdvHourEnd.value)           { displayError(rdvHourEnd,       "L'heure de fin est obligatoire."); valid = false; }
        if (!rdvAcademicSkill.value)     { skillsError.style.display = 'block'; valid = false; }
        if (!rdvProfessionalSkill.value) { compsError.style.display = 'block'; valid = false; }
        if (!rdvEstablishment.value)     { displayError(rdvEstablishment, "L'établissement est obligatoire."); valid = false; }
        if (!rdvLocal.value.trim())      { displayError(rdvLocal,         'Le local est obligatoire.'); valid = false; }

        if (!valid) return;

        const payload = {
            numBeneficiary:              parseInt(rdvBeneficiary.value),
            startDate:                   rdvDate.value,
            endDate:                     rdvDate.value,
            startTime:                   rdvHourStart.value,
            endTime:                     rdvHourEnd.value,
            numAcademicSkillsNeeded:     [parseInt(rdvAcademicSkill.value)],
            numProfessionalSkillsNeeded: [parseInt(rdvProfessionalSkill.value)],
            numEstablishment:            parseInt(rdvEstablishment.value),
            appointmentLocals:           [rdvLocal.value.trim()],
            description:                 document.getElementById('rdvDescription').value.trim()
        };

        fetch('/interprete/planning/beneficiaires/rdv', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    bootstrap.Modal.getInstance(document.getElementById('modalRDV')).hide();
                    calendar.refetchEvents();
                } else {
                    console.error('Erreur lors de la création du RDV');
                }
            })
            .catch(err => console.error('Error sending RDV request:', err));
    });

    /** Resets all modal fields (inputs, checkboxes, selects, time slots) on modal close. */
    document.getElementById('modalRDV').addEventListener('hidden.bs.modal', function () {
        document.getElementById('rdvBeneficiary').value = '';
        document.getElementById('rdvDate').value = '';
        document.getElementById('rdvDate').min = new Date().toISOString().split('T')[0];
        document.getElementById('rdvEstablishment').selectedIndex = 0;
        document.getElementById('rdvAcademicSkill').selectedIndex = 0;
        document.getElementById('rdvProfessionalSkill').selectedIndex = 0;
        document.getElementById('rdvLocal').value = '';
        document.getElementById('rdvDescription').value = '';
        document.getElementById('rdvAcademicSkillsError').style.display = 'none';
        document.getElementById('rdvCompsError').style.display = 'none';

        generateTimeOptions('rdvHourStart', 8 * 60, 18 * 60 + 55);
        generateTimeOptions('rdvHourEnd', 8 * 60 + 5, 19 * 60);

        ['rdvBeneficiary', 'rdvDate', 'rdvHourStart', 'rdvHourEnd', 'rdvEstablishment', 'rdvLocal', 'rdvAcademicSkill', 'rdvProfessionalSkill'].forEach(id => {
            const el = document.getElementById(id);
            el.classList.remove('is-invalid');
            const fb = el.nextElementSibling;
            if (fb && fb.classList.contains('invalid-feedback')) fb.remove();
        });
    });
});