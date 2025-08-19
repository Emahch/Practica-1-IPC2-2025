package com.joca.services.reports;

import com.joca.model.reports.ReportDTO;
import com.joca.model.reports.ReportTypeEnum;
import java.util.List;

/**
 *
 * @author joca
 */
public class ReportConverter {
    private ReportTypeEnum type;
    private List<ReportDTO> reportRows;
    private String title;
    
    private final String HEADER = """
                                <!DOCTYPE html>
                                <html>
                                    <head>
                                        <title>Reporte</title>
                                        <meta charset="UTF-8">
                                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
                                    </head>
                                    <body class="bg-dark">
                                  """;
    private final String AFTER_TITLE = """
                                       <div class="container">
                                                   <table class="table table-dark table-hover">
                                                       <thead>
                                                           <tr>
                                       """;
    private final String FOOTER = """
                                                </tbody>
                                              </table>
                                          </div>
                                      </body>
                                  </html>""";

    public ReportConverter(ReportTypeEnum type, List<ReportDTO> reportRows, String title) {
        this.type = type;
        this.reportRows = reportRows;
        this.title = "<h1 class='text-center text-light m-4'>" + title + "</h1>\n";
    }
    
    public String getHtml() {
        String html = HEADER + title +  AFTER_TITLE;
        for (String columnName : type.getModel()) {
            html = html + "<th>" + columnName + "</th>\n";
        }
        html = html + "</tr>\n" +
"                </thead>\n" +
"                <tbody>\n";
        String rows = "";
        for (ReportDTO reportRow : reportRows) {
            rows = rows + "<tr>\n";
            for (String value : reportRow.getAsRow()) {
                rows = rows + "<td>" + value + "</td>\n";
            }
            rows = rows + "</tr>\n";
        }
        html = html + rows + FOOTER;
        return html;
    }

}
