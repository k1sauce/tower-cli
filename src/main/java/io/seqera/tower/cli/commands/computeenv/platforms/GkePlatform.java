package io.seqera.tower.cli.commands.computeenv.platforms;

import io.seqera.tower.ApiException;
import io.seqera.tower.cli.utils.FilesHelper;
import io.seqera.tower.model.ComputeEnv.PlatformEnum;
import io.seqera.tower.model.GkeComputeConfig;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

import java.io.IOException;

public class GkePlatform extends AbstractPlatform<GkeComputeConfig> {

    @Option(names = {"-r", "--region"}, description = "AWS region", required = true)
    public String region;

    @Option(names = {"--cluster-name"}, description = "The AWS EKS cluster name", required = true)
    public String clusterName;

    @Option(names = {"--namespace"}, description = "Namespace", required = true)
    public String namespace;

    @Option(names = {"--head-account"}, description = "Head service account", required = true)
    public String headAccount;

    @Option(names = {"--storage-claim"}, description = "Storage claim name")
    public String storageClaim;

    @ArgGroup(heading = "%nAdvanced options:%n", validate = false)
    public K8sPlatform.AdvancedOptions adv;

    public GkePlatform() {
        super(PlatformEnum.GKE_PLATFORM);
    }

    @Override
    public GkeComputeConfig computeConfig() throws ApiException, IOException {
        GkeComputeConfig config = new GkeComputeConfig();
        config
                .region(region)
                .clusterName(clusterName)
                .namespace(namespace)
                .headServiceAccount(headAccount)
                .storageClaimName(storageClaim)

                // Advance
                .storageMountPath(adv().storageMount)
                .computeServiceAccount(adv().computeAccount)
                .podCleanup(adv().podCleanup)
                .headPodSpec(FilesHelper.readString(adv().headPodSpec))
                .servicePodSpec(FilesHelper.readString(adv().servicePodSpec))

                // Common
                .platform(type().getValue())
                .workDir(workDir)

                // Stagging
                .preRunScript(preRunScriptString())
                .postRunScript(postRunScriptString());

        return config;
    }

    private K8sPlatform.AdvancedOptions adv() {
        if (adv == null) {
            return new K8sPlatform.AdvancedOptions();
        }
        return adv;
    }
}
