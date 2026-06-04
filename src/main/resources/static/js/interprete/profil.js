let currentSkillType = null;

/* Handles sidebar navigation: on click, removes 'active' from all links and sections,
then sets the clicked link and its corresponding section as active. */
document.querySelectorAll('.sidebar-link').forEach(link => {
    link.addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelectorAll('.sidebar-link').forEach(l => l.classList.remove('active'));
        document.querySelectorAll('.profil-section').forEach(s => s.classList.remove('active'));
        this.classList.add('active');
        document.getElementById('section-' + this.dataset.section).classList.add('active');
    });
});

/* Populates the skill select dropdown based on the modal type (metier or academic),
filtering out skills the interpreter already owns. */
document.getElementById('modalAddSkill').addEventListener('show.bs.modal', function (e) {
    currentSkillType = e.relatedTarget.dataset.type;
    const select = document.getElementById('selectCompetence');
    select.innerHTML = '<option value="" disabled selected>Choisissez une compétence</option>';

    const allSkills = currentSkillType === 'metier' ? allProfessionalSkills : allAcademicSkills;
    const ownedSkills = currentSkillType === 'metier' ? ownedProfSkills : ownedAcadSkills;
    const ownedIds = ownedSkills.map(s =>
        currentSkillType === 'metier' ? s.numProfessionalSkill : s.numAcademicSkill
    );

    allSkills
        .filter(s => !ownedIds.includes(
            currentSkillType === 'metier' ? s.numProfessionalSkill : s.numAcademicSkill
        ))
        .forEach(s => {
            const option = document.createElement('option');
            option.value = currentSkillType === 'metier' ? s.numProfessionalSkill : s.numAcademicSkill;
            option.textContent = s.designation;
            select.appendChild(option);
        });
});

/* Submits the correct hidden form with the selected skill number
depending on the current skill type (metier or academic). */
function submitAddSkill() {
    const select = document.getElementById('selectCompetence');
    if (!select.value) return;
    if (currentSkillType === 'metier') {
        document.getElementById('hiddenNumProfSkill').value = select.value;
        document.getElementById('formAddProfessionalSkill').submit();
    } else {
        document.getElementById('hiddenNumAcadSkill').value = select.value;
        document.getElementById('formAddAcademicSkill').submit();
    }
}

/* Allows you to check the email format before saving */
function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

/* Allows you to check the phone number format before saving */
function isValidPhone(phone) {
    return /^[0-9\s]{7,15}$/.test(phone);
}

/* Toggles a form field between readonly and editable mode,
and updates the edit button color to indicate the current state. */
function toggleEdit(id, btn) {
    const input = document.getElementById(id);
    const isReadonly = input.hasAttribute('readonly');
    if (isReadonly) {
        document.querySelectorAll('.btn-edit').forEach(otherBtn => {
            const otherId = otherBtn.getAttribute('onclick')?.match(/'([^']+)'/)?.[1];
            if (otherId && otherId !== id) {
                const otherInput = document.getElementById(otherId);
                if (otherInput && !otherInput.hasAttribute('readonly')) {
                    otherInput.setAttribute('readonly', true);
                    otherBtn.style.color = '';
                }
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

/* Verifies that the password contains at least 8 characters, one uppercase,
one lowercase, one number, and a special character.*/
function isValidPassword(password) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$/.test(password);
}

/* Validates the new password and its confirmation before closing the modal,
otherwise displays the corresponding errors. */
function savePassword() {
    const newPassword = document.getElementById('inputNewPassword').value;
    const confirmPassword = document.getElementById('inputConfirmPassword').value;
    const passwordError = document.getElementById('passwordError');
    const confirmError = document.getElementById('confirmError');

    passwordError.style.display = 'none';
    confirmError.style.display = 'none';

    if (!isValidPassword(newPassword)) {
        passwordError.innerHTML = 'Le mot de passe doit contenir au minimum :<br>' +
            '- 8 caractères<br>- 1 majuscule<br>- 1 chiffre<br>- 1 caractère spécial (!@#$%...)';
        passwordError.style.display = 'block';
        return;
    }

    if (newPassword !== confirmPassword) {
        confirmError.style.display = 'block';
        return;
    }
    document.getElementById('formPassword').submit();
}

/* Shows the save button as soon as any field value changes. */
function onFieldChanged() {
    document.getElementById('btnSauvegarder').style.display = 'block';
}

document.addEventListener('DOMContentLoaded', function () {

    const urlParams = new URLSearchParams(window.location.search);
    const sectionParam = urlParams.get('section');
    if (sectionParam) {
        document.querySelectorAll('.profil-section').forEach(s => s.classList.remove('active'));
        document.querySelectorAll('.sidebar-link').forEach(l => l.classList.remove('active'));

        const targetSection = document.getElementById('section-' + sectionParam);
        if (targetSection) targetSection.classList.add('active');

        const targetLink = document.querySelector(`.sidebar-link[data-section="${sectionParam}"]`);
        if (targetLink) targetLink.classList.add('active');

        const mobileItem = document.querySelector(`.dropdown-menu [data-section="${sectionParam}"]`);
        if (mobileItem) {
            document.getElementById('mobileSectionLabel').textContent = mobileItem.textContent;
        }
    }

    document.querySelectorAll('input:not([type="radio"]), textarea, select').forEach(el => {
        el.addEventListener('input', onFieldChanged);
        el.addEventListener('change', onFieldChanged);
    });

    document.querySelectorAll('.input-edit-group input, .input-edit-group textarea').forEach(input => {
        input.addEventListener('blur', function () {
            if (!this.hasAttribute('readonly')) {
                const btn = this.closest('.input-edit-group').querySelector('.btn-edit');
                if (btn) toggleEdit(this.id, btn);
            }
        });
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

    /* Validates the new password in real time as the user types,
       showing or hiding the format error message accordingly. */
    document.getElementById('inputNewPassword').addEventListener('input', function () {
        const passwordError = document.getElementById('passwordError');
        if (!isValidPassword(this.value)) {
            passwordError.innerHTML = 'Le mot de passe doit contenir au minimum :<br>' +
                '- 8 caractères<br>- 1 majuscule<br>- 1 chiffre<br>- 1 caractère spécial (!@#$%...)';
            passwordError.style.display = 'block';
        } else {
            passwordError.style.display = 'none';
        }
    });

    /* Checks in real time whether the confirmation password matches the new password,
       showing or hiding the mismatch error message accordingly. */
    document.getElementById('inputConfirmPassword').addEventListener('input', function () {
        const confirmError = document.getElementById('confirmError');
        const newPassword = document.getElementById('inputNewPassword').value;
        if (this.value !== newPassword) {
            confirmError.style.display = 'block';
        } else {
            confirmError.style.display = 'none';
        }
    });

    /* On mobile, handles section navigation via the Bootstrap dropdown. */
    document.querySelectorAll('.dropdown-menu [data-section]').forEach(item => {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelectorAll('.profil-section').forEach(s => s.classList.remove('active'));
            document.querySelectorAll('.sidebar-link').forEach(l => l.classList.remove('active'));
            document.getElementById('section-' + this.dataset.section).classList.add('active');
            document.getElementById('mobileSectionLabel').textContent = this.textContent;
        });
    });
});