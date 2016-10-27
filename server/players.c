#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "players.h"
#include "room.h"
#include "player.h"


int maxNumberOfID = 10000;


/*
 * generuje nove id pro noveho hrace
 */
int generateRandomID (PLAYERS *first) {
	int id;
	while(1){
		srand ( time(NULL) );
		id = rand() % maxNumberOfID;
		if(id == 0) continue;
		
		if (verifyGeneretedID(first, id) == 0){
			break;	
		}
	}
	return id;
	
}

/*
 * vytvori noveho hrace a prida ho do seznamu hracu
 */	
PLAYERS *add_player(struct players *first, char *ip_adress, int socket){
	int id;
	PLAYERS *newPlayer = (PLAYERS *)malloc(sizeof(PLAYERS));
	
	id = generateRandomID(first);
	PLAYER *player = create_player(id, ip_adress, "none", socket); 
	
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
	
	if(playerID >= maxNumberOfID || playerID < 1){
		return first;
	}
	
	PLAYERS *pom = first;
	PLAYERS *returnPom = first;
	
	while (pom != NULL) {
		if (pom->player->playerID == playerID){
			if(pom->next != NULL) {
				pom->next->previous = pom->previous;
			}
			
			if(pom->previous != NULL) {
				pom->previous->next = pom->next;			
			}
			 
			if(pom == first){
				returnPom = first->next;
			}
			
			free(pom->player);
			free(pom->room);
			free(pom);
			break;
		}
		
		pom = pom->next;
	}
	return returnPom;	
}


void clear_players(struct players *first) {
	if (first == NULL) {
		return;
	}

	while (first != NULL) {
		PLAYERS *pom = first;
		free(pom->player);
		free(pom->room);
		
		first = first->next;
		free(pom);
	}
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
PLAYERS *find_player(struct players * first, int playerID) {
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



void print_players(struct players * first){
	if (first == NULL) {
		return;
	}
	
	PLAYERS *pom = first;
	while (pom != NULL) {
		print_player(pom->player);
		pom = pom->next;
	}	
}
