package com.example.githubapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class GitHubController {

    private static final Logger logger = Logger.getLogger(GitHubController.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/repos")
    public ResponseEntity<?> getRepositories(@RequestParam String username) {
        String url = "https://api.github.com/users/" + username + "/repos?per_page=100";
        List<RepositoryDTO> allRepositories = new ArrayList<>();

        try {
            List<RepositoryDTO> repositories;
            int page = 1;

            do {
                String paginatedUrl = url + "&page=" + page;
                logger.info("Requesting URL: " + paginatedUrl);

                // Make API request
                ResponseEntity<List<RepositoryDTO>> response = restTemplate.exchange(
                        paginatedUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});

                repositories = response.getBody();
                if (repositories != null) {
                    repositories.stream()
                            .filter(repo -> !repo.isFork())
                            .forEach(allRepositories::add);
                }
                page++;

            } while (repositories != null && repositories.size() == 100);

            // Fetch branches for each repository
            for (RepositoryDTO repo : allRepositories) {
                repo.setBranches(getBranchesForRepository(username, repo.getName()));
            }

            return ResponseEntity.ok(allRepositories);

        } catch (HttpClientErrorException.NotFound e) {
            logger.warning("User not found: " + username);
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", "User not found"));
        } catch (HttpClientErrorException.Forbidden e) {
            logger.warning("GitHub API rate limit exceeded");
            return ResponseEntity.status(403).body(Map.of("status", 403, "message", "API rate limit exceeded. Try again later."));
        } catch (Exception e) {
            logger.severe("Error fetching repositories: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("status", 500, "message", "Internal Server Error"));
        }
    }

    private List<BranchDTO> getBranchesForRepository(String username, String repoName) {
        String url = "https://api.github.com/repos/" + username + "/" + repoName + "/branches";
        try {
            ResponseEntity<List<BranchDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {});
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warning("Repository not found: " + repoName);
            return new ArrayList<>();
        } catch (HttpClientErrorException.Forbidden e) {
            logger.warning("Rate limit exceeded while fetching branches for: " + repoName);
            return new ArrayList<>();
        } catch (Exception e) {
            logger.severe("Error fetching branches for " + repoName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
