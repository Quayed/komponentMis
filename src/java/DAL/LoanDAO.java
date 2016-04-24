package DAL;

import DTO.LoanDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mathias on 21/03/16.
 */
public class LoanDAO implements ILoanDAO {
    private final Connection CONN;
    private final String DATABASE_NAME = "Loan";

    public LoanDAO(Connection conn) {
        this.CONN = conn;
    }

    @Override
    public int createLoan(LoanDTO loan) {
        if(loan.getBarcode() == null || loan.getStudentId() == null || loan.getLoanDate() == null || loan.getDueDate() == null){
            if (loan.getStudentId() == null)
                return -3;
            if (loan.getLoanDate() == null)
                return -4;
            
            return -1;
        }
        
        String sql = "INSERT INTO " + DATABASE_NAME + "(componentId, studentId, loanDate, dueDate";
        String sqlValues = "?, ?, ?, ?";


        if (loan.getDeliveryDate() != null) {
            sql += ", deliveryDate";
            sqlValues = ", ?";
        }

        if (loan.getDeliveredTo() != null) {
            sql += ", deliveredTo";
            sqlValues += ", ?";
        }

        sql += ") VALUES(" + sqlValues + ")";

        try {
            int param = 1;
            PreparedStatement stm = CONN.prepareStatement(sql);

            stm.setString(param++, loan.getBarcode());

            stm.setString(param++, loan.getStudentId());

            stm.setDate(param++, new java.sql.Date(loan.getLoanDateAsDate().getTime()));

            stm.setDate(param++, new java.sql.Date(loan.getDueDateAsDate().getTime()));

            if (loan.getDeliveryDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getDeliveryDateAsDate().getTime()));

            if (loan.getDeliveredTo() != null)
                stm.setString(param++, loan.getDeliveredTo());

            stm.execute();

            if (stm.getUpdateCount() != 1) {
                return -2;
            } else {
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return 0;
    }

    @Override
    public LoanDTO getLoan(int loanId) {
        try {
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE loanId = ?");
            stm.setInt(1, loanId);
            ResultSet result = stm.executeQuery();
            while (result.next())
                return new LoanDTO(result.getInt("loanId"), result.getString("barcode"), result.getString("studentId"),
                        result.getDate("loanDate"), result.getDate("dueDate"), result.getDate("deliveryDate"), result.getString("deliveredTo"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public LoanDTO[] getLoans() {
        try {
            ResultSet result = CONN.createStatement().executeQuery("SELECT * FROM " + DATABASE_NAME);
            ArrayList<LoanDTO> loans = new ArrayList<>();
            while (result.next())
                loans.add(new LoanDTO(result.getInt("loanId"), result.getString("barcode"), result.getString("studentId"),
                        result.getDate("loanDate"), result.getDate("dueDate"), result.getDate("deliveryDate"), result.getString("deliveredTo")));

            // Check if something was actually found
            if(loans.size() == 0)
                return null;

            return loans.toArray(new LoanDTO[loans.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public LoanDTO[] searchLoans(String keyword) {
        String sql = "SELECT * FROM Loan l "
            +"LEFT JOIN Component c ON l.componentId = c.componentId "
            +"LEFT JOIN ComponentGroup cg ON c.componentGroupId = cg.componentGroupId "
            +"WHERE l.studentId LIKE ? OR "
            +"l.loanDate LIKE ? OR "
            +"l.dueDate LIKE ? OR "
            +"c.barcode LIKE ? OR "
            +"cg.name LIKE ?;";
        keyword = "%" + keyword + "%";
        try{
            ArrayList<LoanDTO> loans = new ArrayList<>();
            PreparedStatement stm = CONN.prepareStatement(sql);
            stm.setString(1, keyword);
            stm.setString(2, keyword);
            stm.setString(3, keyword);
            stm.setString(4, keyword);
            stm.setString(5, keyword);
            ResultSet result = stm.executeQuery();
            
            
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LoanDTO[] getLoansForStudent(String studentId) {
        try{
            ArrayList<LoanDTO> loans = new ArrayList<>();
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE studentId LIKE '%?%'");
            stm.setString(1, studentId);
            ResultSet result = stm.executeQuery();
            while(result.next())
                loans.add(new LoanDTO(result.getInt("loanId"), result.getString("barcode"), result.getString("studentId"),
                        result.getDate("loanDate"), result.getDate("dueDate"), result.getDate("deliveryDate"), result.getString("deliveredTo")));

            // Check if something was actually found
            if(loans.size() == 0)
                return null;

            return loans.toArray(new LoanDTO[loans.size()]);
        } catch(SQLException e){
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public int updateLoan(LoanDTO loan) {
        if (loan.getLoanId() == 0 || (loan.getBarcode() == null && loan.getStudentId() != null && loan.getLoanDate() == null
                && loan.getDueDate() == null && loan.getDeliveryDate() == null && loan.getDeliveredTo() == null)) {
            return -1;
        }

        String sql = "UPDATE " + DATABASE_NAME + " set ";
        String sqlValues = "";

        if (loan.getBarcode() != null) {
            sqlValues += "barcode = ?";
        }

        if (loan.getStudentId() != null) {
            if (sqlValues.equals("")) {
                sqlValues += "studentId = ?";
            } else {
                sqlValues += ", studentId = ?";
            }
        }

        if (loan.getLoanDate() != null) {
            if (sqlValues.equals(""))
                sqlValues += "loanDate = ?";
            else
                sqlValues += ", loanDate = ?";
        }

        if (loan.getDueDate() != null) {
            if (sqlValues.equals(""))
                sqlValues += "dueDate = ?";
            else
                sqlValues += ", dueDate = ?";
        }

        if (loan.getDeliveryDate() != null) {
            if (sqlValues.equals(""))
                sqlValues += "deliveryDate = ?";
            else
                sqlValues += ", deliveryDate = ?";
        }

        if (loan.getDeliveredTo() != null) {
            if (sqlValues.equals(""))
                sqlValues += "deliveredTo = ?";
            else
                sqlValues += ", deliveredTo = ?";
        }


        sql += sqlValues;
        sql += " WHERE loanId = ?";

        try {
            PreparedStatement stm = CONN.prepareStatement(sql);
            int param = 1;

            if (loan.getBarcode() != null)
                stm.setString(param++, loan.getBarcode());
            if (loan.getStudentId() != null)
                stm.setString(param++, loan.getStudentId());
            if (loan.getLoanDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getLoanDateAsDate().getTime()));
            if (loan.getDueDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getDueDateAsDate().getTime()));
            if (loan.getDeliveryDate() != null)
                stm.setDate(param++, new java.sql.Date(loan.getDeliveryDateAsDate().getTime()));
            if (loan.getDeliveredTo() != null)
                stm.setString(param++, loan.getDeliveredTo());

            stm.setInt(param++, loan.getLoanId());

            stm.execute();

            if (stm.getUpdateCount() == 1)
                return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        return 0;
    }

    @Override
    public int deleteLoan(int loanId) {
        try {
            String sql = "DELETE FROM " + DATABASE_NAME + "WHERE loanId = ?";
            PreparedStatement stm = CONN.prepareStatement(sql);
            stm.setInt(1, loanId);
            if (stm.getUpdateCount() == 1) {
                return 1;
            } else {
                return -2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
































