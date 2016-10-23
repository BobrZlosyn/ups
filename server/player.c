#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "players.h"
#include "room.h"
#include "player.h"
#include "decodeMessage.h"

PLAYER *create_player(int playerID, char *playerIP, char *playerName){
	PLAYER *newPlayer = (PLAYER *)malloc(sizeof(PLAYER));
	
	newPlayer->playerID = playerID;
	strcpy(newPlayer->playerIP, playerIP);
	strcpy(newPlayer->playerName, playerName);
	
	return newPlayer;	
}



void print_player(struct player *player) {
	printf("---- Player with id: %d ---- \n", player->playerID);
	printf("---- Name: \t %s \n", player->playerName);
	printf("---- Ship info: \t %s \n", player->shipInfo);
	printf("---- IP adress: \t %s \n", player->playerIP);
	printf("\n");
}


int decode_id_of_player(char *msg, int begin){
	
	char textID [3];
	int end;
	end = strchr(msg,';') - msg;
	printf("end 1 = %d \n", end);
	end = strchr(msg,';') - msg;
	printf("end 2 = %d \n", end);
	
	memcpy( textID, &msg[begin], end - begin);
	textID[end - begin] = '\0';
	printf("id = %s \n", textID);
	return 0;
}
