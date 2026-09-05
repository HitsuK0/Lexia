# Lexia — Gestion des plannings d'interprétation

> ## ⚠️ Branche `demo-lexia`
>
> Cette branche n'est **pas destinée à la production**. Elle existe uniquement pour permettre à quelqu'un qui découvre le projet de le lancer en une seule commande, sans rien installer d'autre que Docker, et de le tester immédiatement.
>
> Pour ça, `docker-compose.yml` contient les identifiants de connexion à la base de données **en clair**, directement dans le fichier versionné (`DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD` du service `app`, ainsi que `POSTGRES_USER`/`POSTGRES_PASSWORD` du service `postgres`). C'est une pratique volontairement simplifiée pour cette démo, et **jamais acceptable en production** : dans un vrai contexte de déploiement, ces secrets seraient injectés via un gestionnaire de secrets (Vault, AWS Secrets Manager, variables d'environnement du pipeline CI/CD, etc.), jamais committés en clair dans le dépôt.
>
> La branche `main` reste la référence : elle utilise un fichier `.env` local (gitignoré) pour la configuration.
>
> ### Lancer la démo
>
> ```bash
> git switch demo-lexia
> docker compose up --build -d
> ```
>
> - L'application est accessible sur `http://localhost:8080`.
>
> ### Identifiants de test
>
>
> | Rôle | Login | Mot de passe |
> |---|---|---|
> | Coordinatrice (admin) | `C0001` | `motdepasse1` |
> | Résa | `C0002` | `motdepasse2` |
> | Résa | `C0003` | `motdepasse3` |
> | Résa | `C0004` | `motdepasse4` |
> | Résa | `C0005` | `motdepasse5` |
> | Interprète | `I0006` | `motdepasse1` |
> | Interprète | `I0007` | `motdepasse2` |
> | Interprète | `I0008` | `motdepasse3` |
> | Interprète | `I0009` | `motdepasse4` |
> | Interprète | `I0010` | `motdepasse5` |
> | Bénéficiaire | `B0011` | `motdepasse6` |
> | Bénéficiaire | `B0012` | `motdepasse7` |
> | Bénéficiaire | `B0013` | `motdepasse8` |
> | Bénéficiaire | `B0014` | `motdepasse9` |
> | Bénéficiaire | `B0015` | `motdepasse10` |

## Stack technique

- **Backend** : Java, Spring Boot / Spring MVC, Thymeleaf, Maven
- **Base de données** : PostgreSQL
- **Frontend** : Bootstrap (+ Bootswatch), JavaScript, FullCalendar
- **Outils** : IntelliJ IDEA, Git/GitLab, Docker

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