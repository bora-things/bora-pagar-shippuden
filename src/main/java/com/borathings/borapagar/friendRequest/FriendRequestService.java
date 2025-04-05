package com.borathings.borapagar.friendRequest;

import com.borathings.borapagar.core.exception.friendRequest.AlreadyFriendsException;
import com.borathings.borapagar.core.exception.friendRequest.DuplicateFriendRequestException;
import com.borathings.borapagar.core.exception.friendRequest.FriendRequestCooldownException;
import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<FriendRequestResponseDto> findAllByToUserIdWithStatus(
            String toUserLogin, Optional<FriendRequestStatus> status) {
        UserEntity toUser = userService.findByLoginOrError(toUserLogin);
        List<FriendRequestEntity> requests =
                friendRequestRepository.findAllByToUserAndOptionalStatus(toUser, status.orElse(null));

        List<UserEntity> fromUsers =
                requests.stream().map(FriendRequestEntity::getFromUser).toList();

        List<StudentEntity> students = studentService.findAllStudentsById(fromUsers);
        Map<UserEntity, StudentEntity> studentMap =
                students.stream().collect(Collectors.toMap(StudentEntity::getUser, student -> student));

        return requests.stream()
                .map(request -> {
                    StudentEntity student = studentMap.get(request.getFromUser());
                    return friendRequestMapper.toFriendRequestResponseDto(request, student);
                })
                .toList();
    }

    public void createFriendRequest(String fromUserLogin, Integer toId) {
        UserEntity fromUser = userService.findByLoginOrError(fromUserLogin);
        UserEntity toUser = userService.findByIdUserOrError(toId);
        List<FriendRequestEntity> requests = friendRequestRepository.findAllByFromUserAndToUser(fromUser, toUser);

        if (toUser.getFriends().contains(fromUser)) {
            throw new AlreadyFriendsException();
        }

        requests.forEach((request) -> {
            if (request.getStatus() == FriendRequestStatus.PENDING) {
                throw new DuplicateFriendRequestException();
            }
            if (LocalDateTime.now().minusDays(7).isBefore(request.getCreatedAt())) {
                throw new FriendRequestCooldownException();
            }
            friendRequestRepository.deleteById(request.getId());
        });

        FriendRequestEntity friendRequestEntity = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();
        friendRequestRepository.save(friendRequestEntity);
    }

    @Transactional
    public void deleteFriendRequest(Long requestId) {
        Optional<FriendRequestEntity> request = friendRequestRepository.findById(requestId);
        if (request.isPresent()) {
            FriendRequestEntity friendRequestEntity = request.get();
            friendRequestEntity.setStatus(FriendRequestStatus.CANCELLED);
            friendRequestRepository.save(friendRequestEntity);
            friendRequestRepository.softDeleteById(request.get().getId());
        } else {
            throw new EntityNotFoundException("Pedido com ID: " + requestId + " não encontrado!");
        }
    }

    @Transactional
    public void updateFriendRequest(Long requestId, FriendRequestStatus status) {
        Optional<FriendRequestEntity> request = friendRequestRepository.findById(requestId);
        if (request.isPresent()) {
            FriendRequestEntity friendRequestEntity = request.get();
            friendRequestEntity.setStatus(status);
            friendRequestRepository.save(friendRequestEntity);
            if (status == FriendRequestStatus.ACCEPTED) {
                userService.createFriendship(friendRequestEntity.getToUser(), friendRequestEntity.getFromUser());
                friendRequestRepository.delete(friendRequestEntity);
            } else {
                friendRequestRepository.softDeleteById(friendRequestEntity.getId());
            }
        } else {
            throw new EntityNotFoundException("Pedido com ID: " + requestId + " não encontrado!");
        }
    }
}
