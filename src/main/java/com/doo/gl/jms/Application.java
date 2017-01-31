package com.doo.gl.jms;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableJms
public class Application {

    public static final String CONTAINER_FACTORY = "factory";

    @Autowired
    private ApplicationContext context;

    public static void main(String args[]) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        JmsTemplate template = context.getBean(JmsTemplate.class);

        template.convertAndSend(Receiver.LISTENER_NAME, new Email("from", "to"));
    }

    @Bean
    public KahaDBPersistenceAdapter persistenceAdapter() {
        KahaDBPersistenceAdapter persistenceAdapter = new KahaDBPersistenceAdapter();
        persistenceAdapter.setDirectory(new File("C:\\Users\\David\\Documents\\Intellij"));
        return persistenceAdapter;
    }

    @Bean
    public BrokerService brokerService() throws IOException {
        BrokerService brokerService = new BrokerService();
        brokerService.setPersistenceAdapter(persistenceAdapter());
        return brokerService;
    }

    @Bean(name = CONTAINER_FACTORY)
    public JmsListenerContainerFactory<?> myFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer
    ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}
