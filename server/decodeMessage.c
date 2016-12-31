#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "decodeMessage.h"


int decode_message(char *rcvMsg, struct message *msg, int id_max_size) {
	int  index, end;
	char id [id_max_size];
	
	if (rcvMsg[0] != '<') {
		msg->isEmpty = 1;
		return 1;
	}else{
		msg->isEmpty = 0;
	}
	
	msg->action = rcvMsg[1];
	index = 3;
	
	while (1) {
		if (rcvMsg[index] == ';') {
			break;
		}
		
		id[index - 3] = rcvMsg[index];
		index++;
	}
	id[index - 3] = '\0';
	index++;
	
	msg->playerID = atoi(id);
	
	end = strchr(rcvMsg,'>') - rcvMsg;
	
	memcpy(msg->data, &rcvMsg[index], end);
	msg->data[end - index] = '\0';
	msg->bytes = end + 1;
	return 0;
	
}
