#ifndef MESSAGE_H
#define MESSAGE_H

typedef
struct message{

	int playerID;
	char data [100];
	char action;

}MESSAGE;

int decode_message(char *rcvMsg, struct message *msg, int id_max_size);

#endif
