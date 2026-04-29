-- ============================================================
--  BDG_LivraisonCom_25 - Schéma de la base de données
--  Application : Supervision des Livraisons
-- ============================================================

CREATE DATABASE IF NOT EXISTS BDG_LivraisonCom_25
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE BDG_LivraisonCom_25;

-- ------------------------------------------------------------
-- Table : Postes
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Postes (
    codeposte   VARCHAR(10)  NOT NULL,
    libelle     VARCHAR(100) NOT NULL,
    indice      INT          NOT NULL DEFAULT 0,
    CONSTRAINT pk_postes PRIMARY KEY (codeposte)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table : Personnel
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Personnel (
    idpers      INT          NOT NULL AUTO_INCREMENT,
    nompers     VARCHAR(50)  NOT NULL,
    prenompers  VARCHAR(50)  NOT NULL,
    adrpers     VARCHAR(200),
    villepers   VARCHAR(100),
    telpers     VARCHAR(20),
    d_embauche  DATE,
    Login       VARCHAR(50)  NOT NULL,
    motP        VARCHAR(255) NOT NULL,
    codeposte   VARCHAR(10),
    CONSTRAINT pk_personnel PRIMARY KEY (idpers),
    CONSTRAINT uq_login UNIQUE (Login),
    CONSTRAINT fk_personnel_poste FOREIGN KEY (codeposte)
        REFERENCES Postes(codeposte)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table : Clients
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Clients (
    noclt       INT          NOT NULL AUTO_INCREMENT,
    nomclt      VARCHAR(50)  NOT NULL,
    prenomclt   VARCHAR(50),
    adrclt      VARCHAR(200),
    villeclt    VARCHAR(100),
    code_postal VARCHAR(10),
    telclt      VARCHAR(20),
    adrmail     VARCHAR(150),
    CONSTRAINT pk_clients PRIMARY KEY (noclt)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table : Articles
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Articles (
    refarticle  VARCHAR(20)    NOT NULL,
    designation VARCHAR(200)   NOT NULL,
    prixA       DECIMAL(10,2)  DEFAULT 0.00,
    prixV       DECIMAL(10,2)  DEFAULT 0.00,
    codetva     VARCHAR(10),
    categorie   VARCHAR(100),
    qtestock    INT            DEFAULT 0,
    CONSTRAINT pk_articles PRIMARY KEY (refarticle)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table : Commandes
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Commandes (
    nocde   INT          NOT NULL AUTO_INCREMENT,
    noclt   INT          NOT NULL,
    datecde DATE         NOT NULL,
    etatcde VARCHAR(20)  NOT NULL DEFAULT 'EN_COURS',
    CONSTRAINT pk_commandes PRIMARY KEY (nocde),
    CONSTRAINT fk_commandes_client FOREIGN KEY (noclt)
        REFERENCES Clients(noclt)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table : LigCdes
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS LigCdes (
    nocde      INT          NOT NULL,
    refarticle VARCHAR(20)  NOT NULL,
    qtecde     INT          NOT NULL DEFAULT 1,
    CONSTRAINT pk_ligcdes PRIMARY KEY (nocde, refarticle),
    CONSTRAINT fk_ligcdes_commande FOREIGN KEY (nocde)
        REFERENCES Commandes(nocde),
    CONSTRAINT fk_ligcdes_article FOREIGN KEY (refarticle)
        REFERENCES Articles(refarticle)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table : LivraisonCom
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS LivraisonCom (
    nocde    INT          NOT NULL,
    dateliv  DATE,
    livreur  INT,
    modepay  VARCHAR(20)  DEFAULT 'ESPECES',
    etatliv  VARCHAR(30)  NOT NULL DEFAULT 'EN_ATTENTE',
    remarques TEXT,
    CONSTRAINT pk_livraisoncom PRIMARY KEY (nocde),
    CONSTRAINT fk_livraison_commande FOREIGN KEY (nocde)
        REFERENCES Commandes(nocde),
    CONSTRAINT fk_livraison_livreur FOREIGN KEY (livreur)
        REFERENCES Personnel(idpers)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table : Messages
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Messages (
    id            INT          NOT NULL AUTO_INCREMENT,
    expediteur    INT          NOT NULL,
    destinataire  INT,
    nocde         INT,
    contenu       TEXT         NOT NULL,
    dateenvoi     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lu            TINYINT(1)   NOT NULL DEFAULT 0,
    type_message  VARCHAR(20)  NOT NULL DEFAULT 'INFO',
    CONSTRAINT pk_messages PRIMARY KEY (id),
    CONSTRAINT fk_msg_expediteur FOREIGN KEY (expediteur)
        REFERENCES Personnel(idpers),
    CONSTRAINT fk_msg_destinataire FOREIGN KEY (destinataire)
        REFERENCES Personnel(idpers),
    CONSTRAINT fk_msg_commande FOREIGN KEY (nocde)
        REFERENCES Commandes(nocde)
) ENGINE=InnoDB;
