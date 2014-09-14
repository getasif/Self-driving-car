/*
 * compass.cpp
 *
 ** Updated on: 09/02/2014
 *      Author: Asif N
 */

#include "compass.hpp"

float Compass_Degrees;                             // Global variable to store Compass heading
extern geo_data_t periodic_geo_data;

Compass::Compass(uint8_t priority) :
   scheduler_task("Compass", 4 * 512, priority),i2c2_device(I2C_Addr)
{
    setRunDuration(250);
    if(checkDeviceResponse())
    {
       writeReg(Config_Reg_A,_75HZ|eight_samples|no_bias);     //configuring compass: frequency=75 Hz,samples=8,bias=no bias.
       writeReg(Config_Reg_B,no_bias);                         //configuring amount of bias as zero.
       writeReg(Mode_reg,continuous);                          //configuring compass in continuous mode.
    }
}


bool Compass::run(void *p)
    {
       float filter=0;
       {
          for(short i=0;i<1;i++)
          {
             if(checkstatus())
             {
                 filter +=get_Compass_HeadingDegree();
             }
          }
          periodic_geo_data.current_angle= get_Zone(filter);
          printf(" current zone %d\n",periodic_geo_data.current_angle);
       }

      return true;
    }

int Compass::get_Zone(float Heading)
{
    int j,zone=0;

    for(j=0;j<20;j++)
    {
        if(Heading>18*j && Heading<=18*(j+1))
        {
            zone=j+1;
            break;
        }
    }

    return zone;
}


float Compass::get_Compass_HeadingDegree(void)
{
    float headingDegrees=0;

    int16_t readvalue_16_X =0;
    readvalue_16_X = readReg(Dataout_X_MSB);
    readvalue_16_X = (readvalue_16_X<<8);
    readvalue_16_X |= readReg(Dataout_X_lSB);

    int16_t readvalue_16_Y =0;
    readvalue_16_Y = readReg(Dataout_Y_MSB);
    readvalue_16_Y = (readvalue_16_Y<<8);
    readvalue_16_Y |= readReg(Dataout_Y_lSB);

    int16_t readvalue_16_Z =0;
    readvalue_16_Z = readReg(Dataout_Z_MSB);
    readvalue_16_Z = (readvalue_16_Z<<8);
    readvalue_16_Z |= readReg(Dataout_Z_lSB);

    float heading = atan2(readvalue_16_Y,readvalue_16_X);
    // Calculate heading when the magnetometer is level, then correct for signs of axis.
    // the equation is derived by performing statistical analysis using regression method.

    if(heading>0)
        headingDegrees = 7.670564116 + 51.29603108*heading;
    else
        headingDegrees = 5 + 357.6307473 + 64.51892397*heading;

    return headingDegrees;
}


