package org.jiang;

import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;

import java.util.List;

public class PaginationPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    private static void generator() {
        String config = PaginationPlugin.class.getClassLoader().getResource("generator/generatorConfig-B.xml").getFile();
        String[] args = {"-Dconfigfile",config,"-overwrite"};
        ShellRunner.main(args);
    }

    public static void main(String[] args) {
        generator();
    }
}
