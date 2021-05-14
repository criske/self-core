/**
 * Copyright (c) 2020-2021, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.core;

import com.selfxdsd.api.Stars;
import com.selfxdsd.api.storage.Storage;
import com.selfxdsd.core.mock.MockJsonResources;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.json.JsonValue;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for {@link GithubStars}.
 * @author criske
 * @version $Id$
 * @since 0.0.30
 */
public final class GithubStarsTestCase {

    /**
     * GithubStars can add a star.
     */
    @Test
    public void canAddStar() {
        final AccessToken.Github token = new AccessToken
            .Github("github_123");
        final MockJsonResources res = new MockJsonResources(
            token,
            mockRequest -> {
                final int code;
                if (mockRequest.getAccessToken() != null) {
                    code = 204;
                } else {
                    code = 401;
                }
                return new MockJsonResources.MockResource(code, JsonValue.NULL);
            }
        );
        final Stars stars = new GithubStars(
            res,
            URI.create("https://api.github.com/user/starred/john/test"),
            Mockito.mock(Storage.class)
        );

        MatcherAssert.assertThat(stars.add(), Matchers.is(true));
        MatcherAssert.assertThat(res.requests().first()
                .getAccessToken().value(),
            Matchers.equalTo(token.value()));
    }

    /**
     * GithubStars.add() returns false when star is not added.
     * (ex: due to unauthorized request).
     */
    @Test
    public void starIsNotAdded() {
        final MockJsonResources res = new MockJsonResources(
            mockRequest -> {
                final int code;
                if (mockRequest.getAccessToken() != null) {
                    code = 204;
                } else {
                    code = 401;
                }
                return new MockJsonResources.MockResource(code, JsonValue.NULL);
            }
        );
        final Stars stars = new GithubStars(
            res,
            URI.create("https://api.github.com/user/starred/john/test"),
            Mockito.mock(Storage.class)
        );

        MatcherAssert.assertThat(stars.add(), Matchers.is(false));
        MatcherAssert.assertThat(res.requests().first()
                .getAccessToken(),
            Matchers.nullValue());
    }

    /**
     * GithubStars can check if a repo is starred.
     */
    @Test
    public void canCheckIfRepoIsStarred() {
        final MockJsonResources res = new MockJsonResources(
            new AccessToken.Github("github_123"),
            req -> new MockJsonResources.MockResource(
                HttpURLConnection.HTTP_NO_CONTENT,
                JsonValue.NULL
            )
        );
        final Stars stars = new GithubStars(
            res,
            URI.create("https://api.github.com/user/starred/john/test"),
            Mockito.mock(Storage.class)
        );
        boolean isStarred = stars.isStarred();
        final MockJsonResources.MockRequest request = res.requests().first();
        MatcherAssert.assertThat(
            isStarred,
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            request.getHeaders(),
            Matchers.equalTo(
                Map.of(
                    "Accept", List.of("application/vnd.github.v3+json"),
                    "Authorization", List.of("token github_123")
                )
            )
        );
        MatcherAssert.assertThat(
            request.getUri(),
            Matchers.equalTo(
                URI.create("https://api.github.com/user/starred/john/test")
            )
        );
        MatcherAssert.assertThat(
            request.getMethod(),
            Matchers.equalTo("GET")
        );
    }

    /**
     * GithubStars can check if a repo is not starred.
     */
    @Test
    public void canCheckIfRepoIsNotStarred() {
        final MockJsonResources res = new MockJsonResources(
            req -> new MockJsonResources.MockResource(
                HttpURLConnection.HTTP_NOT_FOUND,
                JsonValue.NULL
            )
        );
        final Stars stars = new GithubStars(
            res,
            URI.create("https://api.github.com/user/starred/john/test"),
            Mockito.mock(Storage.class)
        );
        boolean isStarred = stars.isStarred();
        MatcherAssert.assertThat(
            isStarred,
            Matchers.is(false)
        );
    }

    /**
     * GithubStars#isStarred returns false if user is not authenticated.
     */
    @Test
    public void cantCheckIfRepoIsStarredWhenUnauthorized() {
        final MockJsonResources res = new MockJsonResources(
            new AccessToken.Github("github_123"),
            req -> new MockJsonResources.MockResource(
                HttpURLConnection.HTTP_UNAUTHORIZED,
                JsonValue.NULL
            )
        );
        final Stars stars = new GithubStars(
            res,
            URI.create("https://api.github.com/user/starred/john/test"),
            Mockito.mock(Storage.class)
        );
        boolean isStarred = stars.isStarred();
        MatcherAssert.assertThat(
            isStarred,
            Matchers.is(false)
        );
    }
}
