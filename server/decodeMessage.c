#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "decodeMessage.h"


int decode_message(char *rcvMsg, struct message *msg, int id_max_size) {
	int  index, end;
	char id [id_max_size];
	
	msg->action = rcvMsg[1];
	index = 3;
	
	for (; index < id_max_size + 3; index++) {
		if (rcvMsg[index] != ';') {
			break;
		}
		
		id[index - 3] = rcvMsg[index];
	}
	id[index - 3] = '\0';
	
	msg->playerID = atoi(id);
	
	end = strchr(rcvMsg,'>') - rcvMsg;
	
	memcpy(msg->data, &rcvMsg[index], end);
	msg->data[end - index] = '\0';
	
	return 0;
	
}
