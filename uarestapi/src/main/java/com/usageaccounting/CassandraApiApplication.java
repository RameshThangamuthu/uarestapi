package com.usageaccounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.usageaccounting.controller.AppAndMSUsageController;
import com.usageaccounting.entity.CloudletVMUsage;

import java.util.ArrayList;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@SpringBootApplication
@ComponentScan(basePackages="com.usageaccounting")
public class CassandraApiApplication {

	static final String USAGE_ACC_EXCHANGE = "usageacc-exchange";
    static final String USAGE_ACC_QUEUE = "usageacc-queue";
    static final String USAGE_CLOUDLET_QUEUE = "usagecloudlet-queue";
    static final String USAGE_ACC_ROUTING_KEY = "usageaccounting";

    @Bean
    TopicExchange exchange() {
            return new TopicExchange(USAGE_ACC_EXCHANGE);
    }

    @Bean
    Queue testQueue() {
            return new Queue(USAGE_ACC_QUEUE);
    }

    @Bean
    Queue testQueue2() {
            return new Queue(USAGE_CLOUDLET_QUEUE);
    }
    
    @Bean
    Binding binding(Queue testQueue, TopicExchange exchange) {
    	return BindingBuilder.bind(testQueue).to(exchange).with(USAGE_ACC_ROUTING_KEY);
    }

    @Bean
    Binding binding_2(Queue testQueue2, TopicExchange exchange) {
    	return BindingBuilder.bind(testQueue2).to(exchange).with(USAGE_ACC_ROUTING_KEY);
    }
    
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter)
	{
	     SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	     container.setConnectionFactory(connectionFactory);
		 container.setQueueNames(USAGE_ACC_QUEUE);
		 container.setMessageListener(listenerAdapter);
	     return container;
	 } 

	@Bean
	SimpleMessageListenerContainer container_2(ConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter_2)
	{
	     SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	     container.setConnectionFactory(connectionFactory);
		 container.setQueueNames(USAGE_CLOUDLET_QUEUE);
		 container.setMessageListener(listenerAdapter_2);
	     return container;
	 } 
	
	@Bean
	 MessageListenerAdapter listenerAdapter(AppAndMSUsageController appAndMSUsageController) {
		return new MessageListenerAdapter(appAndMSUsageController, "appAndMSUsage_Receiver");
	}

	@Bean
	 MessageListenerAdapter listenerAdapter_2(AppAndMSUsageController appAndMSUsageController) {
		return new MessageListenerAdapter(appAndMSUsageController, "cloudletVMUsage_Receiver");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CassandraApiApplication.class, args);
	}
}
