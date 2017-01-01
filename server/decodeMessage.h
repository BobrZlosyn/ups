#ifndef MESSAGE_H
#define MESSAGE_H

typedef
struct message{

	char data [200];
	int playerID;
	char action;
	int isEmpty;
	int bytes;

}MESSAGE;

MESSAGE *create_message();
int decode_message(char *rcvMsg, struct message *msg, int id_max_size);

#endif
