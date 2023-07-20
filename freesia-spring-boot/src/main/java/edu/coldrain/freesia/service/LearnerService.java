package edu.coldrain.freesia.service;

import edu.coldrain.freesia.entity.Learner;
import edu.coldrain.freesia.exception.InvalidCredentialsException;
import edu.coldrain.freesia.repository.LearnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearnerService {

    private final LearnerRepository learnerRepository;

    public void verifyCredentialsAndAuthenticate(String email, String password) {
        final Learner learner = learnerRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials exception."));

        if (!learner.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid credentials exception.");
        }
    }
}
