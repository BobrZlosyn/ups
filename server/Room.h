#ifndef ROOM_H
#define ROOM_H

#include "player.h"

typedef
struct room{

    struct players *player1;
    struct players *player2;
    int roomID;
    int isWaiting;

}ROOM;

ROOM *create_room(struct players *player1);

void add_second_player(struct players *player2, struct room *room);

ROOM *find_free_room(struct players *first, int playerID);
#endif
