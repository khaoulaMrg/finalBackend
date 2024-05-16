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










}
