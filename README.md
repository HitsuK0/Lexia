# Lexia — Gestion des plannings d'interprétation

Application web (Spring Boot + Thymeleaf) développée pour le Centre Comprendre et Parler, une ASBL qui met à disposition des interprètes spécialisés pour des bénéficiaires sourds ou malentendants. Lexia remplace la gestion actuelle via Teams/Excel par un planning centralisé, avec suggestions d'assignation d'interprète et notifications automatiques.

Projet intégré [INDK0002-3] — Bloc 2 Informatique, HERS, année 2025-2026.

## Stack technique

- **Backend** : Java, Spring Boot / Spring MVC, Thymeleaf, Maven
- **Base de données** : Oracle Database
- **Frontend** : Bootstrap (+ Bootswatch), JavaScript, FullCalendar
- **Outils** : IntelliJ IDEA, Git/GitLab

## Installation

```bash
git clone <URL_DU_DÉPÔT>
cd Lexia
```

L'application tourne sur `http://localhost:8080`. Les comptes sont créés uniquement par la coordinatrice (mot de passe aléatoire à changer à la première connexion) — pas d'inscription libre.

## Structure

Packages Java (`src/main/java/be/hers/info/ProjetIntegree`) :

```
be.hers.info.ProjetIntegree
├── Controller
├── DAO
├── DTO
├── POJO
├── Services
└── ProjetIntegreeApplication.java   (point d'entrée Spring Boot)
```

Ressources (`src/main/resources`) :

```
resources
├── static
│   ├── css
│   │   ├── beneficiaire/
│   │   ├── coordinatrice/
│   │   ├── interprete/
│   │   ├── bootstrap.min.css
│   │   ├── bootstrap-icons.min.css
│   │   └── lexia.css
│   ├── fonts/
│   ├── images/
│   └── js
│       ├── beneficiaire/
│       ├── coordinatrice/
│       ├── interprete/
│       ├── bootstrap.bundle.min.js
│       └── fullcalendar.min.js
└── templates
    ├── beneficiaire/
    ├── common/
    ├── coordinatrice/
    ├── interprete/
    ├── Resa/
    └── login.html
```

## Rôles

Bénéficiaire, Interprète, Coordinatrice (admin), Résa (responsable adjoint·e en l'absence de la coordinatrice).

## Documentation complète

Cahier des charges, exigences détaillées et diagrammes UML (cas d'utilisation, classes, relationnel, activité) disponibles dans [`docs/diagrams`](https://claude.ai/chat/docs/diagrams) et dans le rapport complet du projet.

## Équipe

Aïnhoa Leroy Rodriguez, Chloé Wellinger, Jean Vatafu, Jean-François Nicolas, Loïs Rosman, Louis Halet, Quentin Vanderheyden — sous la supervision de I. Dony, C. Peeters et B. Burlion.