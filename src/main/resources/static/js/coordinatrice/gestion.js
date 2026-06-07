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

    /* Stores the professional skill id and designation in the delete modal when it opens. */
    document.getElementById('modalDeleteCompMetier').addEventListener('show.bs.modal', function (e) {
        const btn = e.relatedTarget;
        document.getElementById('deleteCompMetierId').value      = btn.dataset.id;
        document.getElementById('deleteCompMetierDesig').textContent = btn.dataset.designation;
    });

    /* Stores the academic skill id and designation in the delete modal when it opens. */
    document.getElementById('modalDeleteCompAcad').addEventListener('show.bs.modal', function (e) {
        const btn = e.relatedTarget;
        document.getElementById('deleteCompAcadId').value      = btn.dataset.id;
        document.getElementById('deleteCompAcadDesig').textContent = btn.dataset.designation;
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

});
