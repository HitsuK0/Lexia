document.addEventListener('DOMContentLoaded', function () {

    const ROWS_PER_PAGE = 7;
    let currentPage = 1;

    const searchInput = document.getElementById('searchInput');
    const roleFilter = document.getElementById('roleFilter');
    const tableRows = Array.from(document.querySelectorAll('#tableBody tr'));
    const pagination = document.getElementById('pagination');

    /* Returns the rows matching the current search and role filter. */
    function getFilteredRows() {
        const searchValue = searchInput.value.toLowerCase().trim();
        const roleValue = roleFilter.value.toLowerCase();

        return tableRows.filter(function (row) {
            const name = row.querySelector('td:nth-child(1)').textContent.toLowerCase();
            const role = row.getAttribute('data-role').toLowerCase();

            const matchesSearch = name.includes(searchValue);
            const matchesRole = roleValue === '' || role === roleValue;

            return matchesSearch && matchesRole;
        });
    }

    /* Displays the rows for the given page and updates pagination. */
    function renderPage(page) {
        currentPage = page;
        const filtered = getFilteredRows();
        const totalPages = Math.max(1, Math.ceil(filtered.length / ROWS_PER_PAGE));

        if (currentPage > totalPages) currentPage = totalPages;

        tableRows.forEach(row => row.style.display = 'none');

        const start = (currentPage - 1) * ROWS_PER_PAGE;
        const end = start + ROWS_PER_PAGE;
        filtered.slice(start, end).forEach(row => row.style.display = '');

        renderPagination(totalPages);
    }

    /* Builds the pagination buttons dynamically. */
    function renderPagination(totalPages) {
        pagination.innerHTML = '';

        if (totalPages <= 1) return;

        const prevLi = document.createElement('li');
        prevLi.className = 'page-item';
        prevLi.innerHTML = `<a class="page-link" href="#"><i class="bi bi-arrow-left"></i> Previous</a>`;
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
        nextLi.innerHTML = `<a class="page-link" href="#">Next <i class="bi bi-arrow-right"></i></a>`;
        if (currentPage === totalPages) {
            nextLi.style.display = 'none';
        } else {
            nextLi.addEventListener('click', function (e) {
                e.preventDefault();
                renderPage(currentPage + 1);
            });
        }
        pagination.appendChild(nextLi);
    }

    searchInput.addEventListener('input', function () { renderPage(1); });
    roleFilter.addEventListener('change', function () { renderPage(1); });

    renderPage(1);

    /**
     * Shows or hides role-specific fields (interpreter, beneficiary, resa/coordinator)
     * based on the selected role in the add user modal.
     */
    const roleSelect = document.getElementById('roleSelect');
    const interpreteWorkField = document.getElementById('interpreteWorkField');
    const beneficiaryFields = document.getElementById('beneficiaryFields');
    const resaCoordinatorWorkField = document.getElementById('resaCoordinatorWorkField');

    roleSelect.addEventListener('change', function () {
        interpreteWorkField.style.display = this.value === '2' ? 'block' : 'none';
        beneficiaryFields.style.display = this.value === '3' ? 'block' : 'none';
        resaCoordinatorWorkField.style.display = (this.value === '1' || this.value === '4') ? 'block' : 'none';
    });
});