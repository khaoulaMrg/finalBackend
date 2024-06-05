package com.Aswat.reposistories;


import com.Aswat.entity.Category;
import com.Aswat.entity.Post;
import com.Aswat.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByNameContaining(String title);
    //List<Category> findApprovedAndPostedCategories();
    List<Post> findByPosted(boolean posted);




    List<Post> findByCategory_Id(Long categoryId);








    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.approved = true AND p.archived = false")
    List<Post> findByCategoryAndApprovedIsTrue(@Param("category") Category category);

    List<Post> findByCategoryAndArchivedIsTrue(Category category);

    List<Post> findByArchivedAndType_Type(boolean archived, String type);




    List<Post> findByArchived(boolean b);

    List<Post> findByArchivedAndNameContainingAndCategory_CategoryContaining(boolean archived, String name, String category);
    List<Post> findByArchivedAndNameContaining(boolean archived, String name);
    List<Post> findByArchivedAndCategory_CategoryContaining(boolean archived, String category);


    // Méthode pour archiver un post

}
