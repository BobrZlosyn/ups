#ifndef PLAYERS_H
#define PLAYERS_H

#include "room.h"
#include "player.h"

typedef
struct players{
	int isFree;
	struct player *player;
	struct room *room;
	struct players *next;
	struct players *previous;
}PLAYERS;

PLAYERS *add_player(struct players * first, char *ip_adress, int socket);

int verifyGeneretedID(struct players *first, int playerID);

PLAYERS *find_player(struct players * first, int playerID);

ROOM *find_room(struct players * first, int playerID);

PLAYERS *find_player_status(struct players * first, int playerID);

PLAYERS *remove_player(struct players *first, int playerID);

void clear_players(struct players * first);

void print_players(struct players * first);

#endif
