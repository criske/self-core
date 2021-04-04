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
package com.selfxdsd.core.projects;

import com.selfxdsd.api.Language;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link Language} linked responses.
 * @author criske
 * @version $Id$
 * @since 0.0.72
 */
public final class LanguageLinkedResponsesTestCase {

    /**
     * Language can follow a resource linked from responses properties entry
     * value.
     */
    @Test
    public void shouldReadReplyFromClasspathLink() {
        final Language english = new English();
        MatcherAssert.assertThat(
            english.reply("commands.comment"),
            Matchers.startsWith(
                "Hi @%s! Here are the commands which I understand:"
            )
        );
    }

    /**
     * Language can follow a resource linked from responses properties entry
     * value but returns null if the file resource is not found.
     */
    @Test
    public void shouldFailToReadReplyFromClasspathLink() {
        final Language english = new Language(
            "commands_en.properties",
            "responses_bad_link_en.properties") {
        };
        MatcherAssert.assertThat(
            english.reply("commands.comment"),
            Matchers.nullValue()
        );
    }
}