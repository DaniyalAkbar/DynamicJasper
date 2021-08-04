package com.dynamic.jasper.DynamicJasper.service;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Page;
import com.dynamic.jasper.DynamicJasper.model.Employee;
import com.dynamic.jasper.DynamicJasper.repository.EmployeeRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;



    public void addEmployee(Employee empObj){
        employeeRepository.save(empObj);
    }

    public List<Employee> getAllData(){
        return employeeRepository.findAll();
    }


    public String fastReportbuilder(int rowCount, int columnCount) throws ClassNotFoundException, JRException {
        int numberOfRows = rowCount;
        int numberOfColumns = columnCount;
        FastReportBuilder reportBuilder = new FastReportBuilder();
        Page page = Page.Page_A4_Landscape();
        reportBuilder.setTitle("Table Name")
                .setPageSizeAndOrientation(page)
                .setUseFullPageWidth(true)
                .setReportName("Report Name");

        for (int column = 1; column <= 4; column++) {
            reportBuilder.addColumn("Column " + column, "key" + column,
                    String.class.getName(),
                    30);
        }

        List rowsDataList = new ArrayList();

        for (int row = 1; row <= numberOfRows; row++) {
            HashMap<String, String> rowHashMap = new HashMap<>();
            for (int column = 1; column <= numberOfColumns; column++) {
                rowHashMap.put("key" + column,
                " Row " + row + " Column " + column);
            }
            rowsDataList.add(rowHashMap);
        }


        DynamicReport dynamicReport = reportBuilder.build();
        JasperPrint finalReport = DynamicJasperHelper.generateJasperPrint(dynamicReport,
                new ClassicLayoutManager(),
                rowsDataList);

        JasperViewer.viewReport(finalReport);

        // EXPORTING INTO PDF FORMAT
//        JasperExportManager.exportReportToPdfFile(finalReport, "testingInvoice.pdf");
//        this.PDFreportRetrun(finalReport);
        return "PDF Generated Successfully!";
    }

    // TO EXPORT FILE INTO PDF FORMAT
    private void PDFreportRetrun(JasperPrint finalReport) throws JRException {
        JRPdfExporter pdfExporter = new JRPdfExporter();
        ExporterInput exporterInput = new SimpleExporterInput(finalReport);
        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput("testPDF");
        pdfExporter.setExporterOutput(exporterOutput);
        pdfExporter.setExporterInput(exporterInput);


        SimplePdfReportConfiguration configuration = new SimplePdfReportConfiguration();configuration.setIgnoreHyperlink(true);
        pdfExporter.setConfiguration(configuration);


        pdfExporter.exportReport();
    }

}
