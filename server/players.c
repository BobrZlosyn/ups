#include <stdio.h>
#include <stdlib.h>
#include "players.h"
#include "room.h"
#include "player.h"


int maxNumberOfID = 10000;

/*
 * vytvori noveho hrace a prida ho do seznamu hracu
 */	
PLAYERS *add_player(struct players *first){
	PLAYERS *newPlayer = (PLAYERS *)malloc(sizeof(PLAYERS));
	
	int id = generateRandomID(first);
	PLAYER *player = create_player(id, 100, "none"); 
	
	newPlayer->isFree = 1;
	newPlayer->player = player;
	newPlayer->room = NULL;
	newPlayer->next = first;
	newPlayer->previous = NULL;

	if(first != NULL) {
		first->previous = newPlayer;
	}	
	
	return newPlayer;
}

/*
 * smaze hrace podle ID
 */	
PLAYERS *remove_player(struct players *first, int playerID){
	if (first == NULL) {
		return NULL;
	}
	
	if(playerID >= maxNumberOfID || playerID < 0){
		return first;
	}
	
	PLAYERS *pom = first;
	PLAYERS *returnPom = first;
	
	while (pom != NULL) {
		if (pom->player->playerID == playerID){
			
			pom->next->previous = pom->previous; 
			pom->previous->next = pom->next;
			
			if(pom == first){
				returnPom = first->next;
			}
			
			free(pom->player);
			free(pom->room);
			free(pom);
			
			return pom->player;
		}
		
		pom = pom->next;
	}
	
	return returnPom;	
}

/*
 * overi zda vygenerovane id jiz neni obsazeno jinym hracem
 * vraci 1 pokud id je jiz obsazeno, 0 pokud ne
 */
int verifyGeneretedID(struct players *first, int playerID) {
	if (first == NULL) {
		return 0;
	}
	
	PLAYERS *pom = first;
	while (pom != NULL) {
		if (pom->player->playerID == playerID){
			return 1;
		}
		
		pom = pom->next;
	}
	
	return 0;
}

/*
 * vyhleda hrace podle ID 
 */

PLAYER *find_player(struct players * first, int playerID) {
	if (first == NULL) {
		return NULL;
	}
	
	if(playerID >= maxNumberOfID || playerID < 0){
		return NULL;
	}
	
	PLAYERS *pom = first;
	while (pom != NULL) {
		if (pom->player->playerID == playerID){
			return pom->player;
		}
		
		pom = pom->next;
	}
	
	return NULL;
}

/*
 * vyhleda mistnost podle hracova ID 
 */
ROOM *find_room(struct players * first, int playerID){
	if (first == NULL) {
		return NULL;
	}
	
	if(playerID >= maxNumberOfID || playerID < 0){
		return NULL;
	}
	
	PLAYERS *pom = first;
	while (pom != NULL) {
		if (pom->player->playerID == playerID){
			return pom->room;
		}
		
		pom = pom->next;
	}
	
	return NULL;
}

/*
 * vyhleda informace o hraci podle hracova ID 
 */
PLAYERS *find_player_status(struct players * first, int playerID){
	if (first == NULL) {
		return NULL;
	}
	
	if(playerID >= maxNumberOfID || playerID < 0){
		return NULL;
	}
	
	PLAYERS *pom = first;
	while (pom != NULL) {
		if (pom->player->playerID == playerID){
			return pom;
		}
		
		pom = pom->next;
	}
	
	return NULL;
}



/*
 * generuje nove id pro noveho hrace
 */
int generateRandomID (PLAYERS *first) {
	int id;
	while(1){
		id = rand() % maxNumberOfID;
		if (verifyGeneretedID(first, id) == 0){
			break;	
		}
	}
	return id;
	
}
