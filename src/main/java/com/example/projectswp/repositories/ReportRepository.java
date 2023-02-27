package com.example.projectswp.repositories;

import com.example.projectswp.model.Items;
import com.example.projectswp.model.Reports;

import com.example.projectswp.repositories.rowMapper.ReportsRowMapper;
import com.example.projectswp.repositories.ultil.Ultil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
@Repository
public class ReportRepository {
    private static final ReportsRowMapper REPORT_ROW_MAPPER = new ReportsRowMapper();
    @Autowired
    JdbcTemplate jdbcTemplate;
    public Date getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return new Date(dtf.format(now));
    }
    public Reports getReport(int reportID) {
        String sql = "select * from Reports where ReportID = ?";
        List<Reports> report = jdbcTemplate.query(sql,REPORT_ROW_MAPPER, reportID);
        return report.size() != 0? report.get(0): null;
    }
    public List<Reports> getReports() {
        String sql = "Select * from Reports";
        List<Reports> report = jdbcTemplate.query(sql,REPORT_ROW_MAPPER);
        return report.size() != 0? report: null;
    }
    public Reports getLastReport() {
        List<Reports> list = getReports();
        return list.size() != 0 ? list.get(list.size()-1) : null;
    }
    public boolean addReport(Reports report) {
        String sql = "insert into dbo.Reports ([ItemID], [Report_Date_Create], [Report_Date_Update], [Report_Status], [Report_Content], Image)\n" +
                "values (?, ?, ?, ?, ?, ?)";
        int rowAffected = jdbcTemplate.update(sql, report.getItemID(), Ultil.getCurrentDate(), null, report.isStatus(), report.getContent(), report.getImage());
        return rowAffected > 0;
    }

    public boolean updateReport(Reports report) {
        String sql = "update dbo.Reports\n" +
                "set Image = ?,\n" +
                "    Report_Date_Update = ?,\n" +
                "    Report_Content = ?\n" +
                "where ReportID = ?";
        int rowAffected = jdbcTemplate.update(sql, report.getImage(), Ultil.getCurrentDate(), report.getContent(), report.getReportID());
        return rowAffected > 0;
    }
    public boolean deleteReport(int reportID){
        String sql = "DELETE dbo.Reports WHERE ReportID = ?";
        int check = jdbcTemplate.update(sql, reportID);
        return check > 0;
    }

    public boolean updateReportStatus(int reportID) {
        String sql = "update dbo.Reports\n" +
                "set Report_Status = 0\n" +
                "where ReportID = ?";
        int rowAffected = jdbcTemplate.update(sql, reportID);
        return rowAffected > 0;
    }

}
