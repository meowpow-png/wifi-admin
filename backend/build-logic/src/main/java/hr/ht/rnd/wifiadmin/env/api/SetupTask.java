package hr.ht.rnd.wifiadmin.env.api;

import hr.ht.rnd.wifiadmin.env.internal.EnvFiles;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public abstract class SetupTask extends DefaultTask {

    public SetupTask() {
        setGroup("application");
        setDescription("Prepares the project for local development.");
    }

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    @TaskAction
    public void setup() {
        EnvFiles.createEnvFile(
                getOutputFile().get().getAsFile(),
                getLogger()
        );
    }
}
