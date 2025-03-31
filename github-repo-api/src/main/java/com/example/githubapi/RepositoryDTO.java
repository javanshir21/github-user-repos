package com.example.githubapi;

import com.example.githubapi.BranchDTO;
import com.example.githubapi.OwnerDTO;

import java.util.List;

public class RepositoryDTO {
    private String name;
    private String fullName;
    private String description;
    private String language;
    private OwnerDTO owner;  // This is an OwnerDTO object
    private boolean fork;
    private List<BranchDTO> branches;
    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }


    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    // Use this method to get the login from the OwnerDTO
    public String getOwner() {
        return (owner != null) ? owner.getLogin() : null;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDTO> branches) {
        this.branches = branches;
    }
}
