package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.StudentMapper;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Student createNewStudent(Student student) {
        //YOUR CODE STARTS HERE


        String sql = "INSERT INTO student (fName, lName) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getStudentFirstName());
            ps.setString(2, student.getStudentLastName());
            return ps;
        }, keyHolder);

        // ✅ Retrieve generated key (works with H2, MySQL, PostgreSQL)
        Number key = keyHolder.getKey();
        if (key != null) {
            student.setStudentId(key.intValue());
        }

        return student;


        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE


        String sql = "SELECT * FROM student";
        return jdbcTemplate.query(sql, new StudentMapper());

        //YOUR CODE ENDS HERE
    }

    @Override
    public Student findStudentById(int id) {
        //YOUR CODE STARTS HERE

        String sql = "SELECT * FROM student WHERE sid = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new StudentMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            Student notFound = new Student();
            notFound.setStudentFirstName("Student Not Found");
            notFound.setStudentLastName("Student Not Found");
            return notFound;
        }

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateStudent(Student student) {
        //YOUR CODE STARTS HERE
        String sql = "UPDATE student SET fName = ?, lName = ? WHERE sid = ?";
        jdbcTemplate.update(sql, student.getStudentFirstName(), student.getStudentLastName(), student.getStudentId());
        // YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudent(int id) {
        //YOUR CODE STARTS HERE
       // Remove from course_student first
        String deleteCourses = "DELETE FROM course_student WHERE student_id = ?";
        jdbcTemplate.update(deleteCourses, id);

        // Then delete student
        String sql = "DELETE FROM student WHERE sid = ?";
        jdbcTemplate.update(sql, id);

        //YOUR CODE ENDS HERE
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        try {
            String sql = "INSERT INTO course_student (student_id, course_id) VALUES (?, ?)";
            jdbcTemplate.update(sql, studentId, courseId);
        } catch (Exception e) {
            System.out.println("Student: " + studentId + " already enrolled in course: " + courseId);
        }

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        String sql = "DELETE FROM course_student WHERE student_id = ? AND course_id = ?";
        jdbcTemplate.update(sql, studentId, courseId);

        //YOUR CODE ENDS HERE
    }
}
