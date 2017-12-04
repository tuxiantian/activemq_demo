package com.tuxt.activemq.demo.api;

import com.tuxt.activemq.demo.producer.DeferMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tuxt on 2017/12/1.
 */
@RequestMapping("/test/")
@RestController(value = "TestApi")
public class TestApi {
    @Autowired
    private DeferMessageProducer deferMessageProducer;

    @ResponseBody
    @RequestMapping(value = "send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void send(){
        deferMessageProducer.sendMessage();
    }
}
