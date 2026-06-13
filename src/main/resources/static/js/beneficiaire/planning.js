document.addEventListener('DOMContentLoaded', function () {

    /* Initializes the FullCalendar instance with French locale, week view,
    and custom toolbar with previous/next navigation only. */
    const calendar = new FullCalendar.Calendar(document.getElementById('calendar'), {
        locale: 'fr',
        initialView: window.innerWidth < 768
            ? 'timeGridDay'
            : window.innerWidth < 992
                ? 'timeGridThreeDays'
                : 'timeGridWeek',
        headerToolbar: {
            left: 'prev',
            center: 'title',
            right: 'next'
        },
        slotMinTime: '08:00:00',
        slotMaxTime: '19:00:00',
        allDaySlot: false,
        expandRows: false,
        views: {
            timeGridThreeDays: {
                type: 'timeGrid',
                duration: { days: 3 }
            }
        },

        /* Opens the RDV request modal when clicking on an empty time slot,
        pre-filling the start and end date fields and rounding the clicked hour. */
        dateClick: function (info) {
            const today = new Date().toISOString().split('T')[0];
            const clickedDate = info.dateStr.split('T')[0];

            if (clickedDate < today) return;

            document.getElementById('dateStart').value = clickedDate;
            document.getElementById('dateEnd').value = clickedDate;
            document.getElementById('dateStart').min = today;
            document.getElementById('dateEnd').min = clickedDate;

            if (info.dateStr.includes('T')) {
                const heureStr = info.dateStr.split('T')[1].substring(0, 5);
                const [h, m] = heureStr.split(':').map(Number);
                const minutesArrondies = m < 30 ? 0 : 30;
                const totalMinutes = h * 60 + minutesArrondies;
                const heureArrondie = `${String(h).padStart(2, '0')}:${minutesArrondies === 0 ? '00' : '30'}`;

                generateHours('endHour', totalMinutes + 30, 19 * 60);
                document.getElementById('startHour').value = heureArrondie;
            }

            const modal = new bootstrap.Modal(document.getElementById('modalRDV'));
            modal.show();
        },

        height: 'auto',

        /* Fetches the beneficiary's planning events from the server
        for the currently displayed date range. */
        events: function (info, successCallback, failureCallback) {
            fetch(`/beneficiaire/planning/events?start=${info.startStr}&end=${info.endStr}`)
                .then(response => response.json())
                .then(data => successCallback(data))
                .catch(() => failureCallback());
        },

        /* Opens the event detail modal on click, displaying the appointment's
        schedule, interpreter, establishment, local, description and professional skills. */
        eventClick: function (info) {
            const props = info.event.extendedProps;
            const start = info.event.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const end = info.event.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';

            document.getElementById('modalEventTitle').textContent = info.event.title;

            let body = `<p><strong>Horaire :</strong> ${start} – ${end}</p>`;

            if (props.type === 'appointment') {
                if (props.establishment) body += `<p><strong>Établissement :</strong> ${props.establishment}</p>`;
                if (props.locals?.length > 0) body += `<p><strong>Local :</strong> ${props.locals.join(', ')}</p>`;
                if (props.description) body += `<p><strong>Description :</strong> ${props.description}</p>`;
                if (props.professionalSkills) body += `<p><strong>Compétence métier :</strong> ${props.professionalSkills}</p>`;
            }

            document.getElementById('modalEventBody').innerHTML = body;
            new bootstrap.Modal(document.getElementById('modalEvent')).show();
        },

        /* Sets the event display content with an icon based on the appointment status,
        and shows appointment details below the title. */
        eventContent: function (arg) {
            const props = arg.event.extendedProps;

            const isRefuse  = props.status === 'refuse';
            const isAttente = props.status === 'en attente';
            const isAccepte = props.status === 'accepte';

            const icone = isRefuse
                ? '<i class="bi bi-x-circle" style="float:right; font-size:0.85rem;"></i>'
                : isAttente
                    ? '<i class="bi bi-clock" style="float:right; font-size:0.85rem;"></i>'
                    : isAccepte
                        ? '<i class="bi bi-check-circle" style="float:right; font-size:0.85rem;"></i>'
                        : '';

            let html = `<div class="fw-bold">${arg.event.title}</div>`;

            if (props.type === 'appointment') {
                if (props.professionalSkills) html += `<div class="small">${props.professionalSkills}</div>`;
                if (props.establishment) html += `<div class="small">${props.establishment}</div>`;
                if (props.locals && props.locals.length > 0) html += `<div class="small">${props.locals.join(', ')}</div>`;
                if (props.description) html += `<div class="small">📝 ${props.description}</div>`;
            }

            return {html: `<div class="p-1">${icone}${html}</div>`};
        }
    });

    calendar.render();

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('dateStart').min = today;
    document.getElementById('dateEnd').min = today;

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

    generateHours('startHour', 8 * 60, 18 * 60 + 55);
    generateHours('endHour', 8 * 60 + 5, 19 * 60);

    document.getElementById('dateStart').addEventListener('change', function () {
        document.getElementById('dateEnd').min = this.value;
        if (document.getElementById('dateEnd').value < this.value) {
            document.getElementById('dateEnd').value = this.value;
        }
    });

    /* Regenerates the end hour options to always start after the selected start hour. */
    document.getElementById('startHour').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('endHour', h * 60 + m + 5, 19 * 60);
    });

    /* Disables or re-enables the time range selects when the full day checkbox is toggled. */
    document.getElementById('fullDay').addEventListener('change', function () {
        document.getElementById('startHour').disabled = this.checked;
        document.getElementById('endHour').disabled = this.checked;
    });

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

    /* Validates the RDV form before submission and sends it to the server. */
    document.getElementById('btnSend').addEventListener('click', function () {
        let valid = true;

        const dateStart        = document.getElementById('dateStart');
        const dateEnd          = document.getElementById('dateEnd');
        const startHour        = document.getElementById('startHour');
        const endHour          = document.getElementById('endHour');
        const fullDay          = document.getElementById('fullDay').checked;
        const rdvEstablishment = document.getElementById('rdvEstablishment');
        const rdvLocal         = document.getElementById('rdvLocal');
        const rdvAcademicSkill = document.getElementById('rdvAcademicSkill');
        const rdvProfessionalSkill = document.getElementById('rdvProfessionalSkill');
        const skillsError      = document.getElementById('rdvAcademicSkillsError');
        const compsError       = document.getElementById('rdvCompsError');

        [dateStart, dateEnd, startHour, endHour, rdvEstablishment, rdvLocal, rdvAcademicSkill, rdvProfessionalSkill].forEach(el => {
            el.classList.remove('is-invalid');
            el.style.borderColor = '';
            const feedback = el.nextElementSibling;
            if (feedback && feedback.classList.contains('invalid-feedback')) feedback.remove();
        });
        skillsError.style.display = 'none';
        compsError.style.display = 'none';

        if (!dateStart.value)        displayError(dateStart,        'La date de début est obligatoire.');
        if (!dateEnd.value)          displayError(dateEnd,          'La date de fin est obligatoire.');
        if (dateStart.value && dateEnd.value && dateEnd.value < dateStart.value)
            displayError(dateEnd, 'La date de fin doit être après la date de début.');
        if (!rdvAcademicSkill.value)    { skillsError.style.display = 'block'; valid = false; }
        if (!rdvProfessionalSkill.value) { compsError.style.display = 'block'; valid = false; }
        if (!rdvEstablishment.value) displayError(rdvEstablishment, "L'établissement est obligatoire.");
        if (!rdvLocal.value.trim())  displayError(rdvLocal,         'Le local est obligatoire.');
        if (!fullDay) {
            if (!startHour.value) displayError(startHour, "L'heure de début est obligatoire.");
            if (!endHour.value)   displayError(endHour,   "L'heure de fin est obligatoire.");
        }

        if (!valid) return;
        if (document.querySelector('.is-invalid')) return;

        const numAcademicSkillsNeeded = [Number(rdvAcademicSkill.value)];
        const numProfessionalSkillsNeeded = [Number(rdvProfessionalSkill.value)];

        let startTimeValue, endTimeValue;
        if (fullDay) {
            startTimeValue = '00:00';
            endTimeValue = '23:59';
        } else {
            startTimeValue = startHour.value;
            endTimeValue = endHour.value;
        }

        const body = {
            numBeneficiary: Number(document.body.dataset.numBeneficiary),
            appointmentLocals: [rdvLocal.value.trim()],
            startDate: dateStart.value,
            endDate: dateEnd.value,
            startTime: startTimeValue,
            endTime: endTimeValue,
            numEstablishment: Number(rdvEstablishment.value),
            numAcademicSkillsNeeded: numAcademicSkillsNeeded,
            numProfessionalSkillsNeeded: numProfessionalSkillsNeeded
        };

        fetch('/beneficiaire/demandes/rdv', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    calendar.refetchEvents();
                    bootstrap.Modal.getInstance(document.getElementById('modalRDV')).hide();
                } else {
                    alert('Une erreur est survenue lors de la création de la demande.');
                }
            })
            .catch(err => console.error('Error creating appointment:', err));
    });

    /* Resets all modal RDV fields when it is closed. */
    document.getElementById('modalRDV').addEventListener('hidden.bs.modal', function () {
        document.getElementById('dateStart').value = '';
        document.getElementById('dateEnd').value = '';
        document.getElementById('dateStart').min = new Date().toISOString().split('T')[0];
        document.getElementById('dateEnd').min = new Date().toISOString().split('T')[0];
        document.getElementById('rdvLocal').value = '';
        document.getElementById('rdvDescription').value = '';
        document.getElementById('rdvAcademicSkillsError').style.display = 'none';
        document.getElementById('rdvCompsError').style.display = 'none';
        document.getElementById('fullDay').checked = false;
        document.getElementById('startHour').disabled = false;
        document.getElementById('endHour').disabled = false;
        document.getElementById('rdvEstablishment').selectedIndex = 0;
        document.getElementById('rdvAcademicSkill').selectedIndex = 0;
        document.getElementById('rdvProfessionalSkill').selectedIndex = 0;

        generateHours('startHour', 8 * 60, 18 * 60 + 55);
        generateHours('endHour', 8 * 60 + 5, 19 * 60);

        ['dateStart', 'dateEnd', 'startHour', 'endHour', 'rdvEstablishment', 'rdvLocal', 'rdvAcademicSkill', 'rdvProfessionalSkill'].forEach(id => {
            const el = document.getElementById(id);
            el.classList.remove('is-invalid');
            el.style.borderColor = '';
            const fb = el.nextElementSibling;
            if (fb && fb.classList.contains('invalid-feedback')) fb.remove();
        });
    });
});