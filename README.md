Le dossier Clavardage regroupe :

* Tous les fichiers de l'application de clavardage
* Un driver sqlite-jdbc (format .jar)

# Installation préalable

Le projet ne necessite pas l'installation de MySQL !

* Télécharger les fichiers du projet Clavardage sous Eclipse
* Ajouter le driver sqlite au classpath du projet afin qu'il puisse être utilisé. Sous Eclipse : faire un clic droit sur le projet Eclipse, cliquer sur *Propriétés*, *Java Build Path*. Dans l'onglet Librairies, choisir *Add JARs* pour ajouter "sqlite-jdbc-3.23.1.jar".
* Le main est dans la classe Utilisateur, dans le package Application.
* Il suffit d'executer le main pour lancer le programme. 

# Serveur de présence

Le serveur de présence n'est pas en état de marche : nous avons travaillé dessus, mais il n'est pas encore abouti, il est donc inutile de le tester. Les deux classes en cours de développement sont regroupées dans le dossier *Classes_Serveur*.
Nous avons tout de même choisi de vous présenter nos travaux.

## Installation

Il fonctionne sur Eclipse JEE avec la version 6 de Tomcat.
Il faut créer un nouveau package "Serveur" avec la classe "Servlet" et la classe "UtilisateurActif" en librairie.
Il faut également inserer la classe "Serveur_talker" du coté utilisateur.

 ## Conception
 
 Ce serveur HTTP a été concu de la facon suivante : 
 
* Un serveur HTTP stocke la liste des utilisateurs actifs sur le réseau.
 
* Lorsqu'un utilisateur se connecte, il envoie directement au serveur une notification de type "UtilisateurActif" (avec son identifiant, son pseudo, et des informations de type Boolean), afin que le serveur puisse stocker l'information. Cette notification se transmet par le biais d'une requête http de type : 
 http://10.1.5.233:8080/Serveur_servlet/Servlet?maj=0&deconnection=1&pseudo=" + user.GetPseudo() + "&identifiant=" + user.GetIdentifiant ..."
 
* A chaque nouvelle connection, le serveur renvoie la liste des utilisateurs actifs sous forme de  String, de type : 
"," + Pseudo + "," + Identifiant + "," + InetAddress + ";" +  "," + Pseudo + "," + Identifiant + "," + InetAddress + ";"  + ... + "§". Il ajoute ensuite le nouveau connecté à la liste de tous les utilisateurs actifs sur le réseau.
 
* Le nouvel utilisateur n'a plus qu'à re-construire une liste d'utilisateur actif grace à la réponse reçue du serveur.
 
* De plus, toutes les 5 secondes, nous avions prévu, pour chaque utilisateur, de faire une requete de mise à jour : le serveur renverra donc sa liste concatenée en String à chaque mise à jour. Ainsi chaque utilisateur sera au courant de la venue ou du départ d'un utilisateur.
 
Note : 
Le serveur fonctionne de la même manière qu'en UDP : Lorsqu'on reçoit un UtilisateurActif avec l'attribut deconnection à vrai, on le supprime de la liste des utilisateurs actifs sur le réseau.
