#include <stdio.h>
#include <stdlib.h>
#include "players.h"
#include "room.h"
#include "player.h"
#include "rooms.h"


/*
 * overi zda vygenerovane id jiz neni obsazeno jinym hracem
 * vraci 1 pokud id je jiz obsazeno, 0 pokud ne
 */
int verify_genereted_ID_for_room(struct rooms *first, int roomID) {
	if (first == NULL) {
		return 0;
	}
	
	ROOMS *pom = first;
	while (pom != NULL) {
		if (pom->roomID == roomID){
			return 1;
		}
		
		pom = pom->next;
	}
	
	return 0;
}

/*
 * generuje nove id pro noveho hrace
 */
int generate_random_ID_for_room (ROOMS *firstRoom) {
	int id;
	while(1){
		id = rand() % 10000;
		if(id == 0) id++;
		
		if (verify_genereted_ID_for_room(firstRoom, id) == 0){
			break;	
		}
	}
	return id;
}


ROOMS *create_new_room(struct rooms *firstRoom) {
	ROOMS *newRoom = (ROOMS *) malloc(sizeof(ROOMS));
	
	newRoom->next = firstRoom;
	newRoom->previous = NULL;
	newRoom->room = NULL;
	newRoom->roomID = generate_random_ID_for_room(firstRoom);
	firstRoom->previous = newRoom;
	
	return newRoom;
}

void clear_rooms(struct rooms *firstRoom){
	while(firstRoom != NULL){
		ROOMS *pom = firstRoom;
		clear_room(pom->room);
		firstRoom = firstRoom->next;
		free(pom);
	}
}

ROOMS *remove_from_rooms(struct rooms *firstRoom, int roomID){
	
	ROOMS *pom = firstRoom;
	ROOMS *returnRoom = firstRoom;
	
	while(pom != NULL){
		if(pom->roomID == roomID){
			
			if(firstRoom == pom){
				returnRoom = firstRoom->next;
			}
			
			if(pom->next != NULL){
				pom->next->previous = pom->previous;
			}
			
			if(pom->previous != NULL){
				pom->previous->next = pom->next;	
			}
			clear_room(pom->room);
			free(pom);
			break;
		}	
		
		pom = pom->next;
	}
	
	return returnRoom;
}

void print_rooms(struct rooms *firstRoom){
	if (firstRoom == NULL) {
		return;
	}
	
	ROOMS *pom = firstRoom;
	while (pom != NULL) {
		print_room(pom->room);
		pom = pom->next;
	}
}
