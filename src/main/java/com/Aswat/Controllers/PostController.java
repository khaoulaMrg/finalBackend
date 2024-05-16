package com.Aswat.Controllers;

import com.Aswat.Dtos.PostDTO;
import com.Aswat.services.jwt.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping("/api/autho")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping("/post")
    public ResponseEntity<PostDTO> createPost(@ModelAttribute PostDTO postDTO)throws IOException{
        PostDTO postDTO1 = postService.createPost(postDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(postDTO1);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        List<PostDTO> postDTOS = postService.getAllPosts();
        return ResponseEntity.ok(postDTOS);
    }



}
