#ifndef PLAYERS_H
#define PLAYERS_H

#include "Room.h"
#include "player.h"

typedef
struct players{
	int isFree;
	struct player *player;
	struct room *room;
	struct players *next;
	struct players *previous;
}PLAYERS;

PLAYERS *add_player(struct players * first);

int verifyGeneretedID(struct players *first, int playerID);

PLAYER *find_player(struct players * first, int playerID);

ROOM *find_room(struct players * first, int playerID);

PLAYERS *find_player_status(struct players * first, int playerID);

PLAYERS *remove_player(struct players * first, int playerID);

#endif
