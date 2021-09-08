/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.seqera.tower.cli;

import io.seqera.tower.cli.exceptions.MultiplePipelinesFoundException;
import io.seqera.tower.cli.exceptions.NoComputeEnvironmentException;
import io.seqera.tower.cli.exceptions.PipelineNotFoundException;
import io.seqera.tower.cli.responses.PipelinesCreated;
import io.seqera.tower.cli.responses.PipelinesDeleted;
import io.seqera.tower.cli.responses.PipelinesList;
import io.seqera.tower.cli.responses.PipelinesUpdated;
import io.seqera.tower.cli.responses.PipelinesView;
import io.seqera.tower.model.ComputeEnv;
import io.seqera.tower.model.Launch;
import io.seqera.tower.model.PipelineDbDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static io.seqera.tower.cli.commands.AbstractApiCmd.USER_WORKSPACE_NAME;
import static org.apache.commons.lang3.StringUtils.chop;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class PipelinesCmdTest extends BaseCmdTest {

    @Test
    void testUpdate(MockServerClient mock) {

        mock.when(
                request().withMethod("GET").withPath("/pipelines").withQueryStringParameter("search", "sleep_one_minute"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipelines\":[{\"pipelineId\":217997727159863,\"name\":\"sleep_one_minute\",\"description\":null,\"icon\":null,\"repository\":\"https://github.com/pditommaso/nf-sleep\",\"userId\":4,\"userName\":\"jordi\",\"userFirstName\":null,\"userLastName\":null,\"orgId\":null,\"orgName\":null,\"workspaceId\":null,\"workspaceName\":null,\"visibility\":null}],\"totalSize\":1}").withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("GET").withPath("/pipelines/217997727159863/launch"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("pipelines_update")).withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("PUT").withPath("/pipelines/217997727159863").withBody("{\"description\":\"Sleep one minute and exit\",\"launch\":{\"computeEnvId\":\"vYOK4vn7spw7bHHWBDXZ2\",\"pipeline\":\"https://github.com/pditommaso/nf-sleep\",\"workDir\":\"s3://nextflow-ci/jordeu\",\"paramsText\":\"timeout: 60\\n\",\"pullLatest\":false,\"stubRun\":false}}"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipeline\":{\"pipelineId\":217997727159863,\"name\":\"sleep_one_minute\",\"description\":\"Sleep one minute and exit\",\"icon\":null,\"repository\":\"https://github.com/pditommaso/nf-sleep\",\"userId\":4,\"userName\":\"jordi\",\"userFirstName\":null,\"userLastName\":null,\"orgId\":null,\"orgName\":null,\"workspaceId\":null,\"workspaceName\":null,\"visibility\":null}}").withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "update", "-n", "sleep_one_minute", "-d", "Sleep one minute and exit");

        assertEquals("", out.stdErr);
        assertEquals(new PipelinesUpdated(USER_WORKSPACE_NAME, "sleep_one_minute").toString(), out.stdOut);
    }

    @Test
    void testUpdateComputeEnv(MockServerClient mock) {

        mock.when(
                request().withMethod("GET").withPath("/pipelines").withQueryStringParameter("search", "sleep_one_minute"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipelines\":[{\"pipelineId\":217997727159863,\"name\":\"sleep_one_minute\",\"description\":null,\"icon\":null,\"repository\":\"https://github.com/pditommaso/nf-sleep\",\"userId\":4,\"userName\":\"jordi\",\"userFirstName\":null,\"userLastName\":null,\"orgId\":null,\"orgName\":null,\"workspaceId\":null,\"workspaceName\":null,\"visibility\":null}],\"totalSize\":1}").withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("GET").withPath("/pipelines/217997727159863/launch"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("pipelines_update")).withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("GET").withPath("/compute-envs").withQueryStringParameter("status", "AVAILABLE"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"computeEnvs\":[{\"id\":\"isnEDBLvHDAIteOEF44ow\",\"name\":\"demo\",\"platform\":\"aws-batch\",\"status\":\"AVAILABLE\",\"message\":null,\"lastUsed\":null,\"primary\":null,\"workspaceName\":null,\"visibility\":null}]}").withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("GET").withPath("/compute-envs/isnEDBLvHDAIteOEF44ow"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("compute_env_view")).withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("PUT").withPath("/pipelines/217997727159863").withBody("{\"launch\":{\"computeEnvId\":\"isnEDBLvHDAIteOEF44ow\",\"pipeline\":\"https://github.com/pditommaso/nf-sleep\",\"workDir\":\"s3://nextflow-ci/jordeu\",\"paramsText\":\"timeout: 60\\n\",\"pullLatest\":false,\"stubRun\":false}}"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipeline\":{\"pipelineId\":217997727159863,\"name\":\"sleep_one_minute\",\"description\":\"Sleep one minute and exit\",\"icon\":null,\"repository\":\"https://github.com/pditommaso/nf-sleep\",\"userId\":4,\"userName\":\"jordi\",\"userFirstName\":null,\"userLastName\":null,\"orgId\":null,\"orgName\":null,\"workspaceId\":null,\"workspaceName\":null,\"visibility\":null}}").withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "update", "-n", "sleep_one_minute", "-c", "demo");

        assertEquals("", out.stdErr);
        assertEquals(new PipelinesUpdated(USER_WORKSPACE_NAME, "sleep_one_minute").toString(), out.stdOut);
    }

    @Test
    void testCreate(MockServerClient mock) throws IOException {

        mock.when(
                request().withMethod("GET").withPath("/compute-envs").withQueryStringParameter("status", "AVAILABLE"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"computeEnvs\":[{\"id\":\"vYOK4vn7spw7bHHWBDXZ2\",\"name\":\"demo\",\"platform\":\"aws-batch\",\"status\":\"AVAILABLE\",\"message\":null,\"lastUsed\":null,\"primary\":null,\"workspaceName\":null,\"visibility\":null}]}").withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("GET").withPath("/compute-envs/vYOK4vn7spw7bHHWBDXZ2"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("compute_env_demo")).withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("POST").withPath("/pipelines").withBody("{\"name\":\"sleep_one_minute\",\"launch\":{\"computeEnvId\":\"vYOK4vn7spw7bHHWBDXZ2\",\"pipeline\":\"https://github.com/pditommaso/nf-sleep\",\"workDir\":\"s3://nextflow-ci/jordeu\",\"paramsText\":\"timeout: 60\\n\"}}"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipeline\":{\"pipelineId\":18388134856008,\"name\":\"sleep_one_minute\",\"description\":null,\"icon\":null,\"repository\":\"https://github.com/pditommaso/nf-sleep\",\"userId\":4,\"userName\":\"jordi\",\"userFirstName\":null,\"userLastName\":null,\"orgId\":null,\"orgName\":null,\"workspaceId\":null,\"workspaceName\":null,\"visibility\":null}}").withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "create", "-n", "sleep_one_minute", "--params", tempFile("timeout: 60\n", "params", ".yml"), "https://github.com/pditommaso/nf-sleep");

        assertEquals("", out.stdErr);
        assertEquals(new PipelinesCreated(USER_WORKSPACE_NAME, "sleep_one_minute").toString(), out.stdOut);
        assertEquals(0, out.exitCode);

    }

    @Test
    void testCreateWithComputeEnv(MockServerClient mock) {

        mock.when(
                request().withMethod("GET").withPath("/compute-envs").withQueryStringParameter("status", "AVAILABLE"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"computeEnvs\":[{\"id\":\"vYOK4vn7spw7bHHWBDXZ2\",\"name\":\"demo\",\"platform\":\"aws-batch\",\"status\":\"AVAILABLE\",\"message\":null,\"lastUsed\":null,\"primary\":null,\"workspaceName\":null,\"visibility\":null}]}").withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("GET").withPath("/compute-envs/vYOK4vn7spw7bHHWBDXZ2"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("compute_env_demo")).withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("POST").withPath("/pipelines").withBody("{\"name\":\"demo\",\"launch\":{\"computeEnvId\":\"vYOK4vn7spw7bHHWBDXZ2\",\"pipeline\":\"https://github.com/pditommaso/nf-sleep\",\"workDir\":\"s3://nextflow-ci/jordeu\"}}"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipeline\":{\"pipelineId\":18388134856008,\"name\":\"demo\",\"description\":null,\"icon\":null,\"repository\":\"https://github.com/pditommaso/nf-sleep\",\"userId\":4,\"userName\":\"jordi\",\"userFirstName\":null,\"userLastName\":null,\"orgId\":null,\"orgName\":null,\"workspaceId\":null,\"workspaceName\":null,\"visibility\":null}}").withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "create", "-n", "demo", "-c", "demo", "https://github.com/pditommaso/nf-sleep");

        assertEquals("", out.stdErr);
        assertEquals(new PipelinesCreated(USER_WORKSPACE_NAME, "demo").toString(), out.stdOut);
        assertEquals(0, out.exitCode);

    }

    @Test
    void testCreateWithStagingScripts(MockServerClient mock) throws IOException {

        mock.when(
                request().withMethod("GET").withPath("/compute-envs").withQueryStringParameter("status", "AVAILABLE"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"computeEnvs\":[{\"id\":\"vYOK4vn7spw7bHHWBDXZ2\",\"name\":\"demo\",\"platform\":\"aws-batch\",\"status\":\"AVAILABLE\",\"message\":null,\"lastUsed\":null,\"primary\":null,\"workspaceName\":null,\"visibility\":null}]}").withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("GET").withPath("/compute-envs/vYOK4vn7spw7bHHWBDXZ2"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("compute_env_demo")).withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("POST").withPath("/pipelines").withBody("{\"name\":\"staging\",\"launch\":{\"computeEnvId\":\"vYOK4vn7spw7bHHWBDXZ2\",\"pipeline\":\"https://github.com/pditommaso/nf-sleep\",\"workDir\":\"s3://nextflow-ci/staging\",\"preRunScript\":\"pre_run_this\",\"postRunScript\":\"post_run_this\"}}"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipeline\":{\"pipelineId\":21697594587521,\"name\":\"staging\",\"description\":null,\"icon\":null,\"repository\":\"https://github.com/pditommaso/nf-sleep\",\"userId\":4,\"userName\":\"jordi\",\"userFirstName\":null,\"userLastName\":null,\"orgId\":null,\"orgName\":null,\"workspaceId\":null,\"workspaceName\":null,\"visibility\":null}}").withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "create", "-n", "staging", "-w", "s3://nextflow-ci/staging", "--pre-run", tempFile("pre_run_this", "pre", "sh"), "--post-run", tempFile("post_run_this", "post", "sh"), "https://github.com/pditommaso/nf-sleep");

        assertEquals("", out.stdErr);
        assertEquals(new PipelinesCreated(USER_WORKSPACE_NAME, "staging").toString(), out.stdOut);
        assertEquals(0, out.exitCode);

    }

    @Test
    void testMissingComputeEnvironment(MockServerClient mock) {

        mock.when(
                request().withMethod("GET").withPath("/compute-envs").withQueryStringParameter("status", "AVAILABLE"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"computeEnvs\":[]}").withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "create", "-n", "sleep_one_minute", "https://github.com/pditommaso/nf-sleep");

        assertEquals(errorMessage(out.app, new NoComputeEnvironmentException(USER_WORKSPACE_NAME)), out.stdErr);
        assertEquals("", out.stdOut);
        assertEquals(-1, out.exitCode);

    }

    @Test
    void testDelete(MockServerClient mock) {
        mock.when(
                request().withMethod("GET").withPath("/pipelines").withQueryStringParameter("search", "sleep"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("pipelines_sleep")).withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("DELETE").withPath("/pipelines/183522618315672"), exactly(1)
        ).respond(
                response().withStatusCode(204)
        );

        ExecOut out = exec(mock, "pipelines", "delete", "-n", "sleep");

        assertEquals("", out.stdErr);
        assertEquals(new PipelinesDeleted("sleep", USER_WORKSPACE_NAME).toString(), out.stdOut);
        assertEquals(0, out.exitCode);
    }

    @Test
    void testDeleteNotFound(MockServerClient mock) {
        mock.when(
                request().withMethod("GET").withPath("/pipelines").withQueryStringParameter("search", "sleep_all"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipelines\":[],\"totalSize\":0}").withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "delete", "-n", "sleep_all");

        assertEquals(errorMessage(out.app, new PipelineNotFoundException("sleep_all", USER_WORKSPACE_NAME)), out.stdErr);
        assertEquals("", out.stdOut);
        assertEquals(-1, out.exitCode);
    }

    @Test
    void testDeleteMultipleMatch(MockServerClient mock) {
        mock.when(
                request().withMethod("GET").withPath("/pipelines").withQueryStringParameter("search", "hello"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("pipelines_multiple")).withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "delete", "-n", "hello");

        assertEquals(errorMessage(out.app, new MultiplePipelinesFoundException("hello", USER_WORKSPACE_NAME)), out.stdErr);
        assertEquals("", out.stdOut);
        assertEquals(-1, out.exitCode);
    }

    @Test
    void testList(MockServerClient mock) {

        mock.when(
                request().withMethod("GET").withPath("/pipelines"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("pipelines_list")).withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "list");

        assertEquals("", out.stdErr);
        assertEquals(chop(new PipelinesList(USER_WORKSPACE_NAME, List.of(
                new PipelineDbDto()
                        .pipelineId(183522618315672L)
                        .name("sleep_one_minute")
                        .repository("https://github.com/pditommaso/nf-sleep")
                        .userId(4L)
                        .userName("jordi")
        )).toString()), out.stdOut);
        assertEquals(0, out.exitCode);
    }

    @Test
    void testListEmpty(MockServerClient mock) {

        mock.when(
                request().withMethod("GET").withPath("/pipelines"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{ \"pipelines\": [], \"totalSize\": 0 }").withContentType(MediaType.APPLICATION_JSON)
        );

        ExecOut out = exec(mock, "pipelines", "list");

        assertEquals("", out.stdErr);
        assertEquals(chop(new PipelinesList(USER_WORKSPACE_NAME, List.of()).toString()), out.stdOut);
        assertEquals(0, out.exitCode);
    }

    @Test
    void testView(MockServerClient mock) {

        mock.when(
                request().withMethod("GET").withPath("/pipelines").withQueryStringParameter("search", "sleep_one_minute"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody("{\"pipelines\":[{\"pipelineId\":217997727159863,\"name\":\"sleep_one_minute\",\"description\":null,\"icon\":null,\"repository\":\"https://github.com/pditommaso/nf-sleep\",\"userId\":4,\"userName\":\"jordi\",\"userFirstName\":null,\"userLastName\":null,\"orgId\":null,\"orgName\":null,\"workspaceId\":null,\"workspaceName\":null,\"visibility\":null}],\"totalSize\":1}").withContentType(MediaType.APPLICATION_JSON)
        );

        mock.when(
                request().withMethod("GET").withPath("/pipelines/217997727159863/launch"), exactly(1)
        ).respond(
                response().withStatusCode(200).withBody(loadResource("pipelines_update")).withContentType(MediaType.APPLICATION_JSON)
        );


        ExecOut out = exec(mock, "pipelines", "view", "-n", "sleep_one_minute");

        assertEquals("", out.stdErr);
        assertEquals(StringUtils.chop(new PipelinesView(
                USER_WORKSPACE_NAME,
                new PipelineDbDto().pipelineId(217997727159863L).name("sleep_one_minute").repository("https://github.com/pditommaso/nf-sleep"),
                new Launch()
                        .id("oRptz8ekYa3BSA4Nnx7Qn")
                        .pipeline("https://github.com/pditommaso/nf-sleep")
                        .workDir("s3://nextflow-ci/jordeu")
                        .paramsText("timeout: 60\n")
                        .dateCreated(OffsetDateTime.parse("2021-09-08T06:50:54Z"))
                        .lastUpdated(OffsetDateTime.parse("2021-09-08T06:50:54Z"))
                        .resume(false)
                        .pullLatest(false)
                        .stubRun(false)
                        .computeEnv(
                                new ComputeEnv().id("vYOK4vn7spw7bHHWBDXZ2").name("demo")
                        )
                ).toString()), out.stdOut
        );
        assertEquals(0, out.exitCode);

    }

}
