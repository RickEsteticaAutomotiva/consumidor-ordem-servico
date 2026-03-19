package com.automotiva.estetica.rick.consumidor_ordem_servico.adapter;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Component
public class GoogleCalendarConexao {

    @Value("${google.calendar.credentials.file}")
    private String arquivoCredenciais;

    @Value("${google.calendar.application.name}")
    private String nomeAplicacao;

    private Calendar servicoCalendario;

    @PostConstruct
    public void inicializar() {
        try {
            this.servicoCalendario = criarServicoCalendario();
            log.info("Google Calendar inicializado com Service Account");
        } catch (Exception e) {
            log.error("Erro ao inicializar Google Calendar", e);
            throw new RuntimeException(e);
        }
    }

    public Calendar obterServico() {
        return servicoCalendario;
    }

    private Calendar criarServicoCalendario() throws Exception {

        InputStream in = new ClassPathResource(
                arquivoCredenciais.replace("classpath:", "")
        ).getInputStream();

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(in)
                .createScoped(List.of("https://www.googleapis.com/auth/calendar"));

        HttpRequestInitializer requestInitializer =
                new HttpCredentialsAdapter(credentials);

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer
        )
                .setApplicationName(nomeAplicacao)
                .build();
    }
}