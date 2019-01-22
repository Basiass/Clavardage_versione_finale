
Le# Clavardage_versione_finale

Ce dossier regroupe :

* Tous les fichiers de l'application de clavardage
* Un driver sqlite-jdbc (format .jar)

* Les fichiers necessaires au lancement du serveur de présence //TODO

# Installation préalable

Le projet ne necessite pas l'installation de MySQL !

* Télécharger le projet sous Eclipse
* Ajouter le driver sqlite au classpath du projet afin qu'il puisse être utilisé. Sous Eclipse : faire un clic droit sur le projet Eclipse, cliquer sur *Propriétés*, *Java Build Path*. Dans l'onglet Librairies, choisir *Add JARs* pour ajouter "sqlite-jdbc-3.23.1.jar".
* Le main est dans la classe Utilisateur, dans le package Application.
* Il suffit d'executer le main pour lancer le programme. 


# Manuel Utilisateur

L'application de clavardage est un système de chat, vous permettant de communiquer avec d'autres utilisateurs par le biais de messages. Vous devez appartenir au même réseau, et être actifs pour pouvoir entamer une conversation.
Lors de votre première connection, vous devez choisir un identifiant et un mot de passe qui seront sauvergardés localement sur votre machine pour vous permettre de réacceder au service de chat, et d'assurer la confidentialité de vos conversations.
Vous apparaitrez aux yeux des autres utilisateurs sous votre pseudo, que vous pourrez choisir et modifier à votre guise durant vos sessions de clavardage.

Deux versions sont disponibles avec deux modes de contrôle de présence :
* basé sur UDP
* basé sur un serveur HTTP

Fonctionnalités non supportées :
* Envoi d'image

# L'application de clavardage

Voici un guide d'utilisation de l'interface.

## Fenêtre principale

* Lorsque l'utilisateur se connecte, on lui demande de rentrer son pseudo //TODO
* Sur la fenêtre principale, il peut choisir de se déconnecter auquel cas il n'apparaitra plus dans la liste des utilisateurs disponibles
* L'utilisateur peut aussi choisir de changer de pseudo. Ce pseudo doit être non nul, et surtoût non utilisé par un autre utilisateur. S'il n'est pas disponible, un message apparaît en précisant à l'utilisateur que le pseudo entré est déjà utilisé, et il peut retourner sur la fenêtre précédente afin d'en entrer un autre valide. Si l'utilisateur ferme la fenêtre, le changement de mot de passe est annulé.
display fenêtre de changement de pseudo
* En dessous, les Utilisateurs actifs apparaissent en temps réel dès qu'il se connecte. Pour initier une conversation, il suffit de cliquer sur le pseudo de l'utilisateur avec qui on veut communiquer et la fenêtre de conversation associée s'ouvre.

Note :
Certaines fois, il est possible que le contenu de la fenêtre apparaisse grisé sans rien à l'intérieur. Ce n'est pas un problème, il suffit d'agrandir puis de rétrécir la fenêtre pour retrouver un affichage consistant.

## Fenêtre de chat

* Il y a ouverture d'une fenêtre de chat pour chaque nouvelle conversation initiée en cliquant sur le nom d'un utilisateur.

* Lorsqu'on ouvre une conversation avec un utilisateur, l'ensemble de l'historique des messages entre les deux personnes est affiché, même si un des deux a changé de pseudo entre temps. L'affichage des anciens messages est horodaté et affiche le pseudo que les utilisateurs ont au début de la conversation. En revanche, cet historique est local, ce qui veut dire qu'on doit toujours utiliser le même ordinateur pour y avoir accès. Si on change de poste, l'autre machine aura à son tour un historique local de nos conversations.

* La zone à droite de la fenêtre permet à l'utilisateur d'entrer son message. Il faut appuyer sur le bouton *Envoyer* pour que le message soit transmis au destinataire et affiché sur la zone gauche de la fenêtre.

* Lorsqu'un message est reçu, il est affiché dans la zone gauche de la fenêtre avec le pseudo de la personne qui l'a envoyé ainsi que la date.

* Un utilisateur peut quitter la conversation en cliquant sur la croix rouge en haut de la fenêtre ou sur le bouton *Exit*.

* L'utilisateur est informé lorsque son interlocuteur quitte la fenêtre de chat, et il ne peut plus lui envoyer de messages sur cette fenêtre.

Note :
Certaines fois, il est possible que le contenu de la fenêtre apparaisse grisé sans rien à l'intérieur. Ce n'est pas un problème, il suffit d'agrandir puis de rétrécir la fenêtre pour retrouver un affichage consistant.

# Sauvegarde de l'historique via une base de donnée

Le choix d'implémentation pour la persistance des données s'est porté sur une sauvegarde locale. Chaque machine enregistre les conversation d'un utilisateur, reconnaissable grâce à son identifiant. Il peut changer de pseudo entre deux sessions de clavardage et ses messages pourront quand même être restitués dans la fenêtre. Seul un utilisateur peut se connecter sur une machine.

# Serveur de présence

Le serveur de présence n'est pas en état de marche : nous avons travaillé dessus, mais il n'est pas encore abouti, il est donc inutil de le tester.

## Installation

Il fonctionne sur Eclipse JEE avec la version 6 de Tomcat.
Il faut créer un nouveau package "Serveur" avec la classe ".." et la classe "UtilisateurActif" en librairie.
Il faut également inserér la classe "Serveur_talker" du coté utilisateur.

 ## Conception
 
 Ce serveur HTTP a été concu de la facon suivante : 
 - Un utilisateur se connecte et envoie directement au serveur une notification de type "UtilisateurActif" (avec son identifiant, son pseudo, et des informations de type Boolean). Cette notification se transmet par adresse http de type : 
 http://10.1.5.233:8080/Serveur_servlet/Servlet?maj=0&deconnection=1&pseudo=" + user.GetPseudo() + "&identifiant=" + user.GetIdentifiant ..."
 - Le serveur, qui contient une liste d'Utilisateur Actif, lui renvoie cette liste en String, de type : 
 "," + Pseudo + "," + Identifiant + "," + InetAddress + ";" +  "," + Pseudo + "," + Identifiant + "," + InetAddress + ";"  + ... + "§"
 Il rempli ensuite sa liste du nouvel utilisateur
 - Le nouvel utilisateur n'a plus qu'à re-construire une liste d'utilisateur actif grace à la réponse recu.
 
 De plus, toutes les 5 secondes, nous avions prévu, pour chaque utilisateur, de faire une requete de mise à jour : le serveur renverra donc sa liste concatenée en String à chaque mise à jour. Ainsi chaque utilisateur sera au courant de la venue ou du départ d'un utilisateur.
 
 
