#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "players.h"
#include "room.h"
#include "player.h"

PLAYER *create_player(int playerID, unsigned int playerIP, char * playerName){
	PLAYER *newPlayer = (PLAYER *)malloc(sizeof(PLAYER));
	
	newPlayer->playerID = playerID;
	newPlayer->playerIP = playerIP;
	strcpy(newPlayer->playerName, playerName);
	
	return newPlayer;	
}


void set_shipInfo(struct player *player, char *shipInfo, int maxSize, int indexOfBeginning){	
	memcpy( player->shipInfo, &shipInfo[indexOfBeginning], maxSize - indexOfBeginning );
	player->shipInfo[maxSize - indexOfBeginning + 1] = '\0';
}
