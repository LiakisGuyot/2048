# 2048
IMPORTANT :
La case qui bouge regarde quelle est la prochaine case
La case qui bouge est responsable des fusions
La case qui bouge écrase la case statique lors des fusions
La case qui bouge vérifie si elle peut fusionner avec la case statique
La case qui bouge Fusionne (valeur x2) lors du test "canIFuseWith"
Le jeu gère les déplacement des cases (et donc l'écrasement des cases fusionnées statiques)

Click touche direction --> Parcours tout le tableau (chaque case non vide en partant du mur correspondant) et :
-While( CanMove() ) :
  Check case dans la direction
    Si nul (case vide) :
      -Move
    Si case :
      -CanIFuseWith(Case) ?
      Si oui :
        -Supprimer case précédente
        -Deplacer la case
