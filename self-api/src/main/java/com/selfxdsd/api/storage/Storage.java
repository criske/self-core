/**
 * Copyright (c) 2020-2021, Self XDSD Contributors
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 *
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
package com.selfxdsd.api.storage;

import com.selfxdsd.api.*;

/**
 * Storage of Self.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #1135:30min Add a method called `jsonStorage` that will return
 *  JsonStorage interface. JsonStorage needs to be moved from `self-core-impl`
 *  to `self-api` module.
 */
public interface Storage extends AutoCloseable {

    /**
     * Get the users Storage API.
     * @return Users.
     */
    Users users();

    /**
     * Get the project managers Storage API.
     * @return ProjectManagers.
     */
    ProjectManagers projectManagers();

    /**
     * Get the projects Storage API.
     * @return Projects.
     */
    Projects projects();

    /**
     * Get the wallets Storage API.
     * @return Wallets.
     */
    Wallets wallets();

    /**
     * Get the contracts Storage API.
     * @return Contracts.
     */
    Contracts contracts();

    /**
     * Get the invoices Storage API.
     * @return Invoices.
     */
    Invoices invoices();

    /**
     * The the invoiced tasks Storage API.
     * @return InvoicedTasks.
     */
    InvoicedTasks invoicedTasks();

    /**
     * Get the contributors Storage API.
     * @return Contributors.
     */
    Contributors contributors();

    /**
     * Get the tasks Storage API.
     * @return Tasks.
     */
    Tasks tasks();

    /**
     * Get the resignations Storage API.
     * @return Resignations.
     */
    Resignations resignations();

    /**
     * Get the payout methods Storage API.
     * @return PayoutMethods.
     */
    PayoutMethods payoutMethods();

    /**
     * Get the payment methods Storage API.
     * @return PaymentMethods.
     */
    PaymentMethods paymentMethods();

    /**
     * Get the platform invoices Storage API.
     * @return PlatformInvoices.
     */
    PlatformInvoices platformInvoices();

    /**
     * Get the ApiTokens Storage API.
     * @return ApiTokens.
     */
    ApiTokens apiTokens();

    /**
     * Get the payments Storage API.
     * @return Payments.
     */
    Payments payments();

    /**
     * Get the storage for cached Provider Resources.
     * @return JsonStorage.
     */
    JsonStorage jsonStorage();
}
