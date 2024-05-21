package com.Aswat.services.jwt;

import com.Aswat.Dtos.PostDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public interface PostService {


  PostDTO createPost(PostDTO postDTO)  throws IOException;
  List<PostDTO> getAllPosts();


  PostDTO reapproveAndRepostPost(Long id) ;

  List<PostDTO> getApprovedAndPostedPosts();

  PostDTO markPostAsPosted(Long id);

  PostDTO getApprovedPost(Long id);


  PostDTO sendPost(Long id);

  List<PostDTO> getAllCategoriesByTitle(String title);



  PostDTO approvePost(Long id);

  List<PostDTO> getPostedPosts();


  List<PostDTO> getPostsByCategory(Long categoryId);
}
