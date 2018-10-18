package com.dhb.learning.polls.poll.app.repository;

import com.dhb.learning.polls.poll.app.model.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

    List<Poll> findByCreateBy(long userId);

    Optional<Poll> findById(Long aLong);

    Page<Poll> findByCreateBy(Long userId , Pageable pageable);

    long countByCreateBy(Long userId);

    List<Poll> findByIdIn(List<Long> pollIds);

    List<Poll> findByIdIn(List<Long> pollIds , Sort sort);

}


