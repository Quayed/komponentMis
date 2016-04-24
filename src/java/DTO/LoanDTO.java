package DTO;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mathias on 21/03/16.
 */
public class LoanDTO implements Serializable {
    private int loanId;
    private ComponentDTO component;
    private StudentDTO student;
    private String loanDate;
    private String dueDate;
    private String deliveryDate;
    private String deliveredTo;
    private final DateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");;
    
    
    public LoanDTO(){
         
    }
    
    public DateFormat getFORMAT(){
        return this.FORMAT;
    }
    
    public LoanDTO(int loanId, String barcode,  String studentId, String loanDate, String dueDate, String deliveryDate, String deliveredTo) {
        this.loanId = loanId;
        this.component = new ComponentDTO();
        this.component.setBarcode(barcode);
        this.student = new StudentDTO();
        this.student.setStudentId(studentId);
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.deliveryDate = deliveryDate;
        this.deliveredTo = deliveredTo;
    }
    
    public LoanDTO(int loanId, String barcode, String studentId, Date loanDate, Date dueDate, Date deliveryDate, String deliveredTo) {
        this.loanId = loanId;
        this.component = new ComponentDTO();
        this.component.setBarcode(barcode);
        this.student = new StudentDTO();
        this.student.setStudentId(studentId);
        this.loanDate = FORMAT.format(loanDate);
        this.dueDate = FORMAT.format(dueDate);
        this.deliveryDate = (deliveryDate == null) ? null :  FORMAT.format(deliveryDate);
        this.deliveredTo = deliveredTo;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getBarcode() {
        return component.getBarcode();
    }

    public void setBarcode(String barcode) {
        if(component == null)
           component = new ComponentDTO();
        component.setBarcode(barcode);
    }

    public String getStudentId() {
        return student.getStudentId();
    }

    public void setStudentId(String studentId) {
        if(student == null)
            student = new StudentDTO();
        student.setStudentId(studentId);
    }

    public String getLoanDate() {
        return this.loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }
    
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveredTo() {
        return deliveredTo;
    }

    public void setDeliveredTo(String deliveredTo) {
        this.deliveredTo = deliveredTo;
    }
    
    public Date getLoanDateAsDate(){
        try {
            return FORMAT.parse(loanDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void setLoanDateFromDate(Date loanDate){
        this.loanDate = FORMAT.format(loanDate);
    }
    
    public Date getDueDateAsDate(){
        try {
            return FORMAT.parse(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void setDueDateFromDate(Date dueDate){
        this.dueDate = FORMAT.format(dueDate);
    }
    
    public Date getDeliveryDateAsDate(){
        try {
            return FORMAT.parse(deliveryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void setDeliveryDateFromDate(Date deliveryDate){
        this.deliveryDate = FORMAT.format(deliveryDate);
    }
}
