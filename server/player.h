#ifndef PLAYER_H
#define PLAYER_H

typedef
struct player{

	unsigned int playerID;
	unsigned int playerIP;
	char [20] playerName;


}PLAYER;

PLAYER *create_player(unsigned int playerID, unsigned int playerIP, char * playerName);

#endif