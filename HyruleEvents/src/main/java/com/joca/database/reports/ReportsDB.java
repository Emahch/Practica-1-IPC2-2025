package com.joca.database.reports;

import com.joca.database.DBConnection;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterDTO;
import com.joca.model.reports.ActivityReportDTO;
import com.joca.model.reports.ParticipantReportDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joca
 */
public class ReportsDB extends DBConnection {
    
    public List<ParticipantReportDTO> participantReport(List<Filter> filters) throws SQLException, InvalidRequisitesException {
        String query = """
                       SELECT p.email as email, p.type as type, p.name as name, p.institution as institution,
                           CASE 
                               WHEN r.status = 'VALIDADA' THEN 'Si' 
                               ELSE 'No'
                           END AS validated
                       FROM participant_event_registration r 
                       JOIN participant p ON p.email = r.participant_email
                       JOIN event e ON r.event_id = e.id 
                       """;
        FilterDTO filterDTO = processFilters(filters, query);
        List<ParticipantReportDTO> reports = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(filterDTO.getQueryWithFilters())) {
            for (int i = 0; i < filterDTO.getValues().length; i++) {
                st.setObject(i + 1, filterDTO.getValues()[i]);
            }

            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    ParticipantReportDTO prdto = new ParticipantReportDTO();
                    prdto.setEmail(result.getString("email"));
                    prdto.setName(result.getString("name"));
                    prdto.setType(result.getString("type"));
                    prdto.setInstitution(result.getString("institution"));
                    prdto.setValidated(result.getString("validated"));
                    reports.add(prdto);
                }
            }
        }
        return reports;
    }
    
    public List<ActivityReportDTO> activityReport(List<Filter> filters) throws SQLException, InvalidRequisitesException {
        String query = """
                       SELECT 
                           a.id,
                           e.id,
                           a.title,
                           p.name,
                           a.start_time,
                           a.max_capacity,
                           COUNT(at.activity_id) AS current_capacity
                       FROM event e
                       JOIN activity a ON e.id = a.event_id
                       JOIN participant p ON a.speaker_email = p.email
                       LEFT JOIN participant_activity_attendance at ON a.id = at.activity_id 
                       """;
        String group = " GROUP BY a.id, e.id, a.title, p.name, a.start_time, a.max_capacity";
        FilterDTO filterDTO = processFilters(filters, query);
        List<ActivityReportDTO> reports = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement st = connection.prepareStatement(filterDTO.getQueryWithFilters() + group)) {
            for (int i = 0; i < filterDTO.getValues().length; i++) {
                st.setObject(i + 1, filterDTO.getValues()[i]);
            }

            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    ActivityReportDTO ardto = new ActivityReportDTO();
                    ardto.setActivityId(result.getString("a.id"));
                    ardto.setEventId(result.getString("e.id"));
                    ardto.setTitle(result.getString("a.title"));
                    ardto.setNameSpeaker(result.getString("p.name"));
                    LocalTime startTime = result.getTime("a.start_time").toLocalTime();
                    ardto.setStartHour(startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                    ardto.setMaxCapacity(result.getString("a.max_capacity"));
                    ardto.setCurrentCapacity(result.getString("current_capacity"));
                    reports.add(ardto);
                }
            }
        }
        return reports;
    }
}
