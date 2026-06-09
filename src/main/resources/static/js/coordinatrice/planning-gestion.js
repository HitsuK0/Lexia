/**
 * @author Wellinger Chloé
 * @reviewer Nicolas Jean-François, Halet Louis
 */

document.addEventListener('DOMContentLoaded', function () {
    let currentMode = 'coordinator';
    let selectedUserId = null;
    let selectedUserName = null;
    let allUsers = [];
    let currentEvent = null;

    /* Initializes the FullCalendar instance with French locale, week view,
       and custom toolbar with previous/next navigation only. */
    const calendar = new FullCalendar.Calendar(document.getElementById('calendar'), {
        locale: 'fr',
        initialView: window.innerWidth < 768 ? 'timeGridDay' : window.innerWidth < 992 ? 'timeGridThreeDays' : 'timeGridWeek',
        headerToolbar: {
            left: 'prev', center: 'title', right: 'next'
        },
        slotMinTime: '08:00:00',
        slotMaxTime: '19:00:00',
        allDaySlot: false,
        expandRows: false,
        views: {
            timeGridThreeDays: {
                type: 'timeGrid', duration: {days: 3}
            }
        },
        height: 'auto',

        /* Fetches calendar events from the server based on the active mode.
           Loads the coordinator's own events, or the selected interpreter's
           or beneficiary's events if one has been selected.
           Returns an empty array if no user is selected in interpreter/beneficiary mode. */
        events: function (info, successCallback, failureCallback) {
            let url;

            if (currentMode === 'coordinator') {
                url = `/coordinatrice/planning/events?start=${info.startStr}&end=${info.endStr}`;
            } else if (currentMode === 'interpreter' && selectedUserId) {
                url = `/coordinatrice/planning/interpreter/${selectedUserId}/events?start=${info.startStr}&end=${info.endStr}`;
            } else if (currentMode === 'beneficiary' && selectedUserId) {
                url = `/coordinatrice/planning/beneficiary/${selectedUserId}/events?start=${info.startStr}&end=${info.endStr}`;
            } else {
                successCallback([]);
                return;
            }

            fetch(url)
                .then(response => response.json())
                .then(data => successCallback(data))
                .catch(() => failureCallback());
        },

        /* Opens the add appointment modal when clicking on an empty time slot,
           pre-filling the start and end date fields and rounding the clicked hour.
           Prevents opening for past dates. */
        dateClick: function (info) {
            const today = new Date().toISOString().split('T')[0];
            const clickedDate = info.dateStr.split('T')[0];

            if (clickedDate < today) return;

            document.getElementById('rdvDateStart').value = clickedDate;
            document.getElementById('rdvDateEnd').value = clickedDate;
            document.getElementById('rdvDateStart').min = today;
            document.getElementById('rdvDateEnd').min = clickedDate;

            if (info.dateStr.includes('T')) {
                const heureStr = info.dateStr.split('T')[1].substring(0, 5);
                const [h, m] = heureStr.split(':').map(Number);
                const minutesArrondies = m < 30 ? 0 : 30;
                const totalMinutes = h * 60 + minutesArrondies;

                generateHours('rdvEndHour', totalMinutes + 30, 19 * 60);
                document.getElementById('rdvStartHour').value = `${String(h).padStart(2, '0')}:${minutesArrondies === 0 ? '00' : '30'}`;
            }

            new bootstrap.Modal(document.getElementById('modalRDV')).show();
        },

        /* Opens the event detail modal on click, displaying the appointment.
           For absences, displays the reason if one was provided.
           Shows the Edit and Cancel buttons only for appointments. */
        eventClick: function (info) {
            const props = info.event.extendedProps;
            const start = info.event.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const end = info.event.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';

            document.getElementById('modalEventTitle').textContent = info.event.title;

            let body = props.fullDay ? `<p><strong>Horaire :</strong> Journée complète</p>` : `<p><strong>Horaire :</strong> ${start} – ${end}</p>`;

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

            const isAppointment = props.type === 'appointment';
            document.getElementById('btnEditEvent').classList.toggle('d-none', !isAppointment);
            document.getElementById('btnCancelEvent').classList.toggle('d-none', !isAppointment);

            currentEvent = info.event;

            new bootstrap.Modal(document.getElementById('modalEvent')).show();
        },

        /* Sets the event display content with an icon based on the appointment status
           (cancelled or unavailability), and shows appointment details below the title.
           For absences, displays the reason if one was provided. */
        eventContent: function (arg) {
            const props = arg.event.extendedProps;
            const isCancelled = arg.event.title.startsWith('Annulé');
            const isUnavailable = arg.event.title.startsWith('Indisponibilité');

            const icon = isCancelled ? '<i class="bi bi-x-circle" style="float:right; font-size:0.85rem;"></i>' : isUnavailable ? '<i class="bi bi-slash-circle" style="float:right; font-size:0.85rem;"></i>' : '';

            let html = `<div class="fw-bold">${arg.event.title}</div>`;

            if (props.type === 'appointment') {
                if (props.professionalSkills) html += `<div class="small">${props.professionalSkills}</div>`;
                if (props.beneficiary) html += `<div class="small">Bénéficiaire : ${props.beneficiary}</div>`;
                if (props.establishment) html += `<div class="small">${props.establishment}</div>`;
                if (props.locals && props.locals.length > 0) html += `<div class="small">${props.locals.join(', ')}</div>`;
                if (props.description) html += `<div class="small">📝 ${props.description}</div>`;
            } else if (props.type === 'absence') {
                if (props.reason && props.reason !== '') html += `<div class="small">Motif : ${props.reason}</div>`;
            }

            return {html: `<div class="p-1">${icon}${html}</div>`};
        }
    });

    calendar.render();

    /* Pre-loads beneficiaries for the modal RDV on page load,
       since the default mode is coordinator (she is always the interpreter). */
    document.getElementById('rdvUserLabel').textContent = 'Bénéficiaire';
    fetch('/coordinatrice/planning/beneficiaries')
        .then(r => r.json())
        .then(data => populateModalUserSelect(data, '-- Sélectionner un bénéficiaire --'))
        .catch(() => populateModalUserSelect([], '-- Sélectionner un bénéficiaire --'));

    /* Switches the active planning mode and updates the toolbar button styles.
       Resets the selected user and reloads the calendar accordingly.
       In interpreter or beneficiary mode, shows the search zone and loads
       the corresponding user list for autocomplete and dropdown. */
    window.switchMode = function (mode) {
        currentMode = mode;
        selectedUserId = null;
        selectedUserName = null;

        document.getElementById('btnMyPlanning').className = mode === 'coordinator' ? 'btn btn-primary btn-sm' : 'btn btn-outline-primary btn-sm';
        document.getElementById('btnInterpreterPlanning').className = mode === 'interpreter' ? 'btn btn-primary btn-sm' : 'btn btn-outline-primary btn-sm';
        document.getElementById('btnBeneficiaryPlanning').className = mode === 'beneficiary' ? 'btn btn-primary btn-sm' : 'btn btn-outline-primary btn-sm';

        const searchZone = document.getElementById('zone-selection');

        if (mode === 'coordinator') {
            searchZone.classList.add('d-none');
            calendar.refetchEvents();

            document.getElementById('rdvUserLabel').textContent = 'Bénéficiaire';
            fetch('/coordinatrice/planning/beneficiaries')
                .then(r => r.json())
                .then(data => populateModalUserSelect(data, '-- Sélectionner un bénéficiaire --'))
                .catch(() => populateModalUserSelect([], '-- Sélectionner un bénéficiaire --'));
        } else {
            searchZone.classList.remove('d-none');
            document.getElementById('userTypeLabel').textContent = mode === 'interpreter' ? 'interprète' : 'bénéficiaire';

            document.getElementById('userSearchInput').value = '';
            hideContextBanner();
            setCalendarVisible(false);
            calendar.refetchEvents();

            loadUserList(mode);
        }
    };

    /* Fetches the list of interpreters or beneficiaries from the server,
       caches it locally, and populates both the search dropdown and the autocomplete. */
    function loadUserList(mode) {
        const url = mode === 'interpreter' ? '/coordinatrice/planning/interpreters' : '/coordinatrice/planning/beneficiaries';

        fetch(url)
            .then(r => r.json())
            .then(data => {
                allUsers = data;
                populateDropdown(data, mode);
            })
            .catch(() => {
                allUsers = [];
            });

        const modalUrl = mode === 'beneficiary' ? '/coordinatrice/planning/interpreters' : '/coordinatrice/planning/beneficiaries';
        const modalLabel = mode === 'beneficiary' ? 'Interprète' : 'Bénéficiaire';
        const modalPlaceholder = mode === 'beneficiary' ? '-- Sélectionner un interprète --' : '-- Sélectionner un bénéficiaire --';

        document.getElementById('rdvUserLabel').textContent = modalLabel;

        fetch(modalUrl)
            .then(r => r.json())
            .then(data => populateModalUserSelect(data, modalPlaceholder))
            .catch(() => populateModalUserSelect([], modalPlaceholder));
    }

    /* Populates the search zone dropdown with the loaded user list.
       Resets its value and updates the placeholder label to match the current mode. */
    function populateDropdown(users, mode) {
        const dropdown = document.getElementById('userDropdown');
        const label = mode === 'interpreter' ? 'interprète' : 'bénéficiaire';
        dropdown.innerHTML = `<option value="" disabled selected>-- Sélectionner un(e) ${label} --</option>`;
        users.forEach(u => {
            const option = document.createElement('option');
            option.value = u.id;
            option.textContent = u.name;
            dropdown.appendChild(option);
        });
    }

    /* Populates the modal RDV user selector with the given list and placeholder. */
    function populateModalUserSelect(users, placeholder) {
        const select = document.getElementById('rdvUserSelect');
        select.innerHTML = `<option value="" disabled selected>${placeholder}</option>`;
        users.forEach(u => {
            const option = document.createElement('option');
            option.value = u.id;
            option.textContent = u.name;
            select.appendChild(option);
        });
    }

    /* Selects a user when chosen from the dropdown. */
    document.getElementById('userDropdown').addEventListener('change', function () {
        const user = allUsers.find(u => String(u.id) === this.value);
        if (user) selectUser(user);
    });

    const searchInput = document.getElementById('userSearchInput');
    const suggestionsList = document.getElementById('suggestionsList');

    /* Filters the cached user list on each keystroke and renders matching suggestions.
       Requires at least 2 characters before showing results. */
    searchInput.addEventListener('input', function () {
        const query = this.value.trim().toLowerCase();
        suggestionsList.innerHTML = '';

        if (query.length < 2) {
            suggestionsList.classList.add('d-none');
            return;
        }

        const results = allUsers.filter(u => u.name.toLowerCase().includes(query));

        if (results.length === 0) {
            suggestionsList.classList.add('d-none');
            return;
        }

        results.forEach(u => {
            const li = document.createElement('li');
            li.className = 'list-group-item list-group-item-action py-1 px-2 small';
            li.textContent = u.name;
            li.style.cursor = 'pointer';
            li.addEventListener('click', () => selectUser(u));
            suggestionsList.appendChild(li);
        });

        suggestionsList.classList.remove('d-none');
    });

    /* Closes the suggestion dropdown when clicking outside the search field. */
    document.addEventListener('click', function (e) {
        if (!searchInput.contains(e.target) && !suggestionsList.contains(e.target)) {
            suggestionsList.classList.add('d-none');
        }
    });

    /* Selects a user from either the autocomplete or the dropdown,
       displays the context banner, syncs both inputs, and reloads the calendar. */
    function selectUser(u) {
        selectedUserId = u.id;
        selectedUserName = u.name;

        searchInput.value = '';
        suggestionsList.classList.add('d-none');

        document.getElementById('userDropdown').value = u.id;

        showContextBanner(u.name);
        setCalendarVisible(true);
        calendar.refetchEvents();
    }

    /* Resets the current user selection when the reset button is clicked,
       hides the context banner, resets both inputs, and clears the calendar. */
    document.getElementById('btnResetSelection').addEventListener('click', function () {
        selectedUserId = null;
        selectedUserName = null;
        document.getElementById('userDropdown').selectedIndex = 0;
        hideContextBanner();
        setCalendarVisible(false);
        calendar.refetchEvents();
    });

    function showContextBanner(name) {
        document.getElementById('selectedUserName').textContent = name;
        document.getElementById('contextBanner').classList.remove('d-none');
    }

    function hideContextBanner() {
        document.getElementById('contextBanner').classList.add('d-none');
        document.getElementById('selectedUserName').textContent = '';
    }

    function setCalendarVisible(visible) {
        document.getElementById('emptySelectionMessage').classList.toggle('d-none', visible);
        document.getElementById('calendar').style.display = visible ? '' : 'none';
    }

    /* Closes the event detail modal and opens the add appointment modal
       pre-filled with the current event's data for editing. */
    document.getElementById('btnEditEvent').addEventListener('click', function () {
        bootstrap.Modal.getInstance(document.getElementById('modalEvent')).hide();

        if (!currentEvent) return;
        const props = currentEvent.extendedProps;

        const startDate = currentEvent.start.toISOString().split('T')[0];
        const endDate = currentEvent.end ? currentEvent.end.toISOString().split('T')[0] : startDate;
        document.getElementById('rdvDateStart').value = startDate;
        document.getElementById('rdvDateEnd').value = endDate;

        if (!props.fullDay) {
            const startTime = currentEvent.start.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'});
            const endTime = currentEvent.end?.toLocaleTimeString('fr-FR', {hour: '2-digit', minute: '2-digit'}) ?? '';
            generateHours('rdvStartHour', 8 * 60, 18 * 60 + 55);
            generateHours('rdvEndHour', 8 * 60 + 5, 19 * 60);
            document.getElementById('rdvStartHour').value = startTime;
            document.getElementById('rdvEndHour').value = endTime;
        } else {
            document.getElementById('rdvFullDay').checked = true;
            document.getElementById('rdvStartHour').disabled = true;
            document.getElementById('rdvEndHour').disabled = true;
        }

        if (props.establishment) document.getElementById('rdvEstablishment').value = props.establishment;
        if (props.locals && props.locals.length > 0) document.getElementById('rdvLocal').value = props.locals[0];
        if (props.description) document.getElementById('rdvDescription').value = props.description;

        document.getElementById('btnSendRDV').dataset.editId = currentEvent.id;

        new bootstrap.Modal(document.getElementById('modalRDV')).show();
    });

    /* Asks for confirmation then sends a cancellation request to the backend
       for the currently selected appointment. */
    document.getElementById('btnCancelEvent').addEventListener('click', function () {
        if (!currentEvent) return;

        if (!confirm('Êtes-vous sûr de vouloir annuler ce rendez-vous ?')) return;

        // TODO: call backend cancel endpoint
        // fetch(`/coordinatrice/planning/appointments/${currentEvent.id}/cancel`, { method: 'POST' })
        //     .then(() => { calendar.refetchEvents(); })

        bootstrap.Modal.getInstance(document.getElementById('modalEvent')).hide();
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

    generateHours('rdvStartHour', 8 * 60, 18 * 60 + 55);
    generateHours('rdvEndHour', 8 * 60 + 5, 19 * 60);

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('rdvDateStart').min = today;
    document.getElementById('rdvDateEnd').min = today;

    /* Regenerates the end hour options to always start after the selected start hour. */
    document.getElementById('rdvStartHour').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('rdvEndHour', h * 60 + m + 5, 19 * 60);
    });

    /* Updates the minimum end date when the start date changes. */
    document.getElementById('rdvDateStart').addEventListener('change', function () {
        document.getElementById('rdvDateEnd').min = this.value;
        if (document.getElementById('rdvDateEnd').value < this.value) {
            document.getElementById('rdvDateEnd').value = this.value;
        }
    });

    /* Disables or re-enables the time range selects when the full day checkbox is toggled. */
    document.getElementById('rdvFullDay').addEventListener('change', function () {
        document.getElementById('rdvStartHour').disabled = this.checked;
        document.getElementById('rdvEndHour').disabled = this.checked;
    });

    /* Hides the academic skills error message as soon as at least one skill is checked. */
    document.querySelectorAll('.skill-check').forEach(cb => {
        cb.addEventListener('change', function () {
            if (document.querySelectorAll('.skill-check:checked').length > 0) {
                document.getElementById('rdvAcademicSkillsError').style.display = 'none';
            }
        });
    });

    /* Validates the add appointment form before submission. */
    document.getElementById('btnSendRDV').addEventListener('click', function () {
        let valid = true;

        const dateStart = document.getElementById('rdvDateStart');
        const dateEnd = document.getElementById('rdvDateEnd');
        const startHour = document.getElementById('rdvStartHour');
        const endHour = document.getElementById('rdvEndHour');
        const fullDay = document.getElementById('rdvFullDay').checked;
        const rdvEstablishment = document.getElementById('rdvEstablishment');
        const rdvLocal = document.getElementById('rdvLocal');
        const selectedSkills = document.querySelectorAll('.skill-check:checked');
        const skillsError = document.getElementById('rdvAcademicSkillsError');
        const atLeastOneComp = Array.from(document.querySelectorAll('#compTranscription, #compTranslitteration, #compTranslation')).some(cb => cb.checked);
        const compsError = document.getElementById('rdvCompsError');

        [dateStart, dateEnd, startHour, endHour, rdvEstablishment, rdvLocal].forEach(el => {
            el.classList.remove('is-invalid');
            const feedback = el.nextElementSibling;
            if (feedback && feedback.classList.contains('invalid-feedback')) feedback.remove();
        });
        skillsError.style.display = 'none';
        compsError.style.display = 'none';

        function displayError(input, message) {
            input.classList.add('is-invalid');
            const div = document.createElement('div');
            div.classList.add('invalid-feedback');
            div.textContent = message;
            input.insertAdjacentElement('afterend', div);
            valid = false;
        }

        if (!dateStart.value) displayError(dateStart, 'La date de début est obligatoire.');
        if (!dateEnd.value) displayError(dateEnd, 'La date de fin est obligatoire.');
        if (dateStart.value && dateEnd.value && dateEnd.value < dateStart.value) {
            displayError(dateEnd, 'La date de fin doit être après la date de début.');
        }
        if (selectedSkills.length === 0) {
            skillsError.style.display = 'block';
            valid = false;
        }
        if (!atLeastOneComp) {
            compsError.style.display = 'block';
            valid = false;
        }
        if (!rdvEstablishment.value) displayError(rdvEstablishment, 'L\'établissement est obligatoire.');
        if (!rdvLocal.value.trim()) displayError(rdvLocal, 'Le local est obligatoire.');
        if (!fullDay) {
            if (!startHour.value) displayError(startHour, 'L\'heure de début est obligatoire.');
            if (!endHour.value) displayError(endHour, 'L\'heure de fin est obligatoire.');
        }

        if (valid) {
            // TODO: submit to backend endpoint
            bootstrap.Modal.getInstance(document.getElementById('modalRDV')).hide();
        }
    });

    /* Resets all add appointment modal fields to their default state when the modal is closed. */
    document.getElementById('modalRDV').addEventListener('hidden.bs.modal', function () {
        document.getElementById('rdvDateStart').value = '';
        document.getElementById('rdvDateEnd').value = '';
        document.getElementById('rdvDateStart').min = today;
        document.getElementById('rdvDateEnd').min = today;
        document.getElementById('rdvLocal').value = '';
        document.getElementById('rdvDescription').value = '';
        document.getElementById('rdvAcademicSkillsError').style.display = 'none';
        document.getElementById('rdvCompsError').style.display = 'none';
        document.getElementById('rdvFullDay').checked = false;
        document.getElementById('rdvStartHour').disabled = false;
        document.getElementById('rdvEndHour').disabled = false;
        document.getElementById('rdvUserSelect').selectedIndex = 0;
        document.getElementById('rdvEstablishment').selectedIndex = 0;

        document.querySelectorAll('.skill-check').forEach(cb => cb.checked = false);
        document.querySelectorAll('#compTranscription, #compTranslitteration, #compTranslation')
            .forEach(cb => cb.checked = true);

        generateHours('rdvStartHour', 8 * 60, 18 * 60 + 55);
        generateHours('rdvEndHour', 8 * 60 + 5, 19 * 60);

        ['rdvDateStart', 'rdvDateEnd', 'rdvStartHour', 'rdvEndHour', 'rdvEstablishment', 'rdvLocal'].forEach(id => {
            const el = document.getElementById(id);
            el.classList.remove('is-invalid');
            const fb = el.nextElementSibling;
            if (fb && fb.classList.contains('invalid-feedback')) fb.remove();
        });
    });
});