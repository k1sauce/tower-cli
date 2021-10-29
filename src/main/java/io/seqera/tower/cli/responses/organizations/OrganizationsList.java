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

package io.seqera.tower.cli.responses.organizations;

import java.io.PrintWriter;
import java.util.List;

import io.seqera.tower.cli.responses.Response;
import io.seqera.tower.cli.utils.TableList;
import io.seqera.tower.model.OrgAndWorkspaceDbDto;

public class OrganizationsList extends Response {

    public final String userName;
    public final List<OrgAndWorkspaceDbDto> organizations;

    public OrganizationsList(String userName, List<OrgAndWorkspaceDbDto> organizations) {
        this.userName = userName;
        this.organizations = organizations;
    }

    @Override
    public void toString(PrintWriter out) {
        out.println(ansi(String.format("%n  @|bold organizations for %s user:|@%n", userName)));

        if (organizations.isEmpty()) {
            out.println(ansi("    @|yellow No organizations found|@"));
            return;
        }

        TableList table = new TableList(out, 2, "ID", "Name").sortBy(0);
        table.setPrefix("    ");
        organizations.forEach(element -> {
            if (element.getOrgId() != null) {
                table.addRow(element.getOrgId().toString(), element.getOrgName());
            }
        });

        table.print();
        out.println("");
    }
}
