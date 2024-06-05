package com.Aswat.Controllers;

import com.Aswat.Dtos.PostDTO;
import com.Aswat.entity.Post;
import com.Aswat.entity.Type;
import com.Aswat.entity.User;
import com.Aswat.enums.UserRole;
import com.Aswat.reposistories.PostRepository;
import com.Aswat.reposistories.TypeRepository;
import com.Aswat.services.jwt.AuthoSrv;
import com.Aswat.services.jwt.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/customer")
public class PostController {
    private final PostService postService;
    private final AuthoSrv authoSrv;

    @Autowired
    private PostRepository postRepository;
    private TypeRepository typeRepository;

    public PostController(PostService postService, AuthoSrv authoSrv) {
        this.postService = postService;
        this.authoSrv = authoSrv;
    }


    @PostMapping("/post")
    public ResponseEntity<PostDTO> createPost(@ModelAttribute PostDTO postDTO) throws IOException {
        PostDTO postDTO1 = postService.createPost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(postDTO1);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOS = postService.getAllPosts();
        return ResponseEntity.ok(postDTOS);
    }

    @PutMapping("/post/{id}/approve")
    public ResponseEntity<PostDTO> approvePost(@PathVariable Long id, @RequestParam int expirationMinutes) {
        PostDTO approvedPostDTO = postService.approvePost(id, expirationMinutes);
        return ResponseEntity.ok(approvedPostDTO);
    }

    // Contrôleur pour réapprouver et reposter un post
    @PutMapping("/post/{id}/reapprove-repost")
    public ResponseEntity<PostDTO> reapproveAndRepostPost(@PathVariable Long id) {
        try {
            // Réapprouver et reposter le post avec l'identifiant spécifié
            PostDTO reapprovedPostDTO = postService.reapproveAndRepostPost(id);
            if (reapprovedPostDTO != null) {
                return ResponseEntity.ok(reapprovedPostDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Contrôleur pour envoyer un post
    @PostMapping("/post/{id}/send")
    public ResponseEntity<PostDTO> sendPost(@PathVariable Long id) {
        try {
            // Récupérer le post approuvé avec l'identifiant spécifié
            PostDTO approvedPostDTO = postService.getApprovedPost(id);
            if (approvedPostDTO != null) {
                // Envoyer le post approuvé à une autre forme (ou à une autre API)
                // Vous pouvez implémenter la logique pour envoyer le post où vous en avez besoin
                return ResponseEntity.ok(approvedPostDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "http://localhost:3000/*")


    @GetMapping("/posts/approved-by-category")
    public ResponseEntity<List<PostDTO>> getApprovedPostsByCategory(@RequestParam String category) {
        List<PostDTO> approvedPosts = postService.getApprovedPostsByCategory(category);
        return ResponseEntity.ok(approvedPosts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            // Supprimer le post avec l'identifiant spécifié
            boolean deleted = postService.deletePost(id);
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 No Content si la suppression réussit
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found si le post n'est pas trouvé
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error en cas d'erreur
        }
    }


    @GetMapping("/posts/archived")
    public ResponseEntity<List<PostDTO>> getArchivedPosts(@RequestParam(required = false) String type) {
        List<PostDTO> archivedPosts;
        if (type != null) {
            archivedPosts = postService.getArchivedPostsByType(type);
        } else {
            archivedPosts = postService.getArchivedPosts();
        }
        return ResponseEntity.ok(archivedPosts);
    }


    @PutMapping("/{id}/archive")
    public ResponseEntity<Void> archiveAndRemovePost(@PathVariable Long id) {
        boolean success = postService.archiveAndRemovePost(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/posts/{id}/archive")
    public ResponseEntity<Void> archivePost(@PathVariable Long id) {
        boolean success = postService.archivePost(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = authoSrv.getAllUsers();
        List<User> customers = allUsers.stream()
                .filter(user -> user.getUserRole() == UserRole.CUSTOMER)
                .collect(Collectors.toList());
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<User>> getCustomers() {
        List<User> allUsers = authoSrv.getAllUsers();
        List<User> customers = allUsers.stream()
                .filter(user -> user.getUserRole() == UserRole.CUSTOMER)
                .collect(Collectors.toList());
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            // Supprimer l'utilisateur avec l'identifiant spécifié
            boolean deleted = authoSrv.deleteUser(id); // Assurez-vous d'avoir un service userService pour la gestion des utilisateurs
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 No Content si la suppression réussit
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found si l'utilisateur n'est pas trouvé
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error en cas d'erreur
        }
    }
    @GetMapping("/archived/search")
    public List<PostDTO> searchArchivedPosts(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        return postService.searchArchivedPosts(name, category);
    }
}