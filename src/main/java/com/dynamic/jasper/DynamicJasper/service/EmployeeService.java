package com.dynamic.jasper.DynamicJasper.service;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Page;
import com.dynamic.jasper.DynamicJasper.model.Employee;
import com.dynamic.jasper.DynamicJasper.repository.EmployeeRepository;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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


    public String fastReportbuilder() throws ClassNotFoundException, JRException {

        //  FETCHING ALL RECORDS OF EMPOLYEE FROM DB
        List<Employee> allEmployeeList = this.getAllData();

        int numberOfRows = allEmployeeList.size();
        int numberOfColumns = Employee.class.getDeclaredFields().length;    // GETS THE NUMBER OF FIELDS DECLARED IN EMPLOYEE MODEL CLASS
        FastReportBuilder fastReportBuilder = new FastReportBuilder();
        Page page = Page.Page_A4_Portrait();
        fastReportBuilder.setTitle("Employee Records")
                .setSubtitle("This report was generated at " + new Date())
                .setPageSizeAndOrientation(page)
                .setUseFullPageWidth(true)
                .setPrintBackgroundOnOddRows(true);


        for (int column = 1; column <= numberOfColumns; column++) {
            fastReportBuilder.addColumn("Column " + column, "key" + column,
                    String.class.getName(),
                    30);
        }

        List rowsDataList = this.createListOfHash(numberOfRows, numberOfColumns, allEmployeeList);
//        List rowsDataList = new ArrayList();
//
//        for (int row = 1; row <= numberOfRows; row++) {
//            HashMap<String, String> rowHashMap = new HashMap<>();
//            HashMap<Integer, Objec> rowHashMap = new HashMap<>();
//            for (int column = 1; column <= numberOfColumns; column++) {
//                rowHashMap.put("key" + column,
//                " Row " + row + " Column " + column);
//            }
//            rowsDataList.add(rowHashMap);
//        }


        DynamicReport dynamicReport = fastReportBuilder.build();
        JasperPrint finalReport = DynamicJasperHelper.generateJasperPrint(dynamicReport,
                new ClassicLayoutManager(),
                rowsDataList);

        JasperViewer.viewReport(finalReport);

        // EXPORTING INTO PDF FORMAT
//        JasperExportManager.exportReportToPdfFile(finalReport, "testingInvoice.pdf");
//        this.PDFreportRetrun(finalReport);
        return "PDF Generated Successfully!";
    }


    public String dynamicReportBuilder() throws ClassNotFoundException, JRException {
        //  FETCHING ALL RECORDS OF EMPOLYEE FROM DB
        List<Employee> allEmployeeList = this.getAllData();

        int numberOfRows = allEmployeeList.size();
        int numberOfColumns = Employee.class.getDeclaredFields().length;    // GETS THE NUMBER OF FIELDS DECLARED IN EMPLOYEE MODEL CLASS

        FastReportBuilder drb = new FastReportBuilder();
        DynamicReport dr = drb.addColumn("ID", "eid", Long.class.getName(),10)
                .addColumn("Designation", "designation", String.class.getName(),30)
                .addColumn("DOJ", "doj", String.class.getName(),50)
                .addColumn("Name", "name", String.class.getName(),50)
                .addColumn("Salary", "salary", Long.class.getName(),50,true)
                .addGroups(2)
                .setTitle("August 2021, Employee Detail Report")
                .setSubtitle("This report was generated at " + new Date())
                .setPrintBackgroundOnOddRows(true)
                .setUseFullPageWidth(true)
                .build();

        JRDataSource ds = new JRBeanCollectionDataSource( allEmployeeList );
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
        JasperViewer.viewReport(jp);    //finally display the report report

        return "Report Geenrated";
    }











    private List createListOfHash(int numberOfRows, int numberOfColumns, List<Employee> allEmployeeList){

        List rowsDataList = new ArrayList();

        for (int row = 0; row < numberOfRows; row++) {
            HashMap<String, String> rowHashMap = new HashMap<>();
            String[] fields = allEmployeeList.get(row).getPropConcat().split("@",5);
            for (int column = 1; column <= numberOfColumns; column++) {
                rowHashMap.put("key" + column,
                        fields[column-1]);
            }
            rowsDataList.add(rowHashMap);
        }
        return rowsDataList;
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
