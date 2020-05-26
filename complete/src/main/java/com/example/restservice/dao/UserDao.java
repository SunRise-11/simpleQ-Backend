package com.example.restservice.dao;

import com.example.restservice.constants.UserStatus;
import com.example.restservice.model.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends DaoBase {

  @Autowired QueueDao queueDao;

  public User addUserToQueue(String queueId, User user) {
    var entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    var queue = queueDao.getQueue(queueId);
    user.setQueue(queue);
    entityManager.persist(user);
    entityManager.getTransaction().commit();
    entityManager.close();

    return user;
  }

  public User getUser(String tokenId) {
    var entityManager = entityManagerFactory.createEntityManager();
    var user = entityManager.find(User.class, tokenId);
    entityManager.close();
    return user;
  }

  public void UpdateUserStatus(String tokenId, UserStatus status) {
    var entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    var user = entityManager.find(User.class, tokenId);
    user.setStatus(status);
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  public Optional<Long> getAheadCount(String tokenId) {
    var entityManager = entityManagerFactory.createEntityManager();
    var user = entityManager.find(User.class, tokenId);

    if (user.getStatus() == UserStatus.REMOVED) {
      return Optional.empty();
    }

    var aheadCount =
        Optional.of(
            user.getQueue().getUsers().stream()
                .filter(
                    fellowUser ->
                        fellowUser.getTimestamp().before(user.getTimestamp())
                            && !fellowUser.getStatus().equals(UserStatus.REMOVED))
                .count());
    entityManager.close();
    return aheadCount;
  }
}
