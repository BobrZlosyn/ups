#ifndef PLAYER_H
#define PLAYER_H

typedef
struct player{

	int playerID;
	char playerIP [15];
	char playerName [20];
	char shipInfo [100];

}PLAYER;

PLAYER *create_player(int playerID, char *playerIP, char *playerName);

void set_shipInfo(struct player *player, char *shipInfo, int maxSize, int indexOfBeginning);

void print_player(struct player *player);

int decode_id_of_player(char *msg, int begin);

#endif
