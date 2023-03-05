package com.twilio.earthday.earth.pieceone.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwilioService {

    private String accountSid = "YOUR-TWILIO-ACCOUNT-SID";

    private String authToken = "YOUR-ACCOUNT-TOKEN";

    private String fromNumber = "FROM-NUMBER";

    public void sendMessage(String toNumber, String messageBody) {
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(new PhoneNumber(toNumber),
                new PhoneNumber(fromNumber), messageBody).create();
        log.info("Message SID: " + message.getSid());
    }
}
