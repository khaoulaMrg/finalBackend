package com.Aswat.reposistories;


import com.Aswat.Dtos.PostDTO;
import com.Aswat.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByNameContaining(String title);
    //List<Category> findApprovedAndPostedCategories();
    List<Post> findByPosted(boolean posted);
    List<Post> findByApprovedTrueAndPostedTrue();

    List<Post> findByCategoryId(Long categoryId);
}
