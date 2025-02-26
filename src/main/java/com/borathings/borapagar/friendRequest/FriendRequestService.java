package com.borathings.borapagar.friendRequest;

import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Autowired
    private StudentService studentService;


    public List<FriendRequestResponseDto> findAllByToUserId(String toUserLogin) {
        UserEntity toUser = userService.findByLoginOrError(toUserLogin);
        List<FriendRequestEntity> requests = friendRequestRepository.findAllByToUser(toUser);
        List<UserEntity> fromUsers = requests.stream()
                .map(FriendRequestEntity::getFromUser)
                .toList();
        List<StudentEntity> students = studentService.findAllStudentsById(fromUsers);
        Map<UserEntity, StudentEntity> studentMap = students.stream()
                .collect(Collectors.toMap(StudentEntity::getUser, student -> student));

        return requests.stream()
                .map(request -> {
                    StudentEntity student = studentMap.get(request.getFromUser());
                    return friendRequestMapper.toFriendRequestResponseDto(request, student);
                })
                .toList();
    }


    public boolean createFriendRequest(String fromUserLogin, Integer toId) {

        UserEntity fromUser = userService.findByLoginOrError(fromUserLogin);
        UserEntity toUser = userService.findByIdUserOrError(toId);

        if (fromUser == null || toUser == null) {
            return false;
        }
        FriendRequestEntity friendRequestEntity = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();
        friendRequestRepository.save(friendRequestEntity);
        return true;
    }


    public boolean updateFriendRequest(String toUserLogin, Integer fromId, FriendRequestStatus status) {
        UserEntity toUser = userService.findByLoginOrError(toUserLogin); //Usuario que esta aceitando
        UserEntity fromUser = userService.findByIdUserOrError(fromId); //Usuario que enviou
        if (fromUser == null || toUser == null) {
            return false;
        }
        Optional<FriendRequestEntity> request = friendRequestRepository.findByFromUserAndToUser(fromUser, toUser);
        if (request.isPresent()) {
            FriendRequestEntity friendRequestEntity = request.get();
            friendRequestEntity.setStatus(status);
            friendRequestRepository.save(friendRequestEntity);
            if (status == FriendRequestStatus.ACCEPTED) {
                userService.createFriendship(toUser, fromUser);
            }
            return true;
        }
        return false;

    }

}
