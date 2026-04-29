package com.automotiva.estetica.rick.consumidor_ordem_servico.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.calendar.Calendar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.util.List;

@Configuration
public class GoogleCalendarConfig {

    @Bean
    public Calendar calendarService() throws Exception {

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ClassPathResource("calendar-service-account.json").getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/calendar"));

        HttpRequestInitializer requestInitializer =
                new HttpCredentialsAdapter(credentials);

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                requestInitializer
        )
                .setApplicationName("agendamento-rick")
                .build();
    }
}
