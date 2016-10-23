#ifndef ROOM_H
#define ROOM_H

#include "player.h"

typedef
struct room{

    struct PLAYER *player1;
    struct PLAYER *player2;
    int roomID;

}ROOM;

ROOM *create_room(struct PLAYER *player1,struct PLAYER *player2);

#endif
