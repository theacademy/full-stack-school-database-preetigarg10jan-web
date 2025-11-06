package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.TeacherDao;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherServiceInterface {

    //YOUR CODE STARTS HERE

    @Autowired
    private TeacherDao teacherDao;

    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    //YOUR CODE ENDS HERE

    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE

        return teacherDao.getAllTeachers();

        //YOUR CODE ENDS HERE
    }

    public Teacher getTeacherById(int id) {
        //YOUR CODE STARTS HERE


        return teacherDao.findTeacherById(id);

        //YOUR CODE ENDS HERE
    }

    public Teacher addNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE

        if (teacher.getTeacherFName() == null || teacher.getTeacherFName().isBlank()
                || teacher.getTeacherLName() == null || teacher.getTeacherLName().isBlank()) {
            teacher.setTeacherFName("First Name blank, teacher NOT added");
            teacher.setTeacherLName("Last Name blank, teacher NOT added");
            return teacher;
        }

        try {
            return teacherDao.createNewTeacher(teacher);
        } catch (DataAccessException e) {
            teacher.setTeacherFName("Database error, teacher NOT added");
            teacher.setTeacherLName(e.getMessage());
            return teacher;
        }

        //YOUR CODE ENDS HERE
    }

    public Teacher updateTeacherData(int id, Teacher teacher) {
        //YOUR CODE STARTS HERE


        if (teacher.getTeacherId() != id) {
            teacher.setTeacherFName("IDs do not match, teacher not updated");
            teacher.setTeacherLName("IDs do not match, teacher not updated");
            return teacher;
        }

        try {
            teacherDao.updateTeacher(teacher);
        } catch (DataAccessException e) {
            teacher.setTeacherFName("Database error, teacher NOT updated");
            teacher.setTeacherLName(e.getMessage());
        }
        return teacher;

        //YOUR CODE ENDS HERE
    }

    public void deleteTeacherById(int id) {
        //YOUR CODE STARTS HERE

        try {
            teacherDao.deleteTeacher(id);
        } catch (DataAccessException e) {
            System.out.println("Error deleting teacher with ID: " + id);
        }

        //YOUR CODE ENDS HERE
    }
}
