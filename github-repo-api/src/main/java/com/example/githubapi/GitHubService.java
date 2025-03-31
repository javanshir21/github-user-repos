package com.example.githubapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GitHubService {

    @Autowired
    private RestTemplate restTemplate; // You can configure RestTemplate to make HTTP calls

    // Method to get repositories by username
    public List<RepositoryDTO> getRepositories(String username) {
        // 1. Fetch repositories from GitHub
        List<RepositoryDTO> repos = fetchRepositoriesFromGitHub(username);

        // 2. For each repo, fetch branches and set them
        for (RepositoryDTO repo : repos) {
            List<BranchDTO> branches = fetchBranchesForRepository(repo.getOwner(), repo.getName());
            repo.setBranches(branches); // Add branches to the repository
        }

        return repos;
    }

    // Method to fetch repositories from GitHub
    private List<RepositoryDTO> fetchRepositoriesFromGitHub(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RepositoryDTO>>() {}).getBody();
    }

    // Method to fetch branches for a repository
    private List<BranchDTO> fetchBranchesForRepository(String owner, String repo) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/branches";
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<BranchDTO>>() {}).getBody();
    }
}
