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

        /* Opens the absence declaration modal when clicking on an empty time slot,
        pre-filling the start and end date fields and rounding the clicked hour. */
        dateClick: function (info) {
            document.getElementById('dateStart').value = info.dateStr.split('T')[0];
            document.getElementById('dateEnd').value = info.dateStr.split('T')[0];

            const today = new Date().toISOString().split('T')[0];
            document.getElementById('dateStart').min = today;
            document.getElementById('dateEnd').min = info.dateStr.split('T')[0] > today
                ? info.dateStr.split('T')[0]
                : today;

            if (info.dateStr.includes('T')) {
                const heureStr = info.dateStr.split('T')[1].substring(0, 5);
                const [h, m] = heureStr.split(':').map(Number);
                const minutesArrondies = m < 30 ? 0 : 30;
                const totalMinutes = h * 60 + minutesArrondies;
                const heureArrondie = `${String(h).padStart(2, '0')}:${minutesArrondies === 0 ? '00' : '30'}`;

                generateHours('endHour', totalMinutes + 30, 19 * 60);
                document.getElementById('startHour').value = heureArrondie;
            }

            const modal = new bootstrap.Modal(document.getElementById('modalIndispo'));
            modal.show();
        },

        /* Adjusts the calendar height depending on the screen size. */
        height: 'auto',

        /* Fetches the interpreter's planning events from the server
        for the currently displayed date range. */
        events: function (info, successCallback, failureCallback) {
            fetch(`/interprete/planning/events?start=${info.startStr}&end=${info.endStr}`)
                .then(response => response.json())
                .then(data => successCallback(data))
                .catch(() => failureCallback());
        },

        /* Opens the event detail modal on click, displaying the appointment's
        schedule, beneficiary, establishment, local, description and professional skills.
        For absences, displays the reason if one was provided. */
        eventClick: function (info) {
            const props = info.event.extendedProps;
            const start = info.event.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const end = info.event.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';

            document.getElementById('modalEventTitle').textContent = info.event.title;

            let body = props.fullDay
                ? `<p><strong>Horaire :</strong> Journée complète</p>`
                : `<p><strong>Horaire :</strong> ${start} – ${end}</p>`;

            if (props.type === 'appointment') {
                if (props.beneficiary) body += `<p><strong>Bénéficiaire :</strong> ${props.beneficiary}</p>`;
                if (props.establishment) body += `<p><strong>Établissement :</strong> ${props.establishment}</p>`;
                if (props.locals?.length > 0) body += `<p><strong>Local :</strong> ${props.locals.join(', ')}</p>`;
                if (props.description) body += `<p><strong>Description :</strong> ${props.description}</p>`;
                if (props.professionalSkills) body += `<p><strong>Compétence métier :</strong> ${props.professionalSkills}</p>`;
            } else if (props.type === 'absence') {
                if (props.reason && props.reason !== '') {
                    body += `<p><strong>Motif :</strong> ${props.reason}</p>`;
                } else {
                    body += `<p class="text-muted fst-italic">Aucun motif renseigné</p>`;
                }
            }

            document.getElementById('modalEventBody').innerHTML = body;
            new bootstrap.Modal(document.getElementById('modalEvent')).show();
        },

        /* Sets the event display content with an icon based on the appointment status
        (cancelled or unavailability), and shows appointment details below the title.
        For absences, displays the reason if one was provided. */
        eventContent: function (arg) {
            const props = arg.event.extendedProps;
            const isAnnule = arg.event.title.startsWith('Annulé');
            const isIndispo = arg.event.title.startsWith('Indisponibilité');

            const icone = isAnnule
                ? '<i class="bi bi-x-circle" style="float:right; font-size:0.85rem;"></i>'
                : isIndispo
                    ? '<i class="bi bi-slash-circle" style="float:right; font-size:0.85rem;"></i>'
                    : '';

            let html = `<div class="fw-bold text-truncate">${arg.event.title}</div>`;

            if (props.type === 'appointment') {
                if (props.beneficiary) html += `<div class="small text-truncate">Bénéficiaire : ${props.beneficiary}</div>`;
            } else if (props.type === 'absence') {
                if (props.reason && props.reason !== '') html += `<div class="small text-truncate">Motif : ${props.reason}</div>`;
            }

            return {html: `<div class="p-1" style="overflow:hidden;">${icone}${html}</div>`};
        }
    });

    calendar.render();

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('dateStart').min = today;
    document.getElementById('dateEnd').min = today;

    document.getElementById('dateStart').addEventListener('change', function () {
        document.getElementById('dateEnd').min = this.value;
    });

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

    /* Regenerates the end hour options to always start after the selected start hour. */
    document.getElementById('startHour').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        const totalMinutes = h * 60 + m;
        generateHours('endHour', totalMinutes + 5, 19 * 60);
    });

    /* Disables or re-enables the time range selects when the full day checkbox is toggled. */
    document.getElementById('fullDay').addEventListener('change', function () {
        document.getElementById('startHour').disabled = this.checked;
        document.getElementById('endHour').disabled = this.checked;
    });

    /* Validates the absence declaration form before submission,
    displaying inline error messages for missing or invalid fields. */
    /* Validates the absence declaration form before submission,
   keeping the modal open and showing inline errors if fields are missing. */
    document.getElementById('btnSendPlanning').addEventListener('click', function () {
        let valid = true;

        const dateStart = document.getElementById('dateStart');
        const dateEnd   = document.getElementById('dateEnd');
        const startHour = document.getElementById('startHour');
        const endHour   = document.getElementById('endHour');
        const fullDay   = document.getElementById('fullDay').checked;

        [dateStart, dateEnd, startHour, endHour].forEach(el => {
            el.classList.remove('is-invalid');
            el.style.borderColor = '';
            const fb = el.nextElementSibling;
            if (fb && fb.classList.contains('invalid-feedback')) fb.remove();
        });

        function displayError(input, message) {
            input.classList.add('is-invalid');
            input.style.borderColor = '#dc3545';
            const div = document.createElement('div');
            div.classList.add('invalid-feedback');
            div.style.display = 'block';
            div.style.color = '#dc3545';
            div.style.fontSize = '0.875rem';
            div.textContent = message;
            input.insertAdjacentElement('afterend', div);
            valid = false;
        }

        if (!dateStart.value) displayError(dateStart, 'La date de début est obligatoire.');
        if (!dateEnd.value)   displayError(dateEnd,   'La date de fin est obligatoire.');
        if (dateStart.value && dateEnd.value && dateEnd.value < dateStart.value) {
            displayError(dateEnd, 'La date de fin doit être après la date de début.');
        }

        if (!fullDay) {
            if (!startHour.value) displayError(startHour, 'L\'heure de début est obligatoire.');
            if (!endHour.value)   displayError(endHour,   'L\'heure de fin est obligatoire.');
        }

        if (valid) {
            document.querySelector('#modalIndispo form').submit();
        }
    });

    /* Resets all absence modal fields to their default state after the modal is closed. */
    document.getElementById('modalIndispo').addEventListener('hidden.bs.modal', function () {
        document.getElementById('dateStart').value = '';
        document.getElementById('dateEnd').value = '';
        document.getElementById('fullDay').checked = false;
        document.getElementById('startHour').disabled = false;
        document.getElementById('endHour').disabled = false;
        document.getElementById('motif').value = '';
        document.getElementById('motifPrive').checked = false;
        generateHours('startHour', 8 * 60, 18 * 60 + 55);
        generateHours('endHour', 8 * 60 + 5, 19 * 60);
        ['dateStart', 'dateEnd', 'startHour', 'endHour'].forEach(id => {
            const el = document.getElementById(id);
            el.classList.remove('is-invalid');
            const fb = el.nextElementSibling;
            if (fb && fb.classList.contains('invalid-feedback')) fb.remove();
        });
    });
});