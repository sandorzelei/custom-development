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

import java.io.IOException;
import java.util.List;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

public class GitHubPullRequests {
	public static void main(String[] args) {
		String token = "aaa";

		try {
			// Authenticate to GitHub using the token
			GitHub github = GitHub.connectUsingOAuth(token);

			// Get the repository
			GHRepository repo = github.getRepository("intland/cbdev-git");

			
			// List pull requests
			// List<GHPullRequest> pullRequests = repo.getPullRequests(GHIssueState.OPEN);
			List<GHPullRequest> pullRequests = List.of(repo.getPullRequest(12337), repo.getPullRequest(12307));
			// Print pull requests
			for (GHPullRequest pr : pullRequests) {
				System.out.println("PR #" + pr.getNumber() + ": " + pr.getTitle());

				List<GHUser> as = pr.getRequestedReviewers();
				for (GHUser u : as) {
					System.out.println(u.getLogin());
				}

				List<GHTeam> teams = pr.getRequestedTeams();
				for (GHTeam team : teams) {
					team.listMembers().forEach(u -> {
						System.out.println(u.getLogin());
					});
				}

				System.out.println("-------------------------");
				
				PagedIterable<GHPullRequestReview> rev = pr.listReviews();
				rev.forEach(review -> {
					try {
						System.out.println("Review ID: " + review.getId());
						System.out.println("User: " + review.getUser().getLogin());
						System.out.println("State: " + review.getState());
						System.out.println("Body: " + review.getBody());
						System.out.println("-------------------------");
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}