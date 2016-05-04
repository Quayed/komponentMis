/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DTO.StudentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author mathias
 */
public class StudentDAO implements IStudentDAO {

    private final Connection CONN;
    private final String DATABASE_NAME = "Student";

    public StudentDAO(Connection conn) {
        this.CONN = conn;
    }

    @Override
    public int createStudent(StudentDTO student) {
        if (student.getStudentId() == null || student.getName() == null)
            return -1;

        // check that the status is actually valid
        if (student.getStatus() != 0 && student.getStatus() != 1 && student.getStatus() != -1)
            return -4;

        String sql = "INSERT INTO " + DATABASE_NAME + "(studentId, name, status)"
                + " VALUES (?, ?, ?)";

        PreparedStatement stm = null;
        try {
            stm = CONN.prepareStatement(sql);
            int param = 1;

            stm.setString(param++, student.getStudentId());

            stm.setString(param++, student.getName());

            if (student.getStatus() == -1)
                stm.setInt(param++, 0);
            else
                stm.setInt(param++, student.getStatus());

            stm.execute();

            if (stm.getUpdateCount() != 1) {
                return -2;
            } else
                return 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return -3;
    }

    @Override
    public StudentDTO getStudent(String studentId) {
        PreparedStatement stm = null;
        ResultSet result = null;
        try {
            stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + " WHERE studentId = ?");
            stm.setString(1, studentId);
            result = stm.executeQuery();
            while (result.next())
                return new StudentDTO(result.getString("studentId"), result.getString("name"), result.getInt("status"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result != null)
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    @Override
    public StudentDTO[] getStudents() {
        PreparedStatement stm = null;
        ResultSet result = null;
        try {
            stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME);
            ArrayList<StudentDTO> students = new ArrayList<>();
            result = stm.executeQuery();
            while (result.next())
                students.add(new StudentDTO(result.getString("studentId"), result.getString("name"), result.getInt("status")));

            return students.toArray(new StudentDTO[students.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result != null)
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    @Override
    public int updateStudent(StudentDTO student) {
        if (student.getStudentId() == null || (student.getName() == null && student.getStatus() == -1))
            return -1;

        String sql = "UPDATE " + DATABASE_NAME + " SET ";
        String sqlValues = "";

        if (student.getName() != null) {
            sqlValues += "name = ?";
        }

        if (student.getStatus() != -1) {
            // check that status is valid
            if (student.getStatus() != 0 && student.getStatus() != 1)
                return -4;

            if (sqlValues.equals("")) {
                sqlValues += "status = ?";
            } else {
                sqlValues += ", status = ?";
            }
        }

        sql += sqlValues + " WHERE studentId = ?";

        PreparedStatement stm = null;
        try {
            stm = CONN.prepareStatement(sql);
            int param = 1;

            if (student.getName() != null)
                stm.setString(param++, student.getName());

            if (student.getStatus() != -1)
                stm.setInt(param++, student.getStatus());

            stm.setString(param++, student.getStudentId());

            stm.execute();

            if (stm.getUpdateCount() != 1) {
                return -2;
            } else {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return -3;
    }

    @Override
    public int deleteStudent(String studentId) {
        if (studentId == null || studentId.equals(""))
            return -1;

        PreparedStatement stm = null;
        try {
            stm = CONN.prepareStatement("DELETE FROM " + DATABASE_NAME + "WHERE studentId = ?");
            stm.setString(1, studentId);
            stm.execute();

            if (stm.getUpdateCount() == 1) {
                return 1;
            } else {
                return -2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null)
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return -3;
    }

}
