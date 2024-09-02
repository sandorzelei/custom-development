/*
 * Copyright by Intland Software
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Intland Software. ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Intland.
 */

package com.intland.codebeamer.empty;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHIssueState;

import java.io.IOException;
import java.util.List;

public class GitHubPullRequests {
    public static void main(String[] args) {
        String token = "my_token"; // Replace with your actual token

        try {
            // Authenticate to GitHub using the token
            GitHub github = GitHub.connectUsingOAuth(token);

            // Get the repository
            GHRepository repo = github.getRepository("intland/cbdev-git");

            // List pull requests
            List<GHPullRequest> pullRequests = repo.getPullRequests(GHIssueState.OPEN);

            // Print pull requests
            for (GHPullRequest pr : pullRequests) {
                System.out.println("PR #" + pr.getNumber() + ": " + pr.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}