package com.Aswat.services.jwt;

import com.Aswat.Dtos.PostDTO;
import com.Aswat.entity.Category;
import com.Aswat.entity.Post;
import com.Aswat.reposistories.CategoryRepo;
import com.Aswat.reposistories.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@Transactional

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final CategoryRepo categoryRepo;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CategoryRepo categoryRepo){
        this.postRepository = postRepository;
        this.categoryRepo = categoryRepo;
    }
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public PostDTO createPost(PostDTO postDTO)  throws IOException{
        Post post = new Post();
        post.setName(postDTO.getName());
        post.setContent(postDTO.getContent());
        post.setPostedBy(postDTO.getPostedBy());
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


    private List<PostDTO> mapPostsToPostDTO(List<Post> posts) {
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = new PostDTO();

            postDTO.setId(post.getId());
            postDTO.setName(post.getName());
            postDTO.setContent(post.getContent());
            postDTO.setPostedBy(post.getPostedBy());
            postDTO.setDate(post.getDate());

            postDTO.setByteImg(post.getImg());
            if (post.getCategory() != null) {
                postDTO.setCategoryId(post.getCategory());
            }
            // Autres attributs à mapper si nécessaire

            postDTOS.add(postDTO);
        }
        return postDTOS;
    }


    @Override
    public PostDTO approvePost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // Mettre à jour l'état de la catégorie pour l'approuver
            post.setApproved(true);
            postRepository.save(post); // Enregistrer les modifications dans la base de données

            // Créer un objet CategoryDto à partir des attributs de Category
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setName(post.getName());
            postDTO.setContent(post.getContent());
            postDTO.setPostedBy(post.getPostedBy());
            postDTO.setDate(post.getDate());
            postDTO.setApproved(post.isApproved());
            postDTO.setByteImg(post.getImg());
            if (post.getCategory() != null) {
                postDTO.setCategoryId(post.getCategory());
            }

            return postDTO;
        } else {
            // Gérer le cas où la catégorie avec l'ID spécifié n'est pas trouvée
            return null;
        }
    }

    @Override
    public List<PostDTO> getPostedPosts() {
        List<Post> postedPosts = postRepository.findByPosted(true);
        return mapPostsToPostDTO(postedPosts);
    }

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {
        return null;
    }


    @Override
    public PostDTO markPostAsPosted(Long id) {
        // Récupérer le post approuvé avec l'identifiant spécifié
        PostDTO approvedPostDTO = getApprovedPost(id);

        // Marquer le post comme posté dans votre système
        approvedPostDTO.setPosted(true);

        try {
            // Mettre à jour le post dans votre base de données
            Post updatedPost = postRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
            updatedPost.setPosted(true);
            postRepository.save(updatedPost);
        } catch (EntityNotFoundException ex) {
            // Gérer le cas où le post n'est pas trouvé dans la base de données
            throw new RuntimeException("Post not found with id: " + id, ex);
        }

        return approvedPostDTO;
    }


    @Override
    public PostDTO getApprovedPost(Long id) {
        // Récupérer la catégorie depuis la source de données
        Optional<Post> optionalPost = postRepository.findById(id);

        // Vérifier si la catégorie existe
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Vérifier si la catégorie est approuvée
            if (post.isApproved()) {
                PostDTO postDTO = new PostDTO();
                postDTO.setId(post.getId());
                postDTO.setName(post.getName());
                postDTO.setContent(post.getContent());
                postDTO.setPostedBy(post.getPostedBy());
                postDTO.setDate(post.getDate());
                postDTO.setByteImg(post.getImg());
                if (post.getCategory() != null) {
                    postDTO.setCategoryId(post.getCategory());
                }


                return postDTO;
            } else {
                // Gérer le cas où la catégorie n'est pas approuvée
                throw new RuntimeException("La catégorie n'est pas approuvée");
            }
        } else {
            // Gérer le cas où la catégorie avec l'ID spécifié n'est pas trouvée
            throw new RuntimeException("Catégorie non trouvée");
        }
    }

    @Override
    public PostDTO sendPost(Long id) {
        // Récupérer le post approuvé avec l'identifiant spécifié
        PostDTO approvedPostDTO = getApprovedPost(id);

        // Marquer le post comme posté dans votre système
        approvedPostDTO.setPosted(true);

        try {
            // Mettre à jour le post dans votre base de données
            Post updatedPost = postRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
            updatedPost.setPosted(true);
            postRepository.save(updatedPost);
        } catch (EntityNotFoundException ex) {
            // Gérer le cas où le post n'est pas trouvé dans la base de données
            throw new RuntimeException("Post not found with id: " + id, ex);
        }

        return approvedPostDTO;
    }



    @Override
    public List<PostDTO> getAllCategoriesByTitle(String title) {
        return null;
    }




    @Override
    public PostDTO reapproveAndRepostPost(Long id) {
        // Récupérer la catégorie par son ID
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Vérifier si la catégorie est déjà approuvée
            if (post.isApproved()) {
                // Marquer la catégorie comme non postée
                post.setPosted(false);

                // Mettre à jour la catégorie dans la base de données
                postRepository.save(post);

                // Créer un objet CategoryDto à partir des attributs de Category
                PostDTO postDTO = new PostDTO();
                postDTO.setId(post.getId());
                postDTO.setName(post.getName());
                postDTO.setContent(post.getContent());
                postDTO.setPostedBy(post.getPostedBy());
                postDTO.setDate(post.getDate());
                postDTO.setApproved(post.isApproved());
                postDTO.setPosted(post.isPosted());
                postDTO.setByteImg(post.getImg());
                if (post.getCategory() != null) {
                    postDTO.setCategoryId(post.getCategory());
                }
                return postDTO;


                // Retourner la catégorie réapprouvée et non postée
            } else {
                // La catégorie n'est pas approuvée, retourner null ou gérer l'erreur selon vos besoins
                return null;
            }
        } else {
            // Gérer le cas où la catégorie avec l'ID spécifié n'est pas trouvée
            return null;
        }

    }
    // Define this method to get an approved post





        // Vérifier si la catégorie existe

    private List<PostDTO> convertToPostDTOList(List<Post> posts) {
        List<PostDTO> postDTOList = new ArrayList<>();

        for (Post post : posts) {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setName(post.getName());
            postDTO.setContent(post.getContent());
            postDTO.setPostedBy(post.getPostedBy());
            postDTO.setDate(post.getDate());
            postDTO.setApproved(post.isApproved());
            postDTO.setPosted(post.isPosted());
            postDTO.setByteImg(post.getImg());
            if (post.getCategory() != null) {
                postDTO.setCategoryId(post.getCategory());
            }

            postDTOList.add(postDTO);
        }

        return postDTOList;
    }

    @Override
    public List<PostDTO> getApprovedAndPostedPosts() {
        List<Post> approvedAndPostedPosts = postRepository.findByApprovedTrueAndPostedTrue();
        return convertToPostDTOList(approvedAndPostedPosts);
    }



// Autres méthodes du service restent inchangées...



}
