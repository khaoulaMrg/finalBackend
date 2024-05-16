package com.Aswat.services.jwt;

import com.Aswat.Dtos.PostDTO;
import com.Aswat.entity.Category;
import com.Aswat.entity.Post;
import com.Aswat.reposistories.CategoryRepo;
import com.Aswat.reposistories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final CategoryRepo categoryRepo;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CategoryRepo categoryRepo){
        this.postRepository = postRepository;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO)  throws IOException{
        Post post = new Post();
        post.setName(postDTO.getName());
        post.setContent(postDTO.getContent());
        post.setPostedBy(post.getPostedBy());
        post.setDate(postDTO.getDate());
        post.setImg(postDTO.getImg().getBytes());

        Category category= categoryRepo.findById(postDTO.getCategoryId()).orElseThrow();
        post.setCategory(category);
        return postRepository.save(post).getDto();
    }
    public List<PostDTO> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        return  posts.stream().map(Post::getDto).collect(Collectors.toList());
    }



}
