/*
 * controller_geo.cpp
 *
 *  Updated on: 09/02/2014
 *      Author: Asif N
 */

#include "file_logger.h"
#include "io.hpp"
#include "stdio.h"
#include "lpc_sys.h"
#include "string.h"
#include "compass.hpp"
#include "gps.hpp"

#include "controller_geo.hpp"

static can_msg_id_t filter_list[] = {
        {CONTROLLER_MASTER,         CONTROLLER_ALL,     MSG_RESET},
        {CONTROLLER_IO,             CONTROLLER_ALL,     MSG_RESET},
        {CONTROLLER_MASTER,         CONTROLLER_ALL,     MSG_POWERUP_SYN},
        {CONTROLLER_MASTER,         CONTROLLER_ALL,     MSG_POWERUP_ACK},
        {CONTROLLER_MASTER,         CONTROLLER_ALL,     MSG_HEARTBEAT},
        {CONTROLLER_BT_ANDROID,     CONTROLLER_GEO,     MSG_CHECKPOINT_DATA},
};

can_controller controller(CONTROLLER_GEO, filter_list, sizeof(filter_list) / sizeof(can_msg_id_t));
static QueueHandle_t msg_queue = xQueueCreate(MSG_QUEUE_LEN, sizeof(msg_t));

/* TODO: populate this variable to send periodic data */
geo_data_t periodic_geo_data;

checkpoint_data_t current_checkpoint;
checkpoint_data_t next_checkpoint;
checkpoint_request_data_t checkpoint_number;


class periodicGeoTask : public scheduler_task
{
    public:
        periodicGeoTask(uint8_t priority) : scheduler_task("periodicGeoTask", 1024, priority)
        {
            setRunDuration(200); // in milliseconds
        }

        bool run(void *p)
        {
            controller.can_send(CONTROLLER_ALL, MSG_GEO_DATA, (uint8_t *) &periodic_geo_data, sizeof(periodic_geo_data));

            return true;
        }
};

void controllerInit(void)
{
    controller_id_t src;
    msg_id_t        msg_num;
    uint8_t         data[MAX_DATA_LEN];
    uint16_t        len;
#if 0
    LE.on(4); // LED4 will be ON until can communication is setup
    while(1) {
        len = sizeof(data);
        controller.can_recv(&src, &msg_num, data, &len);
        if(msg_num == MSG_POWERUP_SYN) {
            data[0] = CONTROLLER_VERSION;
            controller.can_send(src, MSG_POWERUP_SYN_ACK, data, 1);
        }
        else if(msg_num == MSG_POWERUP_ACK) {
            /* TODO: set date time received in "data" */
            //********SET DATE AND TIME***********


            LE.off(4); // turn OFF LED4 after can communication is setup
            break;
        }
        else {
            /* this should not get executed */
            LE.on(1);
            LOG_ERROR("Unknown msg %s from %s received.\n", message_id_str[msg_num], controller_id_str[src]);
        }
    }
#endif
    /* TODO: start any other tasks of module here, like periodic task */
    scheduler_add_task(new periodicGeoTask(PRIORITY_CRITICAL));
    scheduler_add_task(new gpsTask (PRIORITY_HIGH));
    scheduler_add_task(new Compass (PRIORITY_HIGH));
}

canRxBufferTask :: canRxBufferTask(uint8_t priority) : scheduler_task("canRxBufferTask", 1024, priority)
{
    checkpoint_number.checkpoint_num = 1;
}

bool canRxBufferTask :: run(void *p)
{
    msg_t msg;

    msg.len = sizeof(msg.data);
    controller.can_recv(&msg.src, &msg.msg_num, msg.data, &msg.len);
    if(xQueueSend(msg_queue, &msg, 0) == errQUEUE_FULL) {
        LE.on(1);
        LOG_ERROR("msg_queue full.\n");
    }

    return true;
}

canRxProcessTask :: canRxProcessTask(uint8_t priority) : scheduler_task("canRxProcessTask", 1024, priority)
{
    /* Nothing to init */
}

bool canRxProcessTask :: run(void *p)
{
    msg_t msg;

    if(xQueueReceive(msg_queue, &msg, portMAX_DELAY) == pdFALSE) {
        return true;
    }

    switch(msg.msg_num) {
        case MSG_RESET:
            handle_reset(msg);
            break;

        case MSG_HEARTBEAT:
            handle_heartbeat(msg);
            break;

        case MSG_CHECKPOINT_DATA:
            printf("*************YAY**************");
            handle_checkpoint_data(msg);
            break;

        default:
            /* do nothing */
            break;
    }

    return true;
}

bool canRxProcessTask :: handle_reset(msg_t msg)
{
    printf("Rebooting System\n");
    LOG_FLUSH();
    vTaskDelayMs(2000);
    sys_reboot();

    return true;
}

bool canRxProcessTask :: handle_heartbeat(msg_t msg)
{
    heartbeat_ack_data_t data;

    data.rx_count = controller.get_rx_count();
    data.tx_count = controller.get_tx_count();

    controller.can_send(CONTROLLER_MASTER, MSG_HEARTBEAT_ACK, (uint8_t *) &data, sizeof(data));
    return true;
}

bool canRxProcessTask :: handle_checkpoint_data(msg_t msg)
{
    /* TODO: process incoming checkpoint data */

    checkpoint_data_t data;
    memcpy(&data, msg.data, sizeof(checkpoint_data_t));
    printf("IS NEW ROUTE %d \n",data.is_new_route);
    if (data.is_new_route)
    {
        current_checkpoint = data;
        //printf("***TEST****%d %f %f",current_checkpoint.total_distance,current_checkpoint.latitude,current_checkpoint.longitude);
    }
        else
        next_checkpoint = data;

    checkpoint_number.checkpoint_num++;

    return true;
}
