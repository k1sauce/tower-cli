package io.seqera.tower.cli.commands.computeenv.create;

import io.seqera.tower.cli.commands.computeenv.platforms.GoogleLifeSciencesPlatform;
import io.seqera.tower.cli.commands.computeenv.platforms.Platform;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(
        name = "google",
        description = "Create new Google life sciences compute environment"
)
public class CreateGoogleCmd extends AbstractCreateCmd {

    @Mixin
    public GoogleLifeSciencesPlatform platform;

    @Override
    protected Platform getPlatform() {
        return platform;
    }
}
