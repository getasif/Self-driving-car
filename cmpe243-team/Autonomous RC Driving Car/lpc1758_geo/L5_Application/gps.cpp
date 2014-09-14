/*
 * gps.cpp
 *
 * Created on: 09/02/2014
 *      Author: Asif N
 */


#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

#include "gps.hpp"
#include "uart3.hpp"
#include "str.hpp"
#include "controller_geo.hpp"
#include "io.hpp"

//GLOBAL VARIABLES



float start_latitude;
float start_longitude;
uint16_t back_distance=0;
float desired_heading;


extern geo_data_t periodic_geo_data;
extern checkpoint_data_t current_checkpoint;
extern checkpoint_data_t next_checkpoint;
extern can_controller controller;
extern checkpoint_request_data_t checkpoint_number;

#define LOCAL_PI 3.1415926535897932385

gpsTask::gpsTask(uint8_t priority) :scheduler_task("GPS", 5*2048, priority)
{
    //UART INITIALIZATION
    Uart3 &u3= Uart3::getInstance();
    u3.init(9600,1000,10); //Baud Rate, Rx Queue size, Tx Queue Size
    setRunDuration(200);
    LD.init();
}

float gpsTask::ToRadians(float degrees)
{
    float radians = degrees * LOCAL_PI / 180;
    return radians;
}

float gpsTask::ToDegrees(float radians)
{
    float degrees = radians * 180/LOCAL_PI;
    return degrees;
}

float gpsTask::get_bearing(float lat1, float lng1, float lat2, float lng2)
{
    //float dLat = ToRadians(lat2-lat1);
    float dLng = ToRadians(lng2-lng1);

    float bearing= atan2(sin(dLng)*cos(ToRadians(lat2)),cos(ToRadians(lat1))*sin(ToRadians(lat2))-sin(ToRadians(lat1))*cos(ToRadians(lat2))*cos(dLng));

    bearing = ToDegrees(bearing);
    bearing= fmodf((bearing +360),360);
    return bearing;
}

int gpsTask::getZone(float compass_bearing)
{
    int j,zone=0;

    for(j=0;j<20;j++)
    {
        if(compass_bearing>18*j && compass_bearing<=18*(j+1))
        {
            zone=j+1;
            break;
        }
    }

    return zone;
}


float gpsTask::calculate_distance(float lat1, float lng1, float lat2, float lng2)
{

  float earthRadius = 6371; //kilometers

  float dLat = ToRadians(lat2-lat1);
  float dLng = ToRadians(lng2-lng1);

  float a = sin(dLat/2) * sin(dLat/2) +
             cos(ToRadians(lat1)) * cos(ToRadians(lat2)) * sin(dLng/2) * sin(dLng/2);

  float c = 2 * atan2(sqrt(a),sqrt(1-a));

  float dist = (double) (earthRadius * c);

  return dist*1000*0.3048;
}


bool gpsTask::run(void *p)
{
    //****FOR DEBUGGING PURPOSES
    //printf("*****%d %d %d %f %f",current_checkpoint.is_new_route,current_checkpoint.is_final_checkpoint,current_checkpoint.total_distance,current_checkpoint.latitude,current_checkpoint.longitude);

    //RECIEVE BUFFER
        char rx[300];

    //UART INITIALIZATION
        Uart3 &u33= Uart3::getInstance();
        u33.gets(rx,300,portMAX_DELAY);

        char b[]=",,";      //FIND
        char c[]=",NULL,";  //REPLACE WITH
        char delim[]=",";   //DELIMITER TO SPLIT/TOKENIZE STRING
        char *token;
        char *token_array[15];
        int i=0;

    //TO replace ,, with ,NULL, to ensure no token is missed
        str gps_string=rx;
        gps_string.replaceAll(b,c);
        gps_string.scanf("%s",&rx);

    //TO PARSE GPRMC STRING
        // get the first token
        token = strtok(rx, delim);

        // walk through other tokens
        while( token != NULL )
        {
            token_array[i]=token;
            token = strtok(NULL, delim);
            i++;
        }


         if(!strcmp(token_array[0],"$GPRMC") && (!strcmp(token_array[2],"A"))  && (!strcmp(token_array[4],"N")) && (!strcmp(token_array[6],"W")))
         {
    //GPS STATUS
             periodic_geo_data.is_valid=true;
             printf("STATUS: GPS FIXED \n");

    //****LATITUDE COORDINATES****
             char lat_degree[3]={0},lat_minutes[7]={0};
             float lat_deg,lat_min,final_lat;
             char *latitude=token_array[3];
             //printf("LATITUDE: %s\n",latitude);

             strncpy(lat_degree,latitude,2);
             //printf("%s\n",lat_degree);
             sscanf(lat_degree, "%f", &lat_deg);
             //printf("LATITUDE DEGREES: %f\n",lat_deg);

             strncpy(lat_minutes,latitude+2,7);
             sscanf(lat_minutes, "%f", &lat_min);
             //printf("LATITUDE MINUTES: %f\n",lat_min);

             final_lat=lat_deg+(lat_min/60);
             printf("FINAL LATITUDE: %f\n",final_lat);

             char *latitude_dir=token_array[4];
             //printf("LATITUDE DIRECTION: %s\n",latitude_dir);
              if(!strcmp(latitude_dir,"S"))
                 final_lat*=-1;

             periodic_geo_data.latitude=final_lat;
             printf("%f\n",periodic_geo_data.latitude);

    //****LONGITUDE COORDINATES
             char long_degree[4]={0},long_minutes[7]={0};
             float long_deg,long_min,final_long;
             char *longitude=token_array[5];
             //printf("LONGITUDE: %s\n",longitude);

             strncpy(long_degree,longitude,3);
             sscanf(long_degree, "%f", &long_deg);
             //printf("LONGITUDE DEGREES: %f\n",long_deg);

             strncpy(long_minutes,longitude+3,7);
             sscanf(long_minutes, "%f", &long_min);
             //printf("LONGITUDE MINUTES: %f\n",long_min);

             final_long=long_deg+(long_min/60);
             printf("FINAL LONGITUDE: %f\n",final_long);
             char *longitude_dir=token_array[6];
             //printf("LONGITUDE DIRECTION: %s\n",longitude_dir);
              if(!strcmp(longitude_dir,"W"))
                 final_long*=-1;

             periodic_geo_data.longitude=final_long;
             printf("%f\n",periodic_geo_data.longitude);

    //If it is the first checkpoint set start position
             if(current_checkpoint.is_new_route)
             {
              start_latitude=periodic_geo_data.latitude;
              start_longitude=periodic_geo_data.longitude;
             }

    //Calculate Desired Compass Heading
             desired_heading=get_bearing(periodic_geo_data.latitude,periodic_geo_data.longitude,current_checkpoint.latitude,current_checkpoint.longitude);
             printf("DESIRED COMPASS HEADING: %f\n",desired_heading);

    //Calculate Desired Zone
             periodic_geo_data.desired_angle=getZone(desired_heading);
             printf("ZONE: %d\n",periodic_geo_data.desired_angle);

    //Calculate Distance Travelled
             float distance_travelled=calculate_distance(start_latitude,start_longitude,periodic_geo_data.latitude,periodic_geo_data.longitude) + back_distance;

    //Calculate Distance to Destination
             periodic_geo_data.dist_to_final_destination=current_checkpoint.total_distance-distance_travelled;
             printf("TOTAL Distance: %d\n",current_checkpoint.total_distance);
             printf("Distance to Destination: %d\n",periodic_geo_data.dist_to_final_destination);
    //Calculate Distance to next checkpoint
             periodic_geo_data.dist_to_next_checkpoint=calculate_distance(periodic_geo_data.latitude,periodic_geo_data.longitude,current_checkpoint.latitude,current_checkpoint.longitude);
             printf("Distance to Checkpoint: %d\n",periodic_geo_data.dist_to_next_checkpoint);
    //Calculation when Current Checkpoint expires and to request new checkpoint
             if(periodic_geo_data.dist_to_next_checkpoint<5 )
             {
                 start_latitude=current_checkpoint.latitude;
                 start_longitude=current_checkpoint.longitude;
                 current_checkpoint=next_checkpoint;
                 back_distance+=distance_travelled;
                 if(!next_checkpoint.is_final_checkpoint)
                     controller.can_send(CONTROLLER_BT_ANDROID,MSG_CHECKPOINT_REQUEST, &checkpoint_number.checkpoint_num,(uint16_t)sizeof(checkpoint_number.checkpoint_num));
             }



         }
         else if( !strcmp(token_array[0],"$GPGGA")  )//TO PARSE GPGGA
         {
             //****NUMBER OF SATELLITES****
             if(strcmp(token_array[6],"0"))
             {char *satellites=token_array[7];
             printf("NUMBER OF SATELLITES: %s\n",satellites);
             printf("\n\n");
             int satellite=atoi(satellites);
             LD.setNumber(satellite);
             }
             else
             {
                 periodic_geo_data.is_valid=false;
             }
         }

         return true;
    }
