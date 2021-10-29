/*
 * Copyright (c) 2021, Seqera Labs.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This Source Code Form is "Incompatible With Secondary Licenses", as
 * defined by the Mozilla Public License, v. 2.0.
 */

package io.seqera.tower.cli.responses.members;

import io.seqera.tower.cli.responses.Response;

public class MembersDeleted extends Response {

    public final String user;
    public final String organizationName;

    public MembersDeleted(String user, String organizationName) {
        this.user = user;
        this.organizationName = organizationName;
    }

    @Override
    public String toString() {
        return ansi(String.format("%n  @|yellow Member '%s' deleted from organization '%s'|@%n", user, organizationName));
    }
}
