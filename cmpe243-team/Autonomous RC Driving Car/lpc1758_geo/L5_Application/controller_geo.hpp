/*
 * controller_geo.hpp
 *
 * updated on: 09/02/2014
 *      Author: Asif N
 */

#ifndef CONTROLLER_GEO_HPP_
#define CONTROLLER_GEO_HPP_

#include "can_common.hpp"
#include "scheduler_task.hpp"
#include "can_protocol.hpp"

#define CONTROLLER_VERSION  0x21
#define MSG_QUEUE_LEN       32

void controllerInit(void);

class canRxBufferTask : public scheduler_task
{
    public:
        canRxBufferTask(uint8_t priority);
        bool run(void *p);
};

class canRxProcessTask : public scheduler_task
{
    public:
        canRxProcessTask(uint8_t priority);
        bool run(void *p);
        bool handle_reset(msg_t msg);
        bool handle_heartbeat(msg_t msg);
        bool handle_checkpoint_data(msg_t msg);
};

#endif /* CONTROLLER_GEO_HPP_ */
