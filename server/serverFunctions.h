#ifndef ENVIRONMENT_H
#define ENVIRONMENT_H

#include "players.h"

typedef
struct environment{
	
    int LOGS_ON;
    int LIVE_LOOKING;
	int PLAYER_COUNT;
	int TOTAL_PLAYER_COUNT;
	int TOTAL_ROOM_COUNT;
	int ROOM_COUNT;
	int MESSAGE_SEND_COUNT;
	int MESSAGE_RECV_COUNT;
	int BYTES_RECV_COUNT;
	int BYTES_SEND_COUNT;
	int SHOW_ERRORS;
	int SERVER_PORT;
}ENV;

ENV *create_env();
void *server_env(void *arg);
char *getline();
void print_error(ENV *environment, char *error);
void print_variables(ENV *environment);
void print_avaible_orders();
void functions(char *order, PLAYERS *first, ENV *environment);
void print_log(ENV *environment, char *log);
char *trim_whitespace(char *str);
void clear_environment(ENV *environment);
void print_statistic(ENV *environment, char *statistics);

#endif
