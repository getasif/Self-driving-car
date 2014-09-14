/*
 * subscription_task.cpp
 *
 *  Created on: Oct 6, 2014
 *      Author: admin
 */


#include <string.h>

#include "subscription_task.hpp"
#include "file_logger.h"
#include "scheduler_task.hpp"



/// Shared list of vector pointers between all of the instances of canSubsribedMsgTask (s)
VECTOR<subscribedMsg_t> *canSubscribedMsgTask::mpSubsList[msgRateLast] = { 0 };

/// Extern methods (defined at another *.c file)
extern "C" uint32_t make_id(uint8_t dst, uint16_t msg_num);



canSubscribedMsgTask::
canSubscribedMsgTask(msgRate_t rate,                ///< The message rate of this task
                     VECTOR<subscribedMsg_t> *ptr,  ///< The subscription message list to send
                     uint8_t priority               ///< The priority of this task
                     ) :
    scheduler_task("sendMsg", 3 * 512, priority),   ///< Default constructor calls
    mMsgRate(msgRate1Hz),                           ///< Store our message rate
    mpSubsMsgList(ptr)                              ///< Store our subscription list pointer
{
    /* Set the task rate to call the run() method */
    setRunDuration(msgRateToFreqMs(mMsgRate));

    /* Add our subscribed message list pointer to the list such that
     * addSubscribedMsg() method can add subscribed messages to our list.
     */
    mpSubsList[msgRateToIndex(mMsgRate)] = mpSubsMsgList;
}

bool canSubscribedMsgTask::run(void *p)
{
    can_msg_t msg;
    subscribedMsg_t subsMsg;
    const can_t canbusNum = can1;  /* CAN Bus to use */
    const uint32_t timeoutMs = 50; /* Some reasonable time */

    for (unsigned int i = 0; i < mpSubsMsgList->size(); i++)
    {
        /* Get the item pointer from the list */
        subsMsg = (*mpSubsMsgList) [i];

        /* Copy the CAN message from the CAN message pointer */
        memset(&msg, 0, sizeof(msg));
        msg = *(subsMsg.canMsgPtr);

        /* Form the message ID that we need to use */
        msg.msg_id = make_id(subsMsg.dstAddr, subsMsg.msgNum);

        /* We must be able to at least queue the message without a timeout otherwise
         * either the CAN Bus is over-utilized or our queue sizes are too small.
         */
        if (!CAN_tx(canbusNum, &msg, timeoutMs)) {
            LOG_ERROR("Error sending message from %uHz task within %u ms",
                      msgRateToFreqMs(mMsgRate), timeoutMs);
        }
    }

    return true;
}

bool canSubscribedMsgTask::addSubscribedMsg(msgRate_t rate, uint8_t dst, uint16_t msgNum, can_msg_t *pCanMsg)
{
    bool ok = false;

    /* Populate the fields of the subscribed message */
    subscribedMsg_t subsMsg;
    subsMsg.dstAddr = dst;
    subsMsg.msgNum = msgNum;
    subsMsg.canMsgPtr = pCanMsg;

    /* Look up the vector for this msgRate_t(rate), and check if it has capacity */
    VECTOR<subscribedMsg_t> *vectorPtr =  mpSubsList[msgRateToIndex(rate)];

    if (! (ok = (NULL != vectorPtr))) {
        LOG_ERROR("Vector pointer for %uHz task was NULL, was the task created?",
                   msgRateToFreqMs(rate));
    }
    else {
        /* Add the subscription message to our list or if no capacity, log an error*/
        if ((ok = (vectorPtr->size() < vectorPtr->capacity()))) {
            /* TODO: Check the vector to avoid double subscription, so make sure the
             *       vector doesn't contain the same message number and destination addr
             */
            (*vectorPtr).push_back(subsMsg);
        }
        else {
            LOG_ERROR("List capacity for %uHz task has exceeded maximum subscriptions",
                      msgRateToFreqMs(rate));
        }
    }

    return ok;
}

