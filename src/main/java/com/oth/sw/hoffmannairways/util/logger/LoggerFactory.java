package com.oth.sw.hoffmannairways.util.logger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LoggerFactory {

    @Bean
    @Qualifier("ErrorLogger")
    public LoggerIF createErrorLogger() {
        return new ErrorLogger();
    }

    @Bean
    @Qualifier("QueueLogger")
    public LoggerIF createQueueLogger() {
        return new QueueLogger();
    }

    @Bean
    @Qualifier("DatabaseLogger")
    public LoggerIF createDatabaseLogger() {
        return new DatabaseLogger();
    }

    @Bean
    @Qualifier("SuccessLogger")
    public LoggerIF createSuccessLogger() {
        return new SuccessLogger();
    }


}
