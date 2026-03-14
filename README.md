# M1 Software Architecture - Microservices Project

Projet d'architecture microservices avec authentification, autorisation et administration des utilisateurs.

## Démarrage rapide

```bash
# Démarrer tous les services
docker-compose up -d

# Arrêter les services
docker-compose down

# Réinitialiser la base de données (supprime les données)
docker-compose down -v
docker-compose up -d
```

L'application est accessible via des requette sur `http://localhost:8080`

---

## Conteneurs

| Conteneur      | Description                                      | Port externe |
|----------------|--------------------------------------------------|--------------|
| `nginx`        | Reverse proxy, routage et gateway d'auth         | 8080         |
| `auth`         | Service d'authentification et d'autorisation     | -            |
| `admin`        | Service d'administration des utilisateurs        | -            |
| `serv_a`       | Service métier A (protégé)                       | -            |
| `serv_b`       | Service métier B (protégé)                       | -            |
| `notification` | Service d'envoi d'emails via RabbitMQ            | -            |
| `db`           | PostgreSQL 16                                    | 5433         |
| `rabbitmq`     | Message broker                                   | 5672, 15672  |
| `mailhog`      | Serveur mail de test (interface web)             | 8025         |

---

## Compte Admin par défaut

| Email            | Mot de passe  |
|------------------|---------------|
| `admin@m1.local` | `Admin@123!`  |

---

## Endpoints

### Routes publiques (sans authentification)

#### POST `/register`
Créer un nouveau compte utilisateur.

**Body:**
```json
{
  "email": "user@example.com",
  "password": "MonMotDePasse@123"
}
```

**Réponses:**
| Code | Description |
|------|-------------|
| 200  | Compte créé (email de validation envoyé) |
| 400  | Données invalides (email/password manquant, format email invalide, password trop faible) |
| 409  | Email déjà utilisé |

**Critères du mot de passe:**
- Minimum 8 caractères
- Au moins 1 majuscule
- Au moins 1 minuscule
- Au moins 1 chiffre
- Au moins 1 caractère spécial (`!@#$%^&*()_+-=[]{};\':\",./<>?`)

---

#### POST `/login`
Se connecter et obtenir un token de session.

**Body:**
```json
{
  "email": "user@example.com",
  "password": "MonMotDePasse@123"
}
```

**Réponse succès (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
}
```

**Réponses:**
| Code | Description |
|------|-------------|
| 200  | Connexion réussie, token retourné |
| 401  | Mauvais email ou mot de passe |
| 403  | Email non vérifié |

---

#### POST `/logout`
Se déconnecter (invalide le token de session).

**Body:**
```json
{
  "token": "votre-token-de-session"
}
```

**Réponses:**
| Code | Description |
|------|-------------|
| 200  | Déconnexion réussie |
| 401  | Token invalide |

---

#### GET `/validate_email?token=<token>`
Valider l'email après inscription.

**Réponses:**
| Code | Description |
|------|-------------|
| 200  | Email validé |
| 401  | Token invalide ou expiré |
| 409  | Email déjà vérifié |

---

### Routes protégées (Services A et B)

Ces routes nécessitent un header `Authorization: Bearer <token>` et que l'utilisateur ait accès au service concerné.

#### GET/POST `/a/*`
Accès au Service A.

#### GET/POST `/b/*`
Accès au Service B.

**Réponses:**
| Code | Description |
|------|-------------|
| 200  | Accès autorisé |
| 403  | Token manquant, invalide, ou accès non autorisé au service |

---

### Routes d'administration

Toutes les routes admin nécessitent un header `Authorization: Bearer <token>` avec un compte admin ou modo.

#### POST `/admin/addUser`
Créer un nouvel utilisateur (admin ou modo).

**Header:** `Authorization: Bearer <token>`

**Body:**
```json
{
  "email": "newuser@example.com",
  "password": "Password@123",
  "role": "user"
}
```

**Rôles possibles:** `user`, `modo`

**Permissions:**
| Appelant | Peut créer |
|----------|------------|
| Admin    | user, modo |
| Modo     | user       |

**Réponses:**
| Code | Description |
|------|-------------|
| 200  | Utilisateur créé (email de validation envoyé) |
| 400  | Données invalides |
| 401  | Token invalide ou rôle insuffisant |
| 403  | Opération interdite (ex: créer un admin, modo crée un modo) |
| 409  | Email déjà utilisé |

**Cas limites gérés:**
- Impossible de créer un compte admin
- Un modo ne peut pas créer un autre modo
- Validation du format email
- Validation de la force du mot de passe

---

#### DELETE `/admin/deleteUser`
Supprimer un utilisateur.

**Header:** `Authorization: Bearer <token>`

**Body:**
```json
{
  "email": "user@example.com"
}
```

**Permissions:**
| Appelant | Peut supprimer |
|----------|----------------|
| Admin    | user, modo     |
| Modo     | user           |

**Réponses:**
| Code | Description |
|------|-------------|
| 200  | Utilisateur supprimé |
| 400  | Email manquant |
| 401  | Token invalide ou rôle insuffisant |
| 403  | Opération interdite |
| 404  | Utilisateur non trouvé |

**Cas limites gérés:**
- Impossible de supprimer l'admin
- Un modo ne peut pas supprimer un autre modo
- Un modo ne peut pas supprimer l'admin
- Suppression en cascade (credentials, tokens, etc.)

---

#### PUT `/admin/modifyRole`
Modifier le rôle d'un utilisateur (admin uniquement).

**Header:** `Authorization: Bearer <token>`

**Body:**
```json
{
  "email": "user@example.com",
  "newRole": "modo"
}
```

**Rôles possibles:** `user`, `modo`

**Réponses:**
| Code | Description |
|------|-------------|
| 200  | Rôle modifié |
| 400  | Données invalides ou rôle inexistant |
| 401  | Token invalide ou n'est pas admin |
| 403  | Opération interdite |
| 404  | Utilisateur non trouvé |

**Cas limites gérés:**
- Seul l'admin peut modifier les rôles
- Impossible de promouvoir quelqu'un en admin
- Impossible de modifier le rôle de l'admin

---

## Hiérarchie des rôles

```
Admin (unique)
  └── Peut tout faire sauf créer un autre admin
  └── Peut créer/supprimer des users et modos
  └── Peut modifier les rôles

Modo
  └── Peut créer des users
  └── Peut supprimer des users (pas modo/admin)
  └── Ne peut pas modifier les rôles

User
  └── Accès aux services A/B selon ses autorisations
```

---

## Interfaces de monitoring

| Service   | URL                         | Description              |
|-----------|-----------------------------|--------------------------|
| MailHog   | http://localhost:8025       | Emails de test           |
| RabbitMQ  | http://localhost:15672      | Management (guest/guest) |
| PostgreSQL| localhost:5433              | Base de données          |

---

## Base de données

### Tables principales

- `role` - Rôles (user, admin, modo)
- `identity` - Utilisateurs
- `credential` - Mots de passe hashés (bcrypt)
- `session_token` - Tokens de session
- `validation_token` - Tokens de validation email
- `authority_service` - Services disponibles (A, B)
- `authority_user_service` - Droits d'accès utilisateur/service

---

## Exemples avec cURL (Windows)

```bash
# Login admin
curl -X POST http://localhost:8080/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin@m1.local\",\"password\":\"Admin@123!\"}"

# Créer un utilisateur
curl -X POST http://localhost:8080/admin/addUser ^
  -H "Authorization: Bearer <TOKEN>" ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\",\"password\":\"Test@123!\",\"role\":\"user\"}"

# Modifier un rôle
curl -X PUT http://localhost:8080/admin/modifyRole ^
  -H "Authorization: Bearer <TOKEN>" ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\",\"newRole\":\"modo\"}"

# Supprimer un utilisateur
curl -X DELETE http://localhost:8080/admin/deleteUser ^
  -H "Authorization: Bearer <TOKEN>" ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\"}"
```
