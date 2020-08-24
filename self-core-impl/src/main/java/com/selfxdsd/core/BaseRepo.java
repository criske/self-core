/**
 * Copyright (c) 2020, Self XDSD Contributors
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

import com.selfxdsd.api.Project;
import com.selfxdsd.api.Repo;
import com.selfxdsd.api.User;
import com.selfxdsd.api.exceptions.RepoAlreadyActiveException;
import com.selfxdsd.api.storage.Storage;

import javax.json.JsonObject;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Base implementation of {@link com.selfxdsd.api.Repo}.
 * "Rt" stands for "Runtime"
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
abstract class BaseRepo implements Repo {
    /**
     * URI pointing to this repo.
     */
    private final URI uri;

    /**
     * The Provider's Json resources.
     */
    private final JsonResources resources;

    /**
     * Owner of this repository.
     */
    private final User owner;

    /**
     * This repo's info in JSON.
     */
    private JsonObject json;

    /**
     * Storage used for activation.
     */
    private final Storage storage;

    /**
     * Constructor.
     * @param resources The Provider's Json resources.
     * @param repo URI Pointing to this repo.
     * @param owner Owner of this repo.
     * @param storage Storage used for activation.
     */
    BaseRepo(
        final JsonResources resources,
        final URI repo,
        final User owner,
        final Storage storage
    ) {
        this.resources = resources;
        this.uri = repo;
        this.owner = owner;
        this.storage = storage;
    }

    @Override
    public User owner() {
        return this.owner;
    }

    @Override
    public JsonObject json() {
        if(this.json == null) {
            final Resource repo = this.resources.get(this.uri);
            if(repo.statusCode() == HttpURLConnection.HTTP_OK) {
                this.json = repo.asJsonObject();
            } else {
                throw new IllegalStateException(
                    "Unexpected response when fetching [" + this.uri +"]. "
                  + "Expected 200 OK, but got " + repo.statusCode() + "."
                );
            }
        }
        return this.json;
    }

    /**
     * Get the Storage.
     * @return Storage.
     */
    Storage storage() {
        return this.storage;
    }

    /**
     * Get the JsonResources.
     * @return JsonResources.
     */
    JsonResources resources() {
        return this.resources;
    }

    /**
     * Get the URI.
     * @return URI.
     */
    URI repoUri() {
        return this.uri;
    }

    @Override
    public Project activate() {
        final boolean isActive = this.storage().projects()
            .getProjectById(this.fullName(), this.provider()) != null;
        if (isActive) {
            throw new RepoAlreadyActiveException(this);
        }
        return this.storage()
            .projectManagers()
            .pick(provider())
            .assign(this);
    }

    @Override
    public String provider() {
        return owner().provider().name();
    }

}
