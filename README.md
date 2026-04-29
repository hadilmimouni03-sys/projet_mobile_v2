# Supervision des Livraisons — Application Mobile

Application Android native de suivi et supervision des livraisons, développée dans le cadre du projet de développement mobile 2ING INFO (2025-2026).

---

## Description

L'application permet deux types d'utilisateurs de gérer les livraisons en temps réel :

- **Contrôleur** : consulte les livraisons, effectue des recherches, visualise un tableau de bord, et envoie des informations aux livreurs.
- **Livreur** : consulte sa tournée du jour, met à jour l'état des livraisons, et envoie des messages d'urgence au contrôleur.

---

## Stack technique

| Couche    | Technologie                        |
|-----------|------------------------------------|
| Mobile    | Android (Java), API 24+            |
| Backend   | Spring Boot 2.7.18 + Java 17       |
| Base de données | MySQL 8                      |
| Réseau    | Retrofit 2.9 + OkHttp 4           |
| Auth      | JWT (jjwt 0.11.5)                  |
| Sécurité  | Spring Security + BCrypt           |

---

## Prérequis

- **Java 17** (JDK 17)
- **Maven 3.8+**
- **MySQL 8.0+**
- **Android Studio Hedgehog** (ou supérieur)
- **Android SDK 34**

---

## Structure du projet

```
projet_mobile_v2/
├── backend/              # Projet Spring Boot (Maven)
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/livraison/
│           │   ├── config/        # Sécurité, CORS
│           │   ├── controller/    # REST Controllers
│           │   ├── dto/           # Data Transfer Objects
│           │   ├── model/         # Entités JPA
│           │   ├── repository/    # Spring Data JPA
│           │   ├── security/      # JWT Filter & Util
│           │   └── service/       # Logique métier
│           └── resources/
│               └── application.properties
├── mobile/               # Projet Android
│   ├── app/
│   │   └── src/main/
│   │       ├── java/com/livraison/
│   │       │   ├── api/           # Retrofit ApiService
│   │       │   ├── adapter/       # RecyclerView Adapters
│   │       │   ├── controleur/    # Activities Contrôleur
│   │       │   ├── livreur/       # Activities Livreur
│   │       │   ├── model/         # Modèles de données
│   │       │   └── utils/         # SessionManager
│   │       └── res/               # Layouts, values, drawables
│   └── build.gradle
├── database/
│   ├── schema.sql        # DDL : création des tables
│   └── data_test.sql     # Données de test
└── README.md
```

---

## 1. Configuration de la base de données

### a) Créer la base et les tables

```sql
mysql -u root -p < database/schema.sql
```

### b) Insérer les données de test

```sql
mysql -u root -p < database/data_test.sql
```

### c) Vérifier

```sql
USE BDG_LivraisonCom_25;
SELECT * FROM Personnel;
```

---

## 2. Lancer le Backend (Spring Boot)

### a) Configurer la connexion MySQL

Éditer `backend/src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/BDG_LivraisonCom_25?useSSL=false&serverTimezone=Europe/Paris
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

### b) Compiler et démarrer

```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```

Le backend démarre sur **http://localhost:8080**

### c) Tester l'API

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","motP":"password123"}'
```

---

## 3. Lancer l'Application Android

### a) Ouvrir le projet

1. Ouvrir Android Studio
2. File → Open → sélectionner le dossier `mobile/`
3. Attendre la synchronisation Gradle

### b) Configurer l'URL du backend

Le fichier `RetrofitClient.java` utilise `10.0.2.2:8080` pour l'émulateur Android (qui pointe vers `localhost` de la machine hôte).

- **Émulateur Android** : laisser tel quel (`10.0.2.2:8080`)
- **Appareil physique** : remplacer par l'IP de votre machine (ex: `192.168.1.X:8080`)

### c) Lancer

1. Sélectionner un émulateur ou connecter un appareil Android
2. Cliquer **Run** (▶) dans Android Studio

---

## 4. Comptes de test

| Login      | Mot de passe | Rôle        | Nom              |
|------------|--------------|-------------|------------------|
| `admin`    | `password123`| Contrôleur  | Sophie Martin    |
| `ctrl2`    | `password123`| Contrôleur  | Laura Garcia     |
| `livreur1` | `password123`| Livreur     | Paul Dupont      |
| `livreur2` | `password123`| Livreur     | Marie Bernard    |
| `livreur3` | `password123`| Livreur     | Thomas Leblanc   |

---

## 5. Fonctionnalités implémentées

### Contrôleur
- [x] Tableau de bord (KPIs : total, livré, non livré, en cours, en attente)
- [x] Stats par livreur par état
- [x] Stats par client par état
- [x] Liste de toutes les livraisons sur une période
- [x] Livraisons du jour
- [x] Recherche multicritère (date, livreur, état, n° commande, n° client)
- [x] Envoi de messages d'information aux livreurs
- [x] Consultation des messages reçus
- [x] Consultation des messages d'urgence des livreurs

### Livreur
- [x] Liste des livraisons du jour (ordre, n° commande, client, téléphone, ville)
- [x] Détail complet d'une livraison (contact, adresse, lien Google Maps, articles, montant, mode de paiement)
- [x] Modification de l'état d'une livraison
- [x] Saisie de remarques (pour les livraisons non effectuées)
- [x] Envoi de messages d'urgence au contrôleur (avec contact client et n° commande)
- [x] Consultation des messages reçus du contrôleur

---

## 6. API REST

| Méthode | Endpoint                         | Description                        |
|---------|----------------------------------|------------------------------------|
| POST    | `/api/auth/login`                | Authentification                   |
| GET     | `/api/livraisons/today`          | Livraisons du jour                 |
| GET     | `/api/livraisons`                | Livraisons sur période             |
| GET     | `/api/livraisons/recherche`      | Recherche multicritère             |
| GET     | `/api/livraisons/mes-livraisons` | Mes livraisons (livreur)           |
| GET     | `/api/livraisons/{nocde}`        | Détail d'une livraison             |
| PUT     | `/api/livraisons/{nocde}`        | Modifier état/remarques            |
| GET     | `/api/dashboard`                 | Statistiques tableau de bord       |
| GET     | `/api/livreurs`                  | Liste des livreurs                 |
| POST    | `/api/messages`                  | Envoyer un message                 |
| GET     | `/api/messages/recus`            | Messages reçus                     |
| GET     | `/api/messages/urgence`          | Messages d'urgence                 |
| PUT     | `/api/messages/{id}/lu`          | Marquer comme lu                   |

---

## 7. États des livraisons

| Code        | Libellé     |
|-------------|-------------|
| EN_ATTENTE  | En attente  |
| EN_COURS    | En cours    |
| LIVRE       | Livré       |
| NON_LIVRE   | Non livré   |
| REPORTE     | Reporté     |

---

## 8. Modes de paiement

| Code     | Libellé         |
|----------|-----------------|
| ESPECES  | Espèces         |
| CB       | Carte bancaire  |
| VIREMENT | Virement bancaire|
| CHEQUE   | Chèque          |

---

## Notes importantes

- Le projet ne nécessite **aucun Docker** ni conteneurisation
- Tout s'exécute localement : MySQL + Spring Boot + Android Studio
- La sécurité est assurée par JWT (expiration 24h)
- Les mots de passe sont hashés avec BCrypt
