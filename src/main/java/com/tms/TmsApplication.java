package com.tms;

import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@EnableScheduling
public class TmsApplication {

    private static final String RESTART_FLAG = "tms.relaunched.with.opens";
    private static final String[] REQUIRED_ADD_OPENS = {
            "--add-opens=java.base/java.lang=ALL-UNNAMED",
            "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens=java.base/java.io=ALL-UNNAMED",
            "--add-opens=java.base/java.util=ALL-UNNAMED"
    };

    public static void main(String[] args) {
        if (shouldRelaunchWithAddOpens()) {
            int exitCode = relaunchWithAddOpens(args);
            if (exitCode >= 0) {
                System.exit(exitCode);
                return;
            }
        }

        SpringApplication.run(TmsApplication.class, args);
    }

    private static boolean shouldRelaunchWithAddOpens() {
        if (Boolean.getBoolean(RESTART_FLAG)) {
            return false;
        }

        try {
            Method defineClass = ClassLoader.class.getDeclaredMethod(
                    "defineClass",
                    String.class,
                    byte[].class,
                    int.class,
                    int.class,
                    java.security.ProtectionDomain.class
            );
            defineClass.setAccessible(true);
            return false;
        } catch (InaccessibleObjectException ex) {
            return true;
        } catch (ReflectiveOperationException | SecurityException ex) {
            return false;
        }
    }

    private static int relaunchWithAddOpens(String[] args) {
        String javaHome = System.getProperty("java.home");
        String javaExe = javaHome + "\\bin\\java.exe";

        List<String> command = new ArrayList<>();
        command.add(javaExe);
        for (String addOpens : REQUIRED_ADD_OPENS) {
            command.add(addOpens);
        }

        command.add("-D" + RESTART_FLAG + "=true");
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add(TmsApplication.class.getName());
        for (String arg : args) {
            command.add(arg);
        }

        try {
            Process process = new ProcessBuilder(command)
                    .inheritIO()
                    .start();
            return process.waitFor();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return -1;
        } catch (IOException ex) {
            return -1;
        }
    }
}
