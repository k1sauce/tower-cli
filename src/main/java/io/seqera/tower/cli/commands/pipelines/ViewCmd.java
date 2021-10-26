package io.seqera.tower.cli.commands.pipelines;

import io.seqera.tower.ApiException;
import io.seqera.tower.cli.responses.PipelinesView;
import io.seqera.tower.cli.responses.Response;
import io.seqera.tower.model.DescribeLaunchResponse;
import io.seqera.tower.model.PipelineDbDto;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "view",
        description = "View pipeline details"
)
public class ViewCmd extends AbstractPipelinesCmd {

    @Option(names = {"-n", "--name"}, description = "Pipeline name", required = true)
    public String name;

    @Override
    protected Response exec() throws ApiException {
        PipelineDbDto pipe = pipelineByName(name);
        DescribeLaunchResponse resp = api().describePipelineLaunch(pipe.getPipelineId(), workspaceId());
        return new PipelinesView(workspaceRef(), pipe, resp.getLaunch());
    }


}