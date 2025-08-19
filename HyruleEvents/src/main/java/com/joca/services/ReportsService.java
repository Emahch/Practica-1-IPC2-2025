package com.joca.services;

import com.joca.database.reports.ReportsDB;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.filter.Filter;
import com.joca.model.reports.ActivityReportDTO;
import com.joca.model.reports.ParticipantReportDTO;
import com.joca.model.reports.ReportTypeEnum;
import com.joca.services.reports.ReportConverter;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author joca
 */
public class ReportsService {

    public String getParticipantReport(List<Filter> filters, String eventId) throws SQLException, InvalidRequisitesException {
        ReportsDB reportsDB = new ReportsDB();
        List<ParticipantReportDTO> reportsRows = reportsDB.participantReport(filters);
        ReportConverter reportConverter = new ReportConverter(ReportTypeEnum.PARTICIPANTE, List.copyOf(reportsRows), "Reporte de evento: " + eventId);
        return reportConverter.getHtml();
    }
    
    public String getActivitiesReport(List<Filter> filters, String eventId) throws SQLException, InvalidRequisitesException {
        ReportsDB reportsDB = new ReportsDB();
        List<ActivityReportDTO> reportsRows = reportsDB.activityReport(filters);
        ReportConverter reportConverter = new ReportConverter(ReportTypeEnum.ACTIVIDAD, List.copyOf(reportsRows), "Reporte de actividades: " + eventId);
        return reportConverter.getHtml();
    }
}
