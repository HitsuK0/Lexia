# Lexia — Gestion des plannings d'interprétation

> ## ⚠️ Branche `demo-lexia`
>
> Cette branche n'est **pas destinée à la production**. Elle existe uniquement pour permettre à quelqu'un qui découvre le projet de le lancer en une seule commande, sans rien installer d'autre que Docker, et de le tester immédiatement.
>
> Pour ça, `docker-compose.yml` contient les identifiants de connexion à la base de données **en clair**, directement dans le fichier versionné (`DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD` du service `app`, ainsi que `POSTGRES_USER`/`POSTGRES_PASSWORD` du service `postgres`). C'est une pratique volontairement simplifiée pour cette démo, et **jamais acceptable en production** : dans un vrai contexte de déploiement, ces secrets seraient injectés via un gestionnaire de secrets (Vault, AWS Secrets Manager, variables d'environnement du pipeline CI/CD, etc.), jamais committés en clair dans le dépôt.
>
> La branche `main` reste la référence : elle utilise un fichier `.env` local (gitignoré) pour la configuration, comme il se doit.
>
> ### Lancer la démo
>
> ```bash
> git checkout demo-lexia
> docker compose up --build -d
> ```
>
> Le `-d` (detached) lance les conteneurs en arrière-plan et rend la main tout de suite ; sans lui, la commande reste attachée aux logs. Pour suivre les logs de l'appli ensuite : `docker compose logs -f app`.
>
> - L'application est accessible sur `http://localhost:8080`.
> - Au premier démarrage, PostgreSQL exécute automatiquement les scripts de `docs/SQL/` (création du schéma + jeu de données de test) via `/docker-entrypoint-initdb.d`.
> - Identifiants de test disponibles une fois la base peuplée : voir `docs/SQL/BD_Script_Insertion.sql` (logins générés au format `C000x`/`I000x`/`B000x`, mots de passe en clair du script — hashés en BCrypt à l'insertion).
> - Pour repartir d'une base vierge : `docker compose down -v` (supprime le volume Postgres) puis `docker compose up --build`.

Application web (Spring Boot + Thymeleaf) développée pour le Centre Comprendre et Parler, une ASBL qui met à disposition des interprètes spécialisés pour des bénéficiaires sourds ou malentendants. Lexia remplace la gestion actuelle via Teams/Excel par un planning centralisé, avec suggestions d'assignation d'interprète et notifications automatiques.

Projet intégré [INDK0002-3] — Bloc 2 Informatique, HERS, année 2025-2026.

## Stack technique

- **Backend** : Java, Spring Boot / Spring MVC, Thymeleaf, Maven
- **Base de données** : PostgreSQL
- **Frontend** : Bootstrap (+ Bootswatch), JavaScript, FullCalendar
- **Outils** : IntelliJ IDEA, Git/GitLab, Docker

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