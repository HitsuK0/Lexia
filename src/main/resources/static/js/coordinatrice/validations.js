/**
 * @author Wellinger Chloé
 * @reviewer Nicolas Jean-François, Halet Louis
 */

document.addEventListener('DOMContentLoaded', function () {
    const calendarInstances = {};

    let currentBeneficiaryCard   = null;
    let currentBeneficiaryId     = null;
    let selectedInterpreterId    = null;

    let currentAbsenceCard       = null;
    let currentAbsenceId         = null;
    let refuseTarget             = null;

    const PAGE_SIZE = 10;
    let currentPageBeneficiary  = 1;
    let currentPageInterpreter  = 1;

    /* Switches between the beneficiary and interpreter tabs.
       Collapses any open card in the previous tab. */
    document.querySelectorAll('#validationTabs .nav-link').forEach(btn => {
        btn.addEventListener('click', function () {
            document.querySelectorAll('#validationTabs .nav-link').forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            document.querySelectorAll('.tab-panel').forEach(p => p.classList.add('d-none'));
            document.getElementById(`panel-${this.dataset.tab}`).classList.remove('d-none');

            document.getElementById('pageTitle').textContent = this.dataset.tab === 'beneficiaries'
                ? 'Validations des demandes de rendez-vous'
                : 'Validations des indisponibilités';

            if (currentBeneficiaryCard) { collapseCard(currentBeneficiaryCard, '.request-card-body', '.request-chevron'); currentBeneficiaryCard = null; }
            if (currentAbsenceCard)     { collapseCard(currentAbsenceCard, '.absence-card-body', '.absence-chevron'); currentAbsenceCard = null; }
        });
    });

    /* Expands a card by showing its body and rotating its chevron. */
    function expandCard(card, bodySelector, chevronSelector) {
        const body = card.querySelector(bodySelector);
        body.classList.remove('d-none');
        body.getBoundingClientRect();
        body.classList.add('expanded');
        card.querySelector(chevronSelector).style.transform = 'rotate(180deg)';
    }

    /* Collapses a card with a smooth transition. */
    function collapseCard(card, bodySelector, chevronSelector) {
        const body = card.querySelector(bodySelector);
        body.classList.remove('expanded');
        card.querySelector(chevronSelector).style.transform = 'rotate(0deg)';
        setTimeout(() => body.classList.add('d-none'), 350);
    }

    /* Returns whether a card is currently expanded. */
    function isExpanded(card, bodySelector) {
        return card.querySelector(bodySelector).classList.contains('expanded');
    }

    /* Handles click on a beneficiary request card header.
       Expands the clicked card and collapses the previously open one. */
    document.querySelectorAll('.request-card-header').forEach(header => {
        header.addEventListener('click', function () {
            const card = this.closest('.request-card');
            const open = isExpanded(card, '.request-card-body');

            if (currentBeneficiaryCard && currentBeneficiaryCard !== card) {
                collapseCard(currentBeneficiaryCard, '.request-card-body', '.request-chevron');
            }

            if (open) {
                collapseCard(card, '.request-card-body', '.request-chevron');
                currentBeneficiaryCard = null;
            } else {
                expandCard(card, '.request-card-body', '.request-chevron');
                currentBeneficiaryCard  = card;
                currentBeneficiaryId    = card.dataset.id;
                selectedInterpreterId   = null;

                showInterpretersState(card, 'loading');
                card.querySelector('.interpreters-error').classList.add('d-none');
                initCalendar(card, 'b', '.card-calendar');
                loadInterpreters(card);
            }
        });
    });

    /* Handles click on an interpreter absence card header.
       Expands the clicked card and collapses the previously open one. */
    document.querySelectorAll('.absence-card-header').forEach(header => {
        header.addEventListener('click', function () {
            const card = this.closest('.absence-card');
            const open = isExpanded(card, '.absence-card-body');

            if (currentAbsenceCard && currentAbsenceCard !== card) {
                collapseCard(currentAbsenceCard, '.absence-card-body', '.absence-chevron');
            }

            if (open) {
                collapseCard(card, '.absence-card-body', '.absence-chevron');
                currentAbsenceCard = null;
            } else {
                expandCard(card, '.absence-card-body', '.absence-chevron');
                currentAbsenceCard = card;
                currentAbsenceId   = card.dataset.id;
                initAbsenceCalendar(card);
            }
        });
    });

    /* Initializes or reloads a FullCalendar day-view inside the given card.
       The pending appointment is shown as an orange event. */
    function initCalendar(card) {
        const key = `b-${card.dataset.id}`;
        if (calendarInstances[key]) {
            calendarInstances[key].gotoDate(card.dataset.date);
            calendarInstances[key].refetchEvents();
            return;
        }

        const cal = new FullCalendar.Calendar(card.querySelector('.card-calendar'), {
            locale: 'fr',
            initialView: 'timeGridDay',
            initialDate: card.dataset.date,
            headerToolbar: { left: 'prev', center: 'title', right: 'next' },
            slotMinTime: '08:00:00',
            slotMaxTime: '19:00:00',
            allDaySlot: false,
            height: 400,
            events: function (info, successCallback, failureCallback) {
                fetch(`/coordinatrice/validations/appointment/${card.dataset.id}/events`)
                    .then(r => r.json())
                    .then(data => successCallback(data))
                    .catch(() => failureCallback());
            },
            eventContent: arg => ({ html: `<div class="p-1 fw-bold small">⏳ ${arg.event.title}</div>` })
        });

        cal.render();
        calendarInstances[key] = cal;
    }

    /* Initializes a FullCalendar day-view for an absence card.
       Shows the absence as an orange event. Handles full-day and multi-day absences. */
    function initAbsenceCalendar(card) {
        const key = `i-${card.dataset.id}`;
        if (calendarInstances[key]) {
            calendarInstances[key].gotoDate(card.dataset.dateStart);
            calendarInstances[key].refetchEvents();
            return;
        }

        const isFullDay  = card.dataset.fullDay === 'true';
        const dateStart  = card.dataset.dateStart;

        const cal = new FullCalendar.Calendar(card.querySelector('.absence-calendar'), {
            locale: 'fr',
            initialView: 'timeGridDay',
            initialDate: dateStart,
            headerToolbar: { left: 'prev', center: 'title', right: 'next' },
            slotMinTime: '08:00:00',
            slotMaxTime: '19:00:00',
            allDaySlot: isFullDay,
            height: 400,
            events: function (info, successCallback, failureCallback) {
                fetch(`/coordinatrice/validations/absence/${card.dataset.id}/events`)
                    .then(r => r.json())
                    .then(data => successCallback(data))
                    .catch(() => failureCallback());
            },
            eventContent: arg => ({ html: `<div class="p-1 fw-bold small">⏳ ${arg.event.title}</div>` })
        });

        cal.render();
        calendarInstances[key] = cal;
    }

    function loadInterpreters(card) {
        fetch(`/coordinatrice/validations/appointment/${card.dataset.id}/interpreters`)
            .then(r => r.json())
            .then(data => renderInterpreters(card, data))
            .catch(() => showInterpretersState(card, 'empty'));
    }

    function renderInterpreters(card, interpreters) {
        if (!interpreters || interpreters.length === 0) { showInterpretersState(card, 'empty'); return; }
        const tbody = card.querySelector('.interpreters-tbody');
        tbody.innerHTML = '';
        interpreters.forEach(interp => {
            const tr = document.createElement('tr');
            tr.style.cursor = 'pointer';
            const fullName = `${interp.lastName} ${interp.firstName}`;
            tr.innerHTML = `
            <td><input class="form-check-input interpreter-radio" type="radio" name="interpreterSelect-${card.dataset.id}" value="${interp.numInterpreter}"/></td>
            <td>${fullName}</td>
            <td>${interp.professionalSkills || '—'}</td>
            <td>${interp.academicSkills || '—'}</td>
        `;
            tr.addEventListener('click', function () {
                this.querySelector('.interpreter-radio').checked = true;
                selectedInterpreterId = interp.numInterpreter;
                tbody.querySelectorAll('tr').forEach(r => r.classList.remove('table-active'));
                this.classList.add('table-active');
                card.querySelector('.interpreters-error').classList.add('d-none');
            });
            tbody.appendChild(tr);
        });
        showInterpretersState(card, 'list');
    }

    function showInterpretersState(card, state) {
        card.querySelector('.interpreters-loading').classList.toggle('d-none', state !== 'loading');
        card.querySelector('.interpreters-empty').classList.toggle('d-none',  state !== 'empty');
        card.querySelector('.interpreters-list').classList.toggle('d-none',   state !== 'list');
    }

    document.querySelectorAll('.btn-accept').forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const card = this.closest('.request-card');
            if (!selectedInterpreterId) {
                card.querySelector('.interpreters-error').classList.remove('d-none');
                return;
            }
            fetch(`/coordinatrice/validations/appointment/${card.dataset.id}/accept?numInterpreter=${selectedInterpreterId}`, {
                method: 'POST'
            })
                .then(r => r.text())
                .then(result => {
                    if (result === 'ok') {
                        removeCard(card, 'beneficiary');
                    } else {
                        alert('Une erreur est survenue lors de l\'acceptation.');
                    }
                })
                .catch(err => console.error('Error accepting appointment:', err));
        });
    });

    document.querySelectorAll('.btn-accept-absence').forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const card = this.closest('.absence-card');
            fetch(`/coordinatrice/validations/absence/${card.dataset.id}/accept`, {
                method: 'POST'
            })
                .then(r => r.text())
                .then(result => {
                    if (result === 'ok') {
                        removeCard(card, 'interpreter');
                    } else {
                        alert('Une erreur est survenue lors de l\'acceptation.');
                    }
                })
                .catch(err => console.error('Error accepting absence:', err));
        });
    });

    /* Opens the shared refuse modal for a beneficiary request. */
    document.querySelectorAll('.btn-refuse').forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const card = this.closest('.request-card');
            refuseTarget = { card, type: 'beneficiary' };
            document.getElementById('refuseName').textContent       = card.dataset.beneficiary;
            document.getElementById('refuseSubMessage').textContent = 'Le bénéficiaire sera notifié du refus.';
            new bootstrap.Modal(document.getElementById('modalRefuse')).show();
        });
    });

    /* Opens the shared refuse modal for an interpreter absence. */
    document.querySelectorAll('.btn-refuse-absence').forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const card = this.closest('.absence-card');
            refuseTarget = { card, type: 'interpreter' };
            document.getElementById('refuseName').textContent       = card.dataset.interpreter;
            document.getElementById('refuseSubMessage').textContent = 'L\'interprète sera notifié du refus.';
            new bootstrap.Modal(document.getElementById('modalRefuse')).show();
        });
    });

    /* Confirms the refusal and removes the appropriate card. */
    document.getElementById('btnConfirmRefuse').addEventListener('click', function () {
        bootstrap.Modal.getInstance(document.getElementById('modalRefuse')).hide();
        if (!refuseTarget) return;

        const { card, type } = refuseTarget;
        const url = type === 'beneficiary'
            ? `/coordinatrice/validations/appointment/${card.dataset.id}/refuse`
            : `/coordinatrice/validations/absence/${card.dataset.id}/refuse`;

        fetch(url, { method: 'POST' })
            .then(r => r.text())
            .then(result => {
                if (result === 'ok') {
                    removeCard(card, type);
                } else {
                    alert('Une erreur est survenue lors du refus.');
                }
            })
            .catch(err => console.error('Error refusing request:', err));

        refuseTarget = null;
    });

    /* Removes a card from the DOM, destroys its calendar instance,
       resets state, shows empty message if no cards remain, and updates pagination. */
    function removeCard(card, type) {
        const isBeneficiary = type === 'beneficiary';
        const prefix        = isBeneficiary ? 'b' : 'i';
        const key           = `${prefix}-${card.dataset.id}`;

        if (calendarInstances[key]) { calendarInstances[key].destroy(); delete calendarInstances[key]; }

        card.remove();

        if (isBeneficiary) {
            currentBeneficiaryCard = null;
            currentBeneficiaryId   = null;
            selectedInterpreterId  = null;
            if (!document.querySelectorAll('.request-card').length)
                document.getElementById('emptyBeneficiaries').classList.remove('d-none');
            const visible    = getVisibleCards('.request-card');
            const totalPages = Math.ceil(visible.length / PAGE_SIZE);
            if (currentPageBeneficiary > totalPages) currentPageBeneficiary = Math.max(1, totalPages);
            updatePagination('beneficiary');
        } else {
            currentAbsenceCard = null;
            currentAbsenceId   = null;
            if (!document.querySelectorAll('.absence-card').length)
                document.getElementById('emptyInterpreters').classList.remove('d-none');
            const visible    = getVisibleCards('.absence-card');
            const totalPages = Math.ceil(visible.length / PAGE_SIZE);
            if (currentPageInterpreter > totalPages) currentPageInterpreter = Math.max(1, totalPages);
            updatePagination('interpreter');
        }
    }

    /* Filters beneficiary cards in real time. */
    document.getElementById('searchBeneficiary').addEventListener('input', function () {
        filterCards(this.value, '.request-card', 'beneficiary', 'emptySearchBeneficiary');
        currentPageBeneficiary = 1;
        updatePagination('beneficiary');
    });

    /* Filters interpreter cards in real time. */
    document.getElementById('searchInterpreter').addEventListener('input', function () {
        filterCards(this.value, '.absence-card', 'interpreter', 'emptySearchInterpreter');
        currentPageInterpreter = 1;
        updatePagination('interpreter');
    });

    /* Generic card filter by name. */
    function filterCards(query, cardSelector, nameKey, emptyId) {
        const q     = query.trim().toLowerCase();
        const cards = document.querySelectorAll(cardSelector);
        let visible = 0;

        cards.forEach(card => {
            const name = (card.dataset[nameKey === 'beneficiary' ? 'beneficiary' : 'interpreter'] || '').toLowerCase();
            if (name.includes(q)) { card.classList.remove('d-none'); visible++; }
            else {
                card.classList.add('d-none');
                const bodySelector    = nameKey === 'beneficiary' ? '.request-card-body' : '.absence-card-body';
                const chevronSelector = nameKey === 'beneficiary' ? '.request-chevron'    : '.absence-chevron';
                if (isExpanded(card, bodySelector)) collapseCard(card, bodySelector, chevronSelector);
            }
        });

        document.getElementById(emptyId).classList.toggle('d-none', visible > 0 || q === '');
    }

    function getVisibleCards(selector) {
        return Array.from(document.querySelectorAll(`${selector}:not(.d-none)`));
    }

    /* Renders pagination for a given tab ('beneficiary' or 'interpreter'). */
    function updatePagination(tab) {
        const isBeneficiary = tab === 'beneficiary';
        const cardSelector  = isBeneficiary ? '.request-card' : '.absence-card';
        const navId         = isBeneficiary ? 'paginationNavBeneficiary' : 'paginationNavInterpreter';
        const ulId          = isBeneficiary ? 'paginationBeneficiary'    : 'paginationInterpreter';
        let   currentPage   = isBeneficiary ? currentPageBeneficiary     : currentPageInterpreter;

        const cards      = getVisibleCards(cardSelector);
        const totalPages = Math.ceil(cards.length / PAGE_SIZE);

        cards.forEach((card, idx) => {
            const page = Math.floor(idx / PAGE_SIZE) + 1;
            card.style.display = page === currentPage ? '' : 'none';
        });

        const nav = document.getElementById(navId);
        if (totalPages <= 1) { nav.classList.add('d-none'); return; }
        nav.classList.remove('d-none');

        const ul = document.getElementById(ulId);
        ul.innerHTML = '';

        const prevLi = document.createElement('li');
        prevLi.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
        prevLi.innerHTML = `<a class="page-link" href="#">&laquo;</a>`;
        prevLi.addEventListener('click', e => {
            e.preventDefault();
            if (currentPage > 1) {
                isBeneficiary ? currentPageBeneficiary-- : currentPageInterpreter--;
                updatePagination(tab);
            }
        });
        ul.appendChild(prevLi);

        for (let i = 1; i <= totalPages; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
            li.addEventListener('click', e => {
                e.preventDefault();
                isBeneficiary ? (currentPageBeneficiary = i) : (currentPageInterpreter = i);
                updatePagination(tab);
            });
            ul.appendChild(li);
        }

        const nextLi = document.createElement('li');
        nextLi.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
        nextLi.innerHTML = `<a class="page-link" href="#">&raquo;</a>`;
        nextLi.addEventListener('click', e => {
            e.preventDefault();
            if (currentPage < totalPages) {
                isBeneficiary ? currentPageBeneficiary++ : currentPageInterpreter++;
                updatePagination(tab);
            }
        });
        ul.appendChild(nextLi);
    }

    updatePagination('beneficiary');
    updatePagination('interpreter');

});