# Récap implémentation Mailing (branche pedro_work)

## Objectif
Mettre en place un système de notification e-mail conforme au TP mailing, sans dépendre de l’implémentation finale Auth (mock temporaire autorisé).

## Ce qui a été implémenté

### 1) Nouveau microservice Notification
- Service Spring Boot dédié à l’envoi d’e-mails de vérification.
- Fichiers principaux :
  - [notification/pom.xml](notification/pom.xml)
  - [notification/dockerfile](notification/dockerfile)
  - [notification/src/main/java/M1/S2/TPS/notification/NotificationApplication.java](notification/src/main/java/M1/S2/TPS/notification/NotificationApplication.java)

### 2) Messagerie RabbitMQ (exchange, queue, DLQ)
- Déclaration d’une topologie RabbitMQ côté Notification :
  - Exchange principal : `auth.events`
  - Queue de consommation : `notification.user-registered`
  - DLX : `auth.events.dlq`
  - DLQ : `notification.user-registered.dlq`
- Fichiers :
  - [notification/src/main/java/M1/S2/TPS/notification/config/MessagingProperties.java](notification/src/main/java/M1/S2/TPS/notification/config/MessagingProperties.java)
  - [notification/src/main/java/M1/S2/TPS/notification/config/NotificationConfig.java](notification/src/main/java/M1/S2/TPS/notification/config/NotificationConfig.java)

### 3) Contrat d’événement UserRegistered
- Modèle d’événement implémenté (type, eventId, occurredAt, data{userId,email,tokenId,tokenClear}).
- Fichier :
  - [notification/src/main/java/M1/S2/TPS/notification/model/UserRegisteredEvent.java](notification/src/main/java/M1/S2/TPS/notification/model/UserRegisteredEvent.java)

### 4) Consommation et envoi e-mail
- Consumer RabbitMQ sur `notification.user-registered`.
- Validation du payload reçu.
- Envoi SMTP via MailHog avec lien de vérification.
- Fichiers :
  - [notification/src/main/java/M1/S2/TPS/notification/messaging/UserRegisteredListener.java](notification/src/main/java/M1/S2/TPS/notification/messaging/UserRegisteredListener.java)
  - [notification/src/main/java/M1/S2/TPS/notification/service/VerificationEmailService.java](notification/src/main/java/M1/S2/TPS/notification/service/VerificationEmailService.java)

### 5) Mock temporaire côté mailing (pour test sans Auth complète)
- Endpoint HTTP de test pour publier un événement UserRegistered vers RabbitMQ.
- Permet de tester de bout en bout : publish -> consume -> e-mail dans MailHog.
- Fichiers :
  - [notification/src/main/java/M1/S2/TPS/notification/controller/NotificationMockController.java](notification/src/main/java/M1/S2/TPS/notification/controller/NotificationMockController.java)
  - [notification/src/main/java/M1/S2/TPS/notification/dto/MockUserRegisteredRequest.java](notification/src/main/java/M1/S2/TPS/notification/dto/MockUserRegisteredRequest.java)
  - [notification/src/main/java/M1/S2/TPS/notification/messaging/MockEventPublisher.java](notification/src/main/java/M1/S2/TPS/notification/messaging/MockEventPublisher.java)

### 6) Configuration applicative Notification
- RabbitMQ + SMTP + paramètres app (base URL, from, routing keys, queue names).
- `default-requeue-rejected=false` pour permettre la redirection DLQ en cas d’erreur de consommation.
- Fichier :
  - [notification/src/main/resources/application.yml](notification/src/main/resources/application.yml)

### 7) Intégration Docker Compose
- Activation des services : notification, rabbitmq, mailhog.
- Injection des variables d’environnement nécessaires.
- Correction du montage nginx (fichier de conf).
- Fichier :
  - [docker-compose.yml](docker-compose.yml)

## Comment tester rapidement

## Prérequis
- Docker + Docker Compose

## Lancer la stack utile au mailing
Depuis la racine :

```bash
docker compose up -d rabbitmq mailhog notification
```

## Vérifier les interfaces
- RabbitMQ management : http://localhost:15672 (guest/guest)
- MailHog UI : http://localhost:8025

## Publier un événement mock

```bash
curl -X POST http://localhost:8081/notification/mock/user-registered \
  -H "Content-Type: application/json" \
  -d '{"userId":"u_123","email":"user@test.com"}'
```

Résultat attendu :
1. Message publié sur `auth.events` avec routing key `auth.user-registered`.
2. Message consommé par Notification.
3. E-mail visible dans MailHog avec lien de vérification.

## Points conformes au TP Mailing
- Découplage via RabbitMQ.
- Envoi e-mail asynchrone par service Notification.
- Topologie RabbitMQ avec DLQ.
- SMTP local MailHog.
- Événement métier structuré (eventId/occurredAt + données).
- Possibilité de mocker les dépendances non prêtes (Auth réelle non bloquante).

## Validation réellement exécutée
- `docker compose up -d rabbitmq mailhog notification` : OK
- `POST /notification/mock/user-registered` : HTTP `202`
- MailHog API (`/api/v2/messages`) : `total=1` après publication
- Le mail contient un lien de vérification au format attendu :
  `http://localhost:8080/auth_service/identity/validate_email?tokenId=...&t=...`

## Ce qu’il faut comprendre (important pour la suite)
1. Le service Notification ne gère pas l’authentification : il ne fait que consommer un événement métier et envoyer un mail.
2. Le couplage avec Auth se fait uniquement par contrat d’événement (`UserRegistered`) et routing key RabbitMQ.
3. La DLQ sert à isoler les messages invalides/échoués pour ne pas bloquer la file principale.
4. Le mock publisher est uniquement un outil de dev pour tester sans attendre la fin du service Auth.
5. Quand Auth sera prête, il faudra retirer le mock et conserver le consumer tel quel.

## Réponses aux points soulevés dans TP_mailing.md

### 1) Objectifs pédagogiques
- Découplage via messagerie : **fait** (RabbitMQ).
- Envoi e-mail asynchrone : **fait** (consumer Notification).
- Token hash et vérification côté Auth : **hors scope de ta partie**, laissé à l’équipe Auth.
- Exchange/Queue/DLQ : **fait**.
- SMTP local MailHog : **fait**.

### 2) Contexte fonctionnel
- Partie Notification du flux global (Auth -> Rabbit -> Notification -> mail) : **faite**.
- Partie Auth (register/verify + persistence) : **non implémentée ici volontairement**.

### 3) Architecture cible
- Notification branchée sur `auth.events` et `auth.user-registered` : **fait**.
- Flux vérifié en local avec mock de publication : **fait**.

### 4) Hash du token
- Principe respecté dans le design : Notification ne stocke pas de token.
- Implémentation hash/compare one-shot : **à faire côté Auth**.

### 5) Pourquoi la messagerie
- Découplage et résilience implémentés concrètement (queue + DLQ) : **fait**.

### 6) Rôles des services
- Rôle Notification conforme : consommer `UserRegistered`, construire lien, envoyer mail : **fait**.

### 7) Spécifications événements
- `UserRegistered` avec `eventId`, `occurredAt`, `data` : **fait**.
- Headers recommandés (`x-correlation-id`, `x-schema-version`) : **fait dans le mock publisher**.

### 8) Contrats API Auth
- Non traités ici (scope Auth).

### 9) Modèle de données
- Non traité dans Notification (pas de persistance nécessaire côté mailing pour ce TP).

### 10) Configuration
- RabbitMQ + MailHog activés dans compose : **fait**.
- Config SMTP Notification vers MailHog : **faite**.

### 11) Étapes séance 1
- Côté Notification (déclaration MQ, consommation, envoi mail) : **fait**.
- Test register réel via Auth : **mocké en attendant Auth**, validé de bout en bout.

### 12) Étapes futur
- Base prête pour idempotence/analytics dès que l’équipe Auth publiera les événements réels.

### 13) Critères d’évaluation
- Flux mailing fonctionnel : **fait et démontré**.
- Messagerie + DLQ : **fait**.
- Logs et test manuel reproductible : **fait**.
- Sécurité token hash : **dépend de l’implémentation Auth**.

## Limites connues (volontairement hors scope actuel)
- Le flux Auth complet (création user + hash token + verify one-shot) reste à finaliser côté équipe Auth.
- Le mock publisher est temporaire et sera supprimé quand Auth publiera réellement `UserRegistered`.

## Prochaine étape d’intégration équipe
1. Brancher la publication réelle depuis Auth vers `auth.events` + `auth.user-registered`.
2. Conserver Notification inchangé (consumer déjà prêt).
3. Retirer le endpoint mock quand l’Auth réelle est validée.
