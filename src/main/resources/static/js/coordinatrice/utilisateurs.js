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
    const weeklyWorkField = document.getElementById('weeklyWorkField');
    const beneficiaryFields = document.getElementById('beneficiaryFields');

    roleSelect.addEventListener('change', function () {
        const val = this.value;
        weeklyWorkField.style.display = (val === '1' || val === '2' || val === '4') ? 'block' : 'none';
        beneficiaryFields.style.display = val === '3' ? 'block' : 'none';
    });

    /**
     * Generates a random password that respects the password policy:
     * at least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special character.
     * Guarantees one character from each required category, then fills the rest
     * randomly and shuffles the result so the categories aren't predictably ordered.
     *
     * @param {number} length - total length of the password (must be >= 4, default 12)
     * @returns {string} the generated password
     */
    function generatePassword(length = 12) {
        const uppercase = 'ABCDEFGHJKLMNPQRSTUVWXYZ';
        const lowercase = 'abcdefghijkmnpqrstuvwxyz';
        const digits = '23456789';
        const special = '!@#$%&*?';
        const all = uppercase + lowercase + digits + special;

        function randomChar(charset) {
            const randomValues = new Uint32Array(1);
            crypto.getRandomValues(randomValues);
            return charset[randomValues[0] % charset.length];
        }

        // Guarantee at least one character of each required category
        const passwordChars = [
            randomChar(uppercase),
            randomChar(lowercase),
            randomChar(digits),
            randomChar(special)
        ];

        // Fill the remaining length with random characters from the full charset
        for (let i = passwordChars.length; i < length; i++) {
            passwordChars.push(randomChar(all));
        }

        // Shuffle to avoid a predictable pattern (Fisher-Yates)
        for (let i = passwordChars.length - 1; i > 0; i--) {
            const randomValues = new Uint32Array(1);
            crypto.getRandomValues(randomValues);
            const j = randomValues[0] % (i + 1);
            [passwordChars[i], passwordChars[j]] = [passwordChars[j], passwordChars[i]];
        }

        return passwordChars.join('');
    }

    generatePasswordBtn.addEventListener('click', function () {
        passwordInput.value = generatePassword();
        passwordInput.removeAttribute('readonly');
    });

    passwordInput.addEventListener('focus', function () {
        passwordInput.removeAttribute('readonly');
    });

    // Generates a password by default when the modal opens
    const modalAjout = document.getElementById('modalAjout');
    modalAjout.addEventListener('show.bs.modal', function () {
        passwordInput.value = generatePassword();
        passwordInput.removeAttribute('readonly');
    });
});