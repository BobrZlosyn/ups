#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "room.h"
#include "players.h"


ROOM *create_room(struct players *player1){
	
	if (player1 == NULL){
		return NULL;
	}
	
	if (player1->room != NULL) {
		printf("not empty");
		return player1->room;
	}
	
	ROOM *room = (ROOM *)malloc(sizeof(ROOM));
	room->isWaiting = 1;
	room->player1 = player1;
	room->player2 = NULL;
	
	player1->room = room;
	player1->isAvaible = 1;
	
	return room;
}

ROOM *find_free_room(struct players *first, int playerID) { 
	
	PLAYERS *pom = first;
	while(pom != NULL) {
		if (pom->player->playerID == playerID){
			pom = pom->next;
			continue;
		}
		
		if (pom->room == NULL){
			pom = pom->next;
			continue;
		}
		
		if (pom->room->isWaiting == 1){
			return pom->room;
		}
		
		pom = pom->next;
	}
	
	return NULL;
}


void add_second_player(struct players *player2, struct room *room){
	room->player2 = player2;
	room->isWaiting = 0;
	player2->isAvaible = 1;
	player2->room = room;
}

void generate_starting_id (struct room *room) {	
	if(room == NULL 
		|| room->player1 == NULL
		|| room->player2 == NULL
		) {
		return; 
	}
	
	srand ( time(NULL) );
	if ( (rand() % 2) == 0) {
		room->isPlayingID = room->player1->player->playerID;	
	} else {
		room->isPlayingID = room->player2->player->playerID;
	}
}

void print_room(struct room *room){
	if(room != NULL){
		return;
	}
	
	printf("Room with ID %d \n", room->roomID);
}

