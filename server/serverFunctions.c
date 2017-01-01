#include <pthread.h>
#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include "serverFunctions.h"
#include "players.h"
#include "fileWriter.h"

ENV *create_env() {
	ENV *environment = (ENV *)malloc(sizeof(ENV));
	environment->LIVE_LOOKING = 1;
	environment->LOGS_ON = 1;
	clear_environment(environment);
	environment->SHOW_ERRORS = 1;
	environment->SERVER_PORT = 1234;
	return environment;
}

void clear_environment(ENV *environment){
	environment->PLAYER_COUNT = 0;
	environment->BYTES_RECV_COUNT = 0;
	environment->BYTES_SEND_COUNT = 0;
	environment->MESSAGE_RECV_COUNT = 0;
	environment->MESSAGE_SEND_COUNT = 0;
	environment->ROOM_COUNT = 0;
	environment->TOTAL_PLAYER_COUNT = 0;
	environment->TOTAL_ROOM_COUNT = 0;
}

void print_variables(ENV *environment) {
	
	char statistics[1000];
	
	if(environment->LOGS_ON == 0) {
		sprintf(statistics, " Logovani do souboru je vypnuto");
		printf("%s \n", statistics);
		print_statistic(environment, statistics);
	}else {
		sprintf(statistics," Logovani do souboru zapnuto");
		printf("%s \n", statistics);
		print_statistic(environment, statistics);
	}
	
	if(environment->LIVE_LOOKING == 0) {
		sprintf(statistics," Zive sledovani je vypnuto ");
		printf("%s \n", statistics);
		print_statistic(environment, statistics);
	}else {
		sprintf(statistics," Zive sledovani je zapnuto");
		printf("%s \n", statistics);
		print_statistic(environment, statistics);
	}
	
	if(environment->SHOW_ERRORS == 0) {
		sprintf(statistics," Vypisovani chyb je vypnuto");
		printf("%s \n", statistics);
		print_statistic(environment, statistics);
	}else {
		sprintf(statistics," Vypisovani chyb je zapnuto");
		printf("%s \n", statistics);
		print_statistic(environment, statistics);
	}
	
	sprintf(statistics, " Pocet hracu na serveru: %d ", environment->PLAYER_COUNT);
	printf("%s \n", statistics);
	print_statistic(environment, statistics);
	
	sprintf(statistics, " Pocet prihlasenych hracu za cely beh: %d ", environment->TOTAL_PLAYER_COUNT);
	printf("%s \n", statistics);
	print_statistic(environment, statistics);
	
	sprintf(statistics, " Pocet mistnosti na serveru: %d ", environment->ROOM_COUNT);
	printf("%s \n",statistics);
	print_statistic(environment, statistics);
	
	sprintf(statistics, " Pocet vytvorenych mistnosti za cely beh: %d ", environment->TOTAL_ROOM_COUNT);
	printf("%s \n", statistics);
	print_statistic(environment, statistics);
	
	sprintf(statistics, " Pocet odeslanych zprav ze serveru: %d ", environment->MESSAGE_SEND_COUNT);
	printf("%s \n", statistics);
	print_statistic(environment, statistics);
	
	sprintf(statistics, " Pocet prijatych zprav na serveru: %d ", environment->MESSAGE_RECV_COUNT);
	printf("%s \n", statistics);
	print_statistic(environment, statistics);
	
	sprintf(statistics, " Pocet odeslanych bytu: %d", environment->BYTES_SEND_COUNT);
	printf("%s \n", statistics);
	print_statistic(environment, statistics);
	
	sprintf(statistics, " Pocet prijatych bytu: %d ", environment->BYTES_RECV_COUNT);
	printf("%s \n", statistics);
	print_statistic(environment, statistics);	
	
	sprintf(statistics, " Port serveru: %d", environment->SERVER_PORT);
	printf("%s \n", statistics);
	print_statistic(environment, statistics);
	print_statistic(environment, "---------------------------------------------------------");
		
}

void print_error(ENV *environment, char *error) {
	char errorLine[100];
	sprintf(errorLine, "ERROR: %s", error);
	
	if(environment->SHOW_ERRORS == 1) {
		printf("\n %s", errorLine);
	}
	
	if(environment->LOGS_ON == 1){
		write_line(errorLine, LOG_FILE);
		write_line(errorLine, ERROR_FILE);
	}
}

void print_log(ENV *environment, char *log){
	if(environment->LIVE_LOOKING == 1){
		printf("\n %s", log);
	}
	
	if(environment->LOGS_ON == 1){
		write_line(log, LOG_FILE);
	}
}

void print_statistic(ENV *environment, char *statistics) {
	if(environment->LOGS_ON == 1) {
		write_line(statistics, STATISTIC_FILE);
	}
}

char *getline() {
    char * line = malloc(100), * linep = line;
    size_t lenmax = 100, len = lenmax;
    int c;

    if(line == NULL)
        return NULL;

    for(;;) {
        c = fgetc(stdin);
        if(c == EOF)
            break;

        if(--len == 0) {
            len = lenmax;
            char * linen = realloc(linep, lenmax *= 2);

            if(linen == NULL) {
                free(linep);
                return NULL;
            }
            line = linen + (line - linep);
            linep = linen;
        }

        if((*line++ = c) == '\n')
            break;
    }
    *line--;
    *line = '\0';
    return linep;
}

void print_avaible_orders() {
	printf("Dostupne jsou nasledujici prikazy: \n");
	printf("    exit 	- vypne server\n");
	printf("    show p 	- vypise aktualni profily hracu\n");
	printf("    logs on 	- zapne zapisovani do souboru\n");
	printf("    logs off	- vypne zapisovani do souboru\n");
	printf("    live on 	- zapne zive sledovani prenosu\n");
	printf("    live off 	- vypne zive sledovani prenosu\n");
	printf("    error on  	- zapne zobrazovani chyb\n");
	printf("    error off  	- vypne zobrazovani chyb\n");
	printf("    help  	- zobrazi dostupe prikazy\n");
	printf("    chport 	- zmena portu\n");
	printf("    show all 	- ukaze aktualni stav serveru\n");
	printf("    reset 	- vynuluje statistiky\n");
	printf("    clear 	- vynuluje statistiky\n");
}

void functions(char *order, PLAYERS *first, ENV *environment){

	int result = -1;
	char *trimString = trim_whitespace(order);
	
	if(order == NULL || strlen(trimString) == 0) {	
		free(order);	
		return;
	}
	
	if ((result = strcmp(trimString, "exit")) == 0){
		printf ("***** vypinam server ******\n");
		write_line("***** vypinam server ******", LOG_FILE);
		clear_players(first);
		print_variables(environment);
		free(order);
		exit(42);
		
	} else if ((result = strcmp(trimString, "show p")) == 0){
		print_players(first);
		
	} else if ((result = strcmp(trimString, "show all")) == 0){
		print_variables(environment);
		
	} else if ((result = strcmp(trimString, "logs on")) == 0){
		environment->LOGS_ON = 1;
		printf(" Logovani do souboru bylo zapnuto. \n");
		
	} else if ((result = strcmp(trimString, "logs off")) == 0){
		environment->LOGS_ON = 0;
		printf(" Logovani do souboru bylo vypnuto. \n");
		
	} else if ((result = strcmp(trimString, "live on")) == 0){
		environment->LIVE_LOOKING = 1;
		environment->SHOW_ERRORS = 1;
		printf(" Zive sledovani bylo zapnuto. \n");
		
	} else if ((result = strcmp(trimString, "live off")) == 0){
		environment->LIVE_LOOKING = 0;
		printf(" Zive sledovani bylo vypnuto. \n");
		
	} else if ((result = strcmp(trimString, "error on")) == 0){
		environment->SHOW_ERRORS = 1;
		printf(" Zobrazovani chyb bylo zapnuto. \n");
		
	} else if ((result = strcmp(trimString, "error off")) == 0){
		environment->SHOW_ERRORS = 0;
		printf(" Zobrazovani bylo vypnuto. \n");
		
	} else if ((result = strcmp(trimString, "help")) == 0){
		print_avaible_orders();
		
	} else if ((result = strcmp(trimString, "clear")) == 0){
		system("@cls||clear");
		
	} else if ((result = strcmp(trimString, "chport")) == 0){
		printf(" Zadejte cislo portu: ");
		int port = atoi(getline());
		if(port > 0 && port <= 65535){
			environment->SERVER_PORT = port;
		}
		
	} else if ((result = strcmp(trimString, "reset")) == 0){
		clear_environment(environment);		
		write_line(" statistiky byly resetovany", LOG_FILE);
		printf("statistiky byly resetovany\n");
	}
	
	free(order);
	if(result != 0) {
		printf("Prikaz nebyl rozpoznan! Pro dostupne prikazy napiste \"help\" \n");
	}
	
}



char *trim_whitespace(char *str) {
	
	
	
  char *end;

  while(isspace((unsigned char)*str)) str++;

  if(*str == 0)  
    return str;

  end = str + strlen(str) - 1;
  while(end > str && isspace((unsigned char)*end)) end--;

  *(end+1) = 0;

  return str;
}
