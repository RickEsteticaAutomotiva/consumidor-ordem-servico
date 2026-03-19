package com.automotiva.estetica.rick.consumidor_ordem_servico.adapter;

import com.automotiva.estetica.rick.consumidor_ordem_servico.exception.IntegracaoException;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioEventoRequest;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioEventoResponse;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioPort;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalendarioAdapter implements CalendarioPort {

    private final GoogleCalendarConexao conexao;

    private static final String CALENDARIO = "primary";
    private static final DateTimeFormatter RFC3339 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    // Formatador para pt-BR
    private static final DateTimeFormatter PT_BR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));

    @Override
    public CalendarioEventoResponse criarEvento(CalendarioEventoRequest request) {
        try {
            Calendar servico = conexao.obterServico();
            Event evento = montarEvento(request);
            CalendarioEventoResponse response = toResponse(servico.events().insert(CALENDARIO, evento).execute());
            log.info("Evento criado com sucesso no Google Calendar. ID: {}", response.getId());
            return response;
        } catch (IOException e) {
            log.error("Erro ao criar evento no Google Calendar: {}", e.getMessage());
            throw IntegracaoException.builder()
                    .mensagem("Falha ao criar evento no Google Calendar")
                    .detalhes(e.getMessage())
                    .build();
        }
    }

    // ─── helpers ──────────────────────────────────────────────────────────────

    private CalendarioEventoResponse toResponse(Event event) {
        Instant inicio = event.getStart() != null && event.getStart().getDateTime() != null
                ? Instant.ofEpochMilli(event.getStart().getDateTime().getValue())
                : null;
        Instant fim = event.getEnd() != null && event.getEnd().getDateTime() != null
                ? Instant.ofEpochMilli(event.getEnd().getDateTime().getValue())
                : null;
        return CalendarioEventoResponse.builder()
                .id(event.getId())
                .titulo(event.getSummary())
                .descricao(event.getDescription())
                .localizacao(event.getLocation())
                .dataHoraInicio(inicio)
                .dataHoraFim(fim)
                .build();
    }

    private Event montarEvento(CalendarioEventoRequest request) {
        String fuso = request.getFusoHorario() != null ? request.getFusoHorario() : "America/Sao_Paulo";

        ZonedDateTime inicio = request.getDataHoraInicio().atZone(ZoneId.of(fuso));
        ZonedDateTime fim = request.getDataHoraFim().atZone(ZoneId.of(fuso));

        // Formatar descrição com datas em pt-BR
        String descricaoFormatada = formatarDescricaoEmPtBr(request.getDescricao());

        Event evento = new Event()
                .setSummary(request.getTitulo())
                .setDescription(descricaoFormatada)
                .setLocation(request.getLocalizacao());

        evento.setStart(new EventDateTime()
                .setDateTime(new DateTime(RFC3339.format(inicio)))
                .setTimeZone(fuso));
        evento.setEnd(new EventDateTime()
                .setDateTime(new DateTime(RFC3339.format(fim)))
                .setTimeZone(fuso));

        Event.ExtendedProperties extendedProperties = new Event.ExtendedProperties();
        extendedProperties.setPrivate(Map.of("idOrdemServico", request.getIdOrdemServico()));
        evento.setExtendedProperties(extendedProperties);

        if (request.getEmailsParticipantes() != null && !request.getEmailsParticipantes().isEmpty()) {
            var participantes = request.getEmailsParticipantes().stream()
                    .map(e -> new EventAttendee().setEmail(e))
                    .toList();
            evento.setAttendees(participantes);
        }

        if (request.getVisibilidade() != null) {
            evento.setVisibility(request.getVisibilidade());
        }
        if (request.getTransparencia() != null) {
            evento.setTransparency(request.getTransparencia());
        }

        return evento;
    }

    /**
     * Formata datas ISO 8601 (2026-03-20T14:30:00) para formato pt-BR (20/03/2026 14:30:00)
     *
     * @param descricao Descrição contendo possíveis datas em formato ISO
     * @return Descrição com datas formatadas em pt-BR
     */
    private String formatarDescricaoEmPtBr(String descricao) {
        if (descricao == null || descricao.isEmpty()) {
            return descricao;
        }

        // Padrão para encontrar datas ISO: YYYY-MM-DDTHH:MM:SS
        Pattern pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})");
        Matcher matcher = pattern.matcher(descricao);

        StringBuffer resultado = new StringBuffer();
        while (matcher.find()) {
            String ano = matcher.group(1);
            String mes = matcher.group(2);
            String dia = matcher.group(3);
            String hora = matcher.group(4);
            String minuto = matcher.group(5);
            String segundo = matcher.group(6);

            // Formata para pt-BR: DD/MM/YYYY HH:MM:SS
            String dataFormatada = String.format("%s/%s/%s %s:%s:%s", dia, mes, ano, hora, minuto, segundo);
            matcher.appendReplacement(resultado, Matcher.quoteReplacement(dataFormatada));
        }
        matcher.appendTail(resultado);

        return resultado.toString();
    }
}
