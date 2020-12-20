# GriefPest
#### Plugin Minecraft pour faire des zones claims.
#### Développé en 1.16.4, non testé sur les autres versions.
### Listes des commandes :
- `/pest pos1` - *Définie la position 1 de la zone*
- `/pest pos2` - *Définie la position 2 de la zone*
- `/pest claim <nom de la zone>` - *Permet de claim une zone délimitée par pos1 et pos2*
- `/pest remove <nom de la zone>` - *Permet de se débarasser d'une zone, en gros elle n'est plus claim*
- `/pest edit <nom de la zone> <options|group>` - *Permet de modifier les paramètres d'une zone claim*  
- `/pest size` - *Affiche la taille de la zone dans laquelle le joueur se trouve*
- `/pest number` - *Affiche le nombre de zones claims en tout dans le serveur*
- `/pest amiinanarea` - *Affiche si le joueur se trouve dans une zone claim (utile uniquement quand j'ai commencé le plugin)*
- `/pest zonename` - *Affiche le nom de la zone dans laquelle se trouve le joueur (s'il est dans une zone claim évidemment)*
- `/pest blockedcommands` - *Affiche la liste des commandes bloquées quand un joueur se trouve dans une zone claim*

### Disclaimer :
Plus il y aura de zones claims, plus le plugin consommera de RAM et utilisera les
performances du CPU, étant donné que nous devons vérifier si le joueur se trouve dans
une zone claim, TOUTES les zones doivent être vérifiées une par une jusqu'à trouver la bonne.
Le plugin n'étant pas asynchrone, il se peut que suite à une trop grande quantité de zones
claims, les TPS du serveur baissent.