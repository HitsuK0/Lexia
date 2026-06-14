/* Allows you to check the email format before saving */
function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

/* Allows you to check the phone number format before saving */
function isValidPhone(phone) {
    return /^[0-9\s]{7,15}$/.test(phone);
}

/* Verifies that the password contains at least 8 characters, one uppercase,
one lowercase, one number, and a special character. */
function isValidPassword(password) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$/.test(password);
}

/* Toggles a form field between readonly and editable mode,
and updates the edit button color to indicate the current state.
Only one field can be edited at a time. */
function toggleEdit(id, btn) {
    const input = document.getElementById(id);
    const isReadonly = input.hasAttribute('readonly');

    if (isReadonly) {
        document.querySelectorAll('.input-edit-group input, .input-edit-group textarea').forEach(otherInput => {
            if (otherInput.id !== id && !otherInput.hasAttribute('readonly')) {
                otherInput.setAttribute('readonly', true);
                const otherBtn = otherInput.closest('.input-edit-group').querySelector('.btn-edit');
                if (otherBtn) otherBtn.style.color = '';
            }
        });

        input.removeAttribute('readonly');
        input.focus();
        btn.style.color = '#593196';

        /* Special case: editing the communication languages reveals checkboxes instead of free text. */
        if (id === 'inputLangues') {
            document.getElementById('langueCheckboxes').style.display = 'flex';
        }
    } else {
        if (id === 'inputEmail' && !isValidEmail(input.value)) {
            input.classList.add('is-invalid');
            document.getElementById('emailError').style.display = 'block';
            return;
        }
        if (id === 'inputTel' && !isValidPhone(input.value)) {
            input.classList.add('is-invalid');
            document.getElementById('phoneError').style.display = 'block';
            return;
        }
        document.getElementById('emailError').style.display = 'none';
        document.getElementById('phoneError').style.display = 'none';
        input.classList.remove('is-invalid');
        input.setAttribute('readonly', true);
        btn.style.color = '';

        if (id === 'inputLangues') {
            document.getElementById('langueCheckboxes').style.display = 'none';
        }
    }
}

/* Validates the new password and its confirmation before submission,
otherwise displays the corresponding errors. */
function savePassword() {
    const newPassword = document.getElementById('inputNewPassword').value;
    const confirmPassword = document.getElementById('inputConfirmPassword').value;
    const passwordError = document.getElementById('passwordError');
    const confirmError = document.getElementById('confirmError');

    passwordError.style.display = 'none';
    confirmError.style.display = 'none';

    if (!isValidPassword(newPassword)) {
        passwordError.textContent = 'Veuillez respecter tous les critères du mot de passe.';
        passwordError.style.display = 'block';
        return;
    }
    if (newPassword !== confirmPassword) {
        confirmError.style.display = 'block';
        return;
    }

    document.getElementById('inputNewPassword').closest('form').submit();
}

/* Shows the floating save button when any field has been modified. */
function onFieldChanged() {
    document.getElementById('btnSauvegarder').style.display = 'block';
}

/* Adds a professional or academic skill to the user via a dedicated POST request,
then updates the badge list visually on success. */
function addSkill(selectId, containerId) {
    const select = document.getElementById(selectId);
    const option = select.options[select.selectedIndex];
    if (!option || !option.value) return;

    const id = option.value;
    const designation = option.dataset.designation;
    const userId = document.body.dataset.userId;
    const isAcademic = selectId === 'selectAcademic';
    const endpoint = isAcademic ? 'addAcademicSkill' : 'addProfessionalSkill';

    const params = new URLSearchParams();
    params.append('skillId', id);

    fetch(`/coordinatrice/utilisateurs/interpreter/${userId}/${endpoint}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: params.toString()
    })
        .then(response => {
            if (!response.ok) throw new Error('Erreur lors de l\'ajout de la compétence');

            const span = document.createElement('span');
            span.className = 'badge-skill';
            span.innerHTML = `<i class="bi bi-check-lg"></i> ${designation}
                <button type="button" class="btn-badge-remove" onclick="removeSkill(this, '${containerId}', ${id})" title="Retirer">
                    <i class="bi bi-x"></i>
                </button>`;
            document.getElementById(containerId).appendChild(span);

            option.remove();
            select.value = '';
        })
        .catch(err => console.error('Error adding skill:', err));
}

/* Removes a professional or academic skill from the user via a dedicated POST request,
then removes the badge visually on success. */
function removeSkill(btn, containerId, skillId) {
    const userId = document.body.dataset.userId;
    const isAcademic = containerId === 'badgesAcademics';
    const endpoint = isAcademic ? 'deleteAcademicSkill' : 'deleteProfessionalSkill';

    const params = new URLSearchParams();
    params.append('skillId', skillId);

    fetch(`/coordinatrice/utilisateurs/interpreter/${userId}/${endpoint}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: params.toString()
    })
        .then(response => {
            if (!response.ok) throw new Error('Erreur lors de la suppression de la compétence');
            btn.closest('.badge-skill').remove();
        })
        .catch(err => console.error('Error removing skill:', err));
}

const roleSelectDetailEl = document.getElementById('roleSelectDetail');
const originalRole = roleSelectDetailEl ? roleSelectDetailEl.value : null;

document.addEventListener('DOMContentLoaded', function () {

    /* Warns the coordinator that the user's login will be regenerated on save
    if the selected role changes (only relevant for non-beneficiary users). */
    if (roleSelectDetailEl) {
        roleSelectDetailEl.addEventListener('change', function () {
            const roleChanged = this.value !== originalRole;
            const warning = document.getElementById('idRegenWarning');
            if (warning) warning.style.display = roleChanged ? 'block' : 'none';
        });
    }

    /* Shows the save button as soon as any field value changes. */
    document.querySelectorAll('#formProfile input:not([type="radio"]), #formProfile textarea, #formProfile select').forEach(el => {
        el.addEventListener('input', onFieldChanged);
        el.addEventListener('change', onFieldChanged);
    });
    document.querySelectorAll('#formProfile input[type="radio"]').forEach(el => {
        el.addEventListener('change', onFieldChanged);
    });

    /* Validates the email address in real time as the user types,
    showing or hiding the error message accordingly. */
    document.getElementById('inputEmail').addEventListener('input', function () {
        if (!isValidEmail(this.value)) {
            this.classList.add('is-invalid');
            document.getElementById('emailError').style.display = 'block';
        } else {
            this.classList.remove('is-invalid');
            document.getElementById('emailError').style.display = 'none';
        }
    });

    /* Validates the phone number in real time as the user types,
    showing or hiding the error message accordingly. */
    document.getElementById('inputTel').addEventListener('input', function () {
        if (!isValidPhone(this.value)) {
            this.classList.add('is-invalid');
            document.getElementById('phoneError').style.display = 'block';
        } else {
            this.classList.remove('is-invalid');
            document.getElementById('phoneError').style.display = 'none';
        }
    });

    /* Real-time password criteria check as the user types. */
    document.getElementById('inputNewPassword').addEventListener('input', function () {
        const val = this.value;
        const criteria = [
            {id: 'crit-length', test: val.length >= 8},
            {id: 'crit-upper', test: /[A-Z]/.test(val)},
            {id: 'crit-lower', test: /[a-z]/.test(val)},
            {id: 'crit-number', test: /[0-9]/.test(val)},
            {id: 'crit-special', test: /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(val)}
        ];
        criteria.forEach(c => {
            const el = document.getElementById(c.id);
            el.innerHTML = el.innerHTML.replace(
                /<i[^>]*><\/i>/,
                c.test
                    ? '<i class="bi bi-check-circle-fill text-success"></i>'
                    : '<i class="bi bi-x-circle-fill text-danger"></i>'
            );
        });
    });

    /* Checks in real time whether the confirmation password matches the new password. */
    document.getElementById('inputConfirmPassword').addEventListener('input', function () {
        const confirmError = document.getElementById('confirmError');
        if (this.value !== document.getElementById('inputNewPassword').value) {
            confirmError.style.display = 'block';
        } else {
            confirmError.style.display = 'none';
        }
    });

    /* Resets the password modal fields and criteria on close. */
    document.getElementById('modalResetPassword').addEventListener('hidden.bs.modal', function () {
        document.getElementById('inputNewPassword').value = '';
        document.getElementById('inputConfirmPassword').value = '';
        document.getElementById('passwordError').style.display = 'none';
        document.getElementById('confirmError').style.display = 'none';

        const criteres = ['crit-length', 'crit-upper', 'crit-lower', 'crit-number', 'crit-special'];
        const labels = [
            '8 characters minimum',
            '1 uppercase letter',
            '1 lowercase letter',
            '1 number',
            '1 special character (!@#$%...)'
        ];
        criteres.forEach((id, i) => {
            document.getElementById(id).innerHTML =
                `<i class="bi bi-x-circle-fill text-danger"></i> ${labels[i]}`;
        });
    });

    /* Automatically adds a skill badge when a skill is selected from the dropdown. */
    const selectMetier = document.getElementById('selectMetier');
    if (selectMetier) {
        selectMetier.addEventListener('change', function () {
            addSkill('selectMetier', 'badgesMetiers');
        });
    }

    const selectAcademic = document.getElementById('selectAcademic');
    if (selectAcademic) {
        selectAcademic.addEventListener('change', function () {
            addSkill('selectAcademic', 'badgesAcademics');
        });
    }

    /* Handles the main "Sauvegarder" button.
    Before submitting the main profile form, sends dedicated requests for:
    - the role change (if the role select exists and its value changed)
    - the referent interpreter assignment (if the select exists, for beneficiaries)
    Both requests run in parallel, then the main form is submitted regardless of their result
    (errors are logged to the console but do not block the profile save). */
    document.getElementById('btnSauvegarder').addEventListener('click', function (e) {
        e.preventDefault();

        const userId = document.body.dataset.userId;
        const requests = [];

        if (roleSelectDetailEl && roleSelectDetailEl.value !== originalRole) {
            const roleParams = new URLSearchParams();
            roleParams.append('newRole', roleSelectDetailEl.value);

            requests.push(
                fetch(`/coordinatrice/utilisateurs/interpreter/${userId}/role`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: roleParams.toString()
                }).catch(err => console.error('Error changing role:', err))
            );
        }

        const interpreterRefSelect = document.querySelector('select[name="numInterpreterReferent"]');
        if (interpreterRefSelect) {
            const refParams = new URLSearchParams();
            refParams.append('interpreterId', interpreterRefSelect.value);

            requests.push(
                fetch(`/coordinatrice/utilisateurs/beneficiary/${userId}/referenceInterpreter`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: refParams.toString()
                }).catch(err => console.error('Error setting referent interpreter:', err))
            );
        }

        if (requests.length > 0) {
            Promise.all(requests).then(() => {
                document.getElementById('formProfile').submit();
            });
        } else {
            document.getElementById('formProfile').submit();
        }
    });
});