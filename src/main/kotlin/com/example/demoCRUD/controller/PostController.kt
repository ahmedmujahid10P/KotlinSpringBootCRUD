package com.example.demoCRUD.controller

import com.example.demoCRUD.model.Post
import com.example.demoCRUD.repository.PostRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/myapi")
class PostController( private val postRepository: PostRepository) {

    @GetMapping("/posts")
    fun getAllPosts(): List<Post> = postRepository.findAll();

    @GetMapping("/posts/{id}")
    fun getPostById(@PathVariable(value = "id") postId: Long) : ResponseEntity<Post> {
        return postRepository.findById(postId).map {
                post -> ResponseEntity.ok(post)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createNewPost(@Valid @RequestBody post: Post): Post =
        postRepository.save(post)


    @PutMapping("/posts/{id}")
    fun updatePostById(@PathVariable(value = "id") postId: Long, @Valid @RequestBody newPost: Post): ResponseEntity<Post> {
        return postRepository.findById(postId).map {
            existingPost -> val updatedPost: Post = existingPost.copy(title = newPost.title, content = newPost.content);
            ResponseEntity.ok().body(postRepository.save(updatedPost))
        }.orElse(ResponseEntity.notFound().build())
    }



    @DeleteMapping("/posts/{id}")
    fun deletePostById(@PathVariable(value = "id") postId:Long): ResponseEntity<Void> {

        return postRepository.findById(postId).map { post ->
            postRepository.delete(post)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }

}