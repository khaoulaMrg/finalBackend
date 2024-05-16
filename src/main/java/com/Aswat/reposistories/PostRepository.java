package com.Aswat.reposistories;


import com.Aswat.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByNameContaining(String title);

    List<Post> findByPosted(boolean posted);
    List<Post> findByApprovedTrueAndPostedTrue();
}
