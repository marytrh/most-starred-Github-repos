package com.example.maryem.starredrepos;

/**
 * Created by maryem on 22/12/18.
 */

public class Repo {

    public String repo_name;
    public String repo_description;
    public String repo_owner_name;
    public String repo_number_stars;

   /* public Repo(String repo_name) {
        this.repo_name = repo_name;
    }*/

    public Repo(String repo_name, String repo_description, String repo_owner_name, String repo_number_stars) {
        this.repo_name = repo_name;
        this.repo_description = repo_description;
        this.repo_owner_name = repo_owner_name;
        this.repo_number_stars = repo_number_stars;
    }

    public String getRepo_name() {
        return repo_name;
    }

    public String getRepo_description() {
        return repo_description;
    }

    public String getRepo_owner_name() {
        return repo_owner_name;
    }

    public String getRepo_number_stars() {
        return repo_number_stars;
    }

    public void setRepo_name(String repo_name) {
        this.repo_name = repo_name;
    }

    public void setRepo_description(String repo_description) {
        this.repo_description = repo_description;
    }

    public void setRepo_owner_name(String repo_owner_name) {
        this.repo_owner_name = repo_owner_name;
    }

    public void setRepo_number_stars(String repo_number_stars) {
        this.repo_number_stars = repo_number_stars;
    }
}
