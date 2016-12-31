#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "fileWriter.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <direct.h>
#include <time.h>

struct stat st = {0};

void create_folder(){
	
	#if defined(_WIN32)
    	_mkdir(LOG_FOLDER);
    #else 
    	mkdir(LOG_FOLDER, 0700); 
    #endif	
}


void write_line(char *line, char *file) {
	FILE *fp;
	char path[50];
	char timeNow[50];
	
	sprintf(path,"%s/%s", LOG_FOLDER, file);
	if((fp = fopen(path, "a+")) == NULL) {
	printf("Nemohu zapsat logy do souboru %s.\n", path);
	return;
	}
	
	time_t t = time(NULL);
	struct tm tm = *localtime(&t);	
	sprintf(timeNow, "%d-%02d-%02d %02d:%02d:%02d|\t", tm.tm_year + 1900, tm.tm_mon + 1, tm.tm_mday, tm.tm_hour, tm.tm_min, tm.tm_sec);
	
	fprintf(fp, "%s %s \n", timeNow, line);
	fclose(fp);
 }
 
