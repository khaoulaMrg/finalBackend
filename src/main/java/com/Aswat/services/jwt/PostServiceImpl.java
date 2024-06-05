package com.Aswat.services.jwt;

import com.Aswat.Dtos.PostDTO;
import com.Aswat.Exception.PostNotFoundException;
import com.Aswat.entity.Category;
import com.Aswat.entity.Post;
import com.Aswat.entity.Type;
import com.Aswat.reposistories.CategoryRepo;
import com.Aswat.reposistories.PostRepository;
import com.Aswat.reposistories.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepo categoryRepo;


    private final TypeRepository typeRepository;
    @Autowired
    public PostServiceImpl(PostRepository postRepository, CategoryRepo categoryRepo, TypeRepository typeRepository){
        this.postRepository = postRepository;
        this.categoryRepo = categoryRepo;
        this.typeRepository = typeRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public PostDTO createPost(PostDTO postDTO) throws IOException {
        Post post = new Post();
        post.setName(postDTO.getName());
        post.setContent(postDTO.getContent());
        post.setText(postDTO.getText());

        post.setPostedBy(postDTO.getPostedBy());
        post.setDate(new Date());
        post.setImg(postDTO.getImg().getBytes());
        post.setExpirationDate(LocalDateTime.now().plusMinutes(2));

        Type type = typeRepository.findById(postDTO.getTypeId()).orElseThrow();
        post.setType(type);

        Category category = categoryRepo.findById(postDTO.getCategoryId()).orElseThrow();
        post.setCategory(category);
        return postRepository.save(post).getDto();
    }



    @Override
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> {
                    PostDTO postDTO = post.getDto();
                    Type type = post.getType();
                    Category category = post.getCategory();

                    if (category != null) {
                        postDTO.setCategoryName(category.getCategory());
                    }
                    if (type != null) {
                        postDTO.setTypeName(type.getType());
                    }

                    return postDTO;
                })
                .collect(Collectors.toList());
    }

    private List<PostDTO> mapPostsToPostDTO(List<Post> posts) {
        return posts.stream().map(post -> {
            PostDTO postDTO = post.getDto();
            if (post.getCategory() != null) {
                postDTO.setCategoryId(post.getCategory().getId());
                postDTO.setCategoryName(post.getCategory().getCategory());
            }


            if (post.getType() != null) {
                postDTO.setTypeId(post.getType().getId());
                postDTO.setTypeName(post.getType().getType());
            }
            return postDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public PostDTO approvePost(Long id, int expirationMinutes) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        post.setExpirationDate(LocalDateTime.now().plusMinutes(expirationMinutes));
        post.setApproved(true);
        Post approvedPost = postRepository.save(post);
        return approvedPost.getDto();
    }


    @Override
    public List<PostDTO> getPostedPosts() {
        List<Post> postedPosts = postRepository.findByPosted(true);
        return mapPostsToPostDTO(postedPosts);
    }



    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {
        // Assuming you have a method to get posts by category ID
        List<Post> posts = postRepository.findByCategory_Id(categoryId);
        return mapPostsToPostDTO(posts);
    }

    @Override
    public PostDTO markPostAsPosted(Long id) {
        PostDTO approvedPostDTO = getApprovedPost(id);
        approvedPostDTO.setPosted(true);

        Post updatedPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        updatedPost.setPosted(true);
        postRepository.save(updatedPost);

        return approvedPostDTO;
    }

    @Override
    public PostDTO getApprovedPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if (post.isApproved()) {
                PostDTO postDTO = post.getDto();
                if (post.getCategory() != null) {
                    postDTO.setCategoryId(post.getCategoryId());
                    postDTO.setCategoryName(post.getCategory().getCategory());
                }
                if (post.getType() != null) {
                    postDTO.setTypeId(post.getTypeId());
                    postDTO.setTypeName(post.getType().getType());
                }
                return postDTO;
            } else {
                throw new RuntimeException("The post is not approved");
            }
        } else {
            throw new RuntimeException("Post not found");
        }
    }

    @Override
    public PostDTO sendPost(Long id) {
        PostDTO approvedPostDTO = getApprovedPost(id);
        approvedPostDTO.setPosted(true);

        Post updatedPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        updatedPost.setPosted(true);
        postRepository.save(updatedPost);

        return approvedPostDTO;
    }

    @Override
    public List<PostDTO> getAllCategoriesByTitle(String title) {
        // Implement if necessary
        return null;
    }

    @Override
    public PostDTO approvePost(Long id) {
        return null;
    }

    @Override
    public PostDTO reapproveAndRepostPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if (post.isApproved()) {
                post.setPosted(false);
                postRepository.save(post);

                PostDTO postDTO = post.getDto();
                if (post.getCategory() != null) {
                    postDTO.setCategoryId(post.getCategoryId());
                    postDTO.setCategoryName(post.getCategory().getCategory());
                }

                if (post.getType() != null) {
                    postDTO.setTypeId(post.getTypeId());
                    postDTO.setTypeName(post.getType().getType());
                }
                return postDTO;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }





    public Optional<Post> getPostById(Long id) {
        if (id == null) {
            logger.error("Post ID is null");
            throw new IllegalArgumentException("Post ID cannot be null");
        }

        try {
            Optional<Post> post = postRepository.findById(id);
            if (post.isPresent()) {
                logger.info("Post with ID {} found", id);
            } else {
                logger.warn("Post with ID {} not found", id);
            }
            return post;
        } catch (Exception e) {
            logger.error("Error retrieving post with ID {}", id, e);
            throw new RuntimeException("Error retrieving post with ID " + id, e);
        }
    }
    @Override
    public boolean deletePost(Long id) {
        try {
            postRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<PostDTO> getApprovedPostsByCategory(String categoryName) {
        Category category = categoryRepo.findByCategory(categoryName);
        if (category == null) {
            throw new IllegalArgumentException("Category not found: " + categoryName);
        }

        List<Post> posts = postRepository.findByCategoryAndApprovedIsTrue(category);
        List<PostDTO> activePosts = mapPostsToPostDTO(posts.stream()
                .filter(post -> !post.isArchived())
                .collect(Collectors.toList()));

        // Check if there are any non-archived posts of the specified type "first"
        boolean hasActiveFirstPost = activePosts.stream().anyMatch(post -> "first".equals(post.getTypeName()));

        // If no active first post, include the latest archived first post
        if (!hasActiveFirstPost) {
            List<Post> archivedPosts = postRepository.findByCategoryAndArchivedIsTrue(category);
            List<PostDTO> archivedFirstPosts = mapPostsToPostDTO(archivedPosts.stream()
                    .filter(post -> "first".equals(post.getTypeName()))
                    .sorted((p1, p2) -> p2.getExpirationDate().compareTo(p1.getExpirationDate())) // Get the latest one
                    .collect(Collectors.toList()));
            if (!archivedFirstPosts.isEmpty()) {
                activePosts.add(archivedFirstPosts.get(0)); // Add the latest archived post
            }
        }

        // Handle "trend" posts
        List<PostDTO> trendPosts = activePosts.stream()
                .filter(post -> "trend".equals(post.getTypeName()))
                .collect(Collectors.toList());

        if (trendPosts.size() < 3) {
            // Fetch additional "trend" posts from archived if necessary to maintain at least 3
            List<Post> archivedTrendPosts = postRepository.findByCategoryAndArchivedIsTrue(category);
            List<PostDTO> archivedTrendPostsDTO = mapPostsToPostDTO(archivedTrendPosts.stream()
                    .filter(post -> "trend".equals(post.getTypeName()))
                    .sorted((p1, p2) -> p2.getExpirationDate().compareTo(p1.getExpirationDate())) // Get the latest ones
                    .collect(Collectors.toList()));

            for (PostDTO archivedTrendPost : archivedTrendPostsDTO) {
                if (trendPosts.size() >= 3) break;
                trendPosts.add(archivedTrendPost);
            }
        } else if (trendPosts.size() > 3) {
            // If there are more than 3 trend posts, remove the expired ones
            trendPosts = trendPosts.stream()
                    .filter(post -> post.getExpirationDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            // Ensure only 3 remain
            while (trendPosts.size() > 3) {
                trendPosts.remove(trendPosts.size() - 1);
            }
        }

        // Handle "latest" posts
        List<PostDTO> latestPosts = activePosts.stream()
                .filter(post -> "latest".equals(post.getTypeName()))
                .collect(Collectors.toList());

        if (latestPosts.size() < 2) {
            // Fetch additional "latest" posts from archived if necessary to maintain at least 2
            List<Post> archivedLatestPosts = postRepository.findByCategoryAndArchivedIsTrue(category);
            List<PostDTO> archivedLatestPostsDTO = mapPostsToPostDTO(archivedLatestPosts.stream()
                    .filter(post -> "latest".equals(post.getTypeName()))
                    .sorted((p1, p2) -> p2.getExpirationDate().compareTo(p1.getExpirationDate())) // Get the latest ones
                    .collect(Collectors.toList()));

            for (PostDTO archivedLatestPost : archivedLatestPostsDTO) {
                if (latestPosts.size() >= 2) break;
                latestPosts.add(archivedLatestPost);
            }
        } else if (latestPosts.size() > 2) {
            // If there are more than 2 latest posts, remove the expired ones
            latestPosts = latestPosts.stream()
                    .filter(post -> post.getExpirationDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            // Ensure only 2 remain
            while (latestPosts.size() > 2) {
                latestPosts.remove(latestPosts.size() - 1);
            }
        }

        // Handle "reports" posts
        List<PostDTO> reportsPosts = activePosts.stream()
                .filter(post -> "reports".equals(post.getTypeName()))
                .collect(Collectors.toList());

        if (reportsPosts.size() < 4) {
            // Fetch additional "reports" posts from archived if necessary to maintain at least 4
            List<Post> archivedReportsPosts = postRepository.findByCategoryAndArchivedIsTrue(category);
            List<PostDTO> archivedReportsPostsDTO = mapPostsToPostDTO(archivedReportsPosts.stream()
                    .filter(post -> "reports".equals(post.getTypeName()))
                    .sorted((p1, p2) -> p2.getExpirationDate().compareTo(p1.getExpirationDate())) // Get the latest ones
                    .collect(Collectors.toList()));

            for (PostDTO archivedReportsPost : archivedReportsPostsDTO) {
                if (reportsPosts.size() >= 4) break;
                reportsPosts.add(archivedReportsPost);
            }
        } else if (reportsPosts.size() > 4) {
            // If there are more than 4 reports posts, remove the expired ones
            reportsPosts = reportsPosts.stream()
                    .filter(post -> post.getExpirationDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            // Ensure only 4 remain
            while (reportsPosts.size() > 4) {
                reportsPosts.remove(reportsPosts.size() - 1);
            }
        }

        // Combine all types of posts back into activePosts
        activePosts = activePosts.stream()
                .filter(post -> !"trend".equals(post.getTypeName()) && !"latest".equals(post.getTypeName()) && !"reports".equals(post.getTypeName()))
                .collect(Collectors.toList());
        activePosts.addAll(trendPosts);
        activePosts.addAll(latestPosts);
        activePosts.addAll(reportsPosts);

        return activePosts;
    }



    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setName(post.getName());
        postDTO.setContent(post.getContent());
        postDTO.setPostedBy(post.getPostedBy());
        postDTO.setDate(post.getDate());
        postDTO.setByteImg(post.getByteImg());
        postDTO.setExpirationDate(post.getExpirationDate()); // Make sure this is set correctly
        postDTO.setTypeName(post.getTypeName());
        postDTO.setArchived(post.isArchived());
        return postDTO;
    }


    // Schedule this method to run every minute (or as needed)



    @Override
    public boolean archiveAndRemovePost(Long id) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setArchived(true); // Mark the post as archived
            postRepository.save(post); // Save the changes
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<PostDTO> getArchivedPosts(Type type) {
        return null;
    }

    @Override
    public List<PostDTO> getArchivedPosts() {
        return null;
    }

    @Override
    public List<PostDTO> getArchivedPostsByType(String type) {
        List<Post> archivedPosts = postRepository.findByArchivedAndType_Type(true, type);
        return archivedPosts.stream().map(Post::getDto).collect(Collectors.toList());
    }













    @Override
    public void archiveAndRemoveExpiredPosts() {
        // Récupérer tous les posts non archivés
        List<Post> allPosts = postRepository.findAll();

        // Récupérer la date et l'heure actuelles
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Parcourir tous les posts
        for (Post post : allPosts) {
            // Vérifier si le post a expiré
            if (post.getExpirationDate() != null && post.getExpirationDate().isBefore(currentDateTime)) {
                // Marquer le post comme archivé sans le retirer immédiatement
                post.setArchived(true);
                postRepository.save(post);
            }
        }
    }

    @Override
    public PostDTO savePost(PostDTO newPostDTO) {
        return null;
    }


    @Override
    public boolean archivePost(Long id) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setArchived(true); // Marquer le post comme archivé
            postRepository.save(post); // Sauvegarder les modifications
            return true;
        } else {
            return false;
        }
    }



    public List<PostDTO> searchArchivedPosts(String name, String category) {
        List<Post> posts;
        if (name != null && category != null) {
            posts = postRepository.findByArchivedAndNameContainingAndCategory_CategoryContaining(true, name, category);
        } else if (name != null) {
            posts = postRepository.findByArchivedAndNameContaining(true, name);
        } else if (category != null) {
            posts = postRepository.findByArchivedAndCategory_CategoryContaining(true, category);
        } else {
            posts = postRepository.findByArchived(true);
        }
        return posts.stream().map(Post::getDto).collect(Collectors.toList());
    }

}