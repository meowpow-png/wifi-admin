package hr.ht.rnd.wifiadmin;

import hr.ht.rnd.wifiadmin.env.api.Environment;
import hr.ht.rnd.wifiadmin.env.api.EnvironmentExtension;
import hr.ht.rnd.wifiadmin.env.api.SetupTask;
import hr.ht.rnd.wifiadmin.env.internal.EnvFiles;
import hr.ht.rnd.wifiadmin.env.internal.EnvironmentFactory;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Files;
import java.nio.file.Path;

@NullMarked
@SuppressWarnings("unused")
public final class EnvironmentPlugin implements Plugin<Project> {

    private static final String ENV_FILE = ".env";

    @Override
    public void apply(Project project) {
        var extension = project.getExtensions().create(
                "environment",
                EnvironmentExtension.class
        );
        var env = environment(project.file(ENV_FILE).toPath());
        extension.setEnvironment(env);

        project.getTasks().register("setup", SetupTask.class, task ->
                task.getOutputFile().set(project.file(ENV_FILE))
        );
    }

    private Environment environment(Path path) {
        if (Files.exists(path)) {
            return EnvFiles.loadEnvFile(path);
        }
        return EnvironmentFactory.create();
    }
}
