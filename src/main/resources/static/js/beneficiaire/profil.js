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
and updates the edit button color to indicate the current state. */
function toggleEdit(id, btn) {
    const input = document.getElementById(id);
    const isReadonly = input.hasAttribute('readonly');
    if (isReadonly) {
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
        passwordError.textContent = 'Veuillez respecter tous les critères du mot de passe.';
        passwordError.style.display = 'block';
        return;
    }

    if (newPassword !== confirmPassword) {
        confirmError.style.display = 'block';
        return;
    }
    document.getElementById('formPassword').submit();
}

document.addEventListener('DOMContentLoaded', function () {

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

    /* Resets the password modal fields and criteria on close. */
    document.getElementById('modalPassword').addEventListener('hidden.bs.modal', function () {
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

        const oldPassword = document.querySelector('#formPassword [name="oldPassword"]');
        if (oldPassword) oldPassword.value = '';
    });

});