package com.example.PayMe.controller;

import com.example.PayMe.Entity.PostProject;
import com.example.PayMe.repository.ProjectRepo;
import com.example.PayMe.service.PostProjectImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class PostProjectController {
    private final PostProjectImpl postProjectImp;
    private final ProjectRepo projectRepo;

    @GetMapping("all")
    public ResponseEntity<List<PostProject>> listPost(){
        return ResponseEntity.ok(projectRepo.findAll());
    }
    @PostMapping("savePost")
    public ResponseEntity<PostProject> savePost(@RequestBody PostProject postProject){
        return ResponseEntity.ok(postProjectImp.savePost(postProject));
    }
}
