document.addEventListener('DOMContentLoaded', function () {

    /* Handles tab navigation: on click, removes 'active' from all tabs and sections,
       then activates the clicked tab and its corresponding section. */
    document.querySelectorAll('#gestionTabs .nav-link').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelectorAll('#gestionTabs .nav-link').forEach(l => l.classList.remove('active'));
            document.querySelectorAll('.tab-section').forEach(s => s.classList.remove('active'));
            this.classList.add('active');
            document.getElementById('tab-' + this.dataset.tab).classList.add('active');
        });
    });

    /* Pre-fills the edit establishment modal with the data from the clicked row. */
    document.getElementById('modalEditEtab').addEventListener('show.bs.modal', function (e) {
        const btn = e.relatedTarget;
        document.getElementById('editEtabId').value    = btn.dataset.id;
        document.getElementById('editEtabNom').value   = btn.dataset.name;
        document.getElementById('editEtabRue').value   = btn.dataset.rue;
        document.getElementById('editEtabCp').value    = btn.dataset.cp;
        document.getElementById('editEtabVille').value = btn.dataset.ville;
        document.getElementById('editEtabIdAddress').value = btn.dataset.idAddress;
        document.getElementById('editEtabNumTel').value = btn.dataset.tel;
        const selectRef = document.getElementById('editEtabRef');
        selectRef.value = btn.dataset.referent || '';
    });

    /* Pre-fills the edit referent modal with the data from the clicked row. */
    document.getElementById('modalEditRef').addEventListener('show.bs.modal', function (e) {
        const btn = e.relatedTarget;
        document.getElementById('editRefId').value     = btn.dataset.id;
        document.getElementById('editRefNom').value    = btn.dataset.nom;
        document.getElementById('editRefPrenom').value = btn.dataset.prenom;
        document.getElementById('editRefTel').value    = btn.dataset.tel;
        document.getElementById('editRefEmail').value  = btn.dataset.email;
        const selectEtab = document.getElementById('editRefEtab');
        selectEtab.value = btn.dataset.etab || '';
    });

    /* Stores the referent id in the delete modal when it opens. */
    document.getElementById('modalDeleteRef').addEventListener('show.bs.modal', function (e) {
        document.getElementById('deleteRefId').value = e.relatedTarget.dataset.id;
    });

    // Activation of the tab according to the parameter? tab= in the URL
    const params = new URLSearchParams(window.location.search);
    const tab = params.get('tab');
    if (tab) {
        document.querySelectorAll('#gestionTabs .nav-link').forEach(l => l.classList.remove('active'));
        document.querySelectorAll('.tab-section').forEach(s => s.classList.remove('active'));
        const link = document.querySelector(`#gestionTabs .nav-link[data-tab="${tab}"]`);
        const section = document.getElementById('tab-' + tab);
        if (link) link.classList.add('active');
        if (section) section.classList.add('active');
    }

    /* Generic field validator: shows/removes a Bootstrap invalid-feedback message under the input. */
    function validateRequiredField(input, message) {
        let feedback = input.nextElementSibling;
        if (!feedback || !feedback.classList.contains('invalid-feedback')) {
            feedback = document.createElement('div');
            feedback.classList.add('invalid-feedback');
            input.insertAdjacentElement('afterend', feedback);
        }

        if (!input.value.trim()) {
            input.classList.add('is-invalid');
            feedback.textContent = message;
            feedback.style.display = 'block';
            return false;
        }

        input.classList.remove('is-invalid');
        feedback.style.display = 'none';
        return true;
    }

    /* Validates an email field: checks both presence and basic format. */
    function validateEmailField(input) {
        let feedback = input.nextElementSibling;
        if (!feedback || !feedback.classList.contains('invalid-feedback')) {
            feedback = document.createElement('div');
            feedback.classList.add('invalid-feedback');
            input.insertAdjacentElement('afterend', feedback);
        }

        const value = input.value.trim();
        if (!value) {
            input.classList.add('is-invalid');
            feedback.textContent = 'L\'email est obligatoire.';
            feedback.style.display = 'block';
            return false;
        }
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
            input.classList.add('is-invalid');
            feedback.textContent = 'Adresse e-mail invalide.';
            feedback.style.display = 'block';
            return false;
        }

        input.classList.remove('is-invalid');
        feedback.style.display = 'none';
        return true;
    }

    /* Clears all is-invalid states and hidden feedback messages for a list of inputs. */
    function clearFieldErrors(inputs) {
        inputs.forEach(input => {
            input.classList.remove('is-invalid');
            const feedback = input.nextElementSibling;
            if (feedback && feedback.classList.contains('invalid-feedback')) {
                feedback.style.display = 'none';
            }
        });
    }

    /* Validates the "Ajouter un établissement" form before submission. */
    const formAjoutEtab = document.querySelector('#modalAjoutEtab form');
    formAjoutEtab.addEventListener('submit', function (e) {
        const fields = [
            { el: document.getElementById('ajoutEtabNom'),   msg: 'Le nom de l\'établissement est obligatoire.' },
            { el: document.getElementById('ajoutEtabRue'),   msg: 'La rue et le numéro sont obligatoires.' },
            { el: document.getElementById('ajoutEtabCp'),    msg: 'Le code postal est obligatoire.' },
            { el: document.getElementById('ajoutEtabVille'), msg: 'La ville est obligatoire.' },
            { el: document.getElementById('ajoutEtabNumTel'),msg: 'Le numéro de téléphone est obligatoire.' }
        ];

        let valid = true;
        fields.forEach(f => {
            if (!validateRequiredField(f.el, f.msg)) valid = false;
        });

        if (!valid) e.preventDefault();
    });

    /* Validates the "Modifier l'établissement" form before submission. */
    const formEditEtab = document.querySelector('#modalEditEtab form');
    formEditEtab.addEventListener('submit', function (e) {
        const fields = [
            { el: document.getElementById('editEtabNom'),   msg: 'Le nom de l\'établissement est obligatoire.' },
            { el: document.getElementById('editEtabRue'),   msg: 'La rue et le numéro sont obligatoires.' },
            { el: document.getElementById('editEtabCp'),    msg: 'Le code postal est obligatoire.' },
            { el: document.getElementById('editEtabVille'), msg: 'La ville est obligatoire.' },
            { el: document.getElementById('editEtabNumTel'),msg: 'Le numéro de téléphone est obligatoire.' }
        ];

        let valid = true;
        fields.forEach(f => {
            if (!validateRequiredField(f.el, f.msg)) valid = false;
        });

        if (!valid) e.preventDefault();
    });

    /* Validates the "Ajouter un référent" form before submission. */
    const formAjoutRef = document.querySelector('#modalAjoutRef form');
    formAjoutRef.addEventListener('submit', function (e) {
        const nom    = document.getElementById('ajoutRefNom');
        const prenom = document.getElementById('ajoutRefPrenom');
        const tel    = document.getElementById('ajoutRefTel');
        const email  = document.getElementById('ajoutRefEmail');

        let valid = true;
        if (!validateRequiredField(nom, 'Le nom est obligatoire.')) valid = false;
        if (!validateRequiredField(prenom, 'Le prénom est obligatoire.')) valid = false;
        if (!validateRequiredField(tel, 'Le téléphone est obligatoire.')) valid = false;
        if (!validateEmailField(email)) valid = false;

        if (!valid) e.preventDefault();
    });

    /* Validates the "Modifier le référent" form before submission. */
    const formEditRef = document.querySelector('#modalEditRef form');
    formEditRef.addEventListener('submit', function (e) {
        const nom    = document.getElementById('editRefNom');
        const prenom = document.getElementById('editRefPrenom');
        const tel    = document.getElementById('editRefTel');
        const email  = document.getElementById('editRefEmail');

        let valid = true;
        if (!validateRequiredField(nom, 'Le nom est obligatoire.')) valid = false;
        if (!validateRequiredField(prenom, 'Le prénom est obligatoire.')) valid = false;
        if (!validateRequiredField(tel, 'Le téléphone est obligatoire.')) valid = false;
        if (!validateEmailField(email)) valid = false;

        if (!valid) e.preventDefault();
    });

    /* Clears validation errors on each modal close, so the next time it opens it starts clean. */
    ['modalAjoutEtab', 'modalEditEtab', 'modalAjoutRef', 'modalEditRef'].forEach(modalId => {
        document.getElementById(modalId).addEventListener('hidden.bs.modal', function () {
            clearFieldErrors(this.querySelectorAll('.form-control'));
        });
    });
});
