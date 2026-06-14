document.addEventListener('DOMContentLoaded', function () {

    const ROWS_PER_PAGE = 7;
    let currentPage = 1;
    let currentFilter = 'all';

    /* Generates time options in 5-minute increments between a start and end time (in minutes)
    and populates the given select element. */
    function generateHours(select, start, end) {
        select.innerHTML = '';
        for (let t = start; t <= end; t += 5) {
            const hh = String(Math.floor(t / 60)).padStart(2, '0');
            const mm = String(t % 60).padStart(2, '0');
            const option = document.createElement('option');
            option.value = `${hh}:${mm}`;
            option.textContent = `${hh}:${mm}`;
            select.appendChild(option);
        }
    }

    generateHours(document.getElementById('startHour'), 8 * 60, 18 * 60 + 55);
    generateHours(document.getElementById('endHour'), 8 * 60 + 5, 19 * 60);
    generateHours(document.getElementById('editStartHour'), 8 * 60, 18 * 60 + 55);
    generateHours(document.getElementById('editEndHour'), 8 * 60 + 5, 19 * 60);

    document.getElementById('startHour').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours(document.getElementById('endHour'), h * 60 + m + 5, 19 * 60);
    });

    document.getElementById('editStartHour').addEventListener('change', function () {
        const [h, m] = this.value.split(':').map(Number);
        generateHours(document.getElementById('editEndHour'), h * 60 + m + 5, 19 * 60);
    });

    /* Disables the time range inputs when the full day checkbox is checked */
    document.getElementById('allDay').addEventListener('change', function () {
        document.getElementById('startHour').disabled = this.checked;
        document.getElementById('endHour').disabled = this.checked;
    });

    /* Disables the time range inputs when the full day checkbox is checked in the edit modal.
    Also updates the hidden field so fullDay is always sent in the form. */
    document.getElementById('editAllDay').addEventListener('change', function () {
        document.getElementById('editStartHour').disabled = this.checked;
        document.getElementById('editEndHour').disabled = this.checked;
        document.getElementById('fullDay').value = this.checked ? 'true' : 'false';
    });

    /* Populates the edit modal fields with the data from the clicked row's button */
    document.getElementById('modalEditIndispo').addEventListener('show.bs.modal', function (e) {
        const btn = e.relatedTarget;
        document.getElementById('editStartDate').value = btn.dataset.startDate;
        document.getElementById('editEndDate').value = btn.dataset.endDate;
        document.getElementById('editReason').value = btn.dataset.reason || '';
        document.getElementById('editAbsenceId').value = btn.dataset.absenceId;

        const allDay = btn.dataset.allDay === 'true';
        document.getElementById('editAllDay').checked = allDay;
        document.getElementById('fullDay').value = allDay ? 'true' : 'false';
        document.getElementById('editStartHour').disabled = allDay;
        document.getElementById('editEndHour').disabled = allDay;

        if (!allDay) {
            document.getElementById('editStartHour').value = btn.dataset.startHour;
            document.getElementById('editEndHour').value = btn.dataset.endHour;
        }
    });

    /* Resets the declare form fields after the modal is closed */
    document.getElementById('modalIndispo').addEventListener('hidden.bs.modal', function () {
        document.getElementById('startDate').value = '';
        document.getElementById('endDate').value = '';
        document.getElementById('allDay').checked = false;
        document.getElementById('startHour').disabled = false;
        document.getElementById('endHour').disabled = false;
        document.getElementById('reason').value = '';
        document.getElementById('reasonPrivate').checked = false;
        generateHours(document.getElementById('startHour'), 8 * 60, 18 * 60 + 55);
        generateHours(document.getElementById('endHour'), 8 * 60 + 5, 19 * 60);
    });

    /* Resets the edit form fields after the modal is closed */
    document.getElementById('modalEditIndispo').addEventListener('hidden.bs.modal', function () {
        document.getElementById('editStartDate').value = '';
        document.getElementById('editEndDate').value = '';
        document.getElementById('editAllDay').checked = false;
        document.getElementById('fullDay').value = 'false';
        document.getElementById('editStartHour').disabled = false;
        document.getElementById('editEndHour').disabled = false;
        document.getElementById('editReason').value = '';
        document.getElementById('editReasonPrivate').checked = false;
        generateHours(document.getElementById('editStartHour'), 8 * 60, 18 * 60 + 55);
        generateHours(document.getElementById('editEndHour'), 8 * 60 + 5, 19 * 60);
    });

    /* Prevents selecting a start date before today */
    const today = new Date().toISOString().split('T')[0];

    document.getElementById('startDate').min = today;
    document.getElementById('endDate').min = today;
    document.getElementById('startDate').addEventListener('change', function () {
        document.getElementById('endDate').min = this.value;
    });

    document.getElementById('editStartDate').min = today;
    document.getElementById('editEndDate').min = today;
    document.getElementById('editStartDate').addEventListener('change', function () {
        document.getElementById('editEndDate').min = this.value;
    });

    /* Populates the hidden absence id field in the delete modal */
    document.getElementById('modalDeleteIndispo').addEventListener('show.bs.modal', function (e) {
        const btn = e.relatedTarget;
        document.getElementById('deleteAbsenceId').value = btn.dataset.absenceId;
    });

    /* Returns all rows matching the current filter status */
    function getVisibleRows() {
        const rows = Array.from(document.querySelectorAll('#tableBody tr'));
        return rows.filter(row => currentFilter === 'all' || row.dataset.status === currentFilter);
    }

    /* Shows only the rows belonging to the given page, hides the rest */
    function showPage(page) {
        const visibleRows = getVisibleRows();
        const start = (page - 1) * ROWS_PER_PAGE;
        const end = start + ROWS_PER_PAGE;

        Array.from(document.querySelectorAll('#tableBody tr')).forEach(row => {
            row.style.display = 'none';
        });

        visibleRows.slice(start, end).forEach(row => {
            row.style.display = '';
        });
    }

    /* Renders the pagination bar with Previous/Next buttons and page numbers.
    Does not render anything if there is only one page or fewer. */
    function renderPagination(page, totalPages) {
        const pagination = document.getElementById('pagination');
        pagination.innerHTML = '';

        if (totalPages <= 1) return;

        if (page > 1) {
            pagination.innerHTML += `
                <li class="page-item">
                    <a class="page-link" href="#" onclick="changePage(${page - 1}); return false;">
                        <i class="bi bi-arrow-left"></i> Previous
                    </a>
                </li>`;
        }

        for (let i = 1; i <= totalPages; i++) {
            if (i === 1 || i === totalPages || (i >= page - 2 && i <= page + 2)) {
                pagination.innerHTML += `
                    <li class="page-item ${i === page ? 'active' : ''}">
                        <a class="page-link" href="#" onclick="changePage(${i}); return false;">${i}</a>
                    </li>`;
            } else if (i === page - 3 || i === page + 3) {
                pagination.innerHTML += `<li class="page-item disabled"><a class="page-link">...</a></li>`;
            }
        }

        if (page < totalPages) {
            pagination.innerHTML += `
                <li class="page-item">
                    <a class="page-link" href="#" onclick="changePage(${page + 1}); return false;">
                        Next <i class="bi bi-arrow-right"></i>
                    </a>
                </li>`;
        }
    }

    /* Validates the absence declaration form before submission,
   keeping the modal open and showing inline errors if fields are missing. */
    document.getElementById('btnSendIndispo').addEventListener('click', function () {
        let valid = true;

        const startDate = document.getElementById('startDate');
        const endDate   = document.getElementById('endDate');
        const startHour = document.getElementById('startHour');
        const endHour   = document.getElementById('endHour');
        const fullDay   = document.getElementById('allDay').checked;

        [startDate, endDate, startHour, endHour].forEach(el => {
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

        if (!startDate.value) displayError(startDate, 'La date de début est obligatoire.');
        if (!endDate.value)   displayError(endDate,   'La date de fin est obligatoire.');
        if (startDate.value && endDate.value && endDate.value < startDate.value) {
            displayError(endDate, 'La date de fin doit être après la date de début.');
        }

        if (!fullDay) {
            if (!startHour.value) displayError(startHour, 'L\'heure de début est obligatoire.');
            if (!endHour.value)   displayError(endHour,   'L\'heure de fin est obligatoire.');
        }

        if (valid) {
            document.querySelector('#modalIndispo form').submit();
        }
    });

    /* Updates the table rows and pagination based on current page and filter */
    function updateTable() {
        const totalPages = Math.ceil(getVisibleRows().length / ROWS_PER_PAGE);
        if (currentPage > totalPages) currentPage = 1;
        showPage(currentPage);
        renderPagination(currentPage, totalPages);
    }

    /* Changes the current page and refreshes the table */
    window.changePage = function (page) {
        currentPage = page;
        updateTable();
    };

    /* Filters the table rows by status and resets to page 1 */
    window.filterStatus = function (status) {
        currentFilter = status;
        currentPage = 1;
        updateTable();
    };

    updateTable();
});