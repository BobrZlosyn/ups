#ifndef ROOMS_H
#define ROOMS_H

#include "room.h"

typedef
struct rooms{
	int roomID;
	struct room *room;
	struct rooms *next;
	struct rooms *previous;
}ROOMS;

ROOMS *create_new_room(struct rooms *firstRoom);

void clear_rooms(struct rooms *firstRoom);

ROOMS *remove_from_rooms(struct rooms *firstRoom, int roomID);

void print_rooms(struct rooms *firstRoom);

int verify_genereted_ID_for_room(struct rooms *first, int roomID);

int generate_random_ID_for_room(struct rooms *first);

#endif
