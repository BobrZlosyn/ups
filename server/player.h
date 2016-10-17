#ifndef PLAYER_H
#define PLAYER_H

typedef
struct player{

	int playerID;
	unsigned int playerIP;
	char playerName [20];
	char shipInfo [100];

}PLAYER;

PLAYER *create_player(int playerID, unsigned int playerIP, char * playerName);

void set_shipInfo(struct player *player, char *shipInfo, int maxSize, int indexOfBeginning);

#endif
