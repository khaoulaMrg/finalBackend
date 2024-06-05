package com.Aswat.services.jwt;

import com.Aswat.Dtos.PostDTO;
import com.Aswat.entity.Post;
import com.Aswat.entity.Type;
import io.micrometer.observation.ObservationFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface PostService {
  PostDTO createPost(PostDTO postDTO) throws IOException;
  List<PostDTO> getAllPosts();
  PostDTO reapproveAndRepostPost(Long id);

  List<PostDTO> getPostsByCategory(Long categoryId);

  PostDTO markPostAsPosted(Long id);
  PostDTO getApprovedPost(Long id);
  PostDTO sendPost(Long id);
  List<PostDTO> getAllCategoriesByTitle(String title);
  PostDTO approvePost(Long id);

  PostDTO approvePost(Long id, int expirationMinutes);

  List<PostDTO> getPostedPosts();



  Optional<Post> getPostById(Long id);

  boolean deletePost(Long id);


  List<PostDTO> getApprovedPostsByCategory(String category);






  boolean archiveAndRemovePost(Long id);

  List<PostDTO> getArchivedPosts();

  List<PostDTO> getArchivedPosts(Type type);

  void archiveAndRemoveExpiredPosts();




  PostDTO savePost(PostDTO newPostDTO);

  boolean archivePost(Long id);


  List<PostDTO> getArchivedPostsByType(String type);

    List<PostDTO> searchArchivedPosts(String name, String category);
}
