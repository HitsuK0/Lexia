const allMetierSkills   = ['Transcription', 'Translittération', 'Translation'];
const allAcademicSkills = ['Histoire', 'Mathématiques', 'Economie', 'Anglais', 'Sciences', 'Français', 'Géographie'];

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
    }
}

/* Validates the new password and its confirmation before submission,
otherwise displays the corresponding errors. */
function savePassword() {
    const newPassword     = document.getElementById('inputNewPassword').value;
    const confirmPassword = document.getElementById('inputConfirmPassword').value;
    const passwordError   = document.getElementById('passwordError');
    const confirmError    = document.getElementById('confirmError');

    passwordError.style.display = 'none';
    confirmError.style.display  = 'none';

    if (!isValidPassword(newPassword)) {
        passwordError.textContent = 'Veuillez respecter tous les critères du mot de passe.';
        passwordError.style.display = 'block';
        return;
    }
    if (newPassword !== confirmPassword) {
        confirmError.style.display = 'block';
        return;
    }
    bootstrap.Modal.getInstance(document.getElementById('modalResetPassword')).hide();
}

/* Shows the floating save button when any field has been modified. */
function onFieldChanged() {
    document.getElementById('btnSauvegarder').style.display = 'block';
}

/* Returns the list of skills currently displayed in a badge container. */
function getCurrentSkills(containerId) {
    return Array.from(document.querySelectorAll(`#${containerId} .badge-skill`))
        .map(b => b.childNodes[1]?.textContent?.trim() ?? '');
}

/* Rebuilds the options of a skill select based on skills not yet assigned. */
function refreshSelectOptions(selectId, allSkills, containerId) {
    const current = getCurrentSkills(containerId);
    const select = document.getElementById(selectId);
    const previousValue = select.value;
    select.innerHTML = '<option value="" disabled selected>Ajouter...</option>';
    allSkills.filter(s => !current.includes(s)).forEach(s => {
        const opt = document.createElement('option');
        opt.value = s;
        opt.textContent = s;
        select.appendChild(opt);
    });
    select.value = previousValue;
}

/* Adds a skill badge from the select into the badge container,
then refreshes the select options. */
function addSkill(selectId, containerId) {
    const select = document.getElementById(selectId);
    const value = select.value;
    if (!value) return;

    const allSkills = containerId === 'badgesMetiers' ? allMetierSkills : allAcademicSkills;

    const span = document.createElement('span');
    span.className = 'badge-skill';
    span.innerHTML = `<i class="bi bi-check-lg"></i> ${value}
        <button class="btn-badge-remove" onclick="removeSkill(this, '${containerId}', '${value}')" title="Retirer">
            <i class="bi bi-x"></i>
        </button>`;
    document.getElementById(containerId).appendChild(span);

    refreshSelectOptions(selectId, allSkills, containerId);
    select.value = '';
}

/* Removes a skill badge from the container and adds it back to the select options. */
function removeSkill(btn, containerId, skillName) {
    btn.closest('.badge-skill').remove();
    const selectId  = containerId === 'badgesMetiers' ? 'selectMetier' : 'selectAcademic';
    const allSkills = containerId === 'badgesMetiers' ? allMetierSkills : allAcademicSkills;
    refreshSelectOptions(selectId, allSkills, containerId);
}

const originalRole = document.getElementById('roleSelectDetail').value;

document.addEventListener('DOMContentLoaded', function () {

    /* Initializes the visibility of competences and interpreter reference sections
    on page load based on the role loaded from the server. */
    const isBeneficiary = document.getElementById('roleSelectDetail').value === 'BENEFICIARY';
    document.getElementById('sectionCompetences').style.display   = isBeneficiary ? 'none'  : 'block';
    document.getElementById('sectionInterpreteRef').style.display = isBeneficiary ? 'block' : 'none';

    /* Shows or hides competences/interpreter sections based on selected role.
    Also warns the coordinator that the user ID will be regenerated on save. */
    document.getElementById('roleSelectDetail').addEventListener('change', function () {
        const isBenef    = this.value === 'BENEFICIARY';
        const roleChanged = this.value !== originalRole;

        document.getElementById('sectionCompetences').style.display   = isBenef     ? 'none'  : 'block';
        document.getElementById('sectionInterpreteRef').style.display = isBenef     ? 'block' : 'none';
        document.getElementById('idRegenWarning').style.display        = roleChanged ? 'block' : 'none';
    });

    /* Shows the save button as soon as any field value changes. */
    document.querySelectorAll('input:not([type="radio"]), textarea, select').forEach(el => {
        el.addEventListener('input', onFieldChanged);
        el.addEventListener('change', onFieldChanged);
    });
    document.querySelectorAll('input[type="radio"]').forEach(el => {
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
            { id: 'crit-length',  test: val.length >= 8 },
            { id: 'crit-upper',   test: /[A-Z]/.test(val) },
            { id: 'crit-lower',   test: /[a-z]/.test(val) },
            { id: 'crit-number',  test: /[0-9]/.test(val) },
            { id: 'crit-special', test: /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(val) }
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
    document.getElementById('selectMetier').addEventListener('change', function () {
        addSkill('selectMetier', 'badgesMetiers');
    });

    document.getElementById('selectAcademic').addEventListener('change', function () {
        addSkill('selectAcademic', 'badgesAcademics');
    });
});