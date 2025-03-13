package ohmygod.project.MQdeamo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class RabbitMQHandler {
    @Resource
    private MQSender mqSender;

    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("hello");
    }
    @RequestMapping("/mqfanout")
    @ResponseBody
    public void fanout(){
        mqSender.sendFanout("hello~ fanout");
    }
    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void direct01(){
        mqSender.sendDirect1("hello~ direct01,key is red");
    }
    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void direct02(){
        mqSender.sendDirect2("hello~ direct02,key is blue");
    }
    @RequestMapping("/mq/topic01")
    @ResponseBody
    public void mqTopic1(){
        mqSender.sendTopic1("hello~ topic1,key is #.queue.#");
    }

    @RequestMapping("/mq/topic02")
    @ResponseBody
    public void mqTopic2(){
        mqSender.sendTopic2("hello~ topic2,key is *.queue.#");
    }
}
