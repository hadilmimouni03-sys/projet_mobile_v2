-- ============================================================
--  BDG_LivraisonCom_25 - Données de test
-- ============================================================

USE BDG_LivraisonCom_25;

-- Postes
INSERT INTO Postes (codeposte, libelle, indice) VALUES
('CTR', 'Contrôleur',  1),
('LIV', 'Livreur',     2);

-- Personnel (motP = BCrypt de 'password123')
INSERT INTO Personnel (nompers, prenompers, adrpers, villepers, telpers, d_embauche, Login, motP, codeposte) VALUES
('Martin',   'Sophie',  '10 rue de la Paix',  'Paris',    '0601020304', '2022-01-15', 'admin',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh8.', 'CTR'),
('Dupont',   'Paul',    '5 avenue Hugo',       'Lyon',     '0611223344', '2021-06-01', 'livreur1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh8.', 'LIV'),
('Bernard',  'Marie',   '22 bd Gambetta',      'Marseille','0622334455', '2020-03-20', 'livreur2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh8.', 'LIV'),
('Leblanc',  'Thomas',  '8 rue Voltaire',      'Bordeaux', '0633445566', '2023-09-10', 'livreur3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh8.', 'LIV'),
('Garcia',   'Laura',   '3 impasse des Lilas', 'Toulouse', '0644556677', '2022-11-05', 'ctrl2',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh8.', 'CTR');

-- Clients
INSERT INTO Clients (nomclt, prenomclt, adrclt, villeclt, code_postal, telclt, adrmail) VALUES
('Durand',   'Alice',   '12 rue des Roses',    'Paris',    '75001', '0678901234', 'alice.durand@email.fr'),
('Moreau',   'Pierre',  '45 avenue du Parc',   'Lyon',     '69002', '0689012345', 'pierre.moreau@email.fr'),
('Petit',    'Claire',  '7 bd de la Mer',      'Marseille','13001', '0690123456', 'claire.petit@email.fr'),
('Simon',    'Jacques', '18 rue Nationale',    'Lille',    '59000', '0678234567', 'jacques.simon@email.fr'),
('Laurent',  'Emma',    '30 rue du Commerce',  'Nantes',   '44000', '0667345678', 'emma.laurent@email.fr'),
('Michel',   'Hugo',    '9 allée des Pins',    'Rennes',   '35000', '0656456789', 'hugo.michel@email.fr');

-- Articles
INSERT INTO Articles (refarticle, designation, prixA, prixV, codetva, categorie, qtestock) VALUES
('ART001', 'Ordinateur portable 15"',  600.00,  999.99, 'TVA20', 'Informatique', 50),
('ART002', 'Souris sans fil',           15.00,   29.99, 'TVA20', 'Périphériques',200),
('ART003', 'Clavier mécanique',         40.00,   79.99, 'TVA20', 'Périphériques',150),
('ART004', 'Écran 24 pouces',          180.00,  329.99, 'TVA20', 'Informatique',  80),
('ART005', 'Casque audio Bluetooth',    45.00,   89.99, 'TVA20', 'Audio',         120),
('ART006', 'Webcam HD',                 20.00,   49.99, 'TVA20', 'Périphériques', 90),
('ART007', 'Chargeur USB-C 65W',        12.00,   24.99, 'TVA20', 'Accessoires',  300),
('ART008', 'SSD 1 To externe',          55.00,  109.99, 'TVA20', 'Stockage',     100);

-- Commandes (date de commande passée et du jour)
INSERT INTO Commandes (noclt, datecde, etatcde) VALUES
(1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'LIVREE'),
(2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'LIVREE'),
(3, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'NON_LIVREE'),
(4, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'EN_COURS'),
(5, CURDATE(), 'EN_COURS'),
(6, CURDATE(), 'EN_ATTENTE'),
(1, CURDATE(), 'EN_ATTENTE'),
(2, CURDATE(), 'EN_ATTENTE'),
(3, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'LIVREE'),
(4, CURDATE(), 'EN_COURS');

-- LigCdes
INSERT INTO LigCdes (nocde, refarticle, qtecde) VALUES
(1, 'ART001', 1),
(1, 'ART002', 2),
(2, 'ART003', 1),
(2, 'ART005', 1),
(3, 'ART004', 1),
(4, 'ART006', 2),
(4, 'ART007', 3),
(5, 'ART001', 1),
(5, 'ART003', 1),
(6, 'ART008', 2),
(7, 'ART002', 1),
(7, 'ART007', 2),
(8, 'ART005', 1),
(9, 'ART001', 2),
(10,'ART004', 1),
(10,'ART006', 1);

-- LivraisonCom
INSERT INTO LivraisonCom (nocde, dateliv, livreur, modepay, etatliv, remarques) VALUES
(1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 2, 'ESPECES',  'LIVRE',       NULL),
(2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 3, 'CB',       'LIVRE',       NULL),
(3, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 4, 'ESPECES',  'NON_LIVRE',   'Client absent, a rappelé mais ne répond plus'),
(4, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 2, 'VIREMENT', 'EN_COURS',    NULL),
(5, CURDATE(), 2, 'ESPECES',  'EN_COURS',    NULL),
(6, CURDATE(), 3, 'CB',       'EN_ATTENTE',  NULL),
(7, CURDATE(), 3, 'ESPECES',  'EN_ATTENTE',  NULL),
(8, CURDATE(), 4, 'CHEQUE',   'EN_ATTENTE',  NULL),
(9, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 2, 'ESPECES',  'LIVRE',       NULL),
(10,CURDATE(), 4, 'CB',       'EN_COURS',    NULL);

-- Messages de test
INSERT INTO Messages (expediteur, destinataire, nocde, contenu, type_message, lu) VALUES
(1, 2, 5,  'Attention : le client a changé d\'adresse. Nouvelle adresse : 15 rue Neuve, Paris.', 'INFO',    0),
(2, 1, 4,  'URGENT – Le client (0678901234) refuse la commande #4 : emballage abîmé.', 'URGENCE', 0),
(3, 1, 6,  'Le client (0689012345) ne répond pas au téléphone pour la commande #6.', 'URGENCE', 1),
(1, 3, 6,  'Essayez de rappeler dans 30 minutes, le client est en réunion.', 'INFO',    0),
(1, 4, 8,  'Priorité haute pour la commande #8, le client part en voyage demain matin.', 'INFO',    0);
