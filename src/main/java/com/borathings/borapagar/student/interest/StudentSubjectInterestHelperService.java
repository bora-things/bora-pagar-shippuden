package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSubjectInterestHelperService {

    @Autowired
    private StudentSubjectInterestRepository studentSubjectInterestRepository;

    public List<StudentSubjectInterestEntity> getFriendsInterestsInComponent(StudentEntity student, String code) {
        List<Long> friendsIds = student.getUser().getFriends().stream()
                .map(AbstractModel::getId)
                .toList();
        return studentSubjectInterestRepository.findAllBySubjectCodeAndStudentIn(code, friendsIds);
    }
}
