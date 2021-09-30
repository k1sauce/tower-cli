/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.seqera.tower.cli;

import io.seqera.tower.cli.commands.AbstractCmd;
import io.seqera.tower.cli.commands.ComputeEnvCmd;
import io.seqera.tower.cli.commands.CredentialsCmd;
import io.seqera.tower.cli.commands.LaunchCmd;
import io.seqera.tower.cli.commands.PipelinesCmd;
import io.seqera.tower.cli.commands.RunsCmd;
import io.seqera.tower.cli.commands.WorkspacesCmd;
import picocli.AutoComplete.GenerateCompletion;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

import java.io.PrintWriter;


@Command(
        name = "towr",
        description = "Nextflow Tower CLI",
        subcommands = {
                LaunchCmd.class,
                CredentialsCmd.class,
                PipelinesCmd.class,
                ComputeEnvCmd.class,
                RunsCmd.class,
                WorkspacesCmd.class,
                GenerateCompletion.class,
        }
)
public class Tower extends AbstractCmd {
    @Spec
    public CommandSpec spec;

    @Option(names = {"-t", "--access-token"}, description = "Tower personal access token (TOWER_ACCESS_TOKEN)", defaultValue = "${TOWER_ACCESS_TOKEN}")
    public String token;

    @Option(names = {"-i", "--workspace-id"}, description = "Workspace numeric identifier (TOWER_WORKSPACE_ID)", defaultValue = "${TOWER_WORKSPACE_ID}")
    public Long workspaceId;

    @ArgGroup(exclusive = false)
    public OrgAndWorkspace orgAndWorkspaceNames;

    @Option(names = {"-u", "--url"}, description = "Tower server API endpoint URL. Defaults to tower.nf (TOWER_API_ENDPOINT)", defaultValue = "${TOWER_API_ENDPOINT:-https://api.tower.nf}", required = true)
    public String url;

    @Option(names = {"-j", "--json"}, description = "Show output as JSON")
    public boolean json;

    @Option(names = {"-v", "--verbose"}, description = "Shows HTTP request/response logs at stderr")
    public boolean verbose;

    public Tower() {
    }

    public static void main(String[] args) {
        System.exit(buildCommandLine().execute(args));
    }

    protected static CommandLine buildCommandLine() {
        Tower app = new Tower();
        CommandLine cmd = new CommandLine(app);
        cmd.setUsageHelpLongOptionsMaxWidth(40);
        return cmd;
    }

    @Override
    public Integer call() {
        // if the command was invoked without subcommand, show the usage help
        spec.commandLine().usage(System.err);
        return -1;
    }

    public PrintWriter getErr() {
        return spec.commandLine().getErr();
    }

    public PrintWriter getOut() {
        return spec.commandLine().getOut();
    }

    public static class OrgAndWorkspace {
        @Option(names = {"-w", "--workspace"}, description = "Workspace name (case insensitive)", defaultValue = "${TOWER_WORKSPACE_NAME}")
        public String workspace;

        @Option(names = {"-o", "--organization"}, description = "Organization name (case insensitive)", defaultValue = "${TOWER_ORG_NAME}")
        public String organization;
    }
}
