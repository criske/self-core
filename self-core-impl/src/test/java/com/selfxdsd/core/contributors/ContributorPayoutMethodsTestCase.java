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
package com.selfxdsd.core.contributors;

import com.selfxdsd.api.*;
import com.selfxdsd.api.storage.Storage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * Unit tests for {@link ContributorPayoutMethods}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.22
 */
public final class ContributorPayoutMethodsTestCase {

    /**
     * ContributorPayoutMethods can be iterated.
     */
    @Test
    public void canBeIterated() {
        final PayoutMethods methods = new ContributorPayoutMethods(
            Mockito.mock(Contributor.class),
            Arrays.asList(
                Mockito.mock(PayoutMethod.class),
                Mockito.mock(PayoutMethod.class),
                Mockito.mock(PayoutMethod.class)
            ),
            Mockito.mock(Storage.class)
        );
        MatcherAssert.assertThat(
            methods,
            Matchers.iterableWithSize(3)
        );
    }

    /**
     * ContributorPayoutMethods.ofContributor should return self if the
     * Contributor matches.
     */
    @Test
    public void ofContributorReturnsSelf() {
        final Contributor contributor = Mockito.mock(Contributor.class);
        final PayoutMethods methods = new ContributorPayoutMethods(
            contributor,
            Arrays.asList(
                Mockito.mock(PayoutMethod.class),
                Mockito.mock(PayoutMethod.class),
                Mockito.mock(PayoutMethod.class)
            ),
            Mockito.mock(Storage.class)
        );
        MatcherAssert.assertThat(
            methods.ofContributor(contributor),
            Matchers.is(methods)
        );
    }

    /**
     * ContributorPayoutMethods.ofContributor should throw an ISE if the
     * given contributor is different.
     */
    @Test (expected = IllegalStateException.class)
    public void ofContributorComplainsOnDifferentContributor() {
        final Contributor contributor = Mockito.mock(Contributor.class);
        final Contributor other = Mockito.mock(Contributor.class);
        final PayoutMethods methods = new ContributorPayoutMethods(
            contributor,
            Arrays.asList(
                Mockito.mock(PayoutMethod.class),
                Mockito.mock(PayoutMethod.class),
                Mockito.mock(PayoutMethod.class)
            ),
            Mockito.mock(Storage.class)
        );
        methods.ofContributor(other);
    }

    /**
     * We shouldn't be able to register a PaymentMethod for a different
     * Contributor.
     */
    @Test (expected = IllegalStateException.class)
    public void registerComplainsOnDifferentContributor() {
        final Contributor contributor = Mockito.mock(Contributor.class);
        final Contributor other = Mockito.mock(Contributor.class);
        final PayoutMethods methods = new ContributorPayoutMethods(
            contributor,
            Arrays.asList(
                Mockito.mock(PayoutMethod.class),
                Mockito.mock(PayoutMethod.class),
                Mockito.mock(PayoutMethod.class)
            ),
            Mockito.mock(Storage.class)
        );
        methods.register(
            other,
            PayoutMethod.Type.STRIPE,
            "stripe-123w"
        );
    }

    /**
     * We shouldn't be able to register more PayoutMethods of the same type.
     */
    @Test (expected = IllegalStateException.class)
    public void registerComplainsIfMethodTypeExists() {
        final Contributor contributor = Mockito.mock(Contributor.class);
        final PayoutMethod stripe = Mockito.mock(PayoutMethod.class);
        Mockito.when(stripe.type()).thenReturn(PayoutMethod.Type.STRIPE);
        final PayoutMethods methods = new ContributorPayoutMethods(
            contributor,
            Arrays.asList(stripe),
            Mockito.mock(Storage.class)
        );
        methods.register(
            contributor, PayoutMethod.Type.STRIPE, "stripe-1235ty"
        );
    }

    /**
     * We can register a new PayoutMethod.
     */
    @Test
    public void registerMethodWorks() {
        final PayoutMethod registered = Mockito.mock(PayoutMethod.class);

        final Contributor contributor = Mockito.mock(Contributor.class);
        final PayoutMethod paypal = Mockito.mock(PayoutMethod.class);
        Mockito.when(paypal.type()).thenReturn(PayoutMethod.Type.PAYPAL);

        final PayoutMethods all = Mockito.mock(PayoutMethods.class);
        Mockito.when(
            all.register(
                contributor,
                PayoutMethod.Type.STRIPE,
                "stripe-123w"
            )
        ).thenReturn(registered);
        final Storage storage = Mockito.mock(Storage.class);
        Mockito.when(storage.payoutMethods()).thenReturn(all);

        final PayoutMethods methods = new ContributorPayoutMethods(
            contributor,
            Arrays.asList(paypal),
            storage
        );
        MatcherAssert.assertThat(
            methods,
            Matchers.iterableWithSize(1)
        );
        final PayoutMethod method = methods.register(
            contributor,
            PayoutMethod.Type.STRIPE,
            "stripe-123w"
        );
        MatcherAssert.assertThat(
            method,
            Matchers.is(registered)
        );
        MatcherAssert.assertThat(
            methods,
            Matchers.iterableWithSize(2)
        );
    }

    /**
     * It can return the found PayoutMethod by type.
     */
    @Test
    public void returnsFoundByType() {
        final PayoutMethod stripe = Mockito.mock(PayoutMethod.class);
        Mockito.when(stripe.type()).thenReturn("STRIPE");

        final PayoutMethod paypal = Mockito.mock(PayoutMethod.class);
        Mockito.when(paypal.type()).thenReturn("PAYPAL");

        final PayoutMethods methods = new ContributorPayoutMethods(
            Mockito.mock(Contributor.class),
            Arrays.asList(
                paypal,
                stripe
            ),
            Mockito.mock(Storage.class)
        );

        MatcherAssert.assertThat(
            methods.getByType("STRIPE"),
            Matchers.is(stripe)
        );
    }

    /**
     * It returns null if the type of PayoutMethod is not found.
     */
    @Test
    public void returnsNullByType() {
        final PayoutMethod stripe = Mockito.mock(PayoutMethod.class);
        Mockito.when(stripe.type()).thenReturn("STRIPE");

        final PayoutMethod paypal = Mockito.mock(PayoutMethod.class);
        Mockito.when(paypal.type()).thenReturn("PAYPAL");

        final PayoutMethods methods = new ContributorPayoutMethods(
            Mockito.mock(Contributor.class),
            Arrays.asList(
                paypal,
                stripe
            ),
            Mockito.mock(Storage.class)
        );

        MatcherAssert.assertThat(
            methods.getByType("GREENPAY"),
            Matchers.nullValue()
        );
    }

    /**
     * We can remove a new PayoutMethod.
     */
    @Test
    public void removeWorks() {
        final Contributor contributor = Mockito.mock(Contributor.class);
        final PayoutMethod paypal = Mockito.mock(PayoutMethod.class);
        Mockito.when(paypal.type()).thenReturn(PayoutMethod.Type.PAYPAL);
        final PayoutMethod stripe = Mockito.mock(PayoutMethod.class);
        Mockito.when(stripe.type()).thenReturn(PayoutMethod.Type.STRIPE);
        Mockito.when(stripe.contributor()).thenReturn(contributor);
        Mockito.when(stripe.remove()).thenReturn(Boolean.TRUE);

        final PayoutMethods methods = new ContributorPayoutMethods(
            contributor,
            Arrays.asList(paypal, stripe),
            Mockito.mock(Storage.class)
        );
        MatcherAssert.assertThat(
            methods,
            Matchers.iterableWithSize(2)
        );
        final boolean removed = methods.remove(stripe);

        MatcherAssert.assertThat(
            removed,
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            methods,
            Matchers.iterableWithSize(1)
        );
    }

    /**
     * Method remove should complain if we try to remove the PayoutMethod of
     * another Contributor.
     */
    @Test(expected = IllegalStateException.class)
    public void removeComplainsOnOtherContributor() {
        final Contributor contributor = Mockito.mock(Contributor.class);
        final PayoutMethod paypal = Mockito.mock(PayoutMethod.class);
        Mockito.when(paypal.type()).thenReturn(PayoutMethod.Type.PAYPAL);

        final PayoutMethods methods = new ContributorPayoutMethods(
            contributor,
            Arrays.asList(paypal),
            Mockito.mock(Storage.class)
        );
        MatcherAssert.assertThat(
            methods,
            Matchers.iterableWithSize(1)
        );

        final PayoutMethod stripe = Mockito.mock(PayoutMethod.class);
        Mockito.when(paypal.type()).thenReturn(PayoutMethod.Type.STRIPE);
        Mockito.when(stripe.contributor()).thenReturn(
            Mockito.mock(Contributor.class)
        );
        final boolean removed = methods.remove(stripe);
    }
}
