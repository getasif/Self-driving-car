/*
 * gps.hpp
 *
 *  Created on: 09/02/2014
 *      Author: Asif N
 */

#ifndef GPS_HPP_
#define GPS_HPP_

#include "scheduler_task.hpp"

extern float start_latitude;
extern float start_longitude;
extern uint16_t back_distance;
extern float desired_heading;


class gpsTask : public scheduler_task
{

    private:
        float get_bearing(float lat1, float lng1, float lat2, float lng2);
        float ToDegrees(float radians);
        float ToRadians(float degrees);
        float calculate_distance(float lat1, float lng1, float lat2, float lng2);
    public:
        gpsTask(uint8_t priority);
        int getZone(float compass_bearing);
        bool run(void *p);

};

#endif /* GPS_HPP_ */
