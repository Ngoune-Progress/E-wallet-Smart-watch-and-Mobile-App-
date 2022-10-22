package com.example.PayMe.service;

import com.example.PayMe.Entity.PostProject;
import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.repository.ProjectRepo;
import com.example.PayMe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostProjectImpl implements PostProjectService{
    private final ProjectRepo projectRepo;
    private final UserRepository userRepository;

    @Override
    public PostProject savePost(PostProject postProject) {
        Optional<UserEntity> user = userRepository.findByEmail(postProject.getEmail());
        postProject.setUser(user.get());
        return  projectRepo.save(postProject);
    }
}
