package net.atos.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

public class Enforcer {
    public static void main(String[] args) {
        String fileName = "C:/Users/mreiners/Downloads/enforcer.log";
        System.out.println("Enforcer excludes needed:");
        System.out.println("-------------------------");
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            boolean setModule = false;
            String groupArtifact = "";
            while (line != null) {
                if (line.contains("Failed while enforcing RequireUpperBoundDeps")) {
                    setModule = true;
                }
                if (line.contains("Require upper bound dependencies error for ")) {
                    groupArtifact = StringUtils.substringAfter(line, "Require upper bound dependencies error for ");
                    groupArtifact = StringUtils.substringBefore(groupArtifact, "paths to dependency are:");
                    groupArtifact = StringUtils.substringBeforeLast(groupArtifact, ":");
                    if (!setModule) {
                        System.out.println("<exclude>" + groupArtifact + "</exclude>");
                    }
                }
                if (setModule && line.contains("+-")) {
                    String module = StringUtils.substringAfter(line, "+-");
                    module = StringUtils.substringBeforeLast(module, ":");
                    System.out.println("\nFor module: " + module);
                    System.out.println("\n<exclude>" + groupArtifact + "</exclude>");
                    setModule = false;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

