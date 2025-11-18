package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.core.persistence.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSubjectInterestHelperService {

    @Autowired
    private StudentSubjectInterestRepository studentSubjectInterestRepository;

    public List<StudentSubjectInterestEntity> getFriendsInterestsInComponent(StudentEntity student, String code) {
        List<Long> friendsIds = student.getUser().getFriends().stream()
                .map(AbstractModel::getId)
                .toList();
        return studentSubjectInterestRepository.findAllBySubjectCodeAndStudentInAndDeletedAtIsNull(code, friendsIds);
    }
}
