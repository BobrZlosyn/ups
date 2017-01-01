#ifndef PLAYER_H
#define PLAYER_H

typedef
struct player{

	int playerID;
	int socket;
	char playerIP [15];
	char playerName [20];
	char shipInfo [200];
	char modulInfo [200];

}PLAYER;

PLAYER *create_player(int playerID, char *playerIP, char *playerName, int socket);

void set_shipInfo(struct player *player, char *shipInfo, int maxSize, int indexOfBeginning);

void print_player(struct player *player);

int decode_id_of_player(char *msg, int begin);

#endif
