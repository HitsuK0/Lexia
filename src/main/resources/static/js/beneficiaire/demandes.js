const ROWS_PER_PAGE = 7;
let currentPage = 1;
let currentFilter = '';

/* Returns the rows matching the current filter. */
function getFilteredRows() {
    return Array.from(document.querySelectorAll('#tableBody tr')).filter(row =>
        !currentFilter || row.dataset.status === currentFilter
    );
}

/* Displays the rows for the given page and updates pagination. */
function renderPage(page) {
    currentPage = page;
    const filtered = getFilteredRows();
    const totalPages = Math.ceil(filtered.length / ROWS_PER_PAGE);

    document.querySelectorAll('#tableBody tr').forEach(row => row.style.display = 'none');

    const start = (page - 1) * ROWS_PER_PAGE;
    const end = start + ROWS_PER_PAGE;
    filtered.slice(start, end).forEach(row => row.style.display = '');

    renderPagination(totalPages);
}

/* Builds the pagination buttons dynamically for both top and bottom. */
function renderPagination(totalPages) {
    ['pagination', 'paginationTop'].forEach(id => {
        const pagination = document.getElementById(id);
        pagination.innerHTML = '';

        if (totalPages <= 1) return;

        const prevLi = document.createElement('li');
        prevLi.className = 'page-item';
        prevLi.innerHTML = `<a class="page-link" href="#"><i class="bi bi-arrow-left"></i> Précédent</a>`;
        if (currentPage === 1) {
            prevLi.style.display = 'none';
        } else {
            prevLi.addEventListener('click', function (e) {
                e.preventDefault();
                renderPage(currentPage - 1);
            });
        }
        pagination.appendChild(prevLi);

        for (let i = 1; i <= totalPages; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
            li.addEventListener('click', function (e) {
                e.preventDefault();
                renderPage(i);
            });
            pagination.appendChild(li);
        }

        const nextLi = document.createElement('li');
        nextLi.className = 'page-item';
        nextLi.innerHTML = `<a class="page-link" href="#">Suivant <i class="bi bi-arrow-right"></i></a>`;
        if (currentPage === totalPages) {
            nextLi.style.display = 'none';
        } else {
            nextLi.addEventListener('click', function (e) {
                e.preventDefault();
                renderPage(currentPage + 1);
            });
        }
        pagination.appendChild(nextLi);
    });
}

/* Generates time options for a select element between start and end (in minutes). */
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

document.addEventListener('DOMContentLoaded', function () {

    generateHours('startHour', 8 * 60, 18 * 60 + 55);
    generateHours('endHour', 8 * 60 + 5, 19 * 60);

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('dateStart').min = today;
    document.getElementById('dateEnd').min = today;

    /* Filters the table rows by status and resets to page 1. */
    document.getElementById('statusFilter').addEventListener('change', function () {
        currentFilter = this.value;
        renderPage(1);
    });

    /* Regenerates end hour options after start hour selection. */
    document.getElementById('startHour').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours('endHour', h * 60 + m + 5, 19 * 60);
    });

    /* Disables or re-enables time selects when full day checkbox is toggled. */
    document.getElementById('fullDay').addEventListener('change', function () {
        document.getElementById('startHour').disabled = this.checked;
        document.getElementById('endHour').disabled = this.checked;
    });

    /* Updates dateEnd min when dateStart changes. */
    document.getElementById('dateStart').addEventListener('change', function () {
        document.getElementById('dateEnd').min = this.value;
        if (document.getElementById('dateEnd').value < this.value) {
            document.getElementById('dateEnd').value = this.value;
        }
    });

    /* Populates the delete modal with the appointment id on open. */
    document.getElementById('modalSuppression').addEventListener('show.bs.modal', function (e) {
        const btn = e.relatedTarget;
        document.getElementById('deleteAppointmentId').value = btn.dataset.id;
    });

    /* Validates the RDV form before submission. */
    document.getElementById('btnSend').addEventListener('click', function () {
        let valid = true;

        const dateStart        = document.getElementById('dateStart');
        const dateEnd          = document.getElementById('dateEnd');
        const startHour        = document.getElementById('startHour');
        const endHour          = document.getElementById('endHour');
        const fullDay          = document.getElementById('fullDay').checked;
        const rdvEstablishment = document.getElementById('rdvEstablishment');
        const rdvLocal         = document.getElementById('rdvLocal');
        const rdvAcademicSkill    = document.getElementById('rdvAcademicSkill');
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

        fetch('/demandes/rdv', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    window.location.reload();
                } else {
                    alert('Une erreur est survenue lors de la création de la demande.');
                }
            })
            .catch(err => console.error('Error creating appointment:', err));

        bootstrap.Modal.getInstance(document.getElementById('modalRDV')).hide();
    });

    /* Resets all modal RDV fields on close. */
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

    renderPage(1);
});