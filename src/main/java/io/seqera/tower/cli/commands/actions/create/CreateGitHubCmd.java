package io.seqera.tower.cli.commands.actions.create;

import io.seqera.tower.model.ActionSource;
import picocli.CommandLine;

@CommandLine.Command(
        name = "github",
        description = "Creates a GitHub action"
)
public class CreateGitHubCmd extends AbstractCreateCmd {
    @Override
    protected ActionSource getSource() {
        return ActionSource.GITHUB;
    }
}
